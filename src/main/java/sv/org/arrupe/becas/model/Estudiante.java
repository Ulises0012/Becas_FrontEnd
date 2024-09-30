package sv.org.arrupe.becas.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Estudiante")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_estudiante;

    private String carnet_estudiante;
    private String nombres;
    private String apellidos;

    @Temporal(TemporalType.DATE)
    private Date fecha_nacimiento;
    private Integer edad;
    private String sexo;
    private String grupo_sanguineo;
    private String direccion;
    private String zona_residencia;
    private String telefono;
    private String tipo_transporte;
    private String grado_aspirado;
    private Boolean solicita_estudio_socioeconomico;
    private String discapacidad;
    private String necesidad_educativa_especial;

    // Getters y Setters
    public Long getId_estudiante() {
        return id_estudiante;
    }

    public void setId_estudiante(Long id_estudiante) {
        this.id_estudiante = id_estudiante;
    }

    public String getCarnet_estudiante() {
        return carnet_estudiante;
    }

    public void setCarnet_estudiante(String carnet_estudiante) {
        this.carnet_estudiante = carnet_estudiante;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getGrupo_sanguineo() {
        return grupo_sanguineo;
    }

    public void setGrupo_sanguineo(String grupo_sanguineo) {
        this.grupo_sanguineo = grupo_sanguineo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getZona_residencia() {
        return zona_residencia;
    }

    public void setZona_residencia(String zona_residencia) {
        this.zona_residencia = zona_residencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo_transporte() {
        return tipo_transporte;
    }

    public void setTipo_transporte(String tipo_transporte) {
        this.tipo_transporte = tipo_transporte;
    }

    public String getGrado_aspirado() {
        return grado_aspirado;
    }

    public void setGrado_aspirado(String grado_aspirado) {
        this.grado_aspirado = grado_aspirado;
    }

    public Boolean getSolicita_estudio_socioeconomico() {
        return solicita_estudio_socioeconomico;
    }

    public void setSolicita_estudio_socioeconomico(Boolean solicita_estudio_socioeconomico) {
        this.solicita_estudio_socioeconomico = solicita_estudio_socioeconomico;
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getNecesidad_educativa_especial() {
        return necesidad_educativa_especial;
    }

    public void setNecesidad_educativa_especial(String necesidad_educativa_especial) {
        this.necesidad_educativa_especial = necesidad_educativa_especial;
    }
}
