package co.org.ccb.constituciones.transversal.infraestructura.controladores.impl;

import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametroPorCodigoService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametrosPorPadreService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarVariosParametrosPorCodigoService;
import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.ConsultarVariosParametrosRequest;
import co.org.ccb.constituciones.transversal.transversal.util.RespuestaBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParametroControllerImplTest {

    @Mock
    private ConsultarParametroPorCodigoService consultarParametroPorCodigoService;

    @Mock
    private ConsultarParametrosPorPadreService consultarParametrosPorPadreService;

    @Mock
    private ConsultarVariosParametrosPorCodigoService consultarVariosParametrosPorCodigoService;

    @InjectMocks
    private ParametroControllerImpl controller;

    private ParametroEntidad parametro;
    private List<ParametroEntidad> parametros;

    @BeforeEach
    void setUp() {
        parametro = new ParametroEntidad();
        parametro.setCodigo("TEST_CODE");
        parametro.setValor("Test Value");

        parametros = Arrays.asList(
                parametro,
                createParametro("TEST_CODE_2", "Test Value 2")
        );
    }

    private ParametroEntidad createParametro(String codigo, String valor) {
        ParametroEntidad param = new ParametroEntidad();
        param.setCodigo(codigo);
        param.setValor(valor);
        return param;
    }

    @Test
    void consultarPorCodigo_DebeRetornarParametro_CuandoExisteCodigo() {
        // Arrange
        String codigo = "TEST_CODE";
        when(consultarParametroPorCodigoService.buscarPorCodigo(codigo)).thenReturn(parametro);

        // Act
        var response = controller.consultarPorCodigo(codigo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RespuestaBase respuestaBase = response.getBody();
        assertNotNull(respuestaBase);
        assertEquals("OK", respuestaBase.getMensaje());
        assertEquals(parametro, respuestaBase.getDetalle());
        verify(consultarParametroPorCodigoService).buscarPorCodigo(codigo);
    }

    @Test
    void consultarPorPadre_DebeRetornarListaParametros_CuandoExisteCodigoPadre() {
        // Arrange
        String codigoPadre = "PARENT_CODE";
        when(consultarParametrosPorPadreService.buscarParametros(codigoPadre)).thenReturn(parametros);

        // Act
        var response = controller.consultarPorPadre(codigoPadre);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RespuestaBase respuestaBase = response.getBody();
        assertNotNull(respuestaBase);
        assertEquals("OK", respuestaBase.getMensaje());
        assertEquals(parametros, respuestaBase.getDetalle());
        verify(consultarParametrosPorPadreService).buscarParametros(codigoPadre);
    }

    @Test
    void consultarPorCodigos_DebeRetornarListaParametros_CuandoExistenCodigos() {
        // Arrange
        List<String> codigos = Arrays.asList("TEST_CODE", "TEST_CODE_2");
        ConsultarVariosParametrosRequest request = new ConsultarVariosParametrosRequest();
        request.setCodigos(codigos);

        when(consultarVariosParametrosPorCodigoService.buscarPorCodigos(codigos)).thenReturn(parametros);

        // Act
        var response = controller.consultarPorCodigos(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RespuestaBase respuestaBase = response.getBody();
        assertNotNull(respuestaBase);
        assertEquals("OK", respuestaBase.getMensaje());
        assertEquals(parametros, respuestaBase.getDetalle());
        verify(consultarVariosParametrosPorCodigoService).buscarPorCodigos(codigos);
    }

    @Test
    void consultarPorCodigos_DebeRetornarListaVacia_CuandoRequestEsVacio() {
        // Arrange
        ConsultarVariosParametrosRequest request = new ConsultarVariosParametrosRequest();
        request.setCodigos(Arrays.asList());

        when(consultarVariosParametrosPorCodigoService.buscarPorCodigos(Arrays.asList()))
                .thenReturn(Arrays.asList());

        // Act
        var response = controller.consultarPorCodigos(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RespuestaBase respuestaBase = response.getBody();
        assertNotNull(respuestaBase);
        assertEquals("OK", respuestaBase.getMensaje());
        assertTrue(((List<?>) respuestaBase.getDetalle()).isEmpty());
        verify(consultarVariosParametrosPorCodigoService).buscarPorCodigos(Arrays.asList());
    }
}