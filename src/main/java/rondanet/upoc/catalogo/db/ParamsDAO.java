package rondanet.upoc.catalogo.db;

import common.rondanet.catalogo.core.entity.Param;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rondanet.upoc.catalogo.exceptions.ServiceException;
import rondanet.upoc.catalogo.repository.IParamRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ParamsDAO {

	Logger logger = LogManager.getLogger(ParamsDAO.class);

	@Autowired
	private IParamRepository paramRepository;

	public ParamsDAO(IParamRepository paramRepository) {
		this.paramRepository = paramRepository;
	}

	public List<Param> getAll() {
		return paramRepository.findAll();
	}

	public Param findByNombre(String nombre) {
		logger.log(Level.INFO, "El metodo findByNombre() de la clase ParamsDAO fue ejecutado");
		return paramRepository.findFirstByNombre(nombre);
	}

	/**
	 * Salva una {@link Param} pasada por parámetro
	 * @param toAdd {@link Param}
	 * @return {@link Param}
	 */
	public Param save(Param toAdd) {
		logger.log(Level.INFO, "El método save() de la clase PropiedadesConfigDAO fue ejecutado.");
		toAdd.setFechaCreacion();
		toAdd.setFechaEdicion();
		toAdd = paramRepository.save(toAdd);
		toAdd.setSId(toAdd.getId());
		return paramRepository.save(toAdd);
	}

	/**
	 * Actualiza una {@link Param} pasada por parámetro
	 * @param toUpdate {@link Param}
	 * @return {@link Param}
	 */
	public Param update(Param toUpdate) {
		logger.log(Level.INFO, "El método update() de la clase GruposDAO fue ejecutado.");
		toUpdate.setFechaEdicion();
		toUpdate = paramRepository.save(toUpdate);
		toUpdate.setSId(toUpdate.getId());
		return paramRepository.save(toUpdate);
	}

	/**
	 * Devuelve una {@link Param} donde el {@link String} id de la {@link Param} sea
	 * igual al {@link String} id id pasado por parámetro.
	 * <p></p>
	 * Si ya existe lanza un {@link ServiceException} con el mensaje "Ya existe la propiedad con id (id)."
	 * @param id {@link String}
	 * @throws ServiceException
	 */
	public Param findById(String id) throws ServiceException {
		logger.log(Level.INFO, "El método findById() de la clase PropiedadesConfigDAO fue ejecutado.");
		Optional<Param> propiedadConf = paramRepository.findById(id);
		if(propiedadConf.isPresent()){
			return propiedadConf.get();
		}else{
			throw new ServiceException("No existe la propiedad con id " + id);
		}
	}

}