package co.org.ccb.constituciones.transversal.aplicacion.parametros.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.LogService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametrosPorPadreService;
import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;
import co.org.ccb.constituciones.transversal.dominio.repositorio.ParametroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ConsultarParametrosPorPadreServiceServiceImpl implements ConsultarParametrosPorPadreService {
    private final ParametroRepository repository;
    private final LogService logService;
    public ConsultarParametrosPorPadreServiceServiceImpl(ParametroRepository repository, LogService logService) {
        this.repository = repository;
        this.logService = logService;
    }
    @Override
    public List<ParametroEntidad> buscarParametros(String codigoPadre) {
        var respuesta = this.repository.findAllByCodigoPadreAndActivo(codigoPadre, 1);
        logService.info("Consulta de parametros por padre ["+codigoPadre+"] exitosa.");
        return respuesta;
    }
}
