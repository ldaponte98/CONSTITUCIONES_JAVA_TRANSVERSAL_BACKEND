package co.org.ccb.constituciones.transversal.infraestructura.entrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarVariosParametrosRequest {
    private List<String> codigos;
}
