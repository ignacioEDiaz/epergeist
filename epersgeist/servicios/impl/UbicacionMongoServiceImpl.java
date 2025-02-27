package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.UbicacionMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.UbicacionMongoDTO;
import ar.edu.unq.epersgeist.servicios.UbicacionMongoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UbicacionMongoServiceImpl implements UbicacionMongoService {
    private final UbicacionMongoDAO ubicacionMongoDAO;

    public UbicacionMongoServiceImpl(UbicacionMongoDAO ubicacionMongoDAO) {
        this.ubicacionMongoDAO = ubicacionMongoDAO;
    }

    @Override
    public void save(UbicacionMongoDTO ubicacionMongoDTO) {
        this.ubicacionMongoDAO.save(ubicacionMongoDTO);
    }
}
