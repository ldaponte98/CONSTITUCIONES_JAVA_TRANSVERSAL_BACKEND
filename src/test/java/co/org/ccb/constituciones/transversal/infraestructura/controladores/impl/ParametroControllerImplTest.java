package co.org.ccb.constituciones.transversal.infraestructura.controladores.impl;

import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametroPorCodigoService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametrosPorPadreService;
import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;
import co.org.ccb.constituciones.transversal.transversal.util.RespuestaBase;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParametroControllerImplTest {

    @Mock
    private ConsultarParametroPorCodigoService consultarParametroPorCodigoService;

    @Mock
    private ConsultarParametrosPorPadreService consultarParametrosPorPadreService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ParametroControllerImpl controller;

    private ParametroEntidad parametroMock;
    private List<ParametroEntidad> parametrosListMock;

    @BeforeEach
    void setUp() {
        parametroMock = new ParametroEntidad();
        parametroMock.setCodigo("TEST_CODE");
        parametroMock.setValor("Test Value");

        parametrosListMock = Arrays.asList(
                parametroMock,
                new ParametroEntidad()
        );
    }

    @Test
    void consultarPorCodigo_DeberiaRetornarParametroExistente() {
        // Arrange
        String codigo = "TEST_CODE";
        when(consultarParametroPorCodigoService.buscarPorCodigo(codigo))
                .thenReturn(parametroMock);

        // Act
        RespuestaBase res = new RespuestaBase();
        ResponseEntity<RespuestaBase> response = controller.consultarPorCodigo(request, codigo);

        // Assert
        assertNotNull(response);
        assertEquals(true, response.getBody().isExito());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("OK", response.getBody().getMensaje());
        assertEquals(parametroMock, response.getBody().getDetalle());

        verify(consultarParametroPorCodigoService).buscarPorCodigo(codigo);
    }

    @Test
    void consultarPorCodigo_DeberiaRetornarNullCuandoNoExisteParametro() {
        // Arrange
        String codigo = "NONEXISTENT_CODE";
        when(consultarParametroPorCodigoService.buscarPorCodigo(codigo))
                .thenReturn(null);

        // Act
        ResponseEntity<RespuestaBase> response = controller.consultarPorCodigo(request, codigo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("OK", response.getBody().getMensaje());
        assertNull(response.getBody().getDetalle());

        verify(consultarParametroPorCodigoService).buscarPorCodigo(codigo);
    }

    @Test
    void consultarPorPadre_DeberiaRetornarListaDeParametros() {
        // Arrange
        String codigoPadre = "PARENT_CODE";
        when(consultarParametrosPorPadreService.buscarParametros(codigoPadre))
                .thenReturn(parametrosListMock);

        // Act
        ResponseEntity<RespuestaBase> response = controller.consultarPorPadre(request, codigoPadre);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("OK", response.getBody().getMensaje());
        assertEquals(parametrosListMock, response.getBody().getDetalle());

        verify(consultarParametrosPorPadreService).buscarParametros(codigoPadre);
    }

    @Test
    void consultarPorPadre_DeberiaRetornarListaVaciaCuandoNoHayResultados() {
        // Arrange
        String codigoPadre = "EMPTY_PARENT";
        when(consultarParametrosPorPadreService.buscarParametros(codigoPadre))
                .thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<RespuestaBase> response = controller.consultarPorPadre(request, codigoPadre);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("OK", response.getBody().getMensaje());
        assertTrue(((List<?>) response.getBody().getDetalle()).isEmpty());

        verify(consultarParametrosPorPadreService).buscarParametros(codigoPadre);
    }

    @Test
    void consultarPorCodigo_DeberiaFuncionarConCodigoVacio() {
        // Arrange
        String codigo = "";
        when(consultarParametroPorCodigoService.buscarPorCodigo(codigo))
                .thenReturn(null);

        // Act
        ResponseEntity<RespuestaBase> response = controller.consultarPorCodigo(request, codigo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("OK", response.getBody().getMensaje());
        assertNull(response.getBody().getDetalle());

        verify(consultarParametroPorCodigoService).buscarPorCodigo(codigo);
    }

    @Test
    void consultarPorPadre_DeberiaFuncionarConCodigoPadreVacio() {
        // Arrange
        String codigoPadre = "";
        when(consultarParametrosPorPadreService.buscarParametros(codigoPadre))
                .thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<RespuestaBase> response = controller.consultarPorPadre(request, codigoPadre);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("OK", response.getBody().getMensaje());
        assertTrue(((List<?>) response.getBody().getDetalle()).isEmpty());

        verify(consultarParametrosPorPadreService).buscarParametros(codigoPadre);
    }
}