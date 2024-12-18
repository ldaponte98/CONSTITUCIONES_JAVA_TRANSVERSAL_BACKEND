package co.org.ccb.constituciones.transversal.aplicacion.parametros.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.LogService;
import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;
import co.org.ccb.constituciones.transversal.dominio.repositorio.ParametroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarVariosParametrosPorCodigoServiceImplTest {

    @Mock
    private ParametroRepository repository;

    @Mock
    private LogService logService;

    @InjectMocks
    private ConsultarVariosParametrosPorCodigoServiceImpl service;

    private ParametroEntidad parametro1;
    private ParametroEntidad parametro2;
    private List<String> codigos;

    @BeforeEach
    void setUp() {
        parametro1 = new ParametroEntidad();
        parametro1.setCodigo("PARAM1");
        parametro1.setValor("Valor1");

        parametro2 = new ParametroEntidad();
        parametro2.setCodigo("PARAM2");
        parametro2.setValor("Valor2");

        codigos = Arrays.asList("PARAM1", "PARAM2");
    }

    @Test
    void buscarPorCodigos_DebeRetornarListaDeParametros_CuandoExistenCodigos() {
        // Arrange
        List<ParametroEntidad> parametrosEsperados = Arrays.asList(parametro1, parametro2);
        when(repository.findAllByCodigoIn(codigos)).thenReturn(parametrosEsperados);

        // Act
        List<ParametroEntidad> resultado = service.buscarPorCodigos(codigos);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(parametrosEsperados, resultado);
        verify(repository).findAllByCodigoIn(codigos);
        verify(logService).info(contains("Consulta varios parametros [" + codigos.toString() + "] exitosa."));
    }

    @Test
    void buscarPorCodigos_DebeRetornarListaVacia_CuandoNoExistenCodigos() {
        // Arrange
        List<String> codigosInexistentes = Arrays.asList("NO_EXISTE1", "NO_EXISTE2");
        when(repository.findAllByCodigoIn(codigosInexistentes)).thenReturn(Collections.emptyList());

        // Act
        List<ParametroEntidad> resultado = service.buscarPorCodigos(codigosInexistentes);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).findAllByCodigoIn(codigosInexistentes);
        verify(logService).info(contains("Consulta varios parametros [" + codigosInexistentes.toString() + "] exitosa."));
    }

    @Test
    void buscarPorCodigos_DebeRetornarListaVacia_CuandoListaCodigosEstaVacia() {
        // Arrange
        List<String> codigosVacios = Collections.emptyList();
        when(repository.findAllByCodigoIn(codigosVacios)).thenReturn(Collections.emptyList());

        // Act
        List<ParametroEntidad> resultado = service.buscarPorCodigos(codigosVacios);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).findAllByCodigoIn(codigosVacios);
        verify(logService).info(contains("Consulta varios parametros [" + codigosVacios.toString() + "] exitosa."));
    }
}