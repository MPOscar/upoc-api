package rondanet.upoc.core.security;

import common.rondanet.catalogo.core.entity.Usuario;
import common.rondanet.catalogo.core.resources.UsuarioPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFacade implements IAuthenticationFacade {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UsuarioPrincipal getPrincipalAuth() {
        Authentication authentication = getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return (UsuarioPrincipal) authentication.getPrincipal();
        Usuario usuarioAnonimo = new Usuario();
        usuarioAnonimo.setUsuario("AnonymousUser");
        return new UsuarioPrincipal(usuarioAnonimo, null, null);
    }
}
