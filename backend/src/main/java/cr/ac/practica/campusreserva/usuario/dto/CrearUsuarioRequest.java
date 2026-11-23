package cr.ac.practica.campusreserva.usuario.dto;

import cr.ac.practica.campusreserva.Enums;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearUsuarioRequest(
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Email String correo,
        @Enumerated Enums.TipoUsuario tipo,
        @NotNull boolean activo) {
}
