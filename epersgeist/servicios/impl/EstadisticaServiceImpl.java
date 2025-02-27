package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.FechasIncorrectasException;
import ar.edu.unq.epersgeist.exception.RegistroMasDominanteException;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDao;
import ar.edu.unq.epersgeist.persistencia.dao.estadisticas.EstadisticaNeoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.estadisticas.EstadisticaSnapshotDAO;
import ar.edu.unq.epersgeist.persistencia.dao.estadisticas.EstadisticaSqlDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.RegistroDominanteDAO;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {
    private final EstadisticaNeoDAO estadisticaNeoDAO;
    private final EstadisticaSnapshotDAO estadisticaSnapshotDAO;
    private final RegistroDominanteDAO registroDominanteDAO;
    private final EstadisticaSqlDAO estadisticaSqlDAO;
    private final EspirituDAO espirituDAO;
    private final MediumDao mediumDAO;
    private final UbicacionDAO ubicacionDAO;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired private MongoTemplate mongoTemplate;

    public EstadisticaServiceImpl(RegistroDominanteDAO registroDominanteDAO, EstadisticaNeoDAO estadisticaNeoDAO, EstadisticaSnapshotDAO estadisticaSnapshotDAO, EstadisticaSqlDAO estadisticaSqlDAO, EspirituDAO espirituDAO, MediumDao mediumDAO, UbicacionDAO ubicacionDAO) {
        this.estadisticaNeoDAO = estadisticaNeoDAO;
        this.estadisticaSnapshotDAO = estadisticaSnapshotDAO;
        this.registroDominanteDAO = registroDominanteDAO;
        this.estadisticaSqlDAO = estadisticaSqlDAO;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
        this.ubicacionDAO = ubicacionDAO;
    }

    @Override
    public Ubicacion ubicacionMasDominada(LocalDate fechaInicio, LocalDate fechaFin) {
        Date fechaInicioParse = java.sql.Date.valueOf(fechaInicio);
        Date fechaFinParse = java.sql.Date.valueOf(fechaFin);

        if (fechaInicioParse.after(fechaFinParse)) {
            throw new FechasIncorrectasException("La segunda fecha debe ser menor a la primer fecha ingresada!");
        }

        RegistroDominacion registro = registroDominanteDAO.ubicacionMasDominada(fechaInicioParse, fechaFinParse);
        if (registro == null) {
            throw new RegistroMasDominanteException(
                    "No existe un registro entre esas fechas");
        }

        return ubicacionDAO.findById((long)registro.getIdUbicacion()).orElse(null);
    }

    @Override
    public Espiritu espirituMasDominante(LocalDate fechaInicio, LocalDate fechaFin) {

        Date fechaInicioParse = java.sql.Date.valueOf(fechaInicio);
        Date fechaFinParse = java.sql.Date.valueOf(fechaFin);
        if (fechaInicioParse.after(fechaFinParse)) {
            throw new FechasIncorrectasException("La segunda fecha debe ser mayor a la primer fecha ingresada!");
        }
        MaxCantidadDominacion listaMax = this.registroDominanteDAO
                .obtenerMaxCantidadDominadosEntreFecha(fechaInicioParse, fechaFinParse);
        if (listaMax == null) {
            throw new RegistroMasDominanteException(
                    "No existe un registro donde un espiritu haya dominado a otro que posee mas de 40 de energia!");
        }
        int maxCantidad = listaMax.getTotal();
        RegistroDominacion registrosEspiritus = this.registroDominanteDAO
                .obtenerEspirituMasDominanteEntreFechas(fechaInicioParse, fechaFinParse,maxCantidad);

        return this.espirituDAO.findByIdAndNotDeleted(Long.valueOf(registrosEspiritus.getId()));
    }


    public void crearSnapshot() {
        Map<String, List<Object>> ssSQL = crearSnapshotSQL();
        Map<String, List<String>> ssNeo4j = crearSnapshotNeo4J();
        Map<String, Object> ssMDB = crearSnapshotMongoDB();
        String fechaSS = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        Snapshot snapshot = new Snapshot(ssSQL, ssNeo4j, ssMDB, fechaSS);
        guardarSnapshotEnMongo(snapshot);
    }

    private Map<String,List<Object>> crearSnapshotSQL() {

        Map<String, List<Object>> snapshotsPorSeparado = new HashMap<>();
        List<Espiritu> espiritusSQL = this.espirituDAO.findAll();
        List<Medium> mediumsSQL = this.mediumDAO.findAll();
        List<Ubicacion> ubicacionesSQL = this.ubicacionDAO.findAll();
        List<String> espiritusHabilidades = this.estadisticaSqlDAO.crearSnapshotEspiritusHabilidadesSql();
        List<String> espiritusDominados = this.estadisticaSqlDAO.crearSnapshotEspirituDominadosSql();

        snapshotsPorSeparado.put("Espiritus", Collections.singletonList(espiritusSQL));
        snapshotsPorSeparado.put("Mediums", Collections.singletonList(mediumsSQL));
        snapshotsPorSeparado.put("Ubicaciones", Collections.singletonList(ubicacionesSQL));
        snapshotsPorSeparado.put("EspiritusYHabilidades", Collections.singletonList((espiritusHabilidades)));
        snapshotsPorSeparado.put("EspiritusDominados", Collections.singletonList(espiritusDominados));

        return snapshotsPorSeparado;
    }

    private Map<String, List<String>> crearSnapshotNeo4J() {
        Map<String, List<String>> snapshotsPorSeparado = new HashMap<>();
        snapshotsPorSeparado.put("Habilidades",habilidadesAJson());
        return snapshotsPorSeparado;
    }

    private Map<String, Object> crearSnapshotMongoDB() {
        Map<String, Object> snapshotsPorSeparado = new HashMap<>();

        List<Map> registroDomCollection = mongoTemplate.findAll(Map.class, "registroDominacion");
        List<Map> espirituCollection = mongoTemplate.findAll(Map.class, "EspirituMongoDTO");
        List<Map> mediumCollecion = mongoTemplate.findAll(Map.class, "MediumMongoDTO");
        List<Map> ubicacionCollection = mongoTemplate.findAll(Map.class, "UbicacionMongoDTO");

        snapshotsPorSeparado.put("registroDominacion",registroDomCollection);
        snapshotsPorSeparado.put("EspirituMongoDTO",espirituCollection);
        snapshotsPorSeparado.put("MediumMongoDTO",mediumCollecion);
        snapshotsPorSeparado.put("UbicacionMongoDTO",ubicacionCollection);
        return snapshotsPorSeparado;
    }

    private List<String> habilidadesAJson() {
        List<Habilidad> habilidades = estadisticaNeoDAO.allHabilitiesRelated();
        List<String> habilidadesJson = new ArrayList<>();
        try {
            for (Habilidad habilidad : habilidades){
                String json = mapper.writeValueAsString(habilidad);
                habilidadesJson.add(json); }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error para convertir a JSON", e);
        }
        return habilidadesJson;
    }

    @Override
    public Snapshot obtenerSnapshot(LocalDate fecha) {
        String fechaString = fecha.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return this.estadisticaSnapshotDAO.findByFecha(fechaString);
    }

    public void guardarSnapshotEnMongo(Snapshot snapshot){
        Snapshot mismaFecha = estadisticaSnapshotDAO.findByFecha(snapshot.getFecha());
        if(mismaFecha != null){
            snapshot.setId(mismaFecha.getId());
        }
        this.estadisticaSnapshotDAO.save(snapshot);
    }


}
