package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.EntidadNoExisteEnBDException;
import ar.edu.unq.epersgeist.exception.ExisteEntidadConMismoNombreException;
import ar.edu.unq.epersgeist.exception.NoHayUbicacionesMasCorruptas;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.UbicacionMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.UbicacionMongoDTO;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {
    private final UbicacionDAO ubicacionDAO;
    private final UbicacionMongoDAO ubicacionMongoDAO;

    public UbicacionServiceImpl(UbicacionDAO ubicacionDAO, UbicacionMongoDAO ubicacionMongoDAO) {
        this.ubicacionDAO      = ubicacionDAO;
        this.ubicacionMongoDAO = ubicacionMongoDAO;
    }

    @Override
    public void crear(Ubicacion ubicacion) {
        if (this.ubicacionDAO.findSameName(ubicacion.getNombre()))
            throw new ExisteEntidadConMismoNombreException("Ya existe ubicacion con el mismo nombre en BD");
        this.ubicacionDAO.save(ubicacion);
        this.ubicacionMongoDAO.save(UbicacionMongoDTO.desdeModelo(ubicacion));
    }

    @Override
    public Ubicacion recuperar(Long idDelUbicacion) {
        try{
            return this.ubicacionDAO.findById(idDelUbicacion).orElseThrow(()->
                    new EntidadNoExisteEnBDException("Entidad Ubicacion no existe"));
        }
        catch (InvalidDataAccessApiUsageException e){
            throw new EntidadNoExisteEnBDException("Entidad Ubicacion no existe");
        }
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return this.ubicacionDAO.findAll();
    }


    @Override
    public void actualizar(Ubicacion ubicacion) {
        if(ubicacion.getId() != null && this.ubicacionDAO.existsById(ubicacion.getId())){
            actualizarExistente(ubicacion);
        }
        else{
            throw new EntidadNoExisteEnBDException("No existe ninguna Ubicacion persistida con este ID!");
        }
    }

    @Override
    public void actualizar(Ubicacion ubicacion, Long id) {
        if(id == null || !this.ubicacionDAO.existsById(id))
            throw new EntidadNoExisteEnBDException("No existe ninguna Ubicacion persistida con este ID!");

        Ubicacion ubicacionPersistida =  obtenerEntidadPorId(this.ubicacionDAO,id,"Ubicacion");

        ubicacion.setId(id);
        ubicacion.setTipo(ubicacionPersistida.getTipo());
        actualizarExistente(ubicacion);
    }

    private void actualizarExistente(Ubicacion ubicacion) {
        if(this.ubicacionDAO.findSameName(ubicacion.getNombre())){
            throw new ExisteEntidadConMismoNombreException("Ya existe una Entidad con el nombre " + ubicacion.getNombre());
        }
        else {
            Ubicacion ubicacionActualizar = recuperar(ubicacion.getId());
            ubicacionActualizar.setNombre(ubicacion.getNombre());
            ubicacionActualizar.setTipo(ubicacion.getTipo());
            ubicacionActualizar.setEnergia(ubicacion.getEnergia());

            UbicacionMongoDTO ubimongoActualizar = this.ubicacionMongoDAO.findByIdRelational(String.valueOf(ubicacionActualizar.getId())).get();
            ubimongoActualizar = UbicacionMongoDTO.actualizarDesdeModelo(ubimongoActualizar.getId(), ubicacionActualizar);

            this.ubicacionDAO.save(ubicacionActualizar);
            this.ubicacionMongoDAO.save(ubimongoActualizar);
        }
    }

    @Override
    public void eliminar(Long idDelUbicacion) {
        try{
            this.ubicacionDAO.deleteById(idDelUbicacion);
            this.ubicacionMongoDAO.deleteByIdRelational(String.valueOf(idDelUbicacion));
        }catch (InvalidDataAccessApiUsageException e){
            throw new EntidadNoExisteEnBDException("La ubicacion no posee id!");
        }
    }


    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {

        return this.ubicacionDAO.findAllEspiritById(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return this.ubicacionDAO.finAllMediumOutOFEspirityById(ubicacionId);
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {

        Ubicacion ubiMasCorrupta = this.ubicacionDAO.ubicacionMasCorrupta();

        if (ubiMasCorrupta == null)
            throw new NoHayUbicacionesMasCorruptas("NoHayUbicacionMasCorruptas");

        Medium mediumMasDemoniaco = this.ubicacionDAO.mediumMasDemoniaco(ubiMasCorrupta.getId());

        return new ReporteSantuarioMasCorrupto(ubiMasCorrupta.getNombre(),mediumMasDemoniaco,
                this.ubicacionDAO.cantDemonios(ubiMasCorrupta.getId()),
                this.ubicacionDAO.demoniosLibres(ubiMasCorrupta.getId()));
    }

    private <T> T obtenerEntidadPorId(JpaRepository<T, Long> dao, Long id, String entidadNombre) {
        if (id == null)
            throw new EntidadNoExisteEnBDException("No existe ningun " + entidadNombre + " persistido con ese id!");

        return dao.findById(id).orElseThrow(() ->
                new EntidadNoExisteEnBDException("No existe ningún " + entidadNombre + " persistido con ese id!")
        );
    }

}