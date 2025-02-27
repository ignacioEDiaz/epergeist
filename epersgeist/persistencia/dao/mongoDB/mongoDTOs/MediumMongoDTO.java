package ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs;

import ar.edu.unq.epersgeist.modelo.Medium;
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
@Document("MediumMongoDTO")
public class MediumMongoDTO{
    @Id
    private String id;
    private String idRelacional;
    private String nombre;
    private GeoJsonPoint coordenada;

    public MediumMongoDTO(String idRelacional, String nombre, GeoJsonPoint coordenada){
        this.idRelacional = idRelacional;
        this.nombre       = nombre;
        this.coordenada   = coordenada;
    }

    public static MediumMongoDTO desdeModelo(Medium medium) {
        MediumMongoDTO mediumMongoDTO = new MediumMongoDTO(
                String.valueOf(medium.getId()),
                medium.getNombre(),
                medium.getCoordenada()
        );
        return  mediumMongoDTO;
    }

    public static MediumMongoDTO updateDesdeModelo(String id, Medium medium) {
        MediumMongoDTO mediumMongoDTO = new MediumMongoDTO(
                String.valueOf(medium.getId()),
                medium.getNombre(),
                medium.getCoordenada()
        );
        mediumMongoDTO.id = id;
        return  mediumMongoDTO;
    }
}
