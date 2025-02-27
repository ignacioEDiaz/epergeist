package ar.edu.unq.epersgeist.persistencia.dao.mongoDB;

import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.UbicacionMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UbicacionMongoDAO extends MongoRepository<UbicacionMongoDTO, String> {

    @Query("{ coordenadas: { $geoIntersects: { $geometry: { type: 'Point', coordinates: [?1, ?0] } } } }")
    UbicacionMongoDTO findUbicacionByCoordenada(Double latitud, Double longitud);

    @Query(value = "{ \"idRelacional\" : ?0}")
    Optional<UbicacionMongoDTO> findByIdRelational(@Param("idRelacional") String idRelacional);

    @Query(value = "{'idRelacional' : :#{#idRelacional}}", delete = true)
    void deleteByIdRelational(@Param("idRelacional") String idRelacional);
}
