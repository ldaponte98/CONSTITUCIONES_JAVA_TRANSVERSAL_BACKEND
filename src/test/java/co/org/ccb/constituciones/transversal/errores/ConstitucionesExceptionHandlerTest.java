package co.org.ccb.constituciones.transversal.errores;

import co.org.ccb.constituciones.transversal.aplicacion.logs.RegistrarLogService;
import co.org.ccb.constituciones.transversal.errores.entrada.ConflictException;
import co.org.ccb.constituciones.transversal.errores.entrada.GeneralErrorResponse;
import co.org.ccb.constituciones.transversal.errores.entrada.UnauthorizedException;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConstitucionesExceptionHandlerTest {

    @Mock
    private RegistrarLogService logService;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private ConstitucionesExceptionHandler exceptionHandler;

    private static final String COMPONENTE = "test-component";
    private static final String REQUEST_URI = "/api/test";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(exceptionHandler, "componente", COMPONENTE);
        when(httpRequest.getRequestURI()).thenReturn(REQUEST_URI);
    }

    @Test
    void handleConflictExceptions_deberiaRetornarResponseEntityCorrecto() {
        // Arrange
        String errorMessage = "Error de conflicto";
        ConflictException ex = new ConflictException(errorMessage, errorMessage);

        // Act
        var responseEntity = exceptionHandler.handleConflictExceptions(ex, httpRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        GeneralErrorResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertEquals(REQUEST_URI, response.getPath());

        verify(logService).registrar(any(LogRequest.class));
    }

    @Test
    void handleUnauthorizedExceptions_deberiaRetornarResponseEntityCorrecto() {
        // Arrange
        String errorMessage = "Usuario no autorizado";
        UnauthorizedException ex = new UnauthorizedException(errorMessage);

        // Act
        var responseEntity = exceptionHandler.handleUnauthorizedExceptions(ex, httpRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        GeneralErrorResponse response = (GeneralErrorResponse) responseEntity.getBody();
        assertNotNull(response);
        assertEquals("Usuario no autorizado", response.getMessage());
        assertEquals(REQUEST_URI, response.getPath());
    }

    @Test
    void handleAllExceptions_deberiaRetornarResponseEntityCorrecto() {
        // Arrange
        String errorMessage = "Error interno";
        Exception ex = new RuntimeException(errorMessage);

        // Act
        var responseEntity = exceptionHandler.handleAllExceptions(ex, httpRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        GeneralErrorResponse response = (GeneralErrorResponse) responseEntity.getBody();
        assertNotNull(response);
        assertEquals("Internal server error", response.getMessage());
        assertEquals(errorMessage, response.getError());
        assertEquals(REQUEST_URI, response.getPath());

        verify(logService).registrar(any(LogRequest.class));
    }

    @Test
    void handleArgumentNotValidExceptions_deberiaRetornarResponseEntityCorrecto() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        FieldError fieldError = new FieldError("object", "field", "field error");
        ObjectError globalError = new ObjectError("object", "global error");

        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));
        when(bindingResult.getGlobalErrors()).thenReturn(Arrays.asList(globalError));

        // Act
        var responseEntity = exceptionHandler.handleArgumentNotValidExceptions(ex, httpRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        GeneralErrorResponse response = (GeneralErrorResponse) responseEntity.getBody();
        assertNotNull(response);
        assertEquals("Bad Request", response.getMessage());
        assertTrue(response.getError().toString().contains("field: field error"));
        assertTrue(response.getError().toString().contains("object: global error"));

        verify(logService).registrar(any(LogRequest.class));
    }

    @Test
    void handleMethodArgumentTypeMismatchException_deberiaRetornarResponseEntityCorrecto() {
        // Arrange
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        Throwable cause = mock(Throwable.class);
        Throwable rootCause = new RuntimeException("tipo inválido");

        when(ex.getCause()).thenReturn(cause);
        when(cause.getCause()).thenReturn(rootCause);

        // Act
        var responseEntity = exceptionHandler.handleMethodArgumentTypeMismatchException(ex, httpRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        GeneralErrorResponse response = (GeneralErrorResponse) responseEntity.getBody();
        assertNotNull(response);
        assertEquals("Bad Request", response.getMessage());
        assertEquals("tipo inválido", response.getError());

        verify(logService).registrar(any(LogRequest.class));
    }
}