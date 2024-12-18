package co.org.ccb.constituciones.transversal.aplicacion.logs.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.RegistrarLogService;
import co.org.ccb.constituciones.transversal.dominio.entidad.LogEntidad;
import co.org.ccb.constituciones.transversal.dominio.repositorio.LogRepository;
import co.org.ccb.constituciones.transversal.errores.entrada.LogException;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;
import co.org.ccb.constituciones.transversal.transversal.util.UtilidadesApi;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RegistrarLogServiceImpl implements RegistrarLogService {
    private final LogRepository repository;
    public RegistrarLogServiceImpl(LogRepository repository) {
        this.repository = repository;
    }
    @Override
    public void registrar(LogRequest request) {
        try {
            LogEntidad entidad = LogEntidad.builder()
                    .tipo(request.getTipo())
                    .componente(request.getComponente())
                    .usuario(UtilidadesApi.session.getUsuario())
                    .servicio(request.getServicio())
                    .detalle(request.getMensaje())
                    .build();
            this.repository.save(entidad);
            log.info(request.getMensaje());
        }catch (Exception e){
            log.info("Ocurrio un error registrando el log: " + e.getMessage());
            throw new LogException("Ocurrio un error registrando el log: " + e.getMessage());
        }
    }
}
