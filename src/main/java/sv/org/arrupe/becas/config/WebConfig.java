package sv.org.arrupe.becas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        registry.addInterceptor(authInterceptor()).addPathPatterns("/dashboard/**");
    }
}
