package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.*;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.HabilidadDAO;
import ar.edu.unq.epersgeist.servicios.HabilidadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class HabilidadServiceImpl implements HabilidadService {

    private final HabilidadDAO habilidadDAO;
    private final EspirituDAO espirituDAO;

    public HabilidadServiceImpl(HabilidadDAO habilidadDAO, EspirituDAO espirituDAO) {
        this.habilidadDAO = habilidadDAO;
        this.espirituDAO = espirituDAO;
    }


    @Override
    public Habilidad crear(Habilidad habilidad) {
        Habilidad habilidadExiste = this.obtenerHabilidadPorNombre(habilidadDAO,habilidad.getNombre(),false);
        if (habilidadExiste != null)
            throw new ExisteEntidadConMismoNombreException("Ya existe una habilidad con el nombre: " + habilidadExiste.getNombre());

        return this.habilidadDAO.save(habilidad);
    }

    @Override
    public List<Habilidad> recuperarTodas(){return this.habilidadDAO.findAll();}

    @Override
    public void descubrirHabilidad(String nombreHabilidadOrigen, String nombreHabilidadDestino, Condicion condicion) {
        if(nombreHabilidadOrigen.toLowerCase().equals(nombreHabilidadDestino.toLowerCase())) {
            throw new HabilidadNoSeRelacionaConsigoMismaException("Una habilidad no se puede relacionar consigo misma");
        }

        if (this.estanRelacionadas(nombreHabilidadOrigen, nombreHabilidadDestino)) {
            throw new HabilidadesYaRelacionadasException("Las habilidades ya se encuentran relacionadas");
        }

        Habilidad origen = this.obtenerHabilidadPorNombre(habilidadDAO, nombreHabilidadOrigen, true);
        Habilidad destino = this.obtenerHabilidadPorNombre(habilidadDAO, nombreHabilidadDestino, true);

        condicion.setHabilidad(destino);
        origen.agregarCondicion(condicion);

        habilidadDAO.save(origen);
    }

    private boolean estanRelacionadas(String nombreHabilidadOrigen, String nombreHabilidadDestino) {
        return habilidadDAO.existeRelacion(nombreHabilidadOrigen, nombreHabilidadDestino) ||
                habilidadDAO.existeRelacionOpuesta(nombreHabilidadOrigen, nombreHabilidadDestino);
    }

    @Override
    public void evolucionar(Long espirituId) {
        if(espirituId == null){
            throw new EntidadNoExisteEnBDException("El Espiritu no existe en la BD.");
        }
        Espiritu espirituAEvolucionar = this.espirituDAO.findById(espirituId).orElseThrow(() -> new EntidadNoExisteEnBDException("El Espiritu no existe en la BD."));
        Set<Habilidad> habilidadesAMutar = habilidadesPosibles(espirituId);
        espirituAEvolucionar.mutar(habilidadesAMutar);
        espirituDAO.save(espirituAEvolucionar);
    }

    @Override
    public Set<Habilidad> habilidadesPosibles(Long espirituId) {
        if(espirituId == null){
            throw new EntidadNoExisteEnBDException("El Espiritu no existe en la BD.");
        }
        Espiritu espiritu = this.espirituDAO.findById(espirituId).orElseThrow(() -> new EntidadNoExisteEnBDException("El Espiritu no existe en la BD."));

        Set<String> habilidadesActuales = espiritu.getHabilidades();

        Set<Habilidad> posibles = habilidadDAO.habilidadesCumplenCondicion(
                espiritu.getExorcismosResueltos(),
                espiritu.getExorcismosEvitados(),
                espiritu.getEnergia(),
                espiritu.getNivelDeConexion(),
                habilidadesActuales
        );

        return posibles;
    }

    @Override
    public List<Habilidad> caminoMasRentable(String nombreHabilidadOrigen, String nombreHabilidadDestino, Set<Evaluacion> evaluaciones) {
        if (!(habilidadDAO.existPathBetweenHabilidades(nombreHabilidadOrigen, nombreHabilidadDestino)))
            throw new HabilidadesNoConectadasException("Las Habilidades no estan conectadas!");
        List<Habilidad> camino = this.habilidadDAO.pathsBetweenHabilidades(nombreHabilidadOrigen, nombreHabilidadDestino, evaluaciones);
        if (!(this.existeUnCaminoMasRentable(camino, evaluaciones.size(), nombreHabilidadDestino))) {
            throw new MutacionImposibleException("No hay ningún camino con el set de evaluaciones proporcionado.");
        }
        return camino;
    }
    private boolean existeUnCaminoMasRentable(List<Habilidad> camino, int sizeEvaluaciones, String nombreHabilidadDestino){

        return !(camino.isEmpty()) && camino.stream().anyMatch(h -> h.getNombre().equals(nombreHabilidadDestino)) && camino.size() <= sizeEvaluaciones;
    }

    private Habilidad obtenerHabilidadPorNombre(HabilidadDAO dao, String habilidadNombre,boolean exceptionHabilidadNull) {
        if (habilidadNombre == null || habilidadNombre.trim().isEmpty())
            throw new EntidadNombreInvalidoException("Nombre no puede ser NULL o vacio.");

        Habilidad habilidad = dao.findByNameAndRelathionships(habilidadNombre).orElse(null);

        if(exceptionHabilidadNull && habilidad == null)
            throw new EntidadNoExisteEnBDException("No existe Habilidad.");

        return habilidad;
    }

    public Habilidad recuperarPorNombre(String nombre) {
        return this.habilidadDAO.findByName(nombre).orElse(null);
    }

    public Habilidad recuperarPorNombreYSusRelaciones(String nombre) {
        return this.habilidadDAO.findByNameAndRelathionships(nombre).orElse(null);
    }
    @Override
    public Set<Habilidad> habilidadesConectadas(String nombreHabilidad){
        if ((this.recuperarPorNombre(nombreHabilidad)== null )){ throw new EntidadNoExisteEnBDException("No existe Habilidad.");};
        return this.habilidadDAO.findConnectedHabilidades(nombreHabilidad);
    }

    public Set<Habilidad> recuperarHabilidadesYSusRelaciones(String nombreHabilidadInicio){
        return this.habilidadDAO.allHabilitiesRelated(nombreHabilidadInicio);
    }
    @Override
    public List<Habilidad> caminoMasMutable(Long espirituId, String nombreHabilidad) {
        if ((this.recuperarPorNombre(nombreHabilidad)== null )){ throw new EntidadNoExisteEnBDException("No existe Habilidad.");};
        Espiritu esp = this.espirituDAO.findById(espirituId).orElseThrow(() -> new EntidadNoExisteEnBDException("El Espiritu no existe en la BD."));
        return this.habilidadDAO.caminoMasMutable(esp.getEnergia(),esp.getNivelDeConexion(),esp.getExorcismosEvitados(),esp.getExorcismosResueltos(),nombreHabilidad);
    }

    @Override
    public List<Habilidad> caminoMenosMutable(Long espirituId, String nombreHabilidad) {

        if (this.recuperarPorNombre(nombreHabilidad) == null) {
            throw new EntidadNoExisteEnBDException("No existe Habilidad.");
        }
        Espiritu esp = this.espirituDAO.findById(espirituId)
                .orElseThrow(() -> new EntidadNoExisteEnBDException("El Espiritu no existe en la BD."));

        return this.habilidadDAO.caminoMenosMutable(
                esp.getEnergia(),
                esp.getNivelDeConexion(),
                esp.getExorcismosEvitados(),
                esp.getExorcismosResueltos(),
                nombreHabilidad
        );
    }
}
