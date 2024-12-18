package co.org.ccb.constituciones.transversal.transversal.util;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioSesion {
    private String usuario;
    private String service;
    private String token;
}
