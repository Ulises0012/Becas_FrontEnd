package sv.org.arrupe.becas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sv.org.arrupe.becas.dto.UsuarioDTO;
import java.util.Optional;

@Service
public class UsuarioService {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    
    @Value("${api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();

    public Optional<UsuarioDTO> autenticar(String carnet, String password) {
        try {
            String url = apiUrl + "/login?carnet=" + carnet + "&password=" + password;
            UsuarioDTO response = restTemplate.postForObject(url, null, UsuarioDTO.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            logger.error("Error al autenticar usuario: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<UsuarioDTO> obtenerUsuario(String carnet) {
        try {
            String url = apiUrl + "/usuario/" + carnet;
            UsuarioDTO response = restTemplate.getForObject(url, UsuarioDTO.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            logger.error("Error al obtener usuario: {}", e.getMessage());
            return Optional.empty();
        }
    }
}