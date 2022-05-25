package rondanet.upoc.pedidos.db;

import common.rondanet.pedidos.core.entity.DatosAfiliados;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DatosAfiliadosDAO {

	private final MongoOperations mongoOperations;

	public DatosAfiliadosDAO(@Qualifier("mongoTemplatePedidos") MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public Optional<DatosAfiliados> findByRut(String rut) {
		Query query = new Query();
		List<DatosAfiliados> datosAfiliados = mongoOperations.find(query.addCriteria(Criteria.where("rut").is(rut).andOperator(Criteria.where("eliminado").is(false))), DatosAfiliados.class);
		if (datosAfiliados.size() > 0)
			return Optional.of(datosAfiliados.get(0));
		return Optional.ofNullable(null);
	}

}
