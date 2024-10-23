package sv.org.arrupe.becas.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SolicitudBecaView {

    private static final Logger logger = LoggerFactory.getLogger(SolicitudBecaView.class);

    @Value("${api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/solicitudes_vista")
    public String obtenerSolicitudesVista(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            logger.warn("Intento de acceso sin autenticación");
            return "redirect:/login";
        }

        try {
            // Configurar encabezados con el token
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            // Solicitud para obtener las becas
            ResponseEntity<List> response = restTemplate.exchange(
                apiUrl + "/api/becados/mis-solicitudes",
                HttpMethod.GET,
                request,
                List.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> solicitudes = response.getBody();
                model.addAttribute("solicitudes", formatearSolicitudes(solicitudes));
            } else {
                model.addAttribute("error", "Error al obtener las solicitudes de beca");
            }

            return "solicitudes_vista";
        } catch (Exception e) {
            logger.error("Error al obtener las solicitudes: {}", e.getMessage());
            model.addAttribute("error", "Error al obtener los datos: " + e.getMessage());
            return "solicitudes_vista";
        }
    }

    // Método para formatear solicitudes y manejar posibles errores de fecha
    private List<Map<String, String>> formatearSolicitudes(List<Map<String, Object>> solicitudes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Map<String, String>> solicitudesFormateadas = new ArrayList<>();

        for (Map<String, Object> solicitud : solicitudes) {
            String fechaISO = (String) solicitud.get("fechaCreacion");
            String fechaFormateada;

            try {
                if (fechaISO != null) {
                    LocalDateTime fecha = LocalDateTime.parse(fechaISO);
                    fechaFormateada = fecha.format(formatter);
                } else {
                    fechaFormateada = "N/A";
                }
            } catch (DateTimeParseException e) {
                logger.warn("Fecha inválida para solicitud: {}", fechaISO);
                fechaFormateada = "Fecha inválida";
            }

            // Obtener el estado y transformarlo si es necesario
            String estado = solicitud.getOrDefault("estadoSolicitud", "Desconocido").toString();
            if ("Activo".equalsIgnoreCase(estado)) {
                estado = "Aprobado";
            }

            Map<String, String> solicitudFormateada = Map.of(
                "nombreTipoBeca", solicitud.getOrDefault("nombreTipoBeca", "No disponible").toString(),
                "fechaFormateada", fechaFormateada,
                "motivo", solicitud.getOrDefault("motivo", "Sin motivo").toString(),
                "estadoSolicitud", estado  // Usamos el estado transformado
            );

            solicitudesFormateadas.add(solicitudFormateada);
        }

        return solicitudesFormateadas;
    }
}