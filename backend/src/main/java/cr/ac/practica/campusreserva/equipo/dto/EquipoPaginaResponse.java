package cr.ac.practica.campusreserva.equipo.dto;

import java.util.List;

public record EquipoPaginaResponse(
        List<EquipoResponse> equipos,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
