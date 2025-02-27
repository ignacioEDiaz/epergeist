package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.MediumMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.MediumMongoDTO;
import ar.edu.unq.epersgeist.servicios.MediumMongoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MediumMongoServiceImpl implements MediumMongoService {

    private final MediumMongoDAO mediumMongoDAO;

    public MediumMongoServiceImpl(MediumMongoDAO mediumMongoDAO) {
        this.mediumMongoDAO = mediumMongoDAO;
    }

    @Override
    public void crear(MediumMongoDTO mediumMongoDTO) {
        this.mediumMongoDAO.save(mediumMongoDTO);
    }

    @Override
    public MediumMongoDTO findById(String id) {
        return this.mediumMongoDAO.findById(id).get();
    }

    public MediumMongoDTO findByIdRelational(String id) {
        return this.mediumMongoDAO.findByIdRelational(id).get();
    }
}
