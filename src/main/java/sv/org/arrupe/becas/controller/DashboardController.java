package sv.org.arrupe.becas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        // Aquí puedes agregar datos al modelo si es necesario
        return "dashboard"; // Este es el nombre de la vista que se renderizará
    }
}
