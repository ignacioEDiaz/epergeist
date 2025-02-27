package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import java.util.List;

public interface EspirituService {
    void crear(Espiritu espiritu);
    Espiritu recuperar(Long espirituId);
    void actualizar(Espiritu espiritu);
    void eliminar(Long espirituId);
    List<Espiritu> recuperarTodos();
    Medium conectar(Long espirituIdm, Long mediumId);
    List<Espiritu> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina);
    List<Espiritu> espiritusDemoniacos();

    void crear(Espiritu nuevoEspiritu, Long ubicacionId);
    void actualizarController(Espiritu espirituActualizar);
    void dominar(Long espirituDominanteId, Long espirituADominarId);
    void guardarConUbi(Espiritu espiritu);
    Espiritu recuperarConUbi(Long espirituId);
    List<Espiritu> recuperarDominantesDe_(Long espirituId);

}
