package rondanet.upoc.core.db;

import common.rondanet.catalogo.core.resources.PaginadoResponse;
import common.rondanet.pedidos.core.resources.VisorDocumentosFiltro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import rondanet.upoc.core.entity.UpocDocumento;
import rondanet.upoc.core.repository.IUpocDocumentoRepository;
import rondanet.upoc.core.resources.EstadisticasReporteDeOrdenesDeCompra;
import rondanet.upoc.core.utils.fechas.FechaUtils;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
public class UpocDocumentoDAO {

	Logger logger = LogManager.getLogger(UpocDocumentoDAO.class);

	@Autowired
	IUpocDocumentoRepository upocDocumentoRepository;

	private final MongoOperations mongoOperations;

	public UpocDocumentoDAO(@Qualifier("mongoTemplateUpoc") MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public UpocDocumento save(UpocDocumento upocDocumento) {
		upocDocumento = upocDocumentoRepository.save(upocDocumento);
		if (upocDocumento.getSId() == null){
			upocDocumento.setSId(upocDocumento.getId());
			upocDocumento = upocDocumentoRepository.save(upocDocumento);
		}
		return upocDocumento;
	}

	public Optional<UpocDocumento> findById(String id) {
		Optional<UpocDocumento> upocDocumento = upocDocumentoRepository.findById(id);
		return upocDocumento;
	}

	public Optional<UpocDocumento> findByMensajeId(String id) {
		Optional<UpocDocumento> upocDocumento = upocDocumentoRepository.findFirstBySmensaje(id);
		return upocDocumento;
	}

	public Optional<UpocDocumento> findByDocumentoNumero(String documentoNumero) {
		Optional<UpocDocumento> upocDocumento = upocDocumentoRepository.findFirstByDocumentoNumero(documentoNumero);
		return upocDocumento;
	}

	public PaginadoResponse<List<UpocDocumento>> obtenerListaDeDocumentos(
			String usuario,
			VisorDocumentosFiltro visorDocumentosFiltro,
			int page,
			int limit,
			String sort
	) throws Exception {
		Criteria criteria = obtenerMensajesQuery(
				usuario,
				visorDocumentosFiltro,
				true
		);
		Query mensajesQuery = new Query();
		mensajesQuery.addCriteria(criteria);

		long total = mongoOperations.count(mensajesQuery, UpocDocumento.class);

		SortOperation sortOperation = getSort(sort);

		ProjectionOperation projectionOperation = project().andExclude("upocProductos");

		AggregationOptions.Builder options = newAggregationOptions() ;
		if (sort.contains("documentoNumero")) {
			Collation collation = Collation.of(Locale.ENGLISH).numericOrdering(true);
			options = AggregationOptions.builder().collation(collation);
		}

		Aggregation empresasAggregation = Aggregation.newAggregation(
				match(criteria),
				sortOperation,
				skip((long) (page * limit)),
				limit(limit),
				projectionOperation
		).withOptions(options.build());

		List<UpocDocumento> mensajes = mongoOperations.aggregate(empresasAggregation, "UpocDocumento", UpocDocumento.class).getMappedResults();
		PaginadoResponse<List<UpocDocumento>> paginadoResponse = new PaginadoResponse((long) page, (long) limit, total, mensajes);
		return paginadoResponse;
	}

	public EstadisticasReporteDeOrdenesDeCompra obtenerEstadisticasReporteDeOrdenesDeCompra(
			String usuario,
			VisorDocumentosFiltro visorDocumentosFiltro
	) throws Exception {
		Criteria criteriaTotalDeOrdenes = obtenerMensajesQuery(
				usuario,
				visorDocumentosFiltro,
				null
		);

		Query totalDeOrdenesQuery = new Query();
		totalDeOrdenesQuery.addCriteria(criteriaTotalDeOrdenes);
		long totalDeOrdenes = mongoOperations.count(totalDeOrdenesQuery, UpocDocumento.class);

		Criteria criteriaTotalDeOrdenesConErrores = obtenerMensajesQuery(
				usuario,
				visorDocumentosFiltro,
				true
		);

		Query totalDeOrdenesConErroresQuery = new Query();
		totalDeOrdenesConErroresQuery.addCriteria(criteriaTotalDeOrdenesConErrores);
		long totalDeOrdenesConErrores = mongoOperations.count(totalDeOrdenesQuery, UpocDocumento.class);

		Aggregation documentosAggregationTotalDeLineas = Aggregation.newAggregation(
				match(criteriaTotalDeOrdenes),
				Aggregation.group().sum("totalDeLineas").as("totalDeLineas")
		);
		UpocDocumento upocDocumentoTotalDeLineas = mongoOperations.aggregate(documentosAggregationTotalDeLineas, "UpocDocumento", UpocDocumento.class).getUniqueMappedResult();

		Aggregation documentosAggregationTotalDeLineasConErrores = Aggregation.newAggregation(
				match(criteriaTotalDeOrdenes),
				Aggregation.group().sum("totalDeLineasConError").as("totalDeLineasConError")
		);
		UpocDocumento upocDocumentoTotalDeLineasConErrores = mongoOperations.aggregate(documentosAggregationTotalDeLineasConErrores, "UpocDocumento", UpocDocumento.class).getUniqueMappedResult();

		Aggregation documentosAggregationTotalDeLineasConWarning = Aggregation.newAggregation(
				match(criteriaTotalDeOrdenes),
				Aggregation.group().sum("totalDeLineasConWarning").as("totalDeLineasConWarning")
		);
		UpocDocumento upocDocumentoTotalDeLineasConWarning = mongoOperations.aggregate(documentosAggregationTotalDeLineasConWarning, "UpocDocumento", UpocDocumento.class).getUniqueMappedResult();

		EstadisticasReporteDeOrdenesDeCompra estadisticasReporteDeOrdenesDeCompra = new EstadisticasReporteDeOrdenesDeCompra();
		estadisticasReporteDeOrdenesDeCompra.setTotalDeOrdenesDeCompra(totalDeOrdenes);
		estadisticasReporteDeOrdenesDeCompra.setTotalDeOrdenesDeCompraConError(totalDeOrdenesConErrores);
		estadisticasReporteDeOrdenesDeCompra.setTotalDeLineasEnOrdenesDeCompra(upocDocumentoTotalDeLineas != null ? upocDocumentoTotalDeLineas.getTotalDeLineas() : 0);
		estadisticasReporteDeOrdenesDeCompra.setTotalDeLineasConErrorEnOrdenesDeCompra(upocDocumentoTotalDeLineasConErrores != null ? upocDocumentoTotalDeLineasConErrores.getTotalDeLineasConError() : 0);
		estadisticasReporteDeOrdenesDeCompra.setTotalDeLineasConWarningEnOrdenesDeCompra(upocDocumentoTotalDeLineasConWarning != null ? upocDocumentoTotalDeLineasConWarning.getTotalDeLineasConWarning() : 0);
		return estadisticasReporteDeOrdenesDeCompra;
	}

	public SortOperation getSort(String sort) {
		SortOperation sortOperation = null;
		if(!sort.equals("")) {
			String columnaParaElSort = sort.substring(sort.indexOf("-") + 1);
			if (sort.indexOf("-") > -1) {
				sortOperation = sort(Sort.by(Sort.Direction.DESC, columnaParaElSort));
			} else {
				sortOperation = sort(Sort.by(Sort.Direction.ASC, columnaParaElSort));
			}
		}
		return sortOperation;
	}

	public Criteria obtenerMensajesQuery(
			String usuario,
			VisorDocumentosFiltro visorDocumentosFiltro,
			Boolean documentosConErrores
	) {
		Criteria orCriteria = new Criteria();
		List<Criteria> andExpression =  new ArrayList<>();

		HashMap<String, DateTime> fechas = FechaUtils.formatearFechas(visorDocumentosFiltro.getFechaIni(), visorDocumentosFiltro.getFechaFin());

		Criteria expressionFechaInicial = new Criteria();
		expressionFechaInicial.and("mensajeFechaHora").gte(fechas.get("fechaInicio"));
		andExpression.add(expressionFechaInicial);

		Criteria expressionFechaFinal = new Criteria();
		expressionFechaFinal.and("mensajeFechaHora").lte(fechas.get("fechaFinal"));
		andExpression.add(expressionFechaFinal);

		if (visorDocumentosFiltro.getMostrarDocumentosEnviados()) {
			Criteria expression = new Criteria();
			expression.and("deUsuario").is(usuario);
			andExpression.add(expression);
		} else {
			Criteria expression = new Criteria();
			expression.and("paraUsuario").is(usuario);
			andExpression.add(expression);
		}

		if (visorDocumentosFiltro.getEmpresas() != null && !visorDocumentosFiltro.getEmpresas().isEmpty()) {
			Criteria expression = new Criteria();
			expression.and("deUsuario").in(visorDocumentosFiltro.getEmpresas()).andOperator(Criteria.where("paraUsuario").in(visorDocumentosFiltro.getEmpresas()));
			andExpression.add(expression);
		}

		if (documentosConErrores != null) {
			Criteria expression = new Criteria();
			expression.and("tieneErrores").is(documentosConErrores);
			andExpression.add(expression);
		}

		for (String filter : visorDocumentosFiltro.obtenerFiltros()) {
			Criteria expression = new Criteria();
			expression.orOperator(
					Criteria.where("documentoNumero").regex(filter, "i"),
					Criteria.where("paraUsuario").regex(filter, "i"),
					Criteria.where("deUsuario").regex(filter, "i"),
					Criteria.where("deGLN").regex(filter, "i"),
					Criteria.where("paraGLN").regex(filter, "i"),
					Criteria.where("deRazonSocial").regex(filter, "i"),
					Criteria.where("paraRazonSocial").regex(filter, "i")
			);
			andExpression.add(expression);
		}

		Criteria criteria = orCriteria.andOperator(andExpression.toArray(new Criteria[andExpression.size()]));

		return criteria;
	}

}