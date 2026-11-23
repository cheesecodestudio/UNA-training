package cr.ac.practica.campusreserva.reporte;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cr.ac.practica.campusreserva.reporte.dto.UsoEquipoResponse;

@RestController
@RequestMapping("api/reportes")
public class ReporteController {
    private final ReporteJdbcDao reporteJdbcDao;

    public ReporteController(ReporteJdbcDao reporteJdbcDao) {
        this.reporteJdbcDao = reporteJdbcDao;
    }

    @GetMapping("uso-equipos")
    public List<UsoEquipoResponse> equiposActivos(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        if (!hasta.isAfter(desde)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Hasta deber ser posterior a desde.");
        }
        return reporteJdbcDao.equiposUso(desde, hasta);
    }
}
