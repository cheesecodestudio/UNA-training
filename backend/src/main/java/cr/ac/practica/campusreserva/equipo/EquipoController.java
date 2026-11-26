package cr.ac.practica.campusreserva.equipo;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cr.ac.practica.campusreserva.equipo.dto.CrearEquipoRequest;
import cr.ac.practica.campusreserva.equipo.dto.EquipoPaginaResponse;
import cr.ac.practica.campusreserva.equipo.dto.EquipoResponse;
import cr.ac.practica.campusreserva.equipo.dto.EquipoSelectResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/equipos")
public class EquipoController {
    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @PostMapping
    public EquipoResponse crearEquipo(@Valid @RequestBody CrearEquipoRequest request) {
        return equipoService.crear(request);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public EquipoResponse equipo(@PathVariable Long id) {
        return equipoService.obtener(id);
    }

    @PutMapping("/{id}")
    public EquipoResponse actualizarEquipo(@PathVariable Long id, @RequestBody CrearEquipoRequest request) {
        return equipoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminarEquipo(@PathVariable Long id) {
        equipoService.eliminar(id);
    }

    @GetMapping
    public EquipoPaginaResponse equiposPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort) {
        return equipoService.listarPaginado(page, size, sort);
    }

    @GetMapping("/select")
    public List<EquipoSelectResponse> equipoSelect() {
        return equipoService.selectEquipos();
    }
}
