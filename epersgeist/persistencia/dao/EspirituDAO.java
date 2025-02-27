package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends JpaRepository<Espiritu, Long> {

    @Query("FROM Espiritu e WHERE e.tipo = 'DEMONIO' AND NOT e.deleted")
    Page<Espiritu> espiritusDemoniacos(Pageable pageable);

    @Query("FROM Espiritu e WHERE e.tipo = 'DEMONIO' AND NOT e.deleted ORDER BY e.energia DESC")
    List<Espiritu> espiritusDemoniacos();

    @Query("UPDATE Espiritu e SET e.medium = NULL WHERE e.id = :espId")
    void desconectarEspiritusDeMedium(Long espId);

    @Query("SELECT COUNT(e) > 0 FROM Espiritu e WHERE e.nombre = :name")
    boolean findSameName(@Param("name") String espirituNombre);

    @Query("FROM Espiritu e WHERE e.id = :id AND NOT e.deleted")
    Espiritu findByIdAndNotDeleted(@Param("id") Long espirituId);

    @Query("FROM Espiritu e WHERE NOT e.deleted")
    List<Espiritu> findAllAndNotDeleted();

    @Query("SELECT ed FROM Espiritu ed WHERE :id MEMBER OF ed.dominados")
    List<Espiritu> espiritusQueDominanA_(@Param ("id") Long espirituId);

    @Query(value = "SELECT espiritu_id FROM espiritu_dominados WHERE dominados = :espirituId", nativeQuery = true)
    Long getDominante(@Param("espirituId") Long espirituId);

    @Query(value= "Select e.id, (count(cantidad.id) * 2) as bonificacion " +
            "From epers_epergeist_spring.espiritu e " +
            "Join epers_epergeist_spring.espiritu_dominados ed On e.id = ed.espiritu_id " +
            "Join (Select id, count(habilidades) as total " +
            "    From epers_epergeist_spring.espiritu e " +
            "    Join epers_epergeist_spring.espiritu_habilidades h On e.id = h.espiritu_id " +
            "    group by id " +
            "    having total > 3) as cantidad On ed.dominados = cantidad.id " +
            "Where e.id = :espirituId " +
            "group by id ", nativeQuery = true)
    List<Object[]> obtenerBonificacionPorEspiritu(@Param("espirituId") Long espirituId);

    @Query(value = "SELECT COUNT(*) FROM espiritu_dominados  WHERE espiritu_id = :espirituDominante AND dominados = :espirituDominado", nativeQuery = true)
    Long esDominadoPor(@Param("espirituDominado") Long espirituDominado, @Param("espirituDominante") Long espirituDominante);

    @Query(value = "SELECT * FROM Espiritu e JOIN espiritu_dominados d ON e.id = d.dominados WHERE d.espiritu_id = :espirituDominante", nativeQuery = true)
    List<Espiritu> getDominados(@Param("espirituDominante") Long espirituDominante);
}