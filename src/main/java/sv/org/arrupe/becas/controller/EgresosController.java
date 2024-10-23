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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class EgresosController {

    private static final Logger logger = LoggerFactory.getLogger(EgresosController.class);

    @Value("${api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/egresos_form")
    public String mostrarGestionEgresos(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            logger.warn("Intento de acceso sin autenticación");
            return "redirect:/login";
        }

        try {
            // Configurar encabezados de la solicitud con el token de autenticación
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            // Realizar solicitud para obtener los egresos actuales
            ResponseEntity<Map> responseEgresos = restTemplate.exchange(
                apiUrl + "/api/becados/egresos",
                HttpMethod.GET,
                request,
                Map.class
            );

            // Verificar si la respuesta fue exitosa
            if (responseEgresos.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("egresos", responseEgresos.getBody());
            } else {
                model.addAttribute("error", "Error al obtener los egresos");
            }

            return "egresos_form";
        } catch (Exception e) {
            logger.error("Error al obtener los datos de egresos: {}", e.getMessage());
            model.addAttribute("error", "Error al obtener los datos: " + e.getMessage());
            return "egresos_form";
        }
    }

    @PutMapping("/api/egresos")
    @ResponseBody
    public ResponseEntity<?> actualizarEgresos(
            @RequestBody Map<String, Object> egresos,
            HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            logger.warn("Intento de actualización sin autenticación");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                               .body(Map.of("message", "No autorizado"));
        }

        try {
            logger.info("Iniciando actualización de egresos");

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(egresos, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl + "/api/becados/egresos",
                HttpMethod.PUT,
                request,
                Map.class
            );

            logger.info("Actualización de egresos completada con estado: {}", 
                       response.getStatusCode());

            return ResponseEntity.status(response.getStatusCode())
                               .body(response.getBody());

        } catch (Exception e) {
            logger.error("Error al actualizar egresos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(Map.of(
                                   "message", "Error al actualizar egresos",
                                   "error", e.getMessage()
                               ));
        }
    }
}