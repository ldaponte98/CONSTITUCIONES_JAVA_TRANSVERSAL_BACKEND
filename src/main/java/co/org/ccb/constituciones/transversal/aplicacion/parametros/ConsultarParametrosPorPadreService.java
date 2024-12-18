package co.org.ccb.constituciones.transversal.aplicacion.parametros;

import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;

import java.util.List;

public interface ConsultarParametrosPorPadreService {
    List<ParametroEntidad> buscarParametros(String codigoPadre);
}
