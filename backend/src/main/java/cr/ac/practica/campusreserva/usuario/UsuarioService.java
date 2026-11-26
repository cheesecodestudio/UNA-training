package cr.ac.practica.campusreserva.usuario;

import java.util.List;

import org.springframework.stereotype.Service;

import cr.ac.practica.campusreserva.usuario.dto.CrearUsuarioRequest;
import cr.ac.practica.campusreserva.usuario.dto.UsuarioPaginaResponse;
import cr.ac.practica.campusreserva.usuario.dto.UsuarioResponse;
import cr.ac.practica.campusreserva.usuario.dto.UsuarioSelectResponse;
import cr.ac.practica.campusreserva.exception.OwnException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getTipo(),
                        usuario.isActivo(),
                        ""))
                .toList();
    }

    public List<UsuarioSelectResponse> selectUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioSelectResponse(
                        usuario.getId(),
                        usuario.getNombre()))
                .toList();
    }

    public UsuarioResponse crear(CrearUsuarioRequest request) {
        Usuario usuario = new Usuario(
                request.nombre(),
                request.correo(),
                request.tipo(),
                request.activo() ? 1 : 0);
        usuarioRepository.save(usuario);
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getTipo(),
                usuario.isActivo(),
                "");
    }

    public UsuarioResponse obtener(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new OwnException.EntityNotFoundException("usuario", id));

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getTipo(),
                usuario.isActivo(),
                "");
    }

    public UsuarioResponse actualizar(Long id, CrearUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new OwnException.EntityNotFoundException("usuario", id));

        if (request.nombre() != null) {
            usuario.setNombre(request.nombre());
        }
        if (request.correo() != null) {
            usuario.setCorreo(request.correo());
        }
        if (request.tipo() != null) {
            usuario.setTipo(request.tipo());
        }
        usuario.setActivo(request.activo() ? 1 : 0);

        usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getTipo(),
                usuario.isActivo(),
                "");
    }

    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new OwnException.EntityNotFoundException("usuario", id));
        usuarioRepository.delete(usuario);
    }

    public UsuarioPaginaResponse listarPaginado(int page, int size, String sort) {
        var request = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Usuario> result = usuarioRepository.findAll(request);
        List<UsuarioResponse> usuarios = result.getContent().stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getTipo(),
                        usuario.isActivo(),
                        ""))
                .toList();
        return new UsuarioPaginaResponse(
                usuarios,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }
}
