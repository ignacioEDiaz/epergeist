package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.Set;

public record EspirituDTO(Long id,
                          String nombre,
                          TipoEspiritu tipo,
                          int energia,
                          int nivel,
                          Long mediumId,
                          Long ubicacionId,
                          Set<String> habilidades) {

    public static EspirituDTO desdeModelo(Espiritu espiritu) {
        return new EspirituDTO(
                espiritu.getId(),
                espiritu.getNombre(),
                espiritu.getTipo(),
                espiritu.getEnergia(),
                espiritu.getNivelDeConexion(),
                espiritu.getMedium() != null ?espiritu.getMedium().getId():null,
                espiritu.getUbicacion().getId(),
                espiritu.getHabilidades()
        );
    }

    public Espiritu aModelo(Ubicacion ubicacion) {
        Espiritu espiritu = new Espiritu(this.tipo(),this.nivel(),this.nombre(),ubicacion,this.energia());
        return espiritu;
    }

    public Espiritu aModeloCreate() {
        Espiritu espiritu = new Espiritu.Builder()
                                .nombre(this.nombre)
                                .nivelDeConexion(this.nivel)
                                .tipo(this.tipo)
                                .energia(this.energia)
                                .build();
        return espiritu;
    }

    public Espiritu aModeloUpdate(Long id) {
        Espiritu espiritu = new Espiritu.Builder()
                .id(id)
                .nombre(this.nombre)
                .build();
        return espiritu;
    }
}
