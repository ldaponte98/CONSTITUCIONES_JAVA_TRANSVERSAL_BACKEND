package co.org.ccb.constituciones.transversal.aplicacion.parametros.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.LogService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.impl.ConsultarParametroPorCodigoServiceServiceImpl;
import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;
import co.org.ccb.constituciones.transversal.dominio.repositorio.ParametroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConsultarParametroPorCodigoServiceServiceImplTest {

    @Mock
    private ParametroRepository repository;

    @Mock
    private LogService logService;

    @InjectMocks
    private ConsultarParametroPorCodigoServiceServiceImpl service;

    @Test
    void buscarPorCodigo_DeberiaRetornarParametroExistente() {
        // Arrange
        String codigo = "TEST_PARAM";
        ParametroEntidad parametroEsperado = new ParametroEntidad();

        parametroEsperado = new ParametroEntidad(1, "", "", "", "", 1, Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));

        parametroEsperado = ParametroEntidad.builder()
                .id(1)
                .build();

        parametroEsperado.setId(1);
        parametroEsperado.setCodigo(codigo);
        parametroEsperado.setValor("valor test");
        parametroEsperado.setDescripcion("valor test");
        parametroEsperado.setCodigoPadre("valor test");
        parametroEsperado.setActivo(1);
        parametroEsperado.setFechaInicial(Date.valueOf(LocalDate.now()));
        parametroEsperado.setFechaFinal(Date.valueOf(LocalDate.now()));

        parametroEsperado.getId();
        parametroEsperado.getCodigo();
        parametroEsperado.getValor();
        parametroEsperado.getDescripcion();
        parametroEsperado.getCodigoPadre();
        parametroEsperado.getActivo();
        parametroEsperado.getFechaInicial();
        parametroEsperado.getFechaFinal();


        when(repository.findByCodigo(codigo)).thenReturn(parametroEsperado);
        doNothing().when(logService).info(anyString());

        // Act
        ParametroEntidad resultado = service.buscarPorCodigo(codigo);

        // Assert
        assertNotNull(resultado);
        assertEquals(parametroEsperado, resultado);
        assertEquals(codigo, resultado.getCodigo());
        assertEquals("valor test", resultado.getValor());

        verify(repository).findByCodigo(codigo);
        verify(logService).info("Consulta de parametro [" + codigo + "] exitosa.");
    }

    @Test
    void buscarPorCodigo_DeberiaRetornarNuloCuandoNoExisteParametro() {
        // Arrange
        String codigo = "PARAM_INEXISTENTE";
        when(repository.findByCodigo(codigo)).thenReturn(null);
        doNothing().when(logService).info(anyString());

        // Act
        ParametroEntidad resultado = service.buscarPorCodigo(codigo);

        // Assert
        assertNull(resultado);
        verify(repository).findByCodigo(codigo);
        verify(logService).info("Consulta de parametro [" + codigo + "] exitosa.");
    }

    @Test
    void buscarPorCodigo_DeberiaFuncionarConCodigoVacio() {
        // Arrange
        String codigo = "";
        when(repository.findByCodigo(codigo)).thenReturn(null);
        doNothing().when(logService).info(anyString());

        // Act
        ParametroEntidad resultado = service.buscarPorCodigo(codigo);

        // Assert
        assertNull(resultado);
        verify(repository).findByCodigo(codigo);
        verify(logService).info("Consulta de parametro [" + codigo + "] exitosa.");
    }

    @Test
    void buscarPorCodigo_NoDeberiaFallarConCodigoNulo() {
        // Arrange
        String codigo = null;
        when(repository.findByCodigo(codigo)).thenReturn(null);
        doNothing().when(logService).info(anyString());

        // Act
        ParametroEntidad resultado = service.buscarPorCodigo(codigo);

        // Assert
        assertNull(resultado);
        verify(repository).findByCodigo(codigo);
        verify(logService).info("Consulta de parametro [" + codigo + "] exitosa.");
    }
}