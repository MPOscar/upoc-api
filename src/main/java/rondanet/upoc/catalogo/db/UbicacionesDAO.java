package rondanet.upoc.catalogo.db;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Ubicacion;
import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import rondanet.upoc.catalogo.repository.IUbicacionRepository;

import java.util.List;
import java.util.Optional;

@Component
public class UbicacionesDAO {

	Logger logger = LogManager.getLogger(UbicacionesDAO.class);

	@Autowired
	IUbicacionRepository ubicacionRepository;

	private MongoOperations mongoOperations;

	public UbicacionesDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public Optional<Ubicacion> findById(String id) {
		return ubicacionRepository.findById(id);
	}

	public Optional<Ubicacion> findByKey(String key, String value) {
		Query query = new Query();
		List<Ubicacion> listaUbicaciones = mongoOperations.find(query.addCriteria(Criteria.where(key).is(value).andOperator(Criteria.where("eliminado").is(false))), Ubicacion.class);

		if (listaUbicaciones.size() != 0)
			return Optional.of(listaUbicaciones.get(0));

		return Optional.ofNullable(null);
	}

	public List<Ubicacion> getAll() {
		return ubicacionRepository.findAll();
	}

	public List<Ubicacion> getAll(UsuarioEmpresa usuarioEmpresa) {
		Query query = new Query();
		List<Ubicacion> listaUbicaciones = mongoOperations.find(query.addCriteria(Criteria.where("sempresa").is(usuarioEmpresa.getEmpresa().getSId()).andOperator(Criteria.where("eliminado").is(false))), Ubicacion.class);
		return listaUbicaciones;
	}

}