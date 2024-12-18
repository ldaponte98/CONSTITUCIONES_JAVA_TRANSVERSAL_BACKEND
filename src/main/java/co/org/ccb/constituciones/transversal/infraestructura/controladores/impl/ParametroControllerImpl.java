package co.org.ccb.constituciones.transversal.infraestructura.controladores.impl;

import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametroPorCodigoService;
import co.org.ccb.constituciones.transversal.aplicacion.parametros.ConsultarParametrosPorPadreService;
import co.org.ccb.constituciones.transversal.infraestructura.controladores.ParametroController;
import co.org.ccb.constituciones.transversal.transversal.util.RespuestaBase;
import co.org.ccb.constituciones.transversal.transversal.util.UtilidadesAplicacion;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/v1/parametros")
public class ParametroControllerImpl implements ParametroController {
    private final ConsultarParametroPorCodigoService consultarParametroPorCodigoService;
    private final ConsultarParametrosPorPadreService consultarParametrosPorPadreService;
    public ParametroControllerImpl(ConsultarParametroPorCodigoService consultarParametroPorCodigoService, ConsultarParametrosPorPadreService consultarParametrosPorPadreService) {
        this.consultarParametroPorCodigoService = consultarParametroPorCodigoService;
        this.consultarParametrosPorPadreService = consultarParametrosPorPadreService;
    }
    @Override
    @GetMapping("/detalle/{codigo}")
    public ResponseEntity<RespuestaBase> consultarPorCodigo(HttpServletRequest request, @PathVariable String codigo) {
        var respuesta = UtilidadesAplicacion.responder("OK", consultarParametroPorCodigoService.buscarPorCodigo(codigo));
        return ResponseEntity.ok(respuesta);
    }
    @Override
    @GetMapping("/{codigoPadre}")
    public ResponseEntity<RespuestaBase> consultarPorPadre(HttpServletRequest request, @PathVariable String codigoPadre) {
        var respuesta = UtilidadesAplicacion.responder("OK", consultarParametrosPorPadreService.buscarParametros(codigoPadre));
        return ResponseEntity.ok(respuesta);
    }
}
