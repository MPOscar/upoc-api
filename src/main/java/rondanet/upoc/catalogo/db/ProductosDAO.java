package rondanet.upoc.catalogo.db;

import java.util.*;


import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.ListaDeVenta;
import common.rondanet.catalogo.core.entity.ListaDeVentaVisibilidad;
import common.rondanet.catalogo.core.entity.Producto;
import rondanet.upoc.catalogo.exceptions.ModelException;
import rondanet.upoc.catalogo.pedidosQuery.ListaDeVentaQuerys;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.joda.time.DateTime;
import rondanet.upoc.catalogo.repository.IEmpresaRepository;
import rondanet.upoc.catalogo.repository.IGrupoRepository;
import rondanet.upoc.catalogo.repository.IProductoRepository;
import rondanet.upoc.catalogo.repository.IUserRepository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Component
public class ProductosDAO{

	Logger logger = LogManager.getLogger(ProductosDAO.class);

	@Autowired
    IProductoRepository productoRepository;

	@Autowired
    IEmpresaRepository empresaRepository;

	@Autowired
	EmpresasDAO empresasDAO;

	@Autowired
    IGrupoRepository grupoRepository;

	@Autowired
    IUserRepository userRepository;

	@Autowired
	ListasDeVentaDAO listasDeVentaDAO;

	@Autowired
	ListaDeVentaVisibilidadDAO listaDeVentaVisibilidadDAO;

	private final MongoOperations mongoOperations;

	public ProductosDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IProductoRepository productoRepository, IEmpresaRepository empresaRepository,
						IUserRepository userRepository, EmpresasDAO empresasDAO, IGrupoRepository grupoRepository) {
		this.mongoOperations = mongoOperations;
		this.productoRepository = productoRepository;
		this.empresaRepository = empresaRepository;
		this.userRepository = userRepository;
		this.empresasDAO = empresasDAO;
		this.grupoRepository = grupoRepository;
	}

	/**
	 * Devuelve un {@link Producto} donde {@link String} id del {@link Producto} sea igual al {@link String} id pasado por parámetro
	 * @param id {@link String}
	 * @return {@link Producto}
	 */
	public Producto findById(String id) {
		Optional<Producto> producto = productoRepository.findById(id);
		logger.log(Level.INFO, "El método findById() de la clase ProductosDAO fue ejecutado.");
		return producto.isPresent() ? producto.get() : null;
	}

	/**
	 * Devuelve un {@link Optional}<{@link Producto}> donde el {@link String} cpp y el {@link String} sempresa de {@link Producto}
	 * sea igual al {@link String} cpp y el {@link String} empresaId pasado por parámetro.
	 * @param empresaId {@link String}
	 * @param cpp {@link String}
	 * @return {@link Optional}<{@link Producto}>
	 */
	public Optional<Producto> findByIdEmpresaAndCpp(String empresaId, String cpp) {
		logger.log(Level.INFO, "El metodo findByIdEmpresaAndCpp() de la clase ProductosDAO fue ejecutado.");
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresaId).andOperator(Criteria.where("cpp").is(cpp)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		if (productos.size() > 0)
			return Optional.of(productos.get(0));
		return Optional.ofNullable(null);
	}

	public Optional<Producto> findByIdEmpresaAndGtin(String empresaId, String gtin) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresaId).andOperator(Criteria.where("gtin").is(gtin)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		if (productos.size() > 0)
			return Optional.of(productos.get(0));
		return Optional.ofNullable(null);
	}

	public Optional<Producto> findByIdEmpresaAndCppAndGtin(String empresaId, String cpp,  String gtin) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresaId).andOperator(Criteria.where("cpp").is(cpp), Criteria.where("gtin").is(gtin)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		if (productos.size() > 0)
			return Optional.of(productos.get(0));
		return Optional.ofNullable(null);
	}

	public Producto save(Producto producto) {
		producto.setFechaEdicion();
		if(producto.getFechaCreacion() == null){
			producto.setFechaCreacion();
		}
		if(producto.getDivision() == null) {
			producto.setDivision("");
		}
		if(producto.getLinea() == null) {
			producto.setLinea("");
		}
		if(producto.getMarca() == null) {
			producto.setMarca("");
		}
		if(producto.getSId() == null) {
			producto = productoRepository.save(producto);
			producto.setSId(producto.getId());
		}
		logger.log(Level.INFO, "El método save() de la clase ProductoDAO fue ejecutado.");
		return productoRepository.save(producto);
	}

	public List<Producto> getAll() {
		List<Producto> listaProductos = productoRepository.findAll();
		return listaProductos;
	}

	public List<Producto> findByKey(String key, String value) {
		Query query = new Query();
		List<Producto> productos = new ArrayList<>();
		try {
			query.addCriteria(Criteria.where("eliminado").is(false).orOperator(Criteria.where(key).is(value), Criteria.where("key").is(Long.parseLong(value))));
			productos = mongoOperations.find(query, Producto.class);
		} catch (NumberFormatException e) {
			query.addCriteria(Criteria.where("eliminado").is(false).orOperator(Criteria.where(key).is(value)));
			productos = mongoOperations.find(query, Producto.class);
		}
		return productos;
	}

	public Query getProductoVisibleByBussinesOnSaleListQuery(Empresa empresa, List<String> listasDeVenta, String cpp, String gtin){
		Aggregation productosVisiblesListaDeVentaAggregation = Aggregation.newAggregation(match(Criteria.where("slistaDeVenta").in(listasDeVenta).andOperator(Criteria.where("eliminado").is(false)).orOperator(Criteria.where("esPublico").is(true), Criteria.where("sempresasConVisibilidad").in(empresa.getSId()), Criteria.where("sgruposConVisibilidad").in(empresa.getSempresaGrupos()))), group("sproducto"));
		List<ListaDeVentaVisibilidad> listaDeVentaVisibilidadProductos = mongoOperations.aggregate(productosVisiblesListaDeVentaAggregation, "ListaDeVentaVisibilidad", ListaDeVentaVisibilidad.class).getMappedResults();
		Set<String> productos = new HashSet<>();
		for (ListaDeVentaVisibilidad listaDeVentaVisibilidad: listaDeVentaVisibilidadProductos) {
			productos.add(listaDeVentaVisibilidad.getId());
		}

		Query productosQuery;
		productosQuery = new BasicQuery(ListaDeVentaQuerys.OCULTAR_DISCONTINUADOS_Y_SUSPENDIDOS);

		List<Criteria> orExpression =  new ArrayList<>();
		List<Criteria> andExpression =  new ArrayList<>();

		Criteria expressionId = new Criteria();
		expressionId.and("sid").in(productos);
		andExpression.add(expressionId);

		Criteria expressionEliminado = new Criteria();
		expressionEliminado.and("eliminado").is(false);
		andExpression.add(expressionEliminado);

		if (cpp != null) {
			Criteria expressionCpp = new Criteria();
			expressionCpp.and("cpp").is(cpp);
			andExpression.add(expressionCpp);
		}

		if (gtin != null) {
			Criteria expressionGtin = new Criteria();
			expressionGtin.and("gtin").is(gtin);
			andExpression.add(expressionGtin);
		}

		Criteria expressionEsPublico = new Criteria();
		expressionEsPublico.and("esPublico").is(true);
		orExpression.add(expressionEsPublico);

		Criteria expressionEsPrivado = new Criteria();
		expressionEsPrivado.and("esPrivado").is(true);
		orExpression.add(expressionEsPrivado);

		/*criteria.andOperator(
				Criteria.where("sid").in(productos),
				Criteria.where("eliminado").is(false),
				Criteria.where("cpp").is(cpp),
				Criteria.where("gtin").is(gtin)
		).orOperator(
				Criteria.where("esPublico").is(true),
				Criteria.where("esPrivado").is(true)
		);*/

		Criteria criteria = new Criteria();
		productosQuery.addCriteria(criteria.andOperator(andExpression.toArray(new Criteria[andExpression.size()])).orOperator(orExpression.toArray(new Criteria[orExpression.size()])));

		//productosQuery.addCriteria(criteria);
		return productosQuery;
	}

	public Producto obtenerProductoVisiblesParaEmpresa(
			Empresa empresa,
			Empresa proveedor,
			String cpp,
			String gtin
	) throws ModelException {
		List<ListaDeVenta> listasDeVentas = listasDeVentaDAO.findAllByEmpresa(proveedor);
		List<String> listasDeVentaId = obtenerIdDeListasDeVenta(listasDeVentas);
		Query query = getProductoVisibleByBussinesOnSaleListQuery(empresa, listasDeVentaId, cpp, gtin);
		Producto producto = mongoOperations.findOne(query, Producto.class);
		return producto;
	}

	public List<String> obtenerIdDeListasDeVenta(List<ListaDeVenta> listasDeVentas) {
		List<String> listasDeVentaId = new ArrayList<>();
		for (ListaDeVenta listaDeVenta: listasDeVentas) {
			listasDeVentaId.add(listaDeVenta.getId());
		}
		return listasDeVentaId;
	}
}
