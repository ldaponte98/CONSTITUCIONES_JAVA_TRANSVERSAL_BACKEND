package co.org.ccb.constituciones.transversal.aplicacion.parametros;

import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;

public interface ConsultarParametroPorCodigoService {
    ParametroEntidad buscarPorCodigo(String codigo);
}
