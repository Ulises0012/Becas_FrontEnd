package sv.org.arrupe.becas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.org.arrupe.becas.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCarnet(String carnet);
}