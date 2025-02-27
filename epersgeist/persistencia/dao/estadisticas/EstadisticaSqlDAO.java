package ar.edu.unq.epersgeist.persistencia.dao.estadisticas;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadisticaSqlDAO extends JpaRepository<Espiritu, Long> {

    @Query(value = "SELECT JSON_ARRAYAGG(JSON_OBJECT("
            + "'espiritu_id', espiritu_id, "
            + "'habilidades', habilidades "
            + ")) as json_output FROM Espiritu_habilidades", nativeQuery = true)
    List<String> crearSnapshotEspiritusHabilidadesSql();

    @Query(value = "SELECT IFNULL(JSON_ARRAYAGG(JSON_OBJECT(" +
            "'espiritu_id', espiritu_id, " +
            "'dominados', dominados " +
            ")), '[]') as json_output " +
            "FROM Espiritu_dominados", nativeQuery = true)
    List<String> crearSnapshotEspirituDominadosSql();
}
