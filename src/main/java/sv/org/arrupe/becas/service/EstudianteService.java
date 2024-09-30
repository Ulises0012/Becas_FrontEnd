package sv.org.arrupe.becas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.org.arrupe.becas.model.Estudiante;
import sv.org.arrupe.becas.repository.EstudianteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public Estudiante save(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public List<Estudiante> findAll() {
        return estudianteRepository.findAll();
    }

    public Optional<Estudiante> findById(Long id) {
        return estudianteRepository.findById(id);
    }

    public Optional<Estudiante> update(Long id, Estudiante estudiante) {
        return estudianteRepository.findById(id)
                .map(existingEstudiante -> {
                    existingEstudiante.setNombres(estudiante.getNombres());
                    existingEstudiante.setApellidos(estudiante.getApellidos());
                    // Otros campos que quieras actualizar
                    return estudianteRepository.save(existingEstudiante);
                });
    }

    public void delete(Long id) {
        estudianteRepository.deleteById(id);
    }
}
