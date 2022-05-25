package rondanet.upoc.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class SpringAsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "empresasYUbicacionesDadasDeBajaThreadPoolTaskExecutor")
    public Executor empresasYUbicacionesDadasDeBajaThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "productosThreadPoolTaskExecutor")
    public Executor productosThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "todosLosProductosThreadPoolTaskExecutor")
    public Executor todosLosProductosThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "productosEliminadosThreadPoolTaskExecutor")
    public Executor productosEliminadosThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "visibilidadThreadPoolTaskExecutor")
    public Executor visibilidadThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "visibilidadProductosThreadPoolTaskExecutor")
    public Executor visibilidadProductosThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "imagenesThreadPoolTaskExecutor")
    public Executor imagenesThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "verificarImagenesThreadPoolTaskExecutor")
    public Executor verificarImagenesThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "listaPreciosThreadPoolTaskExecutor")
    public Executor listaPreciosThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "listaDeVentaSincronizacionHaciaAtrasThreadPoolTaskExecutor")
    public Executor listaDeVentaSincronizacionHaciaAtrasThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "empresasSinListaDeVentaSincronizacionHaciaAtrasThreadPoolTaskExecutor")
    public Executor empresasSinListaDeVentaSincronizacionHaciaAtrasThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "productosSincronizacionHaciaAtrasThreadPoolTaskExecutor")
    public Executor productosSincronizacionHaciaAtrasThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "listaDePrecioSincronizacionHaciaAtrasThreadPoolTaskExecutor")
    public Executor listaDePrecioSincronizacionHaciaAtrasThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "mensajesThreadPoolTaskExecutor")
    public Executor mensajesThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "statusMensajesThreadPoolTaskExecutor")
    public Executor statusMensajesThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "bonificacionTipoDocumentoThreadPoolTaskExecutor")
    public Executor bonificacionTipoDocumentoThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}