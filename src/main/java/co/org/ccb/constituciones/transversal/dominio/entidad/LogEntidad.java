package co.org.ccb.constituciones.transversal.dominio.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "logs", schema = "constituciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false)
    private Integer id;
    @Column(name = "FECHA", insertable = false)
    private Date fecha;
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "COMPONENTE")
    private String componente;
    @Column(name = "SERVICIO")
    private String servicio;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "DETALLE")
    private String detalle;
}
