package sv.org.arrupe.becas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.org.arrupe.becas.model.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    // Puedes agregar métodos personalizados aquí si es necesario
}
