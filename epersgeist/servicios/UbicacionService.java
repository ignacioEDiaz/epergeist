package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import java.util.List;

public interface UbicacionService {
    void crear(Ubicacion ubicacion);
    Ubicacion recuperar(Long ubicacionId);
    List<Ubicacion> recuperarTodos();
    void actualizar(Ubicacion ubicacion);
    void eliminar(Long ubicacionId);
    List<Espiritu> espiritusEn(Long ubicacionId);
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);
    ReporteSantuarioMasCorrupto santuarioCorrupto();

    void actualizar(Ubicacion ubicacion, Long id);
}
