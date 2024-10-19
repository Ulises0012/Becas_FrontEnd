package sv.org.arrupe.becas.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class DispositivosController {

    private static final Logger logger = LoggerFactory.getLogger(DispositivosController.class);

    @Value("${api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/dispositivos_form")
    public String mostrarGestionDispositivos(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
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

            // Realizar solicitud para obtener tipos de dispositivos
            ResponseEntity<List> responseTiposDispositivos = restTemplate.exchange(
                apiUrl + "/api/becados/dispositivos/tipos",
                HttpMethod.GET,
                request,
                List.class
            );

            // Realizar solicitud para obtener dispositivos actuales
            ResponseEntity<List> responseDispositivos = restTemplate.exchange(
                apiUrl + "/api/becados/dispositivos/actuales",
                HttpMethod.GET,
                request,
                List.class
            );

            // Verificar si la respuesta fue exitosa para los tipos de dispositivos
            if (responseTiposDispositivos.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("tiposDispositivos", responseTiposDispositivos.getBody());
            } else {
                model.addAttribute("error", "Error al obtener tipos de dispositivos");
            }

            // Verificar si la respuesta fue exitosa para los dispositivos actuales
            if (responseDispositivos.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("dispositivos", responseDispositivos.getBody());
            } else {
                model.addAttribute("error", "Error al obtener dispositivos actuales");
            }

            return "dispositivos_form";

        } catch (Exception e) {
            logger.error("Error al obtener los datos de dispositivos: {}", e.getMessage());
            model.addAttribute("error", "Error al obtener los datos: " + e.getMessage());
            return "dispositivos_form";
        }
    }
}
