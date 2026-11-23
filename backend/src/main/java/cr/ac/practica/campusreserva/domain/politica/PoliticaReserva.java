package cr.ac.practica.campusreserva.domain.politica;

import java.time.Duration;
import java.time.LocalDateTime;

import cr.ac.practica.campusreserva.exception.OwnException.ReservaInvalidaException;

public abstract class PoliticaReserva {
    public abstract Duration maxDuracionReserva();

    public void validarReserva(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null || !fin.isAfter(inicio)) {
            throw new IllegalArgumentException("Intervalo inválido");
        }
        if (Duration.between(inicio, fin).compareTo(maxDuracionReserva()) > 0) {
            throw new ReservaInvalidaException("La duración excede el máximo permitido.");
        }
    }
}
