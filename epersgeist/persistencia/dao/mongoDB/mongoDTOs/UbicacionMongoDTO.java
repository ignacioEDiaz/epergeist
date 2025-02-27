package ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document("UbicacionMongoDTO")
public class UbicacionMongoDTO {

    @Id
    private String id;
    private String idRelacional;
    private String nombre;
    private GeoJsonPolygon  coordenadas;

    public UbicacionMongoDTO(String idRelacional, String nombre, GeoJsonPolygon  coordenadas) {
        this.idRelacional = idRelacional;
        this.nombre       = nombre;
        this.coordenadas  = coordenadas;
    }

    public static UbicacionMongoDTO desdeModelo(Ubicacion ubicacion) {
        UbicacionMongoDTO ubicacionMongoDTO = new UbicacionMongoDTO(
                String.valueOf(ubicacion.getId()),
                ubicacion.getNombre(),
                ubicacion.getCoordenada()
        );
        return  ubicacionMongoDTO;
    }

    public static UbicacionMongoDTO actualizarDesdeModelo(String idMongo, Ubicacion ubicacion) {
        UbicacionMongoDTO ubicacionMongoDTO = new UbicacionMongoDTO(
                String.valueOf(ubicacion.getId()),
                ubicacion.getNombre(),
                ubicacion.getCoordenada()
        );
        ubicacionMongoDTO.id = idMongo;
        return  ubicacionMongoDTO;
    }

}
