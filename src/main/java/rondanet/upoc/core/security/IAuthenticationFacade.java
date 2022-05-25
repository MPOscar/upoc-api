package rondanet.upoc.core.security;

import common.rondanet.catalogo.core.resources.UsuarioPrincipal;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    UsuarioPrincipal getPrincipalAuth();
}
