package co.org.ccb.constituciones.transversal.aplicacion.logs.impl;

import co.org.ccb.constituciones.transversal.dominio.entidad.LogEntidad;
import co.org.ccb.constituciones.transversal.dominio.repositorio.LogRepository;
import co.org.ccb.constituciones.transversal.errores.entrada.LogException;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;
import co.org.ccb.constituciones.transversal.transversal.util.UsuarioSesion;
import co.org.ccb.constituciones.transversal.transversal.util.UtilidadesApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegistrarLogServiceImplTest {

    @Mock
    private LogRepository repository;

    @InjectMocks
    private RegistrarLogServiceImpl logService;

    private static final String TEST_COMPONENT = "test-component";
    private static final String TEST_USER = "test-user";
    private static final String TEST_SERVICE = "test-service";
    private static final String TEST_MESSAGE = "test message";

    private LogRequest request;

    @BeforeEach
    void setUp() {
        // Configurar UtilidadesApi.session
        UtilidadesApi.session = new UsuarioSesion();
        UtilidadesApi.session.setUsuario(TEST_USER);
        UtilidadesApi.session.setService(TEST_SERVICE);

        request = LogRequest.builder()
                .componente(TEST_COMPONENT)
                .servicio(TEST_SERVICE)
                .mensaje(TEST_MESSAGE)
                .build();
    }

    @Test
    void info_DeberiaRegistrarLogTipoInfo() {
        // Arrange
        ArgumentCaptor<LogEntidad> logCaptor = ArgumentCaptor.forClass(LogEntidad.class);
        when(repository.save(any(LogEntidad.class))).thenReturn(new LogEntidad());

        request.setTipo("INFO");
        // Act
        logService.registrar(request);

        // Assert
        verify(repository).save(logCaptor.capture());
        LogEntidad logGuardado = logCaptor.getValue();
        logGuardado.getId();
        logGuardado.getFecha();
        assertEquals("INFO", logGuardado.getTipo());
        assertEquals(TEST_COMPONENT, logGuardado.getComponente());
        assertEquals(TEST_USER, logGuardado.getUsuario());
        assertEquals(TEST_SERVICE, logGuardado.getServicio());
        assertEquals(TEST_MESSAGE, logGuardado.getDetalle());
    }

    @Test
    void error_DeberiaRegistrarLogTipoError() {
        // Arrange
        ArgumentCaptor<LogEntidad> logCaptor = ArgumentCaptor.forClass(LogEntidad.class);
        when(repository.save(any(LogEntidad.class))).thenReturn(new LogEntidad());
        request.setTipo("ERROR");
        // Act
        logService.registrar(request);

        // Assert
        verify(repository).save(logCaptor.capture());
        LogEntidad logGuardado = logCaptor.getValue();

        assertEquals("ERROR", logGuardado.getTipo());
        assertEquals(TEST_COMPONENT, logGuardado.getComponente());
        assertEquals(TEST_USER, logGuardado.getUsuario());
        assertEquals(TEST_SERVICE, logGuardado.getServicio());
        assertEquals(TEST_MESSAGE, logGuardado.getDetalle());
    }

    @Test
    void info_DeberiaManejearExcepcionCuandoFallaGuardado() {
        // Arrange
        when(repository.save(any(LogEntidad.class))).thenThrow(new LogException("Error simulado"));
        boolean exception = false;
        try {
            logService.registrar(request);
        }catch (Exception e){
            exception = true;
        }
        assertTrue(exception);
    }
}