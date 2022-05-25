package rondanet.upoc.catalogo.db;

import common.rondanet.catalogo.core.entity.ListaDeVentaVisibilidad;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import rondanet.upoc.catalogo.repository.IListaDeVentaVisibilidadRepository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Component
public class ListaDeVentaVisibilidadDAO {

	Logger logger = LogManager.getLogger(ListaDeVentaVisibilidadDAO.class);

	@Autowired
    IListaDeVentaVisibilidadRepository listaDeVentaVisibilidadRepository;

	private final MongoOperations mongoOperations;

	public ListaDeVentaVisibilidadDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IListaDeVentaVisibilidadRepository listaDeVentaVisibilidadRepository) {
		this.mongoOperations = mongoOperations;
		this.listaDeVentaVisibilidadRepository = listaDeVentaVisibilidadRepository;
	}

	public ListaDeVentaVisibilidad findByListaDeVentaProducto(String listaDeVentaId, String productoId) {
		Query query = new Query();
		ListaDeVentaVisibilidad listaDeVentaVisibilidad = mongoOperations.findOne(query.addCriteria(Criteria.where("slistaDeVenta").is(listaDeVentaId).andOperator(Criteria.where("sproducto").is(productoId), Criteria.where("eliminado").is(false))), ListaDeVentaVisibilidad.class);
		return listaDeVentaVisibilidad;
	}

	public List<ListaDeVentaVisibilidad> findByListaDeVenta(String listaDeVentaId) {
		Query query = new Query();
		List<ListaDeVentaVisibilidad> listaDeVentaVisibilidadList = mongoOperations.find(query.addCriteria(Criteria.where("slistaDeVenta").is(listaDeVentaId).andOperator(Criteria.where("eliminado").is(false))), ListaDeVentaVisibilidad.class);
		return listaDeVentaVisibilidadList;
	}

	public List<ListaDeVentaVisibilidad> findAllByIdListaDeVenta(String listaDeVentaId) {
		Aggregation listaDeVentaVisibilidadAggregation = Aggregation.newAggregation(match(Criteria.where("slistaDeVenta").is(listaDeVentaId)), group("sid"));
		List<ListaDeVentaVisibilidad> listaDeVentaVisibilidadList  = mongoOperations.aggregate(listaDeVentaVisibilidadAggregation, "ListaDeVentaVisibilidad", ListaDeVentaVisibilidad.class).getMappedResults();
		return listaDeVentaVisibilidadList;
	}

	public ListaDeVentaVisibilidad findById(String listaDeVentaVisibilidadId) {
		return listaDeVentaVisibilidadRepository.findById(listaDeVentaVisibilidadId).isPresent() ? listaDeVentaVisibilidadRepository.findById(listaDeVentaVisibilidadId).get() : null;
	}

	public void delete(ListaDeVentaVisibilidad listaDeVentaVisibilidad){
		listaDeVentaVisibilidadRepository.delete(listaDeVentaVisibilidad);
	}

	public void deleteAll(List<ListaDeVentaVisibilidad> listaDeVentaVisibilidadProductos){
		listaDeVentaVisibilidadRepository.deleteAll(listaDeVentaVisibilidadProductos);
	}

	public ListaDeVentaVisibilidad save(ListaDeVentaVisibilidad listaDeVentaVisibilidad) {
		listaDeVentaVisibilidad.setFechaEdicion();
		listaDeVentaVisibilidad = listaDeVentaVisibilidadRepository.save(listaDeVentaVisibilidad);
		if(listaDeVentaVisibilidad.getSId() == null) {
			listaDeVentaVisibilidad.setSId(listaDeVentaVisibilidad.getId());
		}
		logger.log(Level.INFO, "El método save() de la clase ListaDeVentaVisibiliadDAO fue ejecutado.");
		return listaDeVentaVisibilidadRepository.save(listaDeVentaVisibilidad);
	}

	public List<ListaDeVentaVisibilidad> saveAll(List<ListaDeVentaVisibilidad> listaDeVentaVisibilidadProductos){
		listaDeVentaVisibilidadProductos = listaDeVentaVisibilidadRepository.saveAll(listaDeVentaVisibilidadProductos);
		for (ListaDeVentaVisibilidad listaDeVentaVisibilidad: listaDeVentaVisibilidadProductos) {
			listaDeVentaVisibilidad.setSId(listaDeVentaVisibilidad.getId());
		}
		return listaDeVentaVisibilidadRepository.saveAll(listaDeVentaVisibilidadProductos);
	}

	public List<ListaDeVentaVisibilidad> findAllByGrupo(String idGrupo) {
		Query query = new Query();
		query.addCriteria(Criteria.where("gruposConVisibilidad").in(idGrupo).andOperator(Criteria.where("eliminado").is(false)));
		List<ListaDeVentaVisibilidad> listasVentaVisibilidades = mongoOperations.find(query, ListaDeVentaVisibilidad.class);
		logger.log(Level.INFO, "El método findByKey() de la clase ListasDeVentaDAO fue ejecutado.");
		return listasVentaVisibilidades;
	}

	public List<ListaDeVentaVisibilidad> findAllByProducto(String productoId) {
		Query query = new Query();
		List<ListaDeVentaVisibilidad> listaDeVentaVisibilidadList = mongoOperations.find(query.addCriteria(Criteria.where("sproducto").is(productoId)), ListaDeVentaVisibilidad.class);
		return listaDeVentaVisibilidadList;
	}
}