package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Usuario;
import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IUsuarioEmpresaRepository extends MongoRepository<UsuarioEmpresa, String> {


    public UsuarioEmpresa findFirstByOldId(long oldId);

    public Optional<UsuarioEmpresa> findById(String id);

    public List<UsuarioEmpresa> findByUsuarioAndEmpresa(Usuario usuario, Empresa empresa);

    public List<UsuarioEmpresa> findAllBySusuarioAndSempresa(String idUsuario, String idEmpresa);

    public List<UsuarioEmpresa> findAllByEmpresaAndEliminado(Empresa empresa, boolean eliminado);

    public List<UsuarioEmpresa> findByUsuario(Usuario usuario);

    public List<UsuarioEmpresa> findByUsuarioAndEliminado(Usuario usuario, boolean eliminado);

    public List<UsuarioEmpresa> findAllByEmpresa(Empresa empresa);

    public List<UsuarioEmpresa> findByUsuarioAndActivoAndValidado(Usuario usuario, boolean activo, boolean validado);

    public List<UsuarioEmpresa> findByUsuarioAndEmpresaAndActivoAndValidado(Usuario usuario, Empresa empresa, boolean activo, boolean validado);
}
