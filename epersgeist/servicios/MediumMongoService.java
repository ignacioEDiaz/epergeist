package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.MediumMongoDTO;

public interface MediumMongoService {

    void crear(MediumMongoDTO mediumMongoDTO);
    MediumMongoDTO findById(String id);
}
