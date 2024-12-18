package co.org.ccb.constituciones.transversal.dominio.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "parametros", schema = "constituciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametroEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false)
    private Integer id;
    @Column(name = "PARAMETRO")
    private String codigo;
    @Column(name = "VALOR")
    private String valor;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "parametro_padre")
    private String codigoPadre;
    @JsonIgnore
    @Column(name = "CTR_VIGENTE")
    private Integer activo;
    @JsonIgnore
    @Column(name = "FECHA_INICIAL", insertable = false)
    private Date fechaInicial;
    @JsonIgnore
    @Column(name = "FECHA_FINAL", insertable = false)
    private Date fechaFinal;
}
