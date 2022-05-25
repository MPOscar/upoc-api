package rondanet.upoc.core.security;

import common.rondanet.catalogo.core.entity.Rol;
import common.rondanet.catalogo.core.entity.Usuario;
import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import common.rondanet.catalogo.core.resources.UsuarioPrincipal;
import rondanet.upoc.catalogo.repository.IUserRepository;
import rondanet.upoc.catalogo.repository.IUsuarioEmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUsuarioEmpresaRepository usuarioEmpresaRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameUsuarioEmpresa) {
        String username = usernameUsuarioEmpresa;
        List<String> roles = new ArrayList<String>();
        Optional<UsuarioEmpresa> optionalUsuarioEmpresa = Optional.empty();
        UsuarioEmpresa usuarioEmpresa = null;
        if (usernameUsuarioEmpresa.lastIndexOf("+") > -1) {
            username = usernameUsuarioEmpresa.substring(0,usernameUsuarioEmpresa.indexOf("+"));
            String usuarioEmpresaId = usernameUsuarioEmpresa.substring(usernameUsuarioEmpresa.indexOf("+") + 1);
            if(!usuarioEmpresaId.equals("null"))
                optionalUsuarioEmpresa = usuarioEmpresaRepository.findById(usuarioEmpresaId);
            if (optionalUsuarioEmpresa.isPresent()) {
                usuarioEmpresa = optionalUsuarioEmpresa.get();
                for (Rol rol : usuarioEmpresa.getRoles()) {
                    roles.add(rol.getRol());
                }
            }
        }

        Optional<Usuario> usuario = this.userRepository.findByEmail(username);
        if (!usuario.isPresent()){
            usuario = this.userRepository.findByUsuario(username);
        }
        if(!usuario.isPresent()) {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }

        if (usuario.get().esAdministradorSistema() != null && usuario.get().esAdministradorSistema())
            roles.add("systemAdmin");

        UsuarioPrincipal usuarioPrincipal = new UsuarioPrincipal(usuario.get(), usuarioEmpresa, roles);
        return usuarioPrincipal;
    }
}
