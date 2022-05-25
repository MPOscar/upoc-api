package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IRolRepository extends MongoRepository<Rol, String> {
    public Rol findFirstByOldId(long oldId);
}
