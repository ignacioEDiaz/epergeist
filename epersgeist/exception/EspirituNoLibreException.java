package ar.edu.unq.epersgeist.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;

public class EspirituNoLibreException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final Espiritu espiritu;

    public EspirituNoLibreException(Espiritu espiritu) {
        this.espiritu = espiritu;
    }

    public String getMessage(){
    return "El Espiritu [" + espiritu + "] no esta libre.";
  }
}
