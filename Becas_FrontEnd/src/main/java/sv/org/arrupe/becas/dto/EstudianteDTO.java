package sv.org.arrupe.becas.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EstudianteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
}