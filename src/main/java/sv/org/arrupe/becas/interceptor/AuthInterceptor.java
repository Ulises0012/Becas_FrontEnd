package sv.org.arrupe.becas.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        // Verificar si el usuario está autenticado
        if (session == null || session.getAttribute("usuario") == null) {
            // Redirigir a la página de inicio de sesión si no está autenticado
            response.sendRedirect(request.getContextPath() + "/login");
            return false; // Evitar que se procese la solicitud
        }
        return true; // Continuar con la solicitud
    }
}
