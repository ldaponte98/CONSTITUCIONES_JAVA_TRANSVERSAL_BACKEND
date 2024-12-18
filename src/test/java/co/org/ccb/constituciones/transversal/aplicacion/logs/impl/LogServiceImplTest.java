package co.org.ccb.constituciones.transversal.aplicacion.logs.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.RegistrarLogService;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;
import co.org.ccb.constituciones.transversal.transversal.util.UtilidadesApi;
import co.org.ccb.constituciones.transversal.transversal.util.UsuarioSesion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogServiceImplTest {

    @Mock
    private RegistrarLogService registrarLogService;

    @InjectMocks
    private LogServiceImpl logService;

    @Captor
    private ArgumentCaptor<LogRequest> logRequestCaptor;

    private static final String COMPONENTE = "test-component";
    private static final String SERVICIO = "test-service";
    private static final String MENSAJE = "test message";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(logService, "componente", COMPONENTE);
        UtilidadesApi.session = UsuarioSesion.builder()
                .service(SERVICIO)
                .usuario("test-user")
                .token("test-token")
                .build();
    }

    @Test
    void info_deberiaRegistrarLogTipoInfo() {
        // Act
        logService.info(MENSAJE);

        // Assert
        verify(registrarLogService).registrar(logRequestCaptor.capture());
        LogRequest logRequest = logRequestCaptor.getValue();
        assertEquals(MENSAJE, logRequest.getMensaje());
        assertEquals(COMPONENTE, logRequest.getComponente());
        assertEquals(SERVICIO, logRequest.getServicio());
        assertEquals("INFO", logRequest.getTipo());
    }

    @Test
    void error_deberiaRegistrarLogTipoError() {
        // Act
        logService.error(MENSAJE);

        // Assert
        verify(registrarLogService).registrar(logRequestCaptor.capture());
        LogRequest logRequest = logRequestCaptor.getValue();
        assertEquals(MENSAJE, logRequest.getMensaje());
        assertEquals(COMPONENTE, logRequest.getComponente());
        assertEquals(SERVICIO, logRequest.getServicio());
        assertEquals("ERROR", logRequest.getTipo());
    }

    @Test
    void registrar_cuandoOcurreError_deberiaCapturarExcepcion() {
        // Arrange
        doThrow(new RuntimeException("Error de prueba"))
                .when(registrarLogService).registrar(any(LogRequest.class));

        // Act & Assert
        // No debería lanzar excepción
        assertDoesNotThrow(() -> logService.info(MENSAJE));
        verify(registrarLogService).registrar(any(LogRequest.class));
    }

    @Test
    void registrar_cuandoSessionEsNula_deberiaCapturarExcepcion() {
        // Arrange
        UtilidadesApi.session = null;

        // Act & Assert
        assertDoesNotThrow(() -> logService.info(MENSAJE));
        verify(registrarLogService, never()).registrar(any(LogRequest.class));
    }

    @Test
    void registrar_conDiferentesMensajes_deberiaRegistrarCorrectamente() {
        // Arrange
        String[] mensajes = {"mensaje1", "mensaje2", "mensaje3"};

        // Act
        for (String mensaje : mensajes) {
            logService.info(mensaje);
        }

        // Assert
        verify(registrarLogService, times(mensajes.length)).registrar(any(LogRequest.class));
    }
}