package cr.ac.practica.campusreserva.reserva.dto;

import java.time.LocalDateTime;

import cr.ac.practica.campusreserva.Enums;

public record ReservaResponse(
        Long id,
        Long usuarioId,
        Long equipoId,
        LocalDateTime inicio,
        LocalDateTime fin,
        Enums.EstadoReserva estado, 
        String message) {
}
