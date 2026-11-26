package cr.ac.practica.campusreserva.usuario;

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

import cr.ac.practica.campusreserva.usuario.dto.CrearUsuarioRequest;
import cr.ac.practica.campusreserva.usuario.dto.UsuarioPaginaResponse;
import cr.ac.practica.campusreserva.usuario.dto.UsuarioResponse;
import cr.ac.practica.campusreserva.usuario.dto.UsuarioSelectResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public UsuarioResponse crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        return usuarioService.crear(request);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public UsuarioResponse usuario(@PathVariable Long id) {
        return usuarioService.obtener(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizarUsuario(@PathVariable Long id, @RequestBody CrearUsuarioRequest request) {
        return usuarioService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }

    @GetMapping
    public UsuarioPaginaResponse usuariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort) {
        return usuarioService.listarPaginado(page, size, sort);
    }

    @GetMapping("/select")
    public List<UsuarioSelectResponse> usuariosSelect() {
        return usuarioService.selectUsuarios();
    }
}
