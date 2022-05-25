package rondanet.upoc.pedidos.db;

import rondanet.upoc.pedidos.repository.IMensajesRepository;
import rondanet.upoc.core.utils.Documento;
import common.rondanet.pedidos.core.entity.Mensaje;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
public class MensajesDAO {

	@Autowired
	private IMensajesRepository mensajesRepository;

	private final MongoOperations mongoOperations;

	public MensajesDAO(@Qualifier("mongoTemplatePedidos") MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public List<Mensaje> obtenerListaDeDocumentos(
			Date fechaDeActualizacion,
			int page,
			int limit
	) {
		Criteria criteria = obtenerOrdenesDeCompraQuery(
				new DateTime(fechaDeActualizacion)
		);

		Aggregation empresasAggregation = Aggregation.newAggregation(
				match(criteria),
				skip((long) (page * limit)),
				limit(limit)
		);

		List<Mensaje> mensajes = mongoOperations.aggregate(empresasAggregation, "Mensaje", Mensaje.class).getMappedResults();
		return mensajes;
	}

	public Criteria obtenerOrdenesDeCompraQuery(
			DateTime fechaCreacion
	) {
		Criteria orCriteria = new Criteria();
		List<Criteria> andExpression =  new ArrayList<>();

		Criteria expressionFechaInicial = new Criteria();
		expressionFechaInicial.and("fechaCreacion").gte(fechaCreacion);
		andExpression.add(expressionFechaInicial);

		expressionFechaInicial.and("documentoTipo").is(Documento.TIPO_ORDEN);
		andExpression.add(expressionFechaInicial);

		Criteria criteria = orCriteria.andOperator(andExpression.toArray(new Criteria[andExpression.size()]));

		return criteria;
	}
}
