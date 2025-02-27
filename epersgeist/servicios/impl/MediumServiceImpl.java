package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.*;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.HabilidadDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDao;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.EspirituMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.MediumMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.UbicacionMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.MediumMongoDTO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.UbicacionMongoDTO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.utility.PorcentajeAtaqueExitoso;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumDao mediumDAO;
    private final EspirituDAO espirituDAO;
    private final UbicacionDAO ubicacionDAO;
    private final HabilidadDAO habilidadDAO;

    private final UbicacionMongoDAO ubicacionMongoDAO;
    private final MediumMongoDAO mediumMongoDAO;
    private final EspirituMongoDAO espirituMongoDAO;

    public MediumServiceImpl(MediumDao mediumDAO, EspirituDAO espirituDAO, UbicacionDAO ubicacionDAO, HabilidadDAO habilidadDAO, UbicacionMongoDAO ubicacionMongoDAO, MediumMongoDAO mediumMongoDAO, EspirituMongoDAO espirituMongoDAO) {
        this.mediumDAO         = mediumDAO;
        this.espirituDAO       = espirituDAO;
        this.ubicacionDAO      = ubicacionDAO;
        this.habilidadDAO      = habilidadDAO;
        this.ubicacionMongoDAO = ubicacionMongoDAO;
        this.mediumMongoDAO    = mediumMongoDAO;
        this.espirituMongoDAO  = espirituMongoDAO;
    }

    @Override
    public void crear(Medium medium) {
        if (this.mediumDAO.findSameName(medium.getNombre()))
            throw new ExisteEntidadConMismoNombreException("Ya existe Medium con el mismo nombre en BD");
        this.mediumDAO.save(medium);
        this.mediumMongoDAO.save(MediumMongoDTO.desdeModelo(medium));
    }

    @Override
    public void crear(Medium nuevoMedium, Long ubicacionId) {
        Ubicacion ubicacion = obtenerEntidadPorId(this.ubicacionDAO, ubicacionId,"Ubicacion");
        nuevoMedium.setUbicacion(ubicacion);
        nuevoMedium.setEspiritus(null);
        this.mediumDAO.save(nuevoMedium);
        this.mediumMongoDAO.save(MediumMongoDTO.desdeModelo(nuevoMedium));
    }

    @Override
    public void actualizar(Medium medium, Long mediumId) {
        Medium mediumRecuperado  = obtenerEntidadPorId(this.mediumDAO, mediumId, "Medium");

        medium.setId(mediumId);
        medium.setMana(mediumRecuperado.getMana());
        medium.setUbicacion(mediumRecuperado.getUbicacion());
        medium.setEspiritus(mediumRecuperado.getEspiritus());

        MediumMongoDTO mediumMongo = this.mediumMongoDAO.findByIdRelational(String.valueOf(mediumId)).get();

        this.mediumMongoDAO.save(MediumMongoDTO.updateDesdeModelo(mediumMongo.getId(),medium));
        this.mediumDAO.save(medium);
    }

    @Override
    public void mover(Long mediumId, Double latitud, Double longitud) {
        UbicacionMongoDTO ubicacionPersistida = ubicacionMongoDAO.findUbicacionByCoordenada(latitud,longitud);
        if (ubicacionPersistida == null)
            throw new UbicacionNulaException("No existe una ubicacion con esa coordenada.");

        Ubicacion ubicacion = obtenerEntidadPorId(ubicacionDAO,Long.valueOf(ubicacionPersistida.getIdRelacional()),"Ubicacion");
        ubicacion.setCoordenada(ubicacionPersistida.getCoordenadas());

        Medium medium = obtenerEntidadPorId(mediumDAO,mediumId,"Medium");

        medium.mover(ubicacion);
        medium.setCoordenada(new GeoJsonPoint(longitud,latitud));

        mediumDAO.save(medium);

        MediumMongoDTO mediumMongo = mediumMongoDAO.findByIdRelational(String.valueOf(medium.getId())).get();
        mediumMongo.setCoordenada(medium.getCoordenada());
        mediumMongoDAO.save(mediumMongo);

        medium.getEspiritus().stream().forEach((e) ->{
            e.setCoordenada(new GeoJsonPoint(longitud,latitud));
            espirituMongoDAO.save(EspirituMongoDTO.desdeModelo(e));
        });
    }

    @Override
    public Medium recuperar(Long mediumId) {
        try {
            return this.mediumDAO.findById(mediumId).orElseThrow(
                    () -> new EntidadNoExisteEnBDException("No existe ningun medium persistido con ese id!"));
        } catch (InvalidDataAccessApiUsageException e) {
            throw new EntidadNoExisteEnBDException("El medium no posee id!");
        }

    }

    @Override
    public void actualizar(Medium medium) {
        try {
            Medium newMedium = this.mediumDAO.findById(medium.getId())
                    .orElseThrow(() -> new EntidadNoExisteEnBDException("No existe ningun espitu persistido con ese id!"));

            medium.setId(newMedium.getId());

            MediumMongoDTO mediumMongo = this.mediumMongoDAO.findByIdRelational(String.valueOf(newMedium.getId())).get();

            this.mediumMongoDAO.save(MediumMongoDTO.updateDesdeModelo(mediumMongo.getId(),medium));
            this.mediumDAO.save(medium);

        } catch (InvalidDataAccessApiUsageException e) {
            throw new EntidadNoExisteEnBDException("El medium no posee id!");
        }
    }

    @Override
    public void eliminar(Long mediumId) {
        try {
            this.mediumDAO.deleteById(mediumId);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new EntidadNoExisteEnBDException("El medium no posee id!");
        }
    }

    @Override
    public List<Medium> recuperarTodos() {
        return this.mediumDAO.findAll();
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return this.mediumDAO.espiritus(mediumId);
    }

    public Espiritu invocar(Long mediumId, Long espirituId) {
        try {
            Medium medium = this.mediumDAO.findById(mediumId)
                    .orElseThrow(() -> new EntidadNoExisteEnBDException("Medium no encontrado"));
            medium.setCoordenada(this.mediumMongoDAO.findByIdRelational(String.valueOf(mediumId)).get().getCoordenada());

            Espiritu espiritu = this.espirituDAO.findByIdAndNotDeleted(espirituId);
            if(espiritu == null){
                throw new EntidadNoExisteEnBDException("Espíritu  no encontrado");
            }
            espiritu.setCoordenada(this.espirituMongoDAO.findByIdRelational(String.valueOf(espirituId)).get().getCoordenada());

            if (!(medium.puedeInvocarA(espiritu)))
                throw new InvalidacionDeTipoEnUbicacion(medium.getUbicacion(), espiritu);

            EspirituMongoDTO espirituCercano = this.espirituMongoDAO.estaAdistancia_(
                    medium.getCoordenada().getX(),
                    medium.getCoordenada().getY(),
                    100000,
                    0,
                    String.valueOf(espirituId)
            );

            if (espirituCercano == null) {
                throw new EspirituMuyLejanoException("El espíritu está demasiado lejos para invocarlo.");
            }

            Espiritu espirituActualizado = medium.invocarEspiritu(espiritu);
            mediumDAO.save(medium);
            espirituDAO.save(espirituActualizado);
            return espirituActualizado;

        } catch (InvalidDataAccessApiUsageException e) {
            throw new EntidadNoExisteEnBDException("El espiritu o medium no poseen id!");
        }
    }

    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar, PorcentajeAtaqueExitoso ataque) {
        Medium mediumExorcista  = obtenerEntidadPorId(mediumDAO,idMediumExorcista,"medium exorcista");
        Medium mediumAExorcizar = obtenerEntidadPorId(mediumDAO,idMediumAExorcizar,"medium a exorcizar");

        mediumExorcista.setPrctAtckExitoso(ataque);

        if (!mediumExorcista.checkearSiTieneAngeles())
            throw new ExorcistaSinAngelesException(mediumExorcista);

        this.elevarEspiritus(mediumExorcista.listaEspiritusAngeles());
        mediumExorcista.exorcizarA(mediumAExorcizar);

        actualizarAngeles(mediumExorcista.listaEspiritusAngeles());
        actualizarDemonios(mediumAExorcizar.listaEspiritusDemoniacos());
    }

    private void elevarEspiritus(List<Espiritu> lsEspiritu){
        lsEspiritu.stream().forEach((a)->{
            List<Object[]> bonus = this.espirituDAO.obtenerBonificacionPorEspiritu(a.getId());
            if (!bonus.isEmpty()) {
                Object[] resultado = bonus.get(0);
                Integer bonificacion = ((Long) resultado[1]).intValue();
                a.setBonificacion(bonificacion);
            }
        });
    }

    private void actualizarDemonios(List<Espiritu> demoniosDelExorcizado) {
        for (Espiritu demonio : demoniosDelExorcizado) {
            checkearSiPuedeMutar(demonio);
            if (demonio.getMedium() == null) {
                espirituDAO.desconectarEspiritusDeMedium(demonio.getId());
            }
        }
    }

    private void checkearSiPuedeMutar(Espiritu espiritu){

            Set<Habilidad> posibles = habilidadDAO.habilidadesCumplenCondicion(espiritu.getExorcismosResueltos(),espiritu.getExorcismosEvitados(),
                                      espiritu.getEnergia(), espiritu.getNivelDeConexion(), espiritu.getHabilidades());
            espiritu.mutar(posibles);

    }

    private void actualizarAngeles(List<Espiritu> angelesDelExorcista) {
        for (Espiritu angel : angelesDelExorcista) {
            checkearSiPuedeMutar(angel);
            espirituDAO.save(angel);
        }
    }

    @Override
    public void descansar(Long mediumId) {
        if (mediumId == null)
            throw new EntidadNoExisteEnBDException("No existe ningun medium persistido con ese id!");

        Medium medium = mediumDAO.findById(mediumId)
                .orElseThrow(() -> new EntidadNoExisteEnBDException("No existe ningun medium persistido con ese id!"));
        medium.descansar();
        mediumDAO.save(medium);
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        try {
            Medium medium = mediumDAO.findById(mediumId).orElseThrow(
                    () -> new EntidadNoExisteEnBDException("No existe ningun medium persistido con ese id!"));
            Ubicacion ubicacion = ubicacionDAO.findById(ubicacionId).orElseThrow(
                    () -> new EntidadNoExisteEnBDException("No existe ninguna ubicacion persistida con ese id!"));

            medium.mover(ubicacion);
            mediumDAO.save(medium);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new EntidadNoExisteEnBDException("El meidum o la ubicacion no poseen id!");
        }
    }

    private <T> T obtenerEntidadPorId(JpaRepository<T, Long> dao, Long id, String entidadNombre) {
        if (id == null)
            throw new EntidadNoExisteEnBDException("No existe ningun " + entidadNombre + " persistido con ese id!");

        return dao.findById(id).orElseThrow(() ->
                new EntidadNoExisteEnBDException("No existe ningún " + entidadNombre + " persistido con ese id!")
        );
    }

    @Override
    public void guardarConUbi(Medium medium){
        this.mediumDAO.save(medium);
        this.mediumMongoDAO.save(MediumMongoDTO.desdeModelo(medium));
    }
}
