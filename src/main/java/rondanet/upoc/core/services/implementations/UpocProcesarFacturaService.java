package rondanet.upoc.core.services.implementations;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Producto;
import common.rondanet.catalogo.core.entity.Ubicacion;
import common.rondanet.pedidos.core.entity.Mensaje;
import common.rondanet.pedidos.core.resources.DocumentoCatalogoPayload;
import common.rondanet.pedidos.core.traductor.Fa2;
import common.rondanet.pedidos.core.traductor.Factura;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rondanet.upoc.catalogo.db.EmpresasDAO;
import rondanet.upoc.catalogo.db.ProductosDAO;
import rondanet.upoc.catalogo.db.UbicacionesDAO;
import rondanet.upoc.core.db.UpocDocumentoDAO;
import rondanet.upoc.core.entity.UpocDocumento;
import rondanet.upoc.core.entity.UpocPedido;
import rondanet.upoc.core.entity.UpocProducto;
import rondanet.upoc.core.services.interfaces.IUpocProcesarFacturaService;
import rondanet.upoc.core.utils.Documento;
import rondanet.upoc.core.utils.IEmailHelper;
import rondanet.upoc.pedidos.db.MensajesDAO;

import java.util.*;

@Service
public class UpocProcesarFacturaService implements IUpocProcesarFacturaService {

	Logger logger = LogManager.getLogger(UpocProcesarFacturaService.class);

	@Autowired
	MensajesDAO mensajesDAO;

	@Autowired
	ProductosDAO productosDAO;

	@Autowired
	UbicacionesDAO ubicacionesDAO;

	@Autowired
	EmpresasDAO empresasDAO;

	@Autowired
	UpocDocumentoDAO upocDocumentoDAO;

	@Override
	public void procesarFacturas(Date fechaDeActualizacion) {
		try {
			procesarFacturasParaUPOC(fechaDeActualizacion);
		} catch (Exception e) {
			logger.log(Level.ERROR,"procesarFacturas: " + e.getStackTrace());
		}
	}

	public void procesarFacturasParaUPOC(Date fechaDeActualizacion) throws Exception {
		int page = 0;
		boolean todasLosMensajes = false;
		while (!todasLosMensajes) {
			List<Mensaje> mensajes = mensajesDAO.obtenerListaDeDocumentos(
					fechaDeActualizacion,
					Documento.TIPO_FACTURA,
					page, 100
			);
			procesarFacturasParaUpoc(mensajes);
			page ++;
			if (mensajes.size() < 100) {
				todasLosMensajes = true;
			}
		}
	}

	public void procesarFacturasParaUpoc(List<Mensaje> mensajes) throws Exception {
		for (Mensaje mensaje: mensajes) {
			DocumentoCatalogoPayload documentoCatalogoPayload = new DocumentoCatalogoPayload(mensaje);
			Factura factura = obtenerFactura(documentoCatalogoPayload);
			procesarFactura(factura, mensaje);
		}
	}

	public void procesarFactura(
			Factura factura,
			Mensaje mensaje
	) {
		Optional<UpocDocumento> optionalUpocDocumento = upocDocumentoDAO.buscarUpocDocumento(
				factura.getFa1().getReferenceOrder(),
				mensaje.getParaUsuario(),
				mensaje.getDeUsuario()
		);
		if (optionalUpocDocumento.isPresent()) {
			UpocDocumento upocDocumento = optionalUpocDocumento.get();
			upocDocumento.setSfactura(mensaje.getId());
			upocDocumento.setFacturaNumero(mensaje.getDocumentoNumero());
			Empresa empresa = obtenerEmpresa(factura);
			if (empresa != null) {
				procesarProductosFacturados(empresa, upocDocumento, factura);
			}
			upocDocumentoDAO.save(upocDocumento);
		}
	}

	public void procesarProductosFacturados(
			Empresa empresa,
			UpocDocumento upocDocumento,
			Factura factura
	) {
		upocDocumento.getUpocProductosNoPedidosFacturados().clear();
		for (Fa2 fa2: factura.getFa2list()) {
			UpocPedido upocPedidoFacturado = obtenerPedidoFacturado(empresa, fa2);
			UpocProducto upocProducto = obtenerProductoPedido(upocDocumento, fa2);
			if (upocProducto != null) {
				upocProducto.setPedidoFacturado(upocPedidoFacturado);
			} else {
				upocDocumento.getUpocProductosNoPedidosFacturados().add(upocPedidoFacturado);
			}
		}
		upocDocumentoDAO.save(upocDocumento);
	}

	public UpocPedido obtenerPedidoFacturado(Empresa empresa, Fa2 fa2) {
		Optional<Producto> optionalProducto = productosDAO.findByIdEmpresaAndCppAndGtin(
				empresa.getId(),
				fa2.getItemIdSellerCode(),
				fa2.getItemIdGtin()
		);
		UpocPedido upocPedidoFacturado = new UpocPedido(fa2, optionalProducto.isPresent());
		return upocPedidoFacturado;
	}

	public UpocProducto obtenerProductoPedido(UpocDocumento upocDocumento, Fa2 fa2) {
		UpocProducto productoPedido = null;
		for (UpocProducto upocProducto : upocDocumento.getUpocProductos()) {
			if (
				checkearSiCoincidenGtin(upocProducto, fa2) ||
				checkearSiCoincidenCpp(upocProducto, fa2)
			) {
				productoPedido = upocProducto;
				break;
			}
		}
		return productoPedido;
	}

	public boolean checkearSiCoincidenGtin(UpocProducto upocProducto, Fa2 fa2) {
		return upocProducto.getPedidoRealizado().getGtin().equals(fa2.getItemIdGtin());
	}

	public boolean checkearSiCoincidenCpp(UpocProducto upocProducto, Fa2 fa2) {
		return upocProducto.getPedidoRealizado().getCpp().equals(fa2.getItemIdSellerCode());
	}

	public Empresa obtenerEmpresa(Factura factura) {
		Empresa empresa = null;
		Optional<Ubicacion> ubicacionEmpresa = ubicacionesDAO.findByKey("codigo", factura.getFa1().getSeller_gln());
		if (ubicacionEmpresa.isPresent()) {
			empresa = empresasDAO.findById(ubicacionEmpresa.get().getSempresa());
		}
		return empresa;
	}

	public Factura obtenerFactura(DocumentoCatalogoPayload ordenCatalogo) throws Exception {
		String[] lines = ordenCatalogo.getOrden().split(System.getProperty("line.separator"));
		Integer desdeIndice = 0;
		Factura factura = new Factura();
		factura.setEstado(ordenCatalogo.getEstado());
		factura.setUsuarioQueCambioElStatus(ordenCatalogo.getUsuarioQueCambioElStatus());
		factura.setTipodoc(ordenCatalogo.getTipoDocumento());
		factura.cargarTxt(lines, desdeIndice);
		return factura;
	}

}
