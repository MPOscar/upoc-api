package rondanet.upoc.catalogo.db;

import java.util.*;

import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import rondanet.upoc.catalogo.utils.enums.Roles;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import rondanet.upoc.catalogo.repository.IEmpresaRepository;
import rondanet.upoc.catalogo.repository.IUserRepository;
import rondanet.upoc.catalogo.repository.IUsuarioEmpresaRepository;

@Component
public class UsuarioEmpresaDAO {

	Logger logger = LogManager.getLogger(UsuarioEmpresaDAO.class);

	@Autowired
	private IUsuarioEmpresaRepository usuarioEmpresaRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IEmpresaRepository empresaRepository;

	private final MongoOperations mongoOperations;

	public UsuarioEmpresaDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IUsuarioEmpresaRepository usuarioEmpresaRepository, IEmpresaRepository empresaRepository) {
		this.mongoOperations = mongoOperations;
		this.usuarioEmpresaRepository = usuarioEmpresaRepository;
		this.empresaRepository = empresaRepository;
	}

	/**
	 * Salva un {@link UsuarioEmpresa}
	 * @param usuarioEmpresa {@link UsuarioEmpresa}
	 * @return {@link UsuarioEmpresa}
	 */
	public UsuarioEmpresa save(UsuarioEmpresa usuarioEmpresa) {
		usuarioEmpresa = usuarioEmpresaRepository.save(usuarioEmpresa);
		usuarioEmpresa.setSId(usuarioEmpresa.getId());
		usuarioEmpresaRepository.save(usuarioEmpresa);

		logger.log(Level.INFO, "El método save() de la clase UsuarioEmpresaDAO fue ejecutado.");
		return usuarioEmpresa;
	}

	/**
	 * Devuelve el {@link UsuarioEmpresa} que coincida con los criterios pasados por
	 * parámetros.
	 *
	 * TODO: Evaluar si realmente es necesario chequear el Usuario si ya se está
	 * pasando el id del {@link UsuarioEmpresa}. Es este el lugar correcto para
	 * hacer esa verificación??
	 *
	 * @param idUsuario {@link String}
	 * @param idEmpresa {@link String}
	 * @return {@link Optional}<{@link UsuarioEmpresa}>
	 */
	public List<UsuarioEmpresa> findByIdUsuarioAndIdEmpresa(String idUsuario, String idEmpresa) {
		List<UsuarioEmpresa> usuarioEmpresa = usuarioEmpresaRepository.findAllBySusuarioAndSempresa(idUsuario, idEmpresa);
		return usuarioEmpresa;
	}

	/**
	 * Devuelve un {@link List}<{@link UsuarioEmpresa}> relacionado a la
	 * {@link Empresa} con el id pasado por parámetros. Se verfifica que el
	 * {@link Usuario} y el {@link UsuarioEmpresa} no estén eliminados
	 *
	 * @param idEmpresa {@link String}
	 * @return {@link Optional}<{@link UsuarioEmpresa}>
	 */
	public List<UsuarioEmpresa> obtenerUsuariosEmpresaPorEmpresa(String idEmpresa, boolean activo, boolean validado) {
		Query query = new Query();
		List<UsuarioEmpresa> usuarioEmpresas = mongoOperations.find(query.addCriteria(
				Criteria.where("sempresa").is(idEmpresa).andOperator(Criteria.where("rol").ne(Roles.USUARIO_GS1),
						Criteria.where("activo").is(activo), Criteria.where("validado").is(validado),
						Criteria.where("eliminado").is(false))), UsuarioEmpresa.class
		);
		return usuarioEmpresas;
	}

	public List<UsuarioEmpresa> getAll() {
		return usuarioEmpresaRepository.findAll();
	}

	public void delete(UsuarioEmpresa usuarioEmpresa){
		this.usuarioEmpresaRepository.delete(usuarioEmpresa);
	}
}