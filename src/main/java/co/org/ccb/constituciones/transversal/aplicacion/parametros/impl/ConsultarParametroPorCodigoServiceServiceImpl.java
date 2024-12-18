package co.org.ccb.constituciones.transversal.aplicacion.parametros.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.LogService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametroPorCodigoService;
import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;
import co.org.ccb.constituciones.transversal.dominio.repositorio.ParametroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarParametroPorCodigoServiceServiceImpl implements ConsultarParametroPorCodigoService {
    private final ParametroRepository repository;
    private final LogService logService;
    @Override
    public ParametroEntidad buscarPorCodigo(String codigo) {
        var respuesta = this.repository.findByCodigo(codigo);
        logService.info("Consulta de parametro ["+codigo+"] exitosa.");
        return respuesta;
    }
}
