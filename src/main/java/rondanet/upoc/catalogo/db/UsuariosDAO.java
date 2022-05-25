package rondanet.upoc.catalogo.db;

import common.rondanet.catalogo.core.entity.Usuario;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rondanet.upoc.catalogo.repository.IUserRepository;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.Logger;

@Component
public class UsuariosDAO {

	Logger logger = LogManager.getLogger(UsuariosDAO.class);

	@Autowired
	private IUserRepository userRepository;

	UsuariosDAO(IUserRepository userRepository){
		this.userRepository = userRepository;
	}

	/**
	 * Salva un {@link Usuario} pasado por parámetro
	 * @param u {@link Usuario}
	 * @return {@link Usuario}
	 */
	public Usuario save(Usuario u) {
		u = userRepository.save(u);
		u.setSId(u.getId());
		userRepository.save(u);
		logger.log(Level.INFO, "El método save() de la clase UsuarioDAO fue ejecutado.");
		return u;
	}

	public void delete(Usuario usuario) {
		userRepository.delete(usuario);
	}

	public UsuariosDAO() {
	}

	/**
	 * Busca y edita un {@link Usuario}, para ello comprueba que el {@link Usuario} a editar pertenezca a la {@link Empresa}
	 * Devuelve un {@link Usuario} donde el {@link String} id del {@link Usuario} sea igual al {@link String} id pasado por parámetro
	 * @param id {@link String}
	 * @return {@link Usuario}
	 */
	public Usuario findById(String id) {
		Optional<Usuario> usuario = this.userRepository.findById(id);
		logger.log(Level.INFO, "El método findById() de la clase UsuarioDAO fue ejecutado.");
		return usuario.isPresent() ? usuario.get() : null;
	}

	/**
	 * Devuelve un {@link Optional}<{@link Usuario}> donde {@link String} usuario sea igual al {@link String} searchUsuario pasado por parámetro
	 * @param searchUsuario {@link String}
	 * @return {@link Optional}<{@link Usuario}>
	 */
	public Optional<Usuario> findByUsuario(String searchUsuario) {
		Optional<Usuario> usuario = userRepository.findFirstByUsuario(searchUsuario);
		return usuario;
	}

	/**
	 * Devuelve un {@link Optional}<{@link Usuario}> donde {@link String} usuario sea igual al {@link String} searchUsuario pasado por parámetro
	 * @param searchUsuario {@link String}
	 * @return {@link Optional}<{@link Usuario}>
	 */
	public List<Usuario> findAllByUsuario(String searchUsuario) {
		List<Usuario> usuarios = userRepository.findAllByUsuario(searchUsuario);
		return usuarios;
	}

	public List<Usuario> getAll() {
		return userRepository.findAll();
	}

}