package cr.ac.practica.campusreserva.equipo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Equipo> findParaReservaById(Long id);
}
