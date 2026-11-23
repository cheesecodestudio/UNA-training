package cr.ac.practica.campusreserva.equipo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearEquipoRequest(
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Size(max = 30) String codigo,
        @NotNull boolean activo) {
}
