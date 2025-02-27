package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.ValoresParaEvaluacionInvalidos;
import lombok.*;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@RelationshipProperties
public class Condicion {

    @RelationshipId
    private Long id;

    private Evaluacion evaluacion;
    private int nivel;

    @TargetNode
    private Habilidad habilidad;

    public Condicion(Evaluacion evaluacion, int nivel, Habilidad habilidad) {
        if (nivel <= 0 || (nivel > 100 && (evaluacion == Evaluacion.NIVEL_DE_CONEXION || evaluacion == Evaluacion.CANTIDAD_DE_ENERGIA))){
            throw new ValoresParaEvaluacionInvalidos("Ese nivel no tiene sentido para esa evaluacion");
        }
        this.evaluacion = evaluacion;
        this.nivel = nivel;
        this.habilidad = habilidad;
    }

    public Condicion(Evaluacion evaluacion, int nivel) {
        if (nivel <= 0 || (nivel > 100 && (evaluacion == Evaluacion.NIVEL_DE_CONEXION || evaluacion == Evaluacion.CANTIDAD_DE_ENERGIA))) {
            throw new ValoresParaEvaluacionInvalidos("Ese nivel no tiene sentido para esa evaluacion");
        }
        this.evaluacion = evaluacion;
        this.nivel = nivel;
    }
}
