package co.org.ccb.constituciones.transversal.dominio.repositorio;

import co.org.ccb.constituciones.transversal.dominio.entidad.ParametroEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametroRepository extends JpaRepository<ParametroEntidad, Integer> {
    ParametroEntidad findByCodigo(String codigo);
    List<ParametroEntidad> findAllByCodigoPadreAndActivo(String codigoPadre, Integer active);
}
