package rondanet.upoc;

import java.util.*;

import rondanet.upoc.catalogo.db.SincronizacionDAO;
import rondanet.upoc.core.services.interfaces.IUpocProcesarFacturaService;
import rondanet.upoc.core.services.interfaces.IUpocProcesarOrdenesDeCompraService;
import rondanet.upoc.core.utils.IEmailHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableScheduling
@EnableAsync
@EnableCaching
public class UpocApplication implements CommandLineRunner {

	Logger logger = LogManager.getLogger(UpocApplication.class);

	@Autowired
	private IEmailHelper emailHelper;

	@Autowired
	private SincronizacionDAO sincronizacionDAO;

	@Autowired
	IUpocProcesarOrdenesDeCompraService upocProcesarOrdenesDeCompraService;

	@Autowired
	IUpocProcesarFacturaService upocProcesarFacturaService;

	private static ConfigurableApplicationContext context;

	private static boolean sincronizandoMensajes = false;

	private static boolean sincronizandoFacturas = false;

	public static void main(final String[] args) {
		context = SpringApplication.run(UpocApplication.class, args);
	}

	public static void restart() {
		String[] args = new String[0];
		sincronizandoMensajes = false;
		sincronizandoFacturas = false;
		Restarter.initialize(args);
	}

	@Override
	public void run(String... args) throws Exception {
		sincronizarOrdenesDeCompra();
	}

	@Async("ordenesDeCompraThreadPoolTaskExecutor")
	@Scheduled(cron = "${cronExpressionSincronizarOrdenesDeCompra}")
	public void sincronizarOrdenesDeCompra() {
		if (!this.sincronizandoMensajes) {
			try {
				this.sincronizandoMensajes = true;
				Date dateEndUpdate = this.sincronizacionDAO.getUltimaFechaDeSincronizacion("upocOrdenesDeCompra");
				upocProcesarOrdenesDeCompraService.procesarOrdenenesDeCompra(dateEndUpdate);
				this.sincronizandoMensajes = false;
				this.sincronizacionDAO.actualizarUltimaFechaDeSincronizacion("upocOrdenesDeCompra", new Date());
			} catch (Exception e) {
				logger.log(Level.ERROR,"Sincronizando Ordenes De Compra : " + e.getMessage() +" "+ e.getStackTrace());
				this.emailHelper.sendErrorEmail("sincronizarOrdenesDeCompra()", e);
				restart();
			}
		}
	}

	@Async("facturasThreadPoolTaskExecutor")
	@Scheduled(cron = "${cronExpressionSincronizarFacturas}")
	public void sincronizarFacturas() {
		if (!this.sincronizandoMensajes && !this.sincronizandoFacturas) {
			try {
				this.sincronizandoFacturas = true;
				Date dateEndUpdate = this.sincronizacionDAO.getUltimaFechaDeSincronizacion("upocFacturas");
				upocProcesarFacturaService.procesarFacturas(dateEndUpdate);
				this.sincronizandoFacturas = false;
				this.sincronizacionDAO.actualizarUltimaFechaDeSincronizacion("upocFacturas", new Date());
			} catch (Exception e) {
				logger.log(Level.ERROR,"Sincronizando Facturas: " + e.getMessage() +" "+ e.getStackTrace());
				this.emailHelper.sendErrorEmail("sincronizarFacturas()", e);
				restart();
			}
		}
	}
}
