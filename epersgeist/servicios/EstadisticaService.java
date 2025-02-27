package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;

public interface EstadisticaService {
    void crearSnapshot();
    Snapshot obtenerSnapshot(LocalDate fecha);

    Ubicacion ubicacionMasDominada(LocalDate fechaInicio, LocalDate fechaFin);
    Espiritu espirituMasDominante(LocalDate fechaInicio, LocalDate fechaFin);
}
