package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Snapshot;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SnapshotDTO {
    private String id;
    private Map<String, List<Object>> sql;
    private Map<String, Object> mongo;
    private Map<String, List<String>> neo4j;
    private String fecha;

    public SnapshotDTO(String id, Map<String, List<Object>> sql, Map<String, List<String>> neo4j, Map<String, Object> mongo, String fecha) {
        this.id = id;
        this.sql = sql;
        this.mongo = mongo;
        this.neo4j = neo4j;
        this.fecha = fecha;
    }

    public Snapshot aModelo() {
        return new Snapshot(sql, neo4j, mongo, fecha);
    }

    public static SnapshotDTO desdeModelo(Snapshot snapshot) {
        return new SnapshotDTO(snapshot.getId(), snapshot.getSql(), snapshot.getNeo4j(), snapshot.getMongo(), snapshot.getFecha());
    }
}
