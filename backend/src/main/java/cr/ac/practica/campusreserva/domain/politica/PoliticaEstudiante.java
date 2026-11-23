package cr.ac.practica.campusreserva.domain.politica;

import java.time.Duration;

public final class PoliticaEstudiante extends PoliticaReserva {
    @Override
    public Duration maxDuracionReserva() {
        return Duration.ofHours(2);
    }
}
