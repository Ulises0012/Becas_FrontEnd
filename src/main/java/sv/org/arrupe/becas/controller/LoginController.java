package sv.org.arrupe.becas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sv.org.arrupe.becas.model.Usuario;
import sv.org.arrupe.becas.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    // Ruta raíz
    @GetMapping("/")
    public String rootRedirect(HttpSession session) {
        // Redirigir al dashboard si el usuario está autenticado, de lo contrario al login
        if (session.getAttribute("usuario") != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String carnet, 
                                @RequestParam String password, 
                                HttpSession session,
                                Model model) {
        Optional<Usuario> usuario = usuarioService.autenticar(carnet, password);
        if (usuario.isPresent()) {
            session.setAttribute("usuario", usuario.get());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
