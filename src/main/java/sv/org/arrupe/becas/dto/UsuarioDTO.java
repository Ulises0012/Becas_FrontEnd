package sv.org.arrupe.becas.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String carnet;
    private String password;
    private String rol;
    private Long idEstudiante;
}