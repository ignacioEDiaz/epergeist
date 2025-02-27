package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.HabilidadNoSeRelacionaConsigoMismaException;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Node
public class Habilidad {

    @Id
    @GeneratedValue
    private Long id;
    private String nombre;

    @Relationship(type = "Condicion", direction = Relationship.Direction.OUTGOING)
    private Set<Condicion> condiciones = new HashSet<>();

    public Habilidad(String nombre) {
        this.nombre = nombre;
    }

    public void agregarCondicion(Condicion condicion) {
        if (Objects.equals(this.getNombre().toLowerCase(), condicion.getHabilidad().getNombre().toLowerCase())){
            throw new HabilidadNoSeRelacionaConsigoMismaException("Una habilidad no se puede relacionar consigo misma");
        }
        this.condiciones.add(condicion);
    }
}