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
            return "redirect:/dashboard"; // Redirige al dashboard después de iniciar sesión
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
