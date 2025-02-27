package ar.edu.unq.epersgeist.exception;


final public class EntidadNoExisteEnBDException extends RuntimeException {
    public EntidadNoExisteEnBDException(String message) {
        super(message);
    }
}
