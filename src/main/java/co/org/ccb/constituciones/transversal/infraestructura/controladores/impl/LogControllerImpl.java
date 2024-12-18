package co.org.ccb.constituciones.transversal.infraestructura.controladores.impl;

import co.org.ccb.constituciones.transversal.aplicacion.logs.RegistrarLogService;
import co.org.ccb.constituciones.transversal.infraestructura.controladores.LogController;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;
import co.org.ccb.constituciones.transversal.transversal.util.RespuestaBase;
import co.org.ccb.constituciones.transversal.transversal.util.UtilidadesAplicacion;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/v1/logs")
public class LogControllerImpl implements LogController {
    private final RegistrarLogService registrarLogService;
    public LogControllerImpl(RegistrarLogService registrarLogService) {
        this.registrarLogService = registrarLogService;
    }
    @Override
    @PostMapping("")
    public ResponseEntity<RespuestaBase> registrar(@RequestBody LogRequest request) {
        this.registrarLogService.registrar(request);
        var respuesta = UtilidadesAplicacion.responder("OK", null);
        return ResponseEntity.ok(respuesta);
    }
}
