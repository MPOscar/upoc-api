package rondanet.upoc.catalogo.db;

import common.rondanet.catalogo.core.entity.ListaDePrecio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import rondanet.upoc.catalogo.repository.IListaDePrecioRepository;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Component
public class ListaDePrecioDAO {
	Logger logger = LogManager.getLogger(ListaDePrecioDAO.class);

	@Autowired
    IListaDePrecioRepository listaDePrecioRepository;

	private final MongoOperations mongoOperations;

	public ListaDePrecioDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IListaDePrecioRepository listaDePrecioRepository) {
		this.mongoOperations = mongoOperations;
		this.listaDePrecioRepository = listaDePrecioRepository;
	}

	public ListaDePrecio findFirstByProductoCppAndGlnListaDeVentaAndTargetAndFechaVigencia(String productoCpp, String empresaGln, String grupoClientes, DateTime fechaVigencia){
		Query query = new Query();
		query.addCriteria(Criteria.where("productoCpp").is(productoCpp).andOperator(Criteria.where("glnListaDeVenta").is(empresaGln), Criteria.where("grupoClientes").is(grupoClientes), Criteria.where("fechaVigencia").is(fechaVigencia)));
		ListaDePrecio listaDePrecios = mongoOperations.findOne(query, ListaDePrecio.class);
		return listaDePrecios;
	}

	public List<ListaDePrecio> findAllByGlnAndTargetAndMoneda(String gln, String target, String moneda){
		Query query = new Query();
		query.addCriteria(Criteria.where("glnListaDeVenta").is(gln).andOperator(Criteria.where("grupoClientes").is(target), Criteria.where("moneda").is(moneda)));
		List<ListaDePrecio> listaDePrecios = mongoOperations.find(query, ListaDePrecio.class);
		return listaDePrecios;
	}

	public ListaDePrecio findById(String id){
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		ListaDePrecio listaDePrecio = mongoOperations.findOne(query, ListaDePrecio.class);
		return listaDePrecio;
	}

	public ListaDePrecio save(ListaDePrecio listaDePrecio){
		listaDePrecio = listaDePrecioRepository.save(listaDePrecio);
		listaDePrecio.setSId(listaDePrecio.getId());
		return listaDePrecioRepository.save(listaDePrecio);
	}

	public List<ListaDePrecio> saveAll(List<ListaDePrecio> listaDePrecios){
		listaDePrecios = listaDePrecioRepository.saveAll(listaDePrecios);
		for (ListaDePrecio listaDePrecio: listaDePrecios) {
			listaDePrecio.setSId(listaDePrecio.getId());
		}
		return listaDePrecioRepository.saveAll(listaDePrecios);
	}

	public List<ListaDePrecio> obtenerListaDePrecioActualPorFechaVigencia(String gln, String grupoClientes, ListaDePrecio listaDePrecio){
		Query query = new Query();
		query.addCriteria(Criteria.where("glnListaDeVenta").is(gln).andOperator(Criteria.where("grupoClientes").is(grupoClientes), Criteria.where("fechaVigencia").is(listaDePrecio.getFechaVigencia()), Criteria.where("fechaDeCargaMilisegundos").is(listaDePrecio.getFechaDeCargaMilisegundos()), Criteria.where("eliminado").is(false)));
		List<ListaDePrecio> listaDePrecios = mongoOperations.find(query, ListaDePrecio.class);
		return listaDePrecios;
	}

	public void delete(ListaDePrecio listaDePrecio){
		listaDePrecioRepository.delete(listaDePrecio);
	}

	public void deleteAll(List<ListaDePrecio> listaDePrecios){
		listaDePrecioRepository.deleteAll(listaDePrecios);
	}


	public List<ListaDePrecio> obtenerListasDePreciosActualizadas(String glnDeEmpresa, DateTime fechaCreacion) {
		Set<String> listasDePreciosPorFechaVigencia = new HashSet<>();
		Aggregation listasDePreciosAggregation;
		List<ListaDePrecio> listaDePreciosVigentes = new ArrayList<>();

		listasDePreciosAggregation = Aggregation.newAggregation(match(Criteria.where("glnListaDeVenta").is(glnDeEmpresa).andOperator(Criteria.where("eliminado").is(false), Criteria.where("fechaCreacion").gte(fechaCreacion))), group("grupoClientes", "fechaVigencia").addToSet("_id").as("sid"));

		List<ListaDePrecio> listasDePrecio = mongoOperations.aggregate(listasDePreciosAggregation, "ListaDePrecio", ListaDePrecio.class).getMappedResults();

		for (ListaDePrecio listaDePrecio: listasDePrecio) {
			List <String> idsListaDePrecios = Arrays.asList(listaDePrecio.getSId().split(","));
			Query query = new Query();
			query.addCriteria(Criteria.where("id").in(idsListaDePrecios).andOperator(Criteria.where("eliminado").is(false)));
			query.with(Sort.by(Sort.Direction.DESC, "fechaDeCarga"));
			List<ListaDePrecio> listasDePreciosOrdenadas = mongoOperations.find(query, ListaDePrecio.class);
			if(listasDePreciosOrdenadas.size() > 0){
				listasDePreciosPorFechaVigencia.add(listasDePreciosOrdenadas.get(0).getId());
			}
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(listasDePreciosPorFechaVigencia));
		query.with(Sort.by(Sort.Direction.DESC, "fechaVigencia").and(Sort.by(Sort.Direction.DESC, "fechaDeCarga")));
		List<ListaDePrecio> listaDePrecios = mongoOperations.find(query, ListaDePrecio.class);
		if(listaDePrecios.size() > 0) {
			if (listaDePrecios.get(0).getGrupoClientes().equals("")) {
				listaDePreciosVigentes.add(listaDePrecios.get(0));
			} else {
				for (ListaDePrecio listaDePrecio : listaDePrecios) {
					if (!existeTargetEnListasDePrecio(listaDePreciosVigentes, listaDePrecio)) {
						listaDePreciosVigentes.add(listaDePrecio);
					}
				}
			}
		}
		return listaDePreciosVigentes;
	}

	boolean existeTargetEnListasDePrecio(List<ListaDePrecio> listaDePreciosVigentes, ListaDePrecio listaDePrecio){
		boolean existeTarget = false;
		for (ListaDePrecio listaDePrecioVigente: listaDePreciosVigentes) {
			if(listaDePrecioVigente.getGrupoClientes().equals(listaDePrecio.getGrupoClientes())){
				existeTarget = true;
				break;
			}
		}
		return existeTarget;
	}
}