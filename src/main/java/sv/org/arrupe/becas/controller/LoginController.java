package sv.org.arrupe.becas.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.client.HttpClientErrorException;
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
            logger.info("Usuario ya autenticado con carnet: {}, redirigiendo al dashboard", session.getAttribute("carnet"));
            return "redirect:/dashboard";
        }
        return "index";
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
            String loginUrl = apiUrl + "/signin";
            logger.debug("URL de login: {}", loginUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> body = Map.of("carnet", carnet, "password", password);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            logger.debug("Enviando solicitud a la API para carnet: {}", carnet);
            ResponseEntity<Map> response = restTemplate.exchange(loginUrl, HttpMethod.POST, request, Map.class);
            logger.debug("Respuesta recibida desde API: Status: {}, Body: {}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                String rol = (String) responseBody.get("rol");
                String token = (String) responseBody.get("token");

                logger.info("Login exitoso, carnet: {}, rol: {}", carnet, rol);

                session.setAttribute("carnet", carnet);
                session.setAttribute("rol", rol);
                session.setAttribute("token", token);

                // Obtener datos adicionales del usuario usando el token
                String usuarioUrl = apiUrl + "/usuario/" + carnet;
                HttpHeaders authHeaders = new HttpHeaders();
                authHeaders.setBearerAuth(token);
                HttpEntity<Void> authRequest = new HttpEntity<>(authHeaders);
                ResponseEntity<Map> usuarioResponse = restTemplate.exchange(usuarioUrl, HttpMethod.GET, authRequest, Map.class);

                if (usuarioResponse.getStatusCode() == HttpStatus.OK) {
                    session.setAttribute("usuario", usuarioResponse.getBody());
                    logger.debug("Datos de usuario obtenidos para carnet: {}", carnet);
                }

                if ("Admin".equalsIgnoreCase(rol)) {
                    return "redirect:/dashboard_admin";
                } else if ("Estudiante".equalsIgnoreCase(rol)) {
                    return "redirect:/dashboard";
                } else {
                    logger.warn("Rol no reconocido para carnet: {}, rol: {}", carnet, rol);
                    redirectAttributes.addFlashAttribute("error", "Rol no reconocido");
                    return "redirect:/login";
                }

            } else {
                logger.warn("Login fallido para carnet: {}, estado: {}", carnet, response.getStatusCode());
                redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
                return "redirect:/login";
            }

        } catch (HttpClientErrorException e) {
            logger.error("Error HTTP al procesar login para carnet: {}. Estado: {}, Respuesta: {}", 
                         carnet, e.getStatusCode(), e.getResponseBodyAsString(), e);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
            } else {
                redirectAttributes.addFlashAttribute("error", "Error al procesar el login: " + e.getMessage());
            }
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Error inesperado al procesar login para carnet: {}", carnet, e);
            redirectAttributes.addFlashAttribute("error", "Error inesperado al procesar el login");
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
        return "dashboard_admin";
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  HttpSession session) {
        String carnet = (String) session.getAttribute("carnet");
        logger.info("Cerrando sesión para usuario con carnet: {}", carnet);
        
        // Configurar headers de caché
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        // Invalidar la sesión
        session.invalidate();
        logger.info("Sesión cerrada exitosamente");
        
        return ResponseEntity.ok()
            .header("Cache-Control", "no-cache, no-store, must-revalidate")
            .header("Pragma", "no-cache")
            .header("Expires", "0")
            .body(Map.of("message", "Logout exitoso"));
    }
}