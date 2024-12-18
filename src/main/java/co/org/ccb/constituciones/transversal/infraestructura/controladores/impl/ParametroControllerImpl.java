package co.org.ccb.constituciones.transversal.infraestructura.controladores.impl;

import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametroPorCodigoService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametrosPorPadreService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarVariosParametrosPorCodigoService;
import co.org.ccb.constituciones.transversal.infraestructura.controladores.ParametroController;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.ConsultarVariosParametrosRequest;
import co.org.ccb.constituciones.transversal.transversal.util.RespuestaBase;
import co.org.ccb.constituciones.transversal.transversal.util.UtilidadesAplicacion;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/v1/parametros")
@RequiredArgsConstructor
public class ParametroControllerImpl implements ParametroController {
    private final ConsultarParametroPorCodigoService consultarParametroPorCodigoService;
    private final ConsultarParametrosPorPadreService consultarParametrosPorPadreService;
    private final ConsultarVariosParametrosPorCodigoService consultarVariosParametrosPorCodigoService;
    @Override
    @GetMapping("/buscar-por-codigo/{codigo}")
    public ResponseEntity<RespuestaBase> consultarPorCodigo(@PathVariable String codigo) {
        var respuesta = UtilidadesAplicacion.responder("OK", consultarParametroPorCodigoService.buscarPorCodigo(codigo));
        return ResponseEntity.ok(respuesta);
    }
    @Override
    @GetMapping("/buscar-por-padre/{codigoPadre}")
    public ResponseEntity<RespuestaBase> consultarPorPadre(@PathVariable String codigoPadre) {
        var respuesta = UtilidadesAplicacion.responder("OK", consultarParametrosPorPadreService.buscarParametros(codigoPadre));
        return ResponseEntity.ok(respuesta);
    }

    @Override
    @PostMapping("/buscar-varios-codigos")
    public ResponseEntity<RespuestaBase> consultarPorCodigos(@RequestBody ConsultarVariosParametrosRequest request) {
        var respuesta = UtilidadesAplicacion.responder("OK", consultarVariosParametrosPorCodigoService.buscarPorCodigos(request.getCodigos()));
        return ResponseEntity.ok(respuesta);
    }
}
