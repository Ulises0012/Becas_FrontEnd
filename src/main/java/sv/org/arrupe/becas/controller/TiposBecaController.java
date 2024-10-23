/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.org.arrupe.becas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TiposBecaController {

    @GetMapping("/tipo_de_beca")
    public String mostrarTiposBecas(Model model) {
        // Puedes agregar atributos al modelo aquí si es necesario
        return "tipo_de_beca"; // Esto debe coincidir con el nombre de tu archivo HTML sin la extensión
    }
}
