package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.utility.PorcentajeAtaqueExitoso;
import java.util.List;

public interface MediumService {

        void crear(Medium medium);
        Medium recuperar(Long mediumId);
        void actualizar(Medium medium);
        void eliminar(Long mediumId);
        List<Medium> recuperarTodos();
        List<Espiritu> espiritus(Long mediumId);
        void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar, PorcentajeAtaqueExitoso ataque);
        Espiritu invocar(Long mediumId, Long espirituId);
        void descansar(Long mediumId);
        void mover(Long mediumId, Long ubicacionId);
        void crear(Medium nuevoMedium, Long ubicacionId);
        void actualizar(Medium medium, Long id);
        void mover(Long mediumId, Double latitud, Double longitud);
        void guardarConUbi(Medium medium);
}
