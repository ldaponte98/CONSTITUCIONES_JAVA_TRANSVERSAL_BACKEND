package co.org.ccb.constituciones.transversal.aplicacion.logs;

import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;

public interface RegistrarLogService {
    void registrar(LogRequest request);
}
