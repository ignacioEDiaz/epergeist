package ar.edu.unq.epersgeist.persistencia.dao.estadisticas;

import ar.edu.unq.epersgeist.modelo.Habilidad;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;


public interface EstadisticaNeoDAO extends Neo4jRepository<Habilidad, Long> {

    @Query("MATCH(h:Habilidad) RETURN h")
    List<Habilidad> crearSnapshotNeo4j();

    @Query("MATCH (n:Habilidad)" +
            "OPTIONAL MATCH (n)-[r*1..]-(m)" +
            "RETURN n, collect(r), collect(m)")
    List<Habilidad> allHabilitiesRelated();

}