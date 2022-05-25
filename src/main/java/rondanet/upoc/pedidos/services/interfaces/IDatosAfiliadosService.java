package rondanet.upoc.pedidos.services.interfaces;

public interface IDatosAfiliadosService {

	static final String CLAVE = "Alex Kalalunga";

	String desencriptarPassword(String password);

	String encriptarPassword(String password);
}
