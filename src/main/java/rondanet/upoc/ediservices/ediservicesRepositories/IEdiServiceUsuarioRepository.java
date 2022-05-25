package rondanet.upoc.ediservices.ediservicesRepositories;

import common.rondanet.clasico.core.ediservices.models.Usuario;
import common.rondanet.clasico.core.ediservices.models.UsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IEdiServiceUsuarioRepository extends JpaRepository<Usuario, UsuarioId> {

    @Transactional
    @Query(value = "SELECT * FROM USU u WHERE USU_NOMBRE = 'Administrador'", nativeQuery = true)
    List<Usuario> obtenerTodosLosUsuarioEmpresa();

}