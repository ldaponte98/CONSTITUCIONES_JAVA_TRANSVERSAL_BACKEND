package co.org.ccb.constituciones.transversal.infraestructura.controladores.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.RegistrarLogService;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;
import co.org.ccb.constituciones.transversal.transversal.util.RespuestaBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogControllerImplTest {

    @Mock
    private RegistrarLogService registrarLogService;

    @InjectMocks
    private LogControllerImpl logController;

    private LogRequest logRequest;

    @BeforeEach
    void setUp() {
        logRequest = new LogRequest();
        logRequest.setMensaje("Test message");
        logRequest.setTipo("INFO");
        logRequest.setComponente("TEST");
        logRequest.setServicio("test-service");
    }

    @Test
    void registrar_deberiaRetornarRespuestaExitosa() {
        // Act
        var response = logController.registrar(logRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        RespuestaBase respuestaBase = response.getBody();
        assertNotNull(respuestaBase);
        assertTrue(respuestaBase.isExito());
        assertEquals("OK", respuestaBase.getMensaje());
        assertNull(respuestaBase.getDetalle());

        verify(registrarLogService, times(1)).registrar(logRequest);
    }

    @Test
    void registrar_deberiaLlamarAlServicio() {
        // Act
        logController.registrar(logRequest);

        // Assert
        verify(registrarLogService).registrar(argThat(request ->
                request.getMensaje().equals("Test message") &&
                        request.getTipo().equals("INFO") &&
                        request.getComponente().equals("TEST") &&
                        request.getServicio().equals("test-service")
        ));
    }

    @Test
    void registrar_cuandoHayError_deberiaPropagarExcepcion() {
        // Arrange
        doThrow(new RuntimeException("Error al registrar log"))
                .when(registrarLogService).registrar(any(LogRequest.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                logController.registrar(logRequest)
        );

        verify(registrarLogService).registrar(logRequest);
    }
}