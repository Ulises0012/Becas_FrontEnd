package sv.org.arrupe.becas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.org.arrupe.becas.model.*;
import sv.org.arrupe.becas.service.*;

import java.util.List;

@RestController
@RequestMapping("/solicitud")
public class EstudioSocioeconomicoController {

    @Autowired
    private EstudianteService estudianteService;
/*
    @Autowired
    private ViviendaService viviendaService;

    @Autowired
    private IngresosFamiliaresService ingresosService;

    @Autowired
    private EgresosFamiliaresService egresosService;

    @Autowired
    private UsuariosService usuariosService;
*/
    // ============================= Estudiantes Endpoints =============================
    @PostMapping("/estudiantes")
    public ResponseEntity<Estudiante> createEstudiante(@RequestBody Estudiante estudiante) {
        return ResponseEntity.ok(estudianteService.save(estudiante));
    }

    @GetMapping("/estudiantes")
    public List<Estudiante> getAllEstudiantes() {
        return estudianteService.findAll();
    }

    @GetMapping("/estudiantes/{id}")
    public ResponseEntity<Estudiante> getEstudianteById(@PathVariable Long id) {
        return estudianteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/estudiantes/{id}")
    public ResponseEntity<Estudiante> updateEstudiante(@PathVariable Long id, @RequestBody Estudiante estudiante) {
        return estudianteService.update(id, estudiante)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/estudiantes/{id}")
    public ResponseEntity<Void> deleteEstudiante(@PathVariable Long id) {
        estudianteService.delete(id);
        return ResponseEntity.noContent().build();
    }}
/*
    // ============================= Vivienda Endpoints =============================
    @PostMapping("/viviendas")
    public ResponseEntity<Vivienda> createVivienda(@RequestBody Vivienda vivienda) {
        return ResponseEntity.ok(viviendaService.save(vivienda));
    }

    @GetMapping("/viviendas")
    public List<Vivienda> getAllViviendas() {
        return viviendaService.findAll();
    }

    @GetMapping("/viviendas/{id}")
    public ResponseEntity<Vivienda> getViviendaById(@PathVariable Long id) {
        return viviendaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/viviendas/{id}")
    public ResponseEntity<Vivienda> updateVivienda(@PathVariable Long id, @RequestBody Vivienda vivienda) {
        return viviendaService.update(id, vivienda)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/viviendas/{id}")
    public ResponseEntity<Void> deleteVivienda(@PathVariable Long id) {
        viviendaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ============================= Ingresos Familiares Endpoints =============================
    @PostMapping("/ingresos")
    public ResponseEntity<IngresosFamiliares> createIngresos(@RequestBody IngresosFamiliares ingresos) {
        return ResponseEntity.ok(ingresosService.save(ingresos));
    }

    @GetMapping("/ingresos")
    public List<IngresosFamiliares> getAllIngresos() {
        return ingresosService.findAll();
    }

    @GetMapping("/ingresos/{id}")
    public ResponseEntity<IngresosFamiliares> getIngresosById(@PathVariable Long id) {
        return ingresosService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/ingresos/{id}")
    public ResponseEntity<IngresosFamiliares> updateIngresos(@PathVariable Long id, @RequestBody IngresosFamiliares ingresos) {
        return ingresosService.update(id, ingresos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/ingresos/{id}")
    public ResponseEntity<Void> deleteIngresos(@PathVariable Long id) {
        ingresosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ============================= Egresos Familiares Endpoints =============================
    @PostMapping("/egresos")
    public ResponseEntity<EgresosFamiliares> createEgresos(@RequestBody EgresosFamiliares egresos) {
        return ResponseEntity.ok(egresosService.save(egresos));
    }

    @GetMapping("/egresos")
    public List<EgresosFamiliares> getAllEgresos() {
        return egresosService.findAll();
    }

    @GetMapping("/egresos/{id}")
    public ResponseEntity<EgresosFamiliares> getEgresosById(@PathVariable Long id) {
        return egresosService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/egresos/{id}")
    public ResponseEntity<EgresosFamiliares> updateEgresos(@PathVariable Long id, @RequestBody EgresosFamiliares egresos) {
        return egresosService.update(id, egresos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/egresos/{id}")
    public ResponseEntity<Void> deleteEgresos(@PathVariable Long id) {
        egresosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ============================= Usuarios Endpoints =============================
    @PostMapping("/usuarios")
    public ResponseEntity<Usuarios> createUsuario(@RequestBody Usuarios usuario) {
        return ResponseEntity.ok(usuariosService.save(usuario));
    }

    @GetMapping("/usuarios")
    public List<Usuarios> getAllUsuarios() {
        return usuariosService.findAll();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuarios> getUsuarioById(@PathVariable Long id) {
        return usuariosService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuarios> updateUsuario(@PathVariable Long id, @RequestBody Usuarios usuario) {
        return usuariosService.update(id, usuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuariosService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
*/