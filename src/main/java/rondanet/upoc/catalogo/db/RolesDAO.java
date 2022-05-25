package rondanet.upoc.catalogo.db;

import common.rondanet.catalogo.core.entity.Rol;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import rondanet.upoc.catalogo.repository.IRolRepository;

import java.util.List;
import java.util.Optional;

@Component
public class RolesDAO {

	Logger logger = LogManager.getLogger(RolesDAO.class);

	@Autowired
    IRolRepository rolRepository;

	private final MongoOperations mongoOperations;

	public RolesDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IRolRepository rolRepository) {
		this.mongoOperations = mongoOperations;
		this.rolRepository = rolRepository;
	}

	/**
	 * Devuelve un {@link List}<{@link Rol}> con todos los roles
	 * @return {@link List}<{@link Rol}>
	 */
	public List<Rol> getAll() {
		logger.log(Level.INFO, "El método getAll() de la clase RolesDAO fue ejecutado.");
		return rolRepository.findAll();
	}

	public List<Rol> findAll() {
		Query query = new Query();
		List<Rol> listaRoles = mongoOperations.find(query.addCriteria(Criteria.where("eliminado").is(false)), Rol.class);
		logger.log(Level.INFO, "El método findAll() de la clase RolesDAO fue ejecutado.");
		return listaRoles;
	}

	public Rol findById(String rolId) {
		Optional<Rol> rol = rolRepository.findById(rolId);
		if(rol.isPresent())
			return rol.get();
		return null;
	}

}