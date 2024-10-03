package sv.org.arrupe.becas.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;
    
    @Column(unique = true, nullable = false)
    private String carnet;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Rol rol;
    
    @OneToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;
    
    public enum Rol {
        Estudiante, Admin
    }
}