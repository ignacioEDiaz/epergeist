package ar.edu.unq.epersgeist.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public class ExcesoDeEnergiaPermitidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final int energia;

    public ExcesoDeEnergiaPermitidaException(int energia) {
        this.energia = energia;
    }


    public String getMessage(){
    return "La Energia de la Ubicacion es [" + energia + "], y debe ser entre 0 y 100";
  }
}
