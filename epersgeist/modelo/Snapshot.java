package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
@Document(collection = "snapshots")
public class Snapshot implements Serializable {
    @Id
    private String id;
    private Map<String, List<Object>> sql;
    private Map<String, Object> mongo;
    private Map<String, List<String>> neo4j;
    private String fecha;

    public Snapshot(Map<String, List<Object>> sql, Map<String, List<String>> neo4j, Map<String, Object> mongo, String fecha) {
        this.sql = sql;
        this.mongo = mongo;
        this.neo4j = neo4j;
        this.fecha = fecha;
    }


}
