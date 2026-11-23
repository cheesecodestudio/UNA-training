package cr.ac.practica.campusreserva.usuario.dto;

import java.util.List;

public record UsuarioPaginaResponse(
        List<UsuarioResponse> usuarios,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
