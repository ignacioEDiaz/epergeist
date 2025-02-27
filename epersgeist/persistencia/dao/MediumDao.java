package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MediumDao extends JpaRepository<Medium, Long> {

    @Query("From Espiritu e WHERE e.medium.id = :mediumId")
    List<Espiritu> espiritus(@Param("mediumId")Long mediumId);

    @Query("SELECT COUNT(e) > 0 FROM Medium e WHERE e.nombre = :name")
    boolean findSameName(@Param("name") String mediumNombre);

}
