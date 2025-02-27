package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Document()
public class RegistroDominacion {

    @Id
    private String id;
    private int idDominante;
    private int idDominado;
    private int energiaDominante;
    private int energiaDominado;
    private TipoEspiritu tipoDominante;
    private TipoEspiritu tipoDominado;
    private int  idUbicacion;
    private LocalDate fechaRegistro;

    public RegistroDominacion(Long idDominante, Long idDominado, int energiaDominante,int energiaDominado, TipoEspiritu tipoDominante, TipoEspiritu tipoDominado, Long idUbicacion, LocalDate fecha){
        this.idDominante = Math.toIntExact(idDominante);
        this.idDominado = Math.toIntExact(idDominado);
        this.energiaDominante = energiaDominante;
        this.energiaDominado = energiaDominado;
        this.tipoDominante = tipoDominante;
        this.tipoDominado = tipoDominado;
        this.idUbicacion =  Math.toIntExact(idUbicacion);
        this.fechaRegistro = fecha;
    }
}
