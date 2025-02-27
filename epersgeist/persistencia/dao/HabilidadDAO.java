package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HabilidadDAO extends Neo4jRepository<Habilidad,Long> {


    @Query("MATCH(h: Habilidad {nombre: $nombre }) RETURN h")
    Optional<Habilidad> findByName(@Param("nombre") String nombre);

    @Query("MATCH (h:Habilidad {nombre: $nombre}) " +
            "OPTIONAL MATCH (h)-[r]->(c:Habilidad) " +
            "OPTIONAL MATCH (c)-[r2]->(c2:Habilidad) " +
            "RETURN h, collect(r) as relaciones, collect(c) as habilidadesRelacionadas, " +
            "collect(r2) as relacionesDeHabilidadesRelacionadas, collect(c2) as habilidadesDeHabilidadesRelacionadas")
    Optional<Habilidad> findByNameAndRelathionships(@Param("nombre") String nombre);

    @Query("MATCH (h:Habilidad {nombre: $nombre})-[:Condicion]->(c:Habilidad) RETURN c")
    Set<Habilidad> findConnectedHabilidades(@Param("nombre") String nombre);

    @Query("MATCH (n:Habilidad {nombre: $nombre})" +
            "OPTIONAL MATCH (n)-[r*1..]-(m)" +
            "RETURN n, collect(r), collect(m)")
    Set <Habilidad> allHabilitiesRelated(String nombre);

    @Query("OPTIONAL MATCH p = (c:Habilidad {nombre: $nombreOrigen})-[:Condicion*]->(d:Habilidad {nombre: $nombreDestino}) " +
            "RETURN p IS NOT NULL AS existPath " +
            "LIMIT 1")
    Boolean existPathBetweenHabilidades(@Param("nombreOrigen")String nombreOrigen,@Param("nombreDestino") String nombreDestino);

    @Query("MATCH path=(start:Habilidad {nombre: $nombreHabilidad})-[:Condicion*]->(end) " +
            "WHERE all(rel IN relationships(path) WHERE " +
            "    (rel.evaluacion = 'CANTIDAD_DE_ENERGIA' AND rel.nivel <= $energia) OR " +
            "    (rel.evaluacion = 'NIVEL_DE_CONEXION' AND rel.nivel <= $nivelDeConexion) OR " +
            "    (rel.evaluacion = 'EXORCISMOS_EVITADOS' AND rel.nivel <= $exorcismosEvitados) OR " +
            "    (rel.evaluacion = 'EXORCISMOS_RESUELTOS' AND rel.nivel <= $exorcismosResueltos) " +
            ") " +
            "RETURN nodes(path)[1..] AS habilidades " +
            "ORDER BY length(path) DESC " +
            "LIMIT 1")
    List<Habilidad> caminoMasMutable(
            @Param("energia") int energia,
            @Param("nivelDeConexion") int nivelDeConexion,
            @Param("exorcismosEvitados") int exorcismosEvitados,
            @Param("exorcismosResueltos") int exorcismosResueltos,
            @Param("nombreHabilidad") String nombreHabilidad);

    @Query("MATCH path=(start:Habilidad {nombre: $nombreHabilidad})-[:Condicion*]->(end) " +
            "WHERE all(rel IN relationships(path) WHERE " +
            "    (rel.evaluacion = 'CANTIDAD_DE_ENERGIA' AND rel.nivel <= $energia) OR " +
            "    (rel.evaluacion = 'NIVEL_DE_CONEXION' AND rel.nivel <= $nivelDeConexion) OR " +
            "    (rel.evaluacion = 'EXORCISMOS_EVITADOS' AND rel.nivel <= $exorcismosEvitados) OR " +
            "    (rel.evaluacion = 'EXORCISMOS_RESUELTOS' AND rel.nivel <= $exorcismosResueltos) " +
            ") " +
            "RETURN nodes(path)[1..] AS habilidades " +
            "ORDER BY length(path) ASC " +
            "LIMIT 1")
    List<Habilidad> caminoMenosMutable(
            @Param("energia") int energia,
            @Param("nivelDeConexion") int nivelDeConexion,
            @Param("exorcismosEvitados") int exorcismosEvitados,
            @Param("exorcismosResueltos") int exorcismosResueltos,
            @Param("nombreHabilidad") String nombreHabilidad);

    @Query("MATCH p = (h:Habilidad {nombre: $nombreHabilidadOrigen})-[:Condicion*]->(d:Habilidad {nombre: $nombreHabilidadDestino}) " +
            "WHERE all(rel in relationships(p) WHERE rel.evaluacion IN $evaluaciones) " +
            "WITH p, nodes(p) AS habilidades " +
            "ORDER BY length(p) ASC " +
            "LIMIT 1 " +
            "RETURN habilidades")
    List<Habilidad> pathsBetweenHabilidades(@Param("nombreHabilidadOrigen") String nombreHabilidadOrigen,
                                               @Param("nombreHabilidadDestino") String nombreHabilidadDestino,
                                               @Param("evaluaciones") Set<Evaluacion> evaluaciones);

    @Query("MATCH (h:Habilidad)-[cond:Condicion]->(hDestino:Habilidad) " +
            "WHERE h.nombre IN $habilidadesActuales " +
            "AND (" +
            "(cond.evaluacion = 'EXORCISMOS_RESUELTOS' AND cond.nivel <= $exorcismosResueltos) OR " +
            "(cond.evaluacion = 'EXORCISMOS_EVITADOS' AND cond.nivel <= $exorcismosEvitados) OR " +
            "(cond.evaluacion = 'CANTIDAD_DE_ENERGIA' AND cond.nivel <= $energia) OR " +
            "(cond.evaluacion = 'NIVEL_DE_CONEXION' AND cond.nivel <= $nivelDeConexion)" +
            ") " +
            "RETURN collect(hDestino)")
    Set<Habilidad> habilidadesCumplenCondicion(@Param("exorcismosResueltos") int exorcismosResueltos,
                                               @Param("exorcismosEvitados") int exorcismosEvitados,
                                               @Param("energia") int energia,
                                               @Param("nivelDeConexion") int nivelDeConexion,
                                               @Param("habilidadesActuales") Set<String> habilidadesActuales);

    @Query("MATCH (h1:Habilidad {nombre: $nombreHabilidadOrigen})-[r:Condicion]->(h2:Habilidad {nombre: $nombreHabilidadDestino}) " +
            "RETURN COUNT(r) > 0")
    boolean existeRelacion(@Param("nombreHabilidadOrigen") String nombreHabilidadOrigen, @Param("nombreHabilidadDestino") String nombreHabilidadDestino);

    @Query("MATCH (h1:Habilidad {nombre: $nombreHabilidadDestino})-[r:Condicion]->(h2:Habilidad {nombre: $nombreHabilidadOrigen}) " +
            "RETURN COUNT(r) > 0")
    boolean existeRelacionOpuesta(@Param("nombreHabilidadOrigen") String nombreHabilidadOrigen, @Param("nombreHabilidadDestino") String nombreHabilidadDestino);
}


