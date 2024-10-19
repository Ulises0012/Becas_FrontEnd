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
public class FormularioController {
    private static final Logger logger = LoggerFactory.getLogger(FormularioController.class);

    @Value("${api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // Paso 1: Mostrar formulario inicial de solicitud de beca
    @GetMapping("/solicitar_beca_form")
    public String mostrarFormularioBeca(HttpSession session, Model model) {
        String carnet = (String) session.getAttribute("carnet");
        String token = (String) session.getAttribute("token");

        if (carnet == null || token == null) {
            logger.warn("Intento de acceso sin autenticación");
            return "redirect:/login";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            // Obtener datos del usuario
            ResponseEntity<Map> responseUsuario = restTemplate.exchange(
                apiUrl + "/api/becados/usuario-actual",
                HttpMethod.GET,
                request,
                Map.class
            );

            // Obtener tipos de beca
            ResponseEntity<List> responseTiposBeca = restTemplate.exchange(
                apiUrl + "/api/becados/tipos-beca",
                HttpMethod.GET,
                request,
                List.class
            );

            if (responseUsuario.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("usuario", responseUsuario.getBody());
            } else {
                model.addAttribute("error", "No se pudo cargar la información del usuario");
                return "solicitar_beca_form";
            }

            if (responseTiposBeca.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("tiposBeca", responseTiposBeca.getBody());
            } else {
                model.addAttribute("error", "No se pudieron cargar los tipos de beca");
            }

            return "solicitar_beca_form";

        } catch (Exception e) {
            logger.error("Error al obtener los datos: {}", e.getMessage());
            model.addAttribute("error", "Error al obtener los datos: " + e.getMessage());
            return "solicitar_beca_form";
        }
    }

    // Procesar el envío del primer paso
    @PostMapping("/solicitar-beca")
    public String procesarSolicitudBeca(
            @RequestParam String tipoBecaSeleccionada,
            @RequestParam String motivo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            // Crear objeto para la solicitud
            Map<String, Object> solicitud = Map.of(
                "idTipoBeca", Long.parseLong(tipoBecaSeleccionada),
                "motivo", motivo,
                "estado", "PENDIENTE"
            );

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Crear la entidad HTTP
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(solicitud, headers);

            // Enviar solicitud al backend
            ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl + "/api/becados/solicitar",
                HttpMethod.POST,
                requestEntity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                // Guardar datos importantes en la sesión
                session.setAttribute("becaId", response.getBody().get("id"));
                return "redirect:/form-socioeconomico";
            } else {
                redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud");
                return "redirect:/solicitar_beca_form";
            }

        } catch (Exception e) {
            logger.error("Error al procesar la solicitud: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            return "redirect:/solicitar_beca_form";
        }
    }

    // Mostrar formulario socioeconómico
    @GetMapping("/form-socioeconomico")
    public String mostrarFormularioSocioeconomico(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        Object becaId = session.getAttribute("becaId");

        if (token == null || becaId == null) {
            return "redirect:/solicitar_beca_form";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            // Obtener datos necesarios para el formulario socioeconómico
            ResponseEntity<Map> responseUsuario = restTemplate.exchange(
                apiUrl + "/api/becados/usuario-actual",
                HttpMethod.GET,
                request,
                Map.class
            );

            if (responseUsuario.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("usuario", responseUsuario.getBody());
                return "form_socioeconomico";
            } else {
                model.addAttribute("error", "Error al cargar datos del usuario");
                return "redirect:/solicitar_beca_form";
            }

        } catch (Exception e) {
            logger.error("Error al cargar formulario socioeconómico: {}", e.getMessage());
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
            return "redirect:/solicitar_beca_form";
        }
    }

    // Procesar formulario socioeconómico
    @PostMapping("/procesar-socioeconomico")
    public String procesarFormularioSocioeconomico(
            @RequestBody Map<String, Object> datosFormulario,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("token");
        Object becaId = session.getAttribute("becaId");

        if (token == null || becaId == null) {
            return "redirect:/solicitar_beca_form";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Agregar el ID de la beca a los datos del formulario
            datosFormulario.put("becaId", becaId);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(datosFormulario, headers);

            // Enviar datos socioeconómicos al backend
            ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl + "/api/becados/socioeconomico",
                HttpMethod.POST,
                requestEntity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                // Limpiar datos de la sesión
                session.removeAttribute("becaId");
                redirectAttributes.addFlashAttribute("mensaje", "Solicitud completada con éxito");
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Error al procesar los datos socioeconómicos");
                return "redirect:/form-socioeconomico";
            }

        } catch (Exception e) {
            logger.error("Error al procesar datos socioeconómicos: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al procesar los datos: " + e.getMessage());
            return "redirect:/form-socioeconomico";
        }
    }
}