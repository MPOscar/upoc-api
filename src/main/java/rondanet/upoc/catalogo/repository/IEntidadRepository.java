package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.Entidad;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IEntidadRepository extends MongoRepository<Entidad, String> {
}
