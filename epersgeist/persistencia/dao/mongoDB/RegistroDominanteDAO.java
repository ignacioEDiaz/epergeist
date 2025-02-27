package ar.edu.unq.epersgeist.persistencia.dao.mongoDB;

import ar.edu.unq.epersgeist.modelo.MaxCantidadDominacion;
import ar.edu.unq.epersgeist.modelo.RegistroDominacion;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface RegistroDominanteDAO extends MongoRepository<RegistroDominacion,String> {

    @Aggregation(pipeline = {
            "{ $match: { 'fechaRegistro': { $gte: ?0 , $lte: ?1 }}}",
            "{ $group: { _id: '$idDominante', total: { $sum: 1 }}}",
            "{ $sort: { total: -1 }}",
            "{ $limit : 1}"
    })
    MaxCantidadDominacion obtenerMaxCantidadDominadosEntreFecha(Date fechaInicio, Date fechaFin);

    @Aggregation(pipeline = {
            "{ $match: { 'fechaRegistro': { $gte: ?0 , $lte: ?1 }, 'energiaDominado': { $gte: 40}}}",
            "{ $group: { id: '$idDominante', registroEnergia : { $push: '$energiaDominante' }, total : { $sum : 1}}}",
            "{ $match: { total : { $eq : ?2}}}",
            "{ $project: { id: '$_id', diferencias: { $map: { input: { $range: [0, { $subtract: [{ $size: '$registroEnergia' }, 1]}]}, as: 'index', in: { $subtract: [" +
                    "{ $arrayElemAt:['$registroEnergia', '$$index'] }," +
                    "{ $arrayElemAt:['$registroEnergia', { $add: [ '$$index', 1 ] }]} ] } } } } }",
            "{ $project: { id: '$_id', diferencia: { $sum: '$diferencias' }}}",
            "{ $sort: { diferencia: 1 }}",
            "{ $limit: 1 }",
            "{ $project: { idDominante: '$_id' }}"
    })
    RegistroDominacion obtenerEspirituMasDominanteEntreFechas(Date fechaInicio, Date fechaFin, int max);

    @Aggregation(pipeline = {
            "{ '$match': { 'fechaRegistro': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$group': { " +
                    " id: '$idUbicacion', " +
                    "'angelicales': { '$sum': { '$cond': [{ '$eq': ['$tipoDominante', 'ANGEL'] }, 1, 0] } }, " +
                    "'demoniacos': { '$sum': { '$cond': [{ '$eq': ['$tipoDominante', 'DEMONIO'] }, 1, 0] } } " +
                    "} }",
            "{ '$project': { " +
                    " idUbicacion: '$_id', " +
                    "'diferencia': { '$subtract': ['$angelicales', '$demoniacos'] } " +
                    "} }",
            "{ '$sort': { 'diferencia': -1 } }",
            "{ '$limit': 1 }"
    })
    RegistroDominacion ubicacionMasDominada(Date fechaInicio, Date fechaFin);
}

