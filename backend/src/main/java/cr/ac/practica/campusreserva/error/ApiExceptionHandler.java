package cr.ac.practica.campusreserva.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cr.ac.practica.campusreserva.equipo.dto.EquipoErrorResponse;
import cr.ac.practica.campusreserva.exception.OwnException.*;
import cr.ac.practica.campusreserva.reserva.dto.ReservaErrorResponse;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EquipoErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        EquipoErrorResponse errorResponse = new EquipoErrorResponse("EQUIPO_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> manejarIntegridad(
            DataIntegrityViolationException ex) {

        var error = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "La operación entra en conflicto con las restricciones de los datos.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ReservaInvalidaException.class)
    public ResponseEntity<ReservaErrorResponse> manejarReservaInvalida(
            ReservaInvalidaException ex) {
        return ResponseEntity.status(422)
                .body(new ReservaErrorResponse("RESERVA_INVALIDA", ex.getMessage()));
    }

    @ExceptionHandler(ReservaEnConflictoException.class)
    public ResponseEntity<ReservaErrorResponse> manejarConflictoReserva(
            ReservaEnConflictoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ReservaErrorResponse("CONFLICTO_RESERVA", ex.getMessage()));
    }
}
