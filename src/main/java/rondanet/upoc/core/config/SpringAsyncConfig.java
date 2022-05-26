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

    @Bean(name = "ordenesDeCompraThreadPoolTaskExecutor")
    public Executor ordenesDeCompraThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "facturasThreadPoolTaskExecutor")
    public Executor facturasThreadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}