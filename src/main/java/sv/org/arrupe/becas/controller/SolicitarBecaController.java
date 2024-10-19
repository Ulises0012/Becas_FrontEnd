package sv.org.arrupe.becas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SolicitarBecaController {
    
    @GetMapping("/solicitar-beca")
    public String mostrarFormularioSolicitud() {
        return "solicitar_beca";
    }
}