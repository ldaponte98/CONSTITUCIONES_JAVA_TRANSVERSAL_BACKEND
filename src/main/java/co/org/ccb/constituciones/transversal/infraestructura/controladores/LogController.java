package co.org.ccb.constituciones.transversal.infraestructura.controladores;

import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;
import co.org.ccb.constituciones.transversal.transversal.util.RespuestaBase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface LogController {
    @Tag(name = "Logs")
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Registro de un log", description = "Servicio para registrar un log en la aplicación.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "Exitoso",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = RespuestaBase.class))),

            })
    ResponseEntity<RespuestaBase> registrar(@RequestBody LogRequest request);
}
