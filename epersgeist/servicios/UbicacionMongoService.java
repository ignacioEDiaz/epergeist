package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.UbicacionMongoDTO;

public interface UbicacionMongoService {
    void save(UbicacionMongoDTO desdeModelo);
}
