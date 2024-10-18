package sv.org.arrupe.becas.dto;

import lombok.Data;

@Data
public class IngresosFamiliaresDTO {
    private Long id;
    private Double cantidad;
    private String fuente;
    private Long idEstudiante;
}