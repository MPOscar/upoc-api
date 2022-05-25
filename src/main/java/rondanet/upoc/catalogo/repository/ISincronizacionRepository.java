package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.Sincronizacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ISincronizacionRepository extends MongoRepository<Sincronizacion, String> {

    public Optional<Sincronizacion> findFirstByColleccion(String coleccion);

}
