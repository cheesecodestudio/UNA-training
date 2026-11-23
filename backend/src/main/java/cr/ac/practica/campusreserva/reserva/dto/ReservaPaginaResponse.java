package cr.ac.practica.campusreserva.reserva.dto;

import java.util.List;

public record ReservaPaginaResponse(
        List<ReservaResponse> reservas,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
