package co.org.ccb.constituciones.transversal.aplicacion.parametros.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.LogService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarVariosParametrosPorCodigoService;
import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;
import co.org.ccb.constituciones.transversal.dominio.repositorio.ParametroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ConsultarVariosParametrosPorCodigoServiceImpl implements ConsultarVariosParametrosPorCodigoService {
    private final ParametroRepository repository;
    private final LogService logService;
    @Override
    public List<ParametroEntidad> buscarPorCodigos(List<String> codigos) {
        var respuesta = this.repository.findAllByCodigoIn(codigos);
        logService.info("Consulta varios parametros ["+codigos.toString()+"] exitosa.");
        return respuesta;
    }
}
