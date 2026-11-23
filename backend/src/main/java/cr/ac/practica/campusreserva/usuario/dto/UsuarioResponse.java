package cr.ac.practica.campusreserva.usuario.dto;

import cr.ac.practica.campusreserva.Enums;

public record UsuarioResponse(
        Long id,
        String nombre,
        String correo,
        Enums.TipoUsuario tipo,
        boolean activo,
        String message) {
}
