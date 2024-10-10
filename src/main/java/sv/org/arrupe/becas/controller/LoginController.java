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
    public String index() {
        logger.debug("Accediendo a la página de inicio");
        return "index";  // Muestra la página de inicio sin redirigir

        if (session.getAttribute("carnet") != null) {
            logger.info("Usuario ya autenticado con carnet: {}, redirigiendo al dashboard", session.getAttribute("carnet"));
            return "redirect:/dashboard";
        }
        return "redirect:/login";

    }

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        if (session.getAttribute("carnet") != null) {
            logger.info("Usuario ya autenticado con carnet: {}, redirigiendo al dashboard", session.getAttribute("carnet"));
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
        logger.info("Procesando login para carnet: {}", carnet);
        try {
            // Construir la URL de la API para el login
            String loginUrl = apiUrl + "/login?carnet=" + carnet + "&password=" + password;

            // Realizar la petición a la API
            ResponseEntity<Map> response = restTemplate.postForEntity(loginUrl, null, Map.class);
            logger.debug("Respuesta recibida desde API: {}", response);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                String rol = (String) body.get("rol");  // Obtener el rol del usuario

                logger.info("Login exitoso, carnet: {}, rol: {}", carnet, rol);

                session.setAttribute("carnet", carnet);
                session.setAttribute("rol", rol);  // Guardar el rol en la sesión

                // Obtener datos adicionales del usuario
                String usuarioUrl = apiUrl + "/usuario/" + carnet;
                ResponseEntity<Map> usuarioResponse = restTemplate.getForEntity(usuarioUrl, Map.class);

                if (usuarioResponse.getStatusCode() == HttpStatus.OK) {
                    session.setAttribute("usuario", usuarioResponse.getBody());
                    logger.debug("Datos de usuario obtenidos para carnet: {}", carnet);
                }

                // Redirigir según el rol del usuario
                if ("Admin".equalsIgnoreCase(rol)) {
                    return "redirect:/dashboard_admin";
                } else if ("Estudiante".equalsIgnoreCase(rol)) {
                    return "redirect:/dashboard";
                } else {
                    redirectAttributes.addFlashAttribute("error", "Rol no reconocido");
                    return "redirect:/login";
                }

            } else {
                logger.warn("Login fallido para carnet: {}, estado: {}", carnet, response.getStatusCode());
                redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
                return "redirect:/login";
            }

        } catch (Exception e) {
            logger.error("Error al procesar login para carnet: {}", carnet, e);
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

        logger.info("Accediendo al dashboard para el usuario con carnet: {}", carnet);
        model.addAttribute("carnet", carnet);
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "dashboard";
    }
    
    @GetMapping("/dashboard_admin")
    public String dashboardAdmin(HttpSession session, Model model) {
        String carnet = (String) session.getAttribute("carnet");
        if (carnet == null) {
            logger.warn("Intento de acceso al dashboard admin sin autenticación");
            return "redirect:/login";
        }

        logger.info("Accediendo al dashboard admin para el usuario con carnet: {}", carnet);
        model.addAttribute("carnet", carnet);
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "dashboard_admin"; // Asegúrate de que esta vista existe
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.info("Cerrando sesión para el usuario con carnet: {}", session.getAttribute("carnet"));
        session.invalidate();
        return "redirect:/login";
    }
}
