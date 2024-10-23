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

import java.util.List;
import java.util.Map;

@Controller
public class ElectrodomesticosController {

    private static final Logger logger = LoggerFactory.getLogger(ElectrodomesticosController.class);

    @Value("${api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/electrodomesticos_form")
    public String mostrarGestionElectrodomesticos(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
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

            // Realizar solicitud para obtener tipos de electrodomésticos
            ResponseEntity<List> responseTiposElectrodomesticos = restTemplate.exchange(
                apiUrl + "/api/becados/electrodomesticos/tipos",
                HttpMethod.GET,
                request,
                List.class
            );

            // Realizar solicitud para obtener electrodomésticos actuales
            ResponseEntity<List> responseElectrodomesticos = restTemplate.exchange(
                apiUrl + "/api/becados/electrodomesticos/actuales",
                HttpMethod.GET,
                request,
                List.class
            );

            // Verificar si la respuesta fue exitosa para los tipos de electrodomésticos
            if (responseTiposElectrodomesticos.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("tiposElectrodomesticos", responseTiposElectrodomesticos.getBody());
            } else {
                model.addAttribute("error", "Error al obtener tipos de electrodomésticos");
            }

            // Verificar si la respuesta fue exitosa para los electrodomésticos actuales
            if (responseElectrodomesticos.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("electrodomesticos", responseElectrodomesticos.getBody());
            } else {
                model.addAttribute("error", "Error al obtener electrodomésticos actuales");
            }

            return "electrodomesticos_form";
        } catch (Exception e) {
            logger.error("Error al obtener los datos de electrodomésticos: {}", e.getMessage());
            model.addAttribute("error", "Error al obtener los datos: " + e.getMessage());
            return "electrodomesticos_form";
        }
    }

    @PutMapping("/api/becados/electrodomesticos/actualizar")
    @ResponseBody
    public ResponseEntity<?> actualizarElectrodomesticos(
            @RequestBody List<Map<String, Integer>> electrodomesticos,
            HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            logger.warn("Intento de actualización sin autenticación");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                               .body(Map.of("message", "No autorizado"));
        }

        try {
            logger.info("Iniciando actualización de electrodomésticos");

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<Map<String, Integer>>> request = 
                new HttpEntity<>(electrodomesticos, headers);

            logger.debug("Enviando solicitud al API con {} electrodomésticos", electrodomesticos.size());

            ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl + "/api/becados/electrodomesticos/actualizar",
                HttpMethod.PUT,
                request,
                Map.class
            );

            logger.info("Actualización de electrodomésticos completada con estado: {}", 
                       response.getStatusCode());

            return ResponseEntity.status(response.getStatusCode())
                               .body(response.getBody());

        } catch (Exception e) {
            logger.error("Error al actualizar electrodomésticos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(Map.of(
                                   "message", "Error al actualizar electrodomésticos",
                                   "error", e.getMessage()
                               ));
        }
    }

    @GetMapping("/api/becados/electrodomesticos/tipos")
    @ResponseBody
    public ResponseEntity<?> obtenerTiposElectrodomesticos(HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            logger.warn("Intento de obtener tipos de electrodomésticos sin autenticación");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                               .body(Map.of("message", "No autorizado"));
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<List> response = restTemplate.exchange(
                apiUrl + "/api/becados/electrodomesticos/tipos",
                HttpMethod.GET,
                request,
                List.class
            );

            return ResponseEntity.status(response.getStatusCode())
                               .body(response.getBody());
        } catch (Exception e) {
            logger.error("Error al obtener tipos de electrodomésticos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(Map.of("message", "Error al obtener tipos de electrodomésticos"));
        }
    }

    @GetMapping("/api/becados/electrodomesticos/actuales")
    @ResponseBody
    public ResponseEntity<?> obtenerElectrodomesticosActuales(HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            logger.warn("Intento de obtener electrodomésticos actuales sin autenticación");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                               .body(Map.of("message", "No autorizado"));
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<List> response = restTemplate.exchange(
                apiUrl + "/api/becados/electrodomesticos/actuales",
                HttpMethod.GET,
                request,
                List.class
            );

            return ResponseEntity.status(response.getStatusCode())
                               .body(response.getBody());
        } catch (Exception e) {
            logger.error("Error al obtener electrodomésticos actuales: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(Map.of("message", "Error al obtener electrodomésticos actuales"));
        }
    }
}
