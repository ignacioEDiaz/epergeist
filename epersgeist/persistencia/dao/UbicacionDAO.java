package ar.edu.unq.epersgeist.persistencia.dao;

import java.util.List;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionDAO extends JpaRepository<Ubicacion, Long> {

    @Query("FROM Espiritu e where e.ubicacion.id = :id AND NOT e.deleted")
    List<Espiritu> findAllEspiritById(@Param("id") Long ubicacionId);

    @Query("SELECT m FROM Medium m WHERE m.ubicacion.id = :id AND m.espiritus IS EMPTY")
    List<Medium> finAllMediumOutOFEspirityById(@Param("id") Long ubicacionId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Ubicacion u WHERE u.nombre = :name")
    boolean findSameName(@Param("name") String ubicacionNombre);

    @Query("SELECT u " +
            "FROM Ubicacion u " +
            "LEFT JOIN Espiritu e ON u.id = e.ubicacion.id " +
            "WHERE u.tipo = 'SANTUARIO' " +
            "GROUP BY u.id " +
            "ORDER BY (SUM(CASE WHEN e.tipo = 'DEMONIO' AND NOT e.deleted THEN 1 ELSE 0 END) - SUM(CASE WHEN e.tipo = 'ANGEL' AND NOT e.deleted THEN 1 ELSE 0 END)) DESC, u.id ASC "+
            "LIMIT 1")
    Ubicacion ubicacionMasCorrupta();

    @Query("SELECT m " +
            "FROM Medium m " +
            "LEFT JOIN m.espiritus e " +
            "ON m.id = e.medium.id " +
            "WHERE m.ubicacion.id = :ubicacionId " +
            "GROUP BY m.id " +
            "ORDER BY COUNT(CASE WHEN e.tipo = 'DEMONIO' AND NOT e.deleted THEN 1 ELSE NULL END) DESC, m.id ASC "+
            "LIMIT 1")
    Medium mediumMasDemoniaco(@Param("ubicacionId") Long ubicacionId);

    @Query("SELECT COUNT(e) FROM Espiritu e " +
            "WHERE e.ubicacion.id = :ubicacionId AND e.tipo = 'DEMONIO' AND NOT e.deleted")
    Integer cantDemonios(@Param("ubicacionId") Long ubicacionId);

    @Query("SELECT COUNT(e) FROM Espiritu e " +
            "WHERE e.ubicacion.id = :ubicacionId AND e.tipo = 'DEMONIO' AND NOT e.deleted " +
            "AND e.medium IS NULL")
   Integer demoniosLibres(@Param("ubicacionId") Long ubicacionId);



}

