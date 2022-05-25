package rondanet.upoc.pedidos.repository;

import common.rondanet.pedidos.core.entity.DatosAfiliados;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IDatosAfiliadosRepository extends MongoRepository<DatosAfiliados, String> {
}
