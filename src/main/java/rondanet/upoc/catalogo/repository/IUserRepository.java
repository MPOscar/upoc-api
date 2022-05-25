package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends MongoRepository<Usuario, String> {

    public Usuario findFirstByOldId(long oldId);

    public List<Usuario> findAll();

    public List<Usuario> findAllByUsuario(String usuario);

    public Usuario findOneById(String id);

    public Optional<Usuario> findByUsuario(String usuario);

    public Optional<Usuario> findFirstByUsuario(String usuario);

    public Optional<Usuario> findByEmail(String email);

    public Optional<Usuario> findFirstByEmail(String email);

    public List<Usuario> findAllByEsAdministradorSistema(boolean esAdministradorSistema);

}
