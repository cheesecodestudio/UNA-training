package cr.ac.practica.campusreserva.reserva.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record CrearReservaRequest(
        @NotNull Long usuarioId,
        @NotNull Long equipoId,
        @NotNull LocalDateTime inicio,
        @NotNull LocalDateTime fin) {
}
