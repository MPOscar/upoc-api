package rondanet.upoc.pedidos.services.implementations;

import rondanet.upoc.pedidos.services.interfaces.IDatosAfiliadosService;
import org.gs1uy.core.utils.Encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DatosAfiliadosService implements IDatosAfiliadosService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatosAfiliadosService.class);

	public String desencriptarPassword(String password) {
		return Encrypt.EncriptionEngine(password, CLAVE, false);
	}

	public String encriptarPassword(String password) {
		return Encrypt.EncriptionEngine(password, CLAVE, true);
	}

}
