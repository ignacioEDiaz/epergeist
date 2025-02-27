package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record MediumDTO(Long id,
                        String nombre,
                        Integer manaMax,
                        Integer mana,
                        Long ubicacionId,
                        Set<EspirituDTO> espiritus) {

    public Medium aModelo(Ubicacion ubicacion) {
        Medium medium = new Medium(this.nombre, this.manaMax, this.mana, ubicacion);
        medium.setId(this.id);
        return medium;
   }

    public static MediumDTO desdeModelo(Medium medium){
        return new MediumDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getUbicacion().getId(),
                medium.getEspiritus() == null ? null :
                        medium.getEspiritus().stream().map(
                        EspirituDTO::desdeModelo).collect(Collectors.toCollection(HashSet::new))
        );
    }

    public Medium aModelo() {
        Medium medium = new Medium(this.nombre, this.manaMax, this.mana);
        return medium;
    }
}
