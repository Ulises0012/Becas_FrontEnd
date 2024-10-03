package sv.org.arrupe.becas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.org.arrupe.becas.model.Usuario;
import sv.org.arrupe.becas.repository.UsuarioRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> autenticar(String carnet, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByCarnet(carnet);
        if (usuario.isPresent() && verificarPassword(password, usuario.get().getPassword())) {
            return usuario;
        }
        return Optional.empty();
    }

    private boolean verificarPassword(String passwordIngresada, String passwordAlmacenada) {
        String hashedPassword = hashPassword(passwordIngresada);
        return hashedPassword.equals(passwordAlmacenada);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}