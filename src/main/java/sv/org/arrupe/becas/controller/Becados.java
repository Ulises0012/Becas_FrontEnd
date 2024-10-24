package sv.org.arrupe.becas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/becados")
public class Becados {

    @GetMapping
    public String mostrarSolicitudes() {
        return "becados"; // Nombre del archivo sin la extensión .html
    }
    
    @GetMapping("/dashboard_admin")
    public String dashboard() {
        return "dashboard_admin";
    }
}
