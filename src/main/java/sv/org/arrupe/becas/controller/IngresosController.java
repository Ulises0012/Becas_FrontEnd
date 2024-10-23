package sv.org.arrupe.becas.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sv.org.arrupe.becas.service.UsuarioService;

import java.util.Map;

@Controller
public class IngresosController {

    private static final Logger logger = LoggerFactory.getLogger(IngresosController.class);

    @Value("${api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final UsuarioService usuarioService;

    public IngresosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/ingresos_form")
    public String mostrarGestionIngresos(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            logger.warn("Intento de acceso sin autenticación");
            return "redirect:/login";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<Map> responseIngresos = restTemplate.exchange(
                apiUrl + "/api/becados/ingresos",
                HttpMethod.GET,
                request,
                Map.class
            );

            if (responseIngresos.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("ingresos", responseIngresos.getBody());
            } else {
                model.addAttribute("error", "Error al obtener los ingresos");
            }

            return "ingresos_form";
        } catch (Exception e) {
            logger.error("Error al obtener los ingresos: {}", e.getMessage());
            model.addAttribute("error", "Error al obtener los ingresos: " + e.getMessage());
            return "ingresos_form";
        }
    }

    @PutMapping("/api/becados/ingresos")
    @ResponseBody
    public ResponseEntity<?> actualizarIngresos(
            @RequestBody Map<String, Object> ingresos,
            HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            logger.warn("Intento de actualización sin autenticación");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "No autorizado"));
        }

        try {
            logger.info("Iniciando actualización de ingresos");

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(ingresos, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl + "/api/becados/ingresos",
                HttpMethod.PUT,
                request,
                Map.class
            );

            logger.info("Actualización de ingresos completada con estado: {}", response.getStatusCode());

            return ResponseEntity.status(response.getStatusCode())
                    .body(response.getBody());

        } catch (Exception e) {
            logger.error("Error al actualizar los ingresos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al actualizar los ingresos", "error", e.getMessage()));
        }
    }
}
