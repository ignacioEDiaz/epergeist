package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record UbicacionDTO(Long id,
                           String nombre,
                           TipoUbicacion tipoUbicacion,
                           int energia
                           ) {

    public static UbicacionDTO desdeModelo(Ubicacion ubicacion){
        return  new UbicacionDTO(
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getTipo(),
                ubicacion.getEnergia()
        );
    }

    public Ubicacion aModelo(){
       return new Ubicacion(this.nombre, this.tipoUbicacion, this.energia);
    }

    public Ubicacion aModeloUpdate(){
        return new Ubicacion(this.nombre,this.energia);
    }
}
