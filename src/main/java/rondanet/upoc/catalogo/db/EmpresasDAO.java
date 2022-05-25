package rondanet.upoc.catalogo.db;

import java.util.*;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import rondanet.upoc.catalogo.repository.IEmpresaRepository;
import rondanet.upoc.catalogo.repository.IProductoRepository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Component
public class EmpresasDAO {

	Logger logger = LogManager.getLogger(EmpresasDAO.class);

	@Autowired
	private IEmpresaRepository empresaRepository;

	@Autowired
	private IProductoRepository productoRepository;

	@Autowired
	private UsuariosDAO userRepository;

	@Autowired
	private ListaDeVentaVisibilidadDAO listaDeVentaVisibilidadDAO;

	private final MongoOperations mongoOperations;

	public EmpresasDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IEmpresaRepository empresaRepository, UsuariosDAO userRepository, IProductoRepository productoRepository, ListaDeVentaVisibilidadDAO listaDeVentaVisibilidadDAO) {
		this.mongoOperations = mongoOperations;
		this.empresaRepository = empresaRepository;
		this.userRepository = userRepository;
		this.productoRepository = productoRepository;
		this.listaDeVentaVisibilidadDAO = listaDeVentaVisibilidadDAO;
	}

	/**
	 * Salva una {@link Empresa}
	 * @param e {@link Empresa}
	 * @return {@link Empresa}
	 */
	public Empresa save(Empresa e) {
		e = empresaRepository.save(e);
		e.setSId(e.getId());
		empresaRepository.save(e);
		logger.log(Level.INFO, "El método save() de la clase EmpresaDAO fue ejecutado.");
		return e;
	}

	/**
	 * Obtiene una {@link Empresa} donde {@link String} id sea igual al {@link String} id pasado como parámetro
	 * @param id {@link String}
	 * @return {@link Empresa} o null
	 */
	public Empresa findById(String id) {
		logger.log(Level.INFO, "El método findById() de la clase EmpresaDAO fue ejecutado.");
		return this.findById(id, null);
	}

	/**
	 * Obtiene un {@link Optional}<{@link Empresa}> donde {@link String} id sea igual al {@link String} id pasado como parámetro
	 * @param id {@link String}
	 * @param ue {@link UsuarioEmpresa}, No se usa
	 * @return {@link Empresa} o null
	 */
	public Empresa findById(String id, UsuarioEmpresa ue) {
		Optional<Empresa> empresa = empresaRepository.findById(id);
		return empresa.isPresent() ? empresa.get() : null;
	}

	/**
	 * Obtiene un {@link Optional}<{@link Empresa}> donde el rut de la empresa sea el pasado como parámetro
	 * @param value {@link Long}
	 * @return {@link Optional}<{@link Empresa}>
	 */
	public Optional<Empresa> findByRutEmpresa(String value) {
		logger.log(Level.INFO, "El metodo findByRutEmpresa() de la clase EmpresasDAO fue ejecutado.");
		Query query = new Query();
		List<Empresa> listaEmpresas = new ArrayList<>();
		try {
			listaEmpresas = mongoOperations.find(query.addCriteria(Criteria.where("eliminado").is(false).orOperator(Criteria.where("rut").is(value), Criteria.where("rut").is(Long.parseLong(value)))), Empresa.class);
		} catch (NumberFormatException e) {
			listaEmpresas = mongoOperations.find(query.addCriteria(Criteria.where("eliminado").is(false).orOperator(Criteria.where("rut").is(value))), Empresa.class);
		}
		logger.log(Level.INFO, "El método findByRutEmpresa() de la clase EmpresaDAO fue ejecutado.");
		if (listaEmpresas.size() != 0)
			return Optional.of(listaEmpresas.get(0));
		return Optional.ofNullable(null);
	}

	/**
	 * Devuelve un {@link Optional}<{@link Empresa}> {@link String} key sea igual a {@link String} value pasado por parámetro
	 * @param key {@link String}
	 * @param value {@link String}
	 * @return {@link Optional}<{@link Empresa}>
	 */
	public Optional<Empresa> findByKey(String key, String value) {
		Query query = new Query();
		List<Empresa> listaEmpresas = new ArrayList<>();
		try {
			listaEmpresas = mongoOperations.find(query.addCriteria(Criteria.where("eliminado").is(false).orOperator(Criteria.where(key).is(value), Criteria.where(key).is(Long.parseLong(value)))), Empresa.class);
		} catch (NumberFormatException e) {
			logger.log(Level.ERROR, "Error en el método findByKey() de la clase EpresasDAO. Error", e.getMessage(), ExceptionUtils.getStackTrace(e));
			listaEmpresas = mongoOperations.find(query.addCriteria(Criteria.where("eliminado").is(false).orOperator(Criteria.where(key).is(value))), Empresa.class);
		}
		if (listaEmpresas.size() != 0){
			logger.log(Level.INFO, "El método findByKey() de la clase EmpresasDAO fue ejecutado.");
			return Optional.of(listaEmpresas.get(0));
		}
		logger.log(Level.INFO, "El método findByKey() de la clase EmpresasDAO fue ejecutado.");
		return Optional.ofNullable(null);
	}

    /**
     * Obtiene una {@link List} de todas las {@link Empresa}
     * @return {@link List}<{@link Empresa}>
     */
	public List<Empresa> getAll() {
		logger.log(Level.INFO, "El método getAll() de la clase EmpresasDAO fue ejecutado.");
		return empresaRepository.findAll();
	}

	/**
	 * Obtiene una {@link List} de todas las {@link Empresa}
	 * @return {@link List}<{@link Empresa}>
	 */
	public List<String> getAllCodigoInternoEmpresasPorId(List<String> idDeEmpresas) {
		Aggregation empresasAggregation = Aggregation.newAggregation(match(Criteria.where("eliminado").is(false).andOperator(Criteria.where("sid").in(idDeEmpresas))), group("codigoInterno"));
		List<Empresa> empresas = mongoOperations.aggregate(empresasAggregation, "Empresa", Empresa.class).getMappedResults();
		return obtenerLosIdDeLasEmpresas(empresas);
	}

	public List<String> obtenerLosIdDeLasEmpresas(List<Empresa> empresas){
		List<String> idDeEmpresas = new ArrayList<>();
		for (Empresa empresa: empresas) {
			if(empresa.getId() != null) {
				idDeEmpresas.add(empresa.getId());
			}
		}
		return idDeEmpresas;
	}

}