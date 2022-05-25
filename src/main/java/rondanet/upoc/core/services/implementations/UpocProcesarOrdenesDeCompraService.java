package rondanet.upoc.core.services.implementations;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Producto;
import common.rondanet.catalogo.core.entity.Ubicacion;
import common.rondanet.pedidos.core.traductor.Oc2;
import org.apache.logging.log4j.Level;
import rondanet.upoc.catalogo.db.EmpresasDAO;
import rondanet.upoc.catalogo.db.ProductosDAO;
import rondanet.upoc.catalogo.db.UbicacionesDAO;
import rondanet.upoc.catalogo.exceptions.ModelException;
import rondanet.upoc.core.db.UpocDocumentoDAO;
import rondanet.upoc.core.entity.UpocDocumento;
import rondanet.upoc.core.entity.UpocProducto;
import rondanet.upoc.core.services.interfaces.IUpocProcesarOrdenesDeCompraService;
import rondanet.upoc.core.utils.IEmailHelper;
import rondanet.upoc.pedidos.db.MensajesDAO;
import common.rondanet.pedidos.core.entity.Mensaje;
import common.rondanet.pedidos.core.resources.DocumentoCatalogoPayload;
import common.rondanet.pedidos.core.traductor.OrdenDeCompra;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UpocProcesarOrdenesDeCompraService implements IUpocProcesarOrdenesDeCompraService {

	Logger logger = LogManager.getLogger(UpocProcesarOrdenesDeCompraService.class);

	@Autowired
	MensajesDAO mensajesDAO;

	@Autowired
	ProductosDAO productosDAO;

	@Autowired
	UbicacionesDAO ubicacionesDAO;

	@Autowired
	EmpresasDAO empresasDAO;

	@Autowired
	IEmailHelper emailHelper;

	@Autowired
	UpocDocumentoDAO upocDocumentoDAO;

	@Override
	public void procesarOrdenenesDeCompra(Date fechaDeActualizacion) {
		try {
			procesarOrdenenesDeCompraParaUPOC(fechaDeActualizacion);
		} catch (Exception e) {
			logger.log(Level.ERROR,"sincronizarMensajes: " + e.getStackTrace());
			this.emailHelper.sendErrorEmail("sincronizarMensajes", e);
		}
	}

	@Override
	public void procesarOrdenenesDeCompraParaUPOC(Date fechaDeActualizacion) throws Exception {
		int page = 0;
		boolean todasLosMensajes = false;
		while (!todasLosMensajes) {
			List<Mensaje> mensajes = mensajesDAO.obtenerListaDeDocumentos(fechaDeActualizacion, page, 100);
			procesarOrdenenesDeCompraParaUpoc(mensajes);
			page ++;
			if (mensajes.size() < 100) {
				todasLosMensajes = true;
			}
		}
	}

	public void procesarOrdenenesDeCompraParaUpoc(List<Mensaje> mensajes) throws Exception {
		for (Mensaje mensaje: mensajes) {
			DocumentoCatalogoPayload documentoCatalogoPayload = new DocumentoCatalogoPayload(mensaje);
			OrdenDeCompra ordenDeCompra = obtenerOrdenDeCompra(documentoCatalogoPayload);
			UpocDocumento upocDocumento = new UpocDocumento(mensaje);
			procesarOrdenDeCompra(ordenDeCompra, upocDocumento, mensaje);
		}
	}

	public OrdenDeCompra obtenerOrdenDeCompra(DocumentoCatalogoPayload ordenCatalogo) throws Exception {
		String[] lines = ordenCatalogo.getOrden().split(System.getProperty("line.separator"));
		Integer desdeIndice = 0;
		OrdenDeCompra ordenDeCompra = new OrdenDeCompra(ordenCatalogo);
		ordenDeCompra.cargarTxt(lines, desdeIndice);
		return ordenDeCompra;
	}

	public void procesarOrdenDeCompra(
			OrdenDeCompra ordenDeCompra,
			UpocDocumento upocDocumento,
			Mensaje mensaje
	) throws ModelException {
		HashMap<String, Empresa> empresas = obtenerEmpresas(ordenDeCompra);
		Empresa empresa = empresas.get("empresa");
		Empresa proveedor = empresas.get("proveedor");
		upocDocumento.validarEmpresas(empresa, proveedor);
		if (empresa != null && proveedor != null) {
			Optional<UpocDocumento> optionalUpocDocumento = upocDocumentoDAO.findByMensajeId(mensaje.getId());
			if (optionalUpocDocumento.isPresent()) {
				upocDocumento = optionalUpocDocumento.get();
			}
			List<UpocProducto> upocProductos = new ArrayList<>();
			int cantidadDeLineasConErrores = 0;
			int cantidadDeLineasConWarning = 0;
			upocDocumento.setTotalDeLineas(ordenDeCompra.getOc2list().size());
			for (Oc2 productoEnOrdenDeCompra : ordenDeCompra.getOc2list()) {
				UpocProducto upocProducto = validarProducto(empresa, proveedor, productoEnOrdenDeCompra);
				if (tieneErrores(upocProducto)) {
					cantidadDeLineasConErrores ++;
				} else if (tieneWarning(upocProducto)) {
					cantidadDeLineasConWarning ++;
				}
				upocProductos.add(upocProducto);
			}
			upocDocumento.setTieneErrores(cantidadDeLineasConErrores > 0 || cantidadDeLineasConWarning > 0);
			upocDocumento.setTotalDeLineasConError(cantidadDeLineasConErrores);
			upocDocumento.setUpocProductos(upocProductos);
			upocDocumentoDAO.save(upocDocumento);
		}
	}

	public HashMap<String, Empresa> obtenerEmpresas(OrdenDeCompra ordenDeCompra) {
		Empresa empresa = null;
		Empresa proveedor = null;
		Optional<Ubicacion> ubicacionEmpresa = ubicacionesDAO.findByKey("codigo", ordenDeCompra.getOc1().getBuyer_gln());
		Optional<Ubicacion> ubicacionProveedor = ubicacionesDAO.findByKey("codigo", ordenDeCompra.getOc1().getSeller_gln());
		if (ubicacionEmpresa.isPresent()) {
			empresa = empresasDAO.findById(ubicacionEmpresa.get().getSempresa());
		}
		if (ubicacionProveedor.isPresent()) {
			proveedor = empresasDAO.findById(ubicacionProveedor.get().getSempresa());
		}
		HashMap<String, Empresa> empresas = new HashMap<>();
		empresas.put("empresa", empresa);
		empresas.put("proveedor", proveedor);
		return empresas;
	}

	public boolean tieneErrores(UpocProducto upocProducto) {
		return !upocProducto.getVisible();
	}

	public boolean tieneWarning(UpocProducto upocProducto) {
		return !upocProducto.getCoincidenLasUnidadesPedidas();
	}

	public UpocProducto validarProducto(
			Empresa empresa,
			Empresa proveedor,
			Oc2 productoEnOrdenDeCompra
	) throws ModelException {
		Producto producto = productosDAO.obtenerProductoVisiblesParaEmpresa(
				empresa,
				proveedor,
				productoEnOrdenDeCompra.getItemIdSellerCode(),
				productoEnOrdenDeCompra.getItemIdGtin()
		);
		UpocProducto upocProducto = new UpocProducto();
		upocProducto.upocPedidoRealizado(productoEnOrdenDeCompra, producto);
		if (producto == null) {
			Optional<Producto> productoEnLaEmpresaPorCppAndGtin = productosDAO.findByIdEmpresaAndCppAndGtin(
					empresa.getId(),
					productoEnOrdenDeCompra.getItemIdSellerCode(),
					productoEnOrdenDeCompra.getItemIdGtin()
			);
			if (productoEnLaEmpresaPorCppAndGtin.isPresent()) {
				upocProducto.setExiste(true);
			}
		}
		return upocProducto;
	}
}
