package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.*;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDao;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.EspirituMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.RegistroDominanteDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.EspirituMongoDTO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.exception.NumeroDePaginaMenorACeroException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {
    private final EspirituDAO espirituDAO;
    private final MediumDao mediumDao;
    private final UbicacionDAO ubicacionDAO;
    private final EspirituMongoDAO espirituMongoDAO;
    private final RegistroDominanteDAO registroDominanteDAO;

    public EspirituServiceImpl(EspirituDAO espirituDao, MediumDao mediumDao,
                               UbicacionDAO ubicacionDAO, EspirituMongoDAO espirituMongoDAO,RegistroDominanteDAO registroDominanteDAO) {
        this.mediumDao = mediumDao;
        this.espirituDAO = espirituDao;
        this.ubicacionDAO = ubicacionDAO;
        this.espirituMongoDAO= espirituMongoDAO;
        this.registroDominanteDAO = registroDominanteDAO;
    }

    @Override
    public void crear(Espiritu espiritu) {
        if (this.espirituDAO.findSameName(espiritu.getNombre()))
            throw new ExisteEntidadConMismoNombreException("Ya existe Espiritu con el mismo nombre en BD");
        this.espirituDAO.save(espiritu);
        this.espirituMongoDAO.save(EspirituMongoDTO.desdeModelo(espiritu));
    }


    @Override
    public void crear(Espiritu espiritu, Long ubicacionId) {
        if (this.espirituDAO.findSameName(espiritu.getNombre()))
            throw new ExisteEntidadConMismoNombreException("Ya existe Espiritu con el mismo nombre en BD");

        Ubicacion ubi = this.ubicacionDAO.findById(ubicacionId).get();
        espiritu.setUbicacion(ubi);
        this.espirituDAO.save(espiritu);
        this.espirituMongoDAO.save(EspirituMongoDTO.desdeModelo(espiritu));
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        if (espirituId == null)
            throw new EntidadNoExisteEnBDException("No existe ningun espiritu persistido con ese id!");

        Espiritu espirituPersistido = this.espirituDAO.findByIdAndNotDeleted(espirituId);

        if (espirituPersistido == null)
            throw new EntidadNoExisteEnBDException("No existe ningun espiritu persistido con ese id!");

        return espirituPersistido;
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        if(espiritu.getId() == null)
            throw new EntidadNoExisteEnBDException("No existe ningun espiritu persistido con ese id!");

        Espiritu newEspiritu = this.espirituDAO.findById(espiritu.getId())
                .orElseThrow(() -> new EntidadNoExisteEnBDException("No existe ningun espiritu persistido con ese id!"));

        espiritu.setId(newEspiritu.getId());

        EspirituMongoDTO espirituMongoDTO = espirituMongoDAO.findByIdRelational(String.valueOf(espiritu.getId())).get();

        this.espirituMongoDAO.save(EspirituMongoDTO.updateDesdeModelo(espirituMongoDTO.getId(), espiritu));
        this.espirituDAO.save(espiritu);
    }

    public void actualizarController(Espiritu espiritu) {
        if(espiritu.getId() == null)
            throw new EntidadNoExisteEnBDException("No existe ningun espiritu persistido con ese id!");

        Espiritu espirituRec = obtenerEntidadPorId(this.espirituDAO,espiritu.getId(),"espiritu");
        Ubicacion ubicacion  = obtenerEntidadPorId(this.ubicacionDAO,espirituRec.getUbicacion().getId(),"ubicacion");

        espiritu.setUbicacion(ubicacion);
        espiritu.setTipo(espirituRec.getTipo());
        espiritu.setMedium(espirituRec.getMedium());
        espiritu.setNivelDeConexion(espirituRec.getNivelDeConexion());
        espiritu.setEnergia(espirituRec.getEnergia());

        EspirituMongoDTO espirituMongoDTO = espirituMongoDAO.findByIdRelational(String.valueOf(espiritu.getId())).get();

        this.espirituMongoDAO.save(EspirituMongoDTO.updateDesdeModelo(espirituMongoDTO.getId(), espiritu));
        this.espirituDAO.save(espiritu);
    }


    @Override
    public void eliminar(Long espirituId) {
        try {
            Espiritu espiritu = recuperar(espirituId);
            espiritu.setDeleted(true);
            espiritu.setMedium(null);
            espiritu.setEnergia(0);
            espiritu.setNivelDeConexion(0);
            actualizar(espiritu);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new EntidadNoExisteEnBDException("El Espiritu no posee id!");
        }
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return this.espirituDAO.findAllAndNotDeleted();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        if(espirituId == null || mediumId == null)
            throw new EntidadNoExisteEnBDException("No existe ningun espiritu persistido con ese id!");

        Espiritu espiritu = this.espirituDAO.findByIdAndNotDeleted(espirituId);
        if (espiritu == null)
            throw new EntidadNoExisteEnBDException("No existe ningun espiritu persistido con ese id!");

        Medium medium = this.mediumDao.findById(mediumId)
                .orElseThrow(() -> new EntidadNoExisteEnBDException("No existe ningun medium persistido con ese id!"));

        if (!medium.mismaUbicacion(espiritu))
            throw  new DistintaUbicacionException("No estan en la misma ubicacion");

        if(checkearSiEsDominado(espirituId)) {
            throw new EspirituDominadoException("El Espiritu ya esta siendo dominado por otro Espiritu.");
        }
        else{
            borradoOConectarMediumCon_(medium, espiritu);
            espiritu.aumentarConexion(medium);
            this.espirituDAO.save(espiritu);
        }
        return medium;
    }

    private boolean checkearSiEsDominado(Long espirituId){
        List<Espiritu> dominantes = espirituDAO.espiritusQueDominanA_(espirituId);
        return !dominantes.isEmpty();
    }

    private void borradoOConectarMediumCon_(Medium medium, Espiritu espiritu){
        medium.conectarseAEspiritu(espiritu);
    }

    @Override
    public List<Espiritu> espiritusDemoniacos() {
        return this.espirituDAO.espiritusDemoniacos();
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina) {
        if (pagina < 0) {
            throw new NumeroDePaginaMenorACeroException("El número de página " + pagina + " es menor a 0");
        }
        if (cantidadPorPagina < 0) {
            throw new CantidadPorPaginaMenorACeroException("La cantidad por página " + cantidadPorPagina + " es menor ó igual 0");
        }

        Sort sort = direccion == Direccion.ASCENDENTE ? Sort.by("energia").ascending() : Sort.by("energia").descending();
        Pageable pageable = PageRequest.of(pagina, cantidadPorPagina, sort);

        Page<Espiritu> demoniosPage = espirituDAO.espiritusDemoniacos(pageable);

        return demoniosPage.getContent();
    }


    private <T> T obtenerEntidadPorId(JpaRepository<T, Long> dao, Long id, String entidadNombre) {
        return dao.findById(id).orElseThrow(() ->
                new EntidadNoExisteEnBDException("No existe ningún " + entidadNombre + " persistido con ese id!")
        );
    }

    public Set<String> recuperarHabilidadesDe(Long espirituId) {
        if (espirituId == null)
            throw new EntidadNoExisteEnBDException("No existe ningun espiritu persistido con ese id!");

        Espiritu espiritu = this.espirituDAO.findByIdAndNotDeleted(espirituId);

        if (espiritu == null)
            throw new EntidadNoExisteEnBDException("No existe ningun espiritu persistido con ese id!");

        return espiritu.getHabilidades();
    }

    @Override
    public void dominar(Long espirituDominanteId, Long espirituADominarId) {
        Espiritu espirituDominante = this.recuperarConUbi(espirituDominanteId);
        Espiritu espirituADominar = this.recuperarConUbi(espirituADominarId);

        validarEspirituADominar(espirituDominanteId, espirituADominar);

        if (!estaDentroDeDistancia(espirituDominante, espirituADominar))
            throw new DominacionInvalidoException("Distancia inválida para Dominar");

        ejecutarDominacion(espirituDominante, espirituADominar);
    }

    private void validarEspirituADominar(Long espirituDominanteId, Espiritu espirituADominar) {
        if (!espirituADominar.estaLibre() || espirituADominar.getEnergia() >= 50) {
            throw new DominacionInvalidoException("El espíritu no cumple con los requisitos de energía o no es libre");
        }
        if (espirituDAO.esDominadoPor(espirituDominanteId, espirituADominar.getId()) == 1) {
            throw new DominacionImposibleException("El espíritu no puede dominar a su dominante");
        }
        if (espirituDAO.getDominante(espirituADominar.getId()) != null) {
            throw new EspirituDominadoException("El espíritu ya ha sido dominado");
        }
    }

    private boolean estaDentroDeDistancia(Espiritu espirituDominante, Espiritu espirituADominar) {
        EspirituMongoDTO spiritDTO = espirituMongoDAO.estaAdistancia_(
                espirituDominante.getCoordenada().getX(),
                espirituDominante.getCoordenada().getY(),
                5000,
                2000,
                String.valueOf(espirituADominar.getId())
        );
        return spiritDTO != null;
    }

    private void ejecutarDominacion(Espiritu espirituDominante, Espiritu espirituADominar) {
        espirituDominante.addDominado(espirituADominar.getId());
        espirituDominante.addEspirituDominado(espirituADominar);
        espirituDAO.save(espirituDominante);
        registrarDominacion(espirituDominante, espirituADominar);
    }

    private void registrarDominacion(Espiritu espirituDominante, Espiritu espirituDominar){

            RegistroDominacion registro = new RegistroDominacion(
                    espirituDominante.getId(),
                    espirituDominar.getId(),
                    espirituDominante.getEnergia(),
                    espirituDominar.getEnergia(),
                    espirituDominante.getTipo(),
                    espirituDominar.getTipo(),
                    espirituDominante.getUbicacion().getId(),
                    LocalDate.now());
            this.registroDominanteDAO.save(registro);

    }

    @Override
    public void guardarConUbi(Espiritu espiritu){
        this.espirituDAO.save(espiritu);
        this.espirituMongoDAO.save(EspirituMongoDTO.desdeModelo(espiritu));
    }

    @Override
    public Espiritu recuperarConUbi(Long espirituId){
        Espiritu esp =this.espirituDAO.findById(espirituId)
                .orElseThrow(() -> new EntidadNoExisteEnBDException("Espíritu  no encontrado"));
        esp.setCoordenada(this.espirituMongoDAO.findByIdRelational(String.valueOf(espirituId)).get().getCoordenada());
        return esp;
    }

    @Override
    public List<Espiritu> recuperarDominantesDe_(Long espirituId){
        return this.espirituDAO.espiritusQueDominanA_(espirituId);
    }

    public List<Espiritu> getDominados(Long espirituId){
        return this.espirituDAO.getDominados(espirituId);
    }

}



