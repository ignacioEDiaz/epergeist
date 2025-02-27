package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import java.util.List;
import java.util.Set;

public interface HabilidadService {
    Habilidad crear(Habilidad habilidad);
    void descubrirHabilidad(String nombreHabilidadOrigen, String nombreHabilidadDestino, Condicion condicion);
    void evolucionar(Long espirituId);
    Set<Habilidad> habilidadesConectadas(String nombreHabilidad);
    Set<Habilidad> habilidadesPosibles(Long espirituId);
    List<Habilidad> caminoMasRentable(String nombreHabilidadOrigen, String nombreHabilidadDestino, Set<Evaluacion> evaluaciones);
    List<Habilidad> caminoMasMutable(Long espirituId, String nombreHabilidad);
    List<Habilidad> caminoMenosMutable(Long espirituId, String nombreHabilidad);
    List<Habilidad> recuperarTodas();
}
