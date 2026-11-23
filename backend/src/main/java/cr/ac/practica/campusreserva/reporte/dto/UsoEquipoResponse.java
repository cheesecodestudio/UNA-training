package cr.ac.practica.campusreserva.reporte.dto;

public record UsoEquipoResponse(
        Long idEquipo,
        String nombreEquipo,
        Long totalReservas) {
}
