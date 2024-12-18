package co.org.ccb.constituciones.transversal.dominio.repositorio;

import co.org.ccb.constituciones.transversal.dominio.entidad.LogEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntidad, Integer> {
}
