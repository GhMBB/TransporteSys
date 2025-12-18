package com.example.transportesys.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuración de caché con Caffeine.
 * Mejora el rendimiento de endpoints de consulta frecuente.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
            "vehiculos",
            "vehiculosActivos",
            "vehiculosLibres",
            "conductores",
            "conductoresActivos",
            "conductoresSinVehiculos",
            "conteoVehiculos",
            "reporteVehiculosLibres",
            "reporteConductoresSinVehiculos",
            "reporteVehiculosPorConductor"
        );

        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(500)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .recordStats());

        return cacheManager;
    }
}
