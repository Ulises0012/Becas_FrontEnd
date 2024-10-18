package sv.org.arrupe.becas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/solicitudes")
public class Solicitudes {

    @GetMapping
    public String mostrarSolicitudes() {
        return "solicitudes"; // Nombre del archivo sin la extensión .html
    }
    
    @GetMapping("/dashboard_admin")
    public String dashboard() {
        return "dashboard_admin";
    }
}
