package ar.edu.unq.epersgeist.persistencia.dao.mongoDB;

import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.MediumMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface MediumMongoDAO extends MongoRepository<MediumMongoDTO, String> {
    @Query(value = "{ \"idRelacional\" : ?0}")
    Optional<MediumMongoDTO> findByIdRelational(@Param("idRelacional") String idRelacional);
}
