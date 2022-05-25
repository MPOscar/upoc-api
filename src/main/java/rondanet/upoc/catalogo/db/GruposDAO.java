package rondanet.upoc.catalogo.db;

import java.util.*;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Grupo;
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

import rondanet.upoc.catalogo.repository.IEmpresaRepository;
import rondanet.upoc.catalogo.repository.IGrupoRepository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;


@Component
public class GruposDAO {

	Logger logger = LogManager.getLogger(GruposDAO.class);

	@Autowired
    IGrupoRepository grupoRepository;

	@Autowired
    IEmpresaRepository empresaRepository;

	@Autowired
	private EmpresasDAO businessRepository;

	private final MongoOperations mongoOperations;

	public GruposDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IGrupoRepository grupoRepository, IEmpresaRepository empresaRepository, EmpresasDAO businessRepository) {
		this.mongoOperations = mongoOperations;
		this.grupoRepository = grupoRepository;
		this.empresaRepository = empresaRepository;
		this.businessRepository = businessRepository;
	}

	public Grupo findById(String id) {
		Optional<Grupo> grupo = grupoRepository.findById(id);
		logger.log(Level.INFO, "El método findById() de la clase GruposDAO fue ejecutado.");
		return grupo.isPresent() ? grupo.get() : null;
	}

	public Grupo save(Grupo toAdd) {
		toAdd.setFechaCreacion();
		toAdd.setFechaEdicion();
		toAdd = grupoRepository.save(toAdd);
		toAdd.setSId(toAdd.getId());
		logger.log(Level.INFO, "El método save() de la clase GruposDAO fue ejecutado.");
		return grupoRepository.save(toAdd);
	}

	public List<Grupo> saveAll(List<Grupo> grupos) {
		return grupoRepository.saveAll(grupos);
	}

	public Grupo update(Grupo toUpdate) {
		toUpdate.setFechaEdicion();
		toUpdate = grupoRepository.save(toUpdate);
		toUpdate.setSId(toUpdate.getId());
		logger.log(Level.INFO, "El método update() de la clase GruposDAO fue ejecutado.");
		return grupoRepository.save(toUpdate);
	}

	public List<Grupo> getAll() {
		Query query = new Query();
		List<Grupo> grupos = mongoOperations.find(query.addCriteria(Criteria.where("eliminado").is(false)), Grupo.class);
		return grupos;
	}

	public List<Grupo> getAllByEmpresa(Empresa empresa) {
		Query query = new Query();
		List<Grupo> grupos = mongoOperations.find(query.addCriteria(Criteria.where("sempresa").is(empresa.getId()).andOperator(Criteria.where("eliminado").is(false))), Grupo.class);
		return grupos;
	}

	public List<Grupo> getAllGruposIdByEmpresa(Empresa empresa) {
		Aggregation gruposAggregation = Aggregation.newAggregation(match(Criteria.where("sempresa").is(empresa.getId()).andOperator(Criteria.where("eliminado").is(false))), group("sid"));
		List<Grupo> grupos = mongoOperations.aggregate(gruposAggregation, "Grupo", Grupo.class).getMappedResults();
		return grupos;
	}

	public List<Grupo> obtenerTodosLosGruposNoActualizados(Empresa empresa) {
		Query query = new Query();
		List<Grupo> grupos = mongoOperations.find(query.addCriteria(Criteria.where("sempresa").is(empresa.getId()).andOperator(Criteria.where("eliminado").is(false), Criteria.where("fueActualizado").is(false))), Grupo.class);
		return grupos;
	}

	public List<Grupo> findByKey(String key, String value) {
		Query query = new Query();
		List<Grupo> grupos = mongoOperations.find(query.addCriteria(Criteria.where(key).is(value).andOperator(Criteria.where("eliminado").is(false))), Grupo.class);
		return grupos;
	}

	public Grupo obtenerGrupo(String nombre, String empresaId) {
		Optional<Grupo> optionalGrupo = grupoRepository.findFirstByNombreAndSempresa(nombre, empresaId);
		return optionalGrupo.isPresent() ? optionalGrupo.get() : null;
	}

	public Grupo obtenerGrupoPorEmpresa(String nombre, Empresa empresa) {
		Optional<Grupo> optionalGrupo = grupoRepository.findFirstByNombreAndSempresa(nombre, empresa.getId());
		return optionalGrupo.isPresent() ? optionalGrupo.get() : null;
	}

	public List<Grupo> obtenerTodosLosGruposAlosQuePerteneceLaEmpresa(Empresa empresa, Empresa cliente) {
		Query query = new Query();
		List<Grupo> grupos = mongoOperations.find(query.addCriteria(Criteria.where("sempresa").is(empresa.getId()).andOperator(Criteria.where("eliminado").is(false), Criteria.where("sempresas").in(cliente.getSId()))), Grupo.class);
		return grupos;
	}

}
