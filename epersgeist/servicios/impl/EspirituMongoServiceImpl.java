package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.EspirituMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.EspirituMongoDTO;
import ar.edu.unq.epersgeist.servicios.EspirituMongoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EspirituMongoServiceImpl implements EspirituMongoService {
    private final EspirituMongoDAO espirituMongoDAO;

    public EspirituMongoServiceImpl(EspirituMongoDAO espirituMongoDAO) {
        this.espirituMongoDAO = espirituMongoDAO;
    }

    @Override
    public void save(EspirituMongoDTO espirituMongoDTO) {
        this.espirituMongoDAO.save(espirituMongoDTO);
    }

    @Override
    public EspirituMongoDTO findById(String id) {
        return this.espirituMongoDAO.findById(id).get();
    }
}
