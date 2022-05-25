package rondanet.upoc.catalogo.db;

import common.rondanet.catalogo.core.entity.Sincronizacion;
import rondanet.upoc.catalogo.repository.ISincronizacionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
public class SincronizacionDAO {

	Logger logger = LogManager.getLogger(SincronizacionDAO.class);

	@Autowired
	private ISincronizacionRepository sincronizacionRepository;

	public void actualizarUltimaFechaDeSincronizacion(String collecion, Date ultimaFechaDeSincronizacion) {
		Sincronizacion sincronizacion;
		Optional<Sincronizacion> optionalSincronizacion = sincronizacionRepository.findFirstByColleccion(collecion);
		if(optionalSincronizacion.isPresent()){
			sincronizacion = optionalSincronizacion.get();
		}else {
			sincronizacion = new Sincronizacion(collecion, ultimaFechaDeSincronizacion);
			sincronizacion.setFechaCreacion();
		}
		sincronizacion.setFechaEdicion();
		sincronizacion.setUltimaFechaSincronizacion(ultimaFechaDeSincronizacion);
		sincronizacionRepository.save(sincronizacion);
	}

	public Date getUltimaFechaDeSincronizacion(String collecion) {
		Date ultimaFechaDeSincronizacion = new Date();
		Optional<Sincronizacion> optionalSincronizacion = sincronizacionRepository.findFirstByColleccion(collecion);
		if(optionalSincronizacion.isPresent()){
			ultimaFechaDeSincronizacion = optionalSincronizacion.get().getUltimaFechaSincronizacion();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ultimaFechaDeSincronizacion);
		calendar.add(Calendar.MINUTE, -1);
		ultimaFechaDeSincronizacion = calendar.getTime();
		return ultimaFechaDeSincronizacion;
	}

}