package cr.ac.practica.campusreserva.domain.politica;

import java.time.Duration;

public final class PoliticaDocente extends PoliticaReserva {
    @Override
    public Duration maxDuracionReserva() {
        return Duration.ofHours(4);
    }
}
