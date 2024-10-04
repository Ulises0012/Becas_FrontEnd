package sv.org.arrupe.becas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sv.org.arrupe.becas.model.Usuario;
import sv.org.arrupe.becas.repository.UsuarioRepository;
import sv.org.arrupe.becas.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")  // Prefijo para las rutas del controlador
public class UnifiedController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ---------- ENDPOINTS PARA GESTIONAR VISTAS (Dashboard y Login) ----------

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        // Aquí puedes agregar datos al modelo si es necesario
        return "dashboard";  // Nombre de la vista que se renderizará (dashboard.html)
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";  // Nombre de la vista que se renderizará (login.html)
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String carnet,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        Optional<Usuario> usuario = usuarioService.autenticar(carnet, password);
        if (usuario.isPresent()) {
            session.setAttribute("usuario", usuario.get());
            return "redirect:/dashboard";  // Redirige al dashboard después de iniciar sesión
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";  // Si el login falla, vuelve a la vista de login
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Invalida la sesión del usuario
        return "redirect:/login";  // Redirige al login después de cerrar sesión
    }

    // ---------- ENDPOINTS API PARA CRUD DE USUARIOS ----------
/*
    @GetMapping("/usuarios")
    @ResponseBody
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PutMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario actualizacion = usuarioExistente.get();
            actualizacion.setNombre(usuario.getNombre());
            actualizacion.setCorreo(usuario.getCorreo());
            actualizacion.setPassword(usuario.getPassword());
            return ResponseEntity.ok(usuarioRepository.save(actualizacion));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }*/

    // ---------- API de Login ----------
    
}
