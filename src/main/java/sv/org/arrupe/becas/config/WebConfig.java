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
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/**") // Primero protege todas las rutas
                .excludePathPatterns(
                    "/login",           // Página de login
                    "/procesar-login",  // Endpoint de procesamiento de login
                    "/logout",          // Endpoint de logout
                    "/css/**",          // Recursos estáticos CSS
                    "/js/**",           // Recursos estáticos JavaScript
                    "/images/**",       // Recursos estáticos de imágenes
                    "/api/**",          // APIs públicas
                    "/error",           // Páginas de error
                    "/favicon.ico",     // Favicon
                    "/assets/**"        // Otros recursos estáticos
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")

                .allowedOrigins(
                    "http://192.242.6.131",  
                    "http://localhost",
                    "http://localhost:8080"   // Común para desarrollo local
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                .allowedHeaders("*")
                .exposedHeaders(
                    "Authorization",
                    "Cache-Control",
                    "Content-Type",
                    "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Headers",
                    "Access-Control-Allow-Methods"
                )
                .allowCredentials(true)
                .maxAge(3600L); // Cache CORS preflight durante 1 hora
    }
}