package co.org.ccb.constituciones.transversal.aplicacion.logs.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.LogService;
import co.org.ccb.constituciones.transversal.aplicacion.logs.RegistrarLogService;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;
import co.org.ccb.constituciones.transversal.transversal.util.UtilidadesApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final RegistrarLogService registrarLogService;
    @Value("${spring.application.name}")
    private String componente;
    @Override
    public void info(String mensaje) {
        this.registrar(mensaje, "INFO");
    }
    @Override
    public void error(String mensaje) {
        this.registrar(mensaje, "ERROR");
    }
    private void registrar(String mensaje, String tipo){
        try {
            registrarLogService.registrar(LogRequest.builder()
                    .mensaje(mensaje)
                    .componente(componente)
                    .servicio(UtilidadesApi.session.getService())
                    .tipo(tipo)
                    .build());
        }catch (Exception e){
            log.error("Ocurrio un error registrando un log: " + e.getMessage());
        }
    }
}
