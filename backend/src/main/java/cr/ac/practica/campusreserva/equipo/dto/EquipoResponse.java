package cr.ac.practica.campusreserva.equipo.dto;

public record EquipoResponse(
        Long id,
        String nombre,
        String codigo,
        boolean activo,
        String message) {
}
