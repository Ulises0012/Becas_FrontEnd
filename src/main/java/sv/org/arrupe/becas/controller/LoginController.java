package sv.org.arrupe.becas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.client.ResourceAccessException;

@Controller
public class LoginController {
    
    private final String API_URL = "http://192.242.6.129:8081";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @GetMapping("/")
    public String root() {
        // Mostrar directamente la página index/welcome
        return "index";
    }
    
    @GetMapping("/login")
    public String mostrarFormularioLogin(HttpSession session) {
        return session.getAttribute("usuarioAutenticado") != null ? 
               "redirect:/dashboard" : "login";
    }
    
    @PostMapping("/procesar-login")
    public String procesarLogin(@RequestParam String carnet,
                              @RequestParam String password,
                              HttpSession session,
                              HttpServletRequest request,
                              Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("carnet", carnet);
            requestBody.add("password", password);
            
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            System.out.println("Enviando solicitud de login a la API para carnet: " + carnet);
            
            ResponseEntity<String> response = restTemplate.exchange(
                API_URL + "/login",
                HttpMethod.POST,
                requestEntity,
                String.class
            );
            
            System.out.println("Respuesta recibida de la API: " + response.getBody());
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, String> responseMap = objectMapper.readValue(
                    response.getBody(), 
                    objectMapper.getTypeFactory().constructMapType(
                        HashMap.class, 
                        String.class, 
                        String.class
                    )
                );
                
                if ("success".equals(responseMap.get("status"))) {
                    System.out.println("Login exitoso, creando sesión para carnet: " + carnet);
                    session.invalidate();
                    session = request.getSession(true);  // Crear nueva sesión
                    session.setAttribute("usuarioAutenticado", true);
                    session.setAttribute("carnet", responseMap.get("carnet"));
                    return "redirect:/dashboard";
                }
            }
            
            System.out.println("Login fallido para carnet: " + carnet);
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
            
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión con el servidor: " + e.getMessage());
            model.addAttribute("error", "Error de conexión con el servidor. Por favor, intente más tarde.");
            return "login";
        } catch (Exception e) {
            System.err.println("Error en el login: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al procesar la solicitud");
            return "login";
        }
    }
    
    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        if (session.getAttribute("usuarioAutenticado") == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("carnet", session.getAttribute("carnet"));
        return "dashboard";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}