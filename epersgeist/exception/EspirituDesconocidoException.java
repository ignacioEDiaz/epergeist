package ar.edu.unq.epersgeist.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;


public class EspirituDesconocidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final Espiritu espiritu;

    public EspirituDesconocidoException(Espiritu espiritu) {
        this.espiritu = espiritu;
    }

    @Override
    public String getMessage(){
        return "El Espiritu [" + espiritu + "] no se esta persistiendo.";
    }
}
