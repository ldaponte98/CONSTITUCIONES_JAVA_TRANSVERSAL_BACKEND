package co.org.ccb.constituciones.transversal.infraestructura.controladores;

import co.org.ccb.constituciones.transversal.infraestructura.entrada.ConsultarVariosParametrosRequest;
import co.org.ccb.constituciones.transversal.transversal.util.RespuestaBase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ParametroController {
    @Tag(name = "Parametros")
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Consulta de parametro por codigo", description = "Servicio para consulta de parametro por codigo")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "Exitoso",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = RespuestaBase.class))),

            })
    ResponseEntity<RespuestaBase> consultarPorCodigo(String codigo);

    @Tag(name = "Parametros")
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Consulta de parametros por codigo de padre", description = "Servicio para consulta de parametros por codigo de padre")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "Exitoso",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = RespuestaBase.class))),

            })
    ResponseEntity<RespuestaBase> consultarPorPadre(String codigoPadre);

    @Tag(name = "Parametros")
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Consulta de varios parametros por codigo", description = "Servicio para consultar varios parametros por codigo")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "Exitoso",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = RespuestaBase.class))),

            })
    ResponseEntity<RespuestaBase> consultarPorCodigos(@RequestBody ConsultarVariosParametrosRequest request);
}
