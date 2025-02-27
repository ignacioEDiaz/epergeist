package ar.edu.unq.epersgeist.persistencia.dao.estadisticas;

import ar.edu.unq.epersgeist.modelo.Snapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstadisticaSnapshotDAO extends MongoRepository<Snapshot, Long> {
    Snapshot findByFecha(String fecha);
}
