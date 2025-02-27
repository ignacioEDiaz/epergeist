package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.EspirituMongoDTO;

public interface EspirituMongoService {
    void save(EspirituMongoDTO espirituMongoDTO);
    EspirituMongoDTO findById(String valueOf);
}
