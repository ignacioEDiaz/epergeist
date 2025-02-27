package ar.edu.unq.epersgeist.exception;

public class CantidadPorPaginaMenorACeroException extends RuntimeException {

    public CantidadPorPaginaMenorACeroException(String message) {
        super(message);
    }
}
