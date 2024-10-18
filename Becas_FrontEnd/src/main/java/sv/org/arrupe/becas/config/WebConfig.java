package sv.org.arrupe.becas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import sv.org.arrupe.becas.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(); // Crear una instancia del interceptor
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Aplicar el interceptor a las rutas que deseas proteger
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/dashboard/**")  // Protege solo las rutas del dashboard
                .excludePathPatterns("/login", "/api/**"); // Excluye el login y las rutas de API de autenticación
    }

    // Configuración CORS para permitir solicitudes entre el frontend y el backend
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica CORS solo a las rutas de API
                .allowedOrigins("http://localhost:8080")  // URL de tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
