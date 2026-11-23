package cr.ac.practica.campusreserva.reserva;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cr.ac.practica.campusreserva.reserva.dto.CrearReservaRequest;
import cr.ac.practica.campusreserva.reserva.dto.ReservaPaginaResponse;
import cr.ac.practica.campusreserva.reserva.dto.ReservaResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ReservaPaginaResponse reservasPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort) {
        return reservaService.listarPaginado(page, size, sort);
    }

    @PostMapping
    public ReservaResponse crearReserva(@Valid @RequestBody CrearReservaRequest request) {
        return reservaService.crear(request);
    }

    @PatchMapping("/{id}/cancelacion")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarReserva(@PathVariable Long id) {
        reservaService.cancelar(id);
    }
}
