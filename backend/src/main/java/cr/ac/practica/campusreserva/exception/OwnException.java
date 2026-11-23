package cr.ac.practica.campusreserva.exception;

public class OwnException {
    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String entityName, Long id) {
            super("No existe un " + entityName + " con ID " + id);
        }
    }

    public static class ReservaInvalidaException extends IllegalArgumentException {
        public ReservaInvalidaException(String mensaje) {
            super(mensaje);
        }
    }

    public static class ReservaEnConflictoException extends RuntimeException {
        public ReservaEnConflictoException(String mensaje) {
            super(mensaje);
        }
    }
}
