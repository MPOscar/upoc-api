package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.Ubicacion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUbicacionRepository extends MongoRepository<Ubicacion, String> {
    public Ubicacion findFirstByOldId(long oldId);
}
