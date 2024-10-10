package sv.org.arrupe.becas.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;

@Controller
public class LoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @Value("${api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String index(HttpSession session) {
        logger.debug("Accediendo a la página de inicio");
        if (session.getAttribute("carnet") != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        if (session.getAttribute("carnet") != null) {
            logger.debug("Usuario ya autenticado, redirigiendo a dashboard");
            return "redirect:/dashboard";
        }
        logger.debug("Mostrando página de login");
        return "login";
    }

    @PostMapping("/procesar-login")
    public String procesarLogin(@RequestParam String carnet, 
                              @RequestParam String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        logger.debug("Intentando iniciar sesión para el carnet: {}", carnet);
        
        try {
            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Construir la URL de la API
            String loginUrl = apiUrl + "/login?carnet=" + carnet + "&password=" + password;
            
            // Realizar la petición a la API
            ResponseEntity<Map> response = restTemplate.postForEntity(loginUrl, null, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                logger.debug("Login exitoso para el carnet: {}", carnet);
                session.setAttribute("carnet", carnet);
                
                // Obtener datos adicionales del usuario
                String usuarioUrl = apiUrl + "/usuario/" + carnet;
                ResponseEntity<Map> usuarioResponse = restTemplate.getForEntity(usuarioUrl, Map.class);
                
                if (usuarioResponse.getStatusCode() == HttpStatus.OK) {
                    session.setAttribute("usuario", usuarioResponse.getBody());
                }
                
                return "redirect:/dashboard";
            } else {
                logger.warn("Login fallido para el carnet: {}", carnet);
                redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
                return "redirect:/login";
            }
            
        } catch (Exception e) {
            logger.error("Error al procesar el login: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al procesar el login");
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String carnet = (String) session.getAttribute("carnet");
        if (carnet == null) {
            logger.warn("Intento de acceso al dashboard sin autenticación");
            return "redirect:/login";
        }
        
        logger.debug("Accediendo al dashboard para el usuario: {}", carnet);
        model.addAttribute("carnet", carnet);
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.debug("Cerrando sesión para el usuario: {}", session.getAttribute("carnet"));
        session.invalidate();
        return "redirect:/login";
    }
}