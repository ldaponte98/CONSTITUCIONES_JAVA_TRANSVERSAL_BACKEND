package co.org.ccb.constituciones.transversal.aplicacion.parametros.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.LogService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.impl.ConsultarParametrosPorPadreServiceServiceImpl;
import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;
import co.org.ccb.constituciones.transversal.dominio.repositorio.ParametroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConsultarParametrosPorPadreServiceServiceImplTest {

    @Mock
    private ParametroRepository repository;

    @Mock
    private LogService logService;

    @InjectMocks
    private ConsultarParametrosPorPadreServiceServiceImpl service;

    @Test
    void buscarParametros_DeberiaRetornarListaDeParametros() {
        // Arrange
        String codigoPadre = "PADRE_TEST";
        ParametroEntidad param1 = new ParametroEntidad();
        param1.setCodigo("HIJO1");
        param1.setCodigoPadre(codigoPadre);
        param1.setActivo(1);

        ParametroEntidad param2 = new ParametroEntidad();
        param2.setCodigo("HIJO2");
        param2.setCodigoPadre(codigoPadre);
        param2.setActivo(1);

        List<ParametroEntidad> parametrosEsperados = Arrays.asList(param1, param2);

        when(repository.findAllByCodigoPadreAndActivo(codigoPadre, 1))
                .thenReturn(parametrosEsperados);
        doNothing().when(logService).info(anyString());

        // Act
        List<ParametroEntidad> resultado = service.buscarParametros(codigoPadre);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(parametrosEsperados, resultado);

        verify(repository).findAllByCodigoPadreAndActivo(codigoPadre, 1);
        verify(logService).info("Consulta de parametros por padre [" + codigoPadre + "] exitosa.");
    }

    @Test
    void buscarParametros_DeberiaRetornarListaVaciaCuandoNoHayResultados() {
        // Arrange
        String codigoPadre = "PADRE_SIN_HIJOS";
        when(repository.findAllByCodigoPadreAndActivo(codigoPadre, 1))
                .thenReturn(Collections.emptyList());
        doNothing().when(logService).info(anyString());

        // Act
        List<ParametroEntidad> resultado = service.buscarParametros(codigoPadre);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findAllByCodigoPadreAndActivo(codigoPadre, 1);
        verify(logService).info("Consulta de parametros por padre [" + codigoPadre + "] exitosa.");
    }

    @Test
    void buscarParametros_DeberiaFuncionarConCodigoPadreVacio() {
        // Arrange
        String codigoPadre = "";
        when(repository.findAllByCodigoPadreAndActivo(codigoPadre, 1))
                .thenReturn(Collections.emptyList());
        doNothing().when(logService).info(anyString());

        // Act
        List<ParametroEntidad> resultado = service.buscarParametros(codigoPadre);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findAllByCodigoPadreAndActivo(codigoPadre, 1);
        verify(logService).info("Consulta de parametros por padre [" + codigoPadre + "] exitosa.");
    }

    @Test
    void buscarParametros_DeberiaFuncionarConCodigoPadreNulo() {
        // Arrange
        String codigoPadre = null;
        when(repository.findAllByCodigoPadreAndActivo(codigoPadre, 1))
                .thenReturn(Collections.emptyList());
        doNothing().when(logService).info(anyString());

        // Act
        List<ParametroEntidad> resultado = service.buscarParametros(codigoPadre);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findAllByCodigoPadreAndActivo(codigoPadre, 1);
        verify(logService).info("Consulta de parametros por padre [" + codigoPadre + "] exitosa.");
    }
}