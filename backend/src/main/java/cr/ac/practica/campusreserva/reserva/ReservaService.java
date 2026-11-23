package cr.ac.practica.campusreserva.reserva;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cr.ac.practica.campusreserva.Enums;
import cr.ac.practica.campusreserva.domain.politica.PoliticaDocente;
import cr.ac.practica.campusreserva.domain.politica.PoliticaEstudiante;
import cr.ac.practica.campusreserva.domain.politica.PoliticaReserva;
import cr.ac.practica.campusreserva.equipo.Equipo;
import cr.ac.practica.campusreserva.equipo.EquipoRepository;
import cr.ac.practica.campusreserva.exception.OwnException.EntityNotFoundException;
import cr.ac.practica.campusreserva.exception.OwnException.ReservaEnConflictoException;
import cr.ac.practica.campusreserva.exception.OwnException.ReservaInvalidaException;
import cr.ac.practica.campusreserva.reserva.dto.CrearReservaRequest;
import cr.ac.practica.campusreserva.reserva.dto.ReservaPaginaResponse;
import cr.ac.practica.campusreserva.reserva.dto.ReservaResponse;
import cr.ac.practica.campusreserva.usuario.Usuario;
import cr.ac.practica.campusreserva.usuario.UsuarioRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipoRepository equipoRepository;
    private final Clock reloj;

    public ReservaService(
            ReservaRepository reservaRepository,
            UsuarioRepository usuarioRepository,
            EquipoRepository equipoRepository,
            Clock reloj) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.equipoRepository = equipoRepository;
        this.reloj = reloj;
    }

    @Transactional(readOnly = true)
    public ReservaPaginaResponse listarPaginado(int page, int size, String sort) {
        var request = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Reserva> result = reservaRepository.findAll(request);
        List<ReservaResponse> reservas = result.getContent().stream()
                .map(reserva -> new ReservaResponse(
                        reserva.getId(),
                        reserva.getUsuario().getId(),
                        reserva.getEquipo().getId(),
                        reserva.getInicio(),
                        reserva.getFin(),
                        reserva.getEstado(),
                        ""))
                .toList();
        return new ReservaPaginaResponse(
                reservas,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    @Transactional
    public ReservaResponse crear(CrearReservaRequest request) {
        LocalDateTime inicio = request.inicio();
        LocalDateTime fin = request.fin();

        if (!fin.isAfter(inicio)) {
            throw new ReservaInvalidaException("El fin debe ser posterior al inicio.");
        }

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("usuario", request.usuarioId()));

        if (!usuario.isActivo()) {
            throw new ReservaEnConflictoException("El usuario está inactivo.");
        }

        PoliticaReserva politica = switch (usuario.getTipo()) {
            case ESTUDIANTE -> new PoliticaEstudiante();
            case DOCENTE -> new PoliticaDocente();
        };
        politica.validarReserva(inicio, fin);

        Equipo equipo = equipoRepository.findParaReservaById(request.equipoId())
                .orElseThrow(() -> new EntityNotFoundException("equipo", request.equipoId()));

        if (!Integer.valueOf(1).equals(equipo.getActivo())) {
            throw new ReservaEnConflictoException("El equipo está inactivo.");
        }

        if (!inicio.isAfter(LocalDateTime.now(reloj))) {
            throw new ReservaInvalidaException("El inicio debe estar en el futuro.");
        }

        boolean ocupado = reservaRepository
                .existsByEquipo_IdAndEstadoAndInicioLessThanAndFinGreaterThan(
                        equipo.getId(), Enums.EstadoReserva.ACTIVA, fin, inicio);

        if (ocupado) {
            throw new ReservaEnConflictoException("El equipo ya está reservado para ese horario.");
        }

        Reserva guardada = reservaRepository.save(
                new Reserva(usuario, equipo, inicio, fin, Enums.EstadoReserva.ACTIVA));

        return new ReservaResponse(
                guardada.getId(), usuario.getId(), equipo.getId(),
                guardada.getInicio(), guardada.getFin(), guardada.getEstado(), "");
    }

    @Transactional
    public void cancelar(Long id) {
        Long equipoId = reservaRepository.buscarEquipoIdPorReservaId(id)
                .orElseThrow(() -> new EntityNotFoundException("equipo en reserva", id));

        Equipo equipo = equipoRepository.findParaReservaById(equipoId)
                .orElseThrow(() -> new EntityNotFoundException("equipo", equipoId));

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("reserva", id));

        if (!reserva.IsCancelada()) {
            reserva.setEstado(Enums.EstadoReserva.CANCELADA);
            reservaRepository.save(reserva);
        }
    }
}
