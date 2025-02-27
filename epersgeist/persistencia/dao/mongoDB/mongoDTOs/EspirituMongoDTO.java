package ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document("EspirituMongoDTO")
public class EspirituMongoDTO {
    @Id
    private String id;
    private String idRelacional;
    private String nombre;
    private GeoJsonPoint coordenada;

    public EspirituMongoDTO(String idRelacional, String nombre, GeoJsonPoint coordenada) {
        this.idRelacional = idRelacional;
        this.nombre = nombre;
        this.coordenada = coordenada;
    }

    public static EspirituMongoDTO desdeModelo(Espiritu espiritu) {
        return new EspirituMongoDTO(
                String.valueOf(espiritu.getId()),
                espiritu.getNombre(),
                espiritu.getCoordenada()
        );
    }

    public static EspirituMongoDTO updateDesdeModelo(String id,Espiritu espiritu) {
        EspirituMongoDTO espirituMongoDTO = new EspirituMongoDTO(
                String.valueOf(espiritu.getId()),
                espiritu.getNombre(),
                espiritu.getCoordenada()
        );
        espirituMongoDTO.id = id;
        return espirituMongoDTO;
    }

}