package ar.edu.unq.epersgeist.persistencia.dao.mongoDB;

import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.EspirituMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface EspirituMongoDAO extends MongoRepository<EspirituMongoDTO, String> {

        @Query(value = "{ 'coordenada': { " +
                "$near: { " +
                "$geometry: { " +
                "type: 'Point', " +
                "coordinates: [?0, ?1] " +
                "}, " +
                "$maxDistance: ?2, " +
                "$minDistance: ?3 " +
                "} " +
                "}, " +
                "'idRelacional': '?4' }")
        EspirituMongoDTO estaAdistancia_(@Param("x") double x,
                                         @Param("y") double y,
                                         @Param("max") int distanciaMax,
                                         @Param("min") int distanciaMin,
                                         @Param("idRelacional") String idRelacional);

        @Query(value = "{ \"idRelacional\" : ?0}")
        Optional<EspirituMongoDTO> findByIdRelational(@Param("idRelacional") String idRelacional);
}
