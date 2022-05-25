package rondanet.upoc.pedidos.repository;

import common.rondanet.pedidos.core.entity.Mensaje;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IMensajesRepository extends MongoRepository<Mensaje, String> {
    Optional<Mensaje> findFirstByMensajeId(int mensajeId);
}
