package ar.edu.unq.epersgeist.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public class InvalidacionDeTipoEnUbicacion extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final Ubicacion ubicacion;
  private final Espiritu espiritu;

  public InvalidacionDeTipoEnUbicacion(Ubicacion ubicacion, Espiritu espiritu) {
    this.ubicacion = ubicacion;
    this.espiritu = espiritu;
  }
  @Override
  public String getMessage(){
    return "El espiritu con tipo : [" + espiritu.getTipo() + "] no esta permitido en el [" + ubicacion.getTipo() + "]";
  }
}
