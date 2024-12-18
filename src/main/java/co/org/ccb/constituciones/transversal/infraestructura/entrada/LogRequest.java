package co.org.ccb.constituciones.transversal.infraestructura.entrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogRequest {
    private String componente;
    private String servicio;
    private String mensaje;
    private String tipo;
}
