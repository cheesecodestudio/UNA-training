package cr.ac.practica.campusreserva.reserva;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cr.ac.practica.campusreserva.Enums.EstadoReserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByEquipo_IdAndEstadoAndInicioLessThanAndFinGreaterThan(
            Long equipoId,
            EstadoReserva estado,
            LocalDateTime finSolicitado,
            LocalDateTime inicioSolicitado);

    @Query("select r.equipo.id from Reserva r where r.id = :reservaId")
    Optional<Long> buscarEquipoIdPorReservaId(@Param("reservaId") Long reservaId);
}
