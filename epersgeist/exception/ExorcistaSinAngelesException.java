package ar.edu.unq.epersgeist.exception;

import ar.edu.unq.epersgeist.modelo.Medium;


public class ExorcistaSinAngelesException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final Medium medium;

    public ExorcistaSinAngelesException(Medium medium) {
        this.medium = medium;
    }

    @Override
    public String getMessage(){
        return "El Medium [" + medium + "] no contiene Angeles vinculados.";
    }
}
