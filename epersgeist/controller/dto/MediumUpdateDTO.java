package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;

public record MediumUpdateDTO(Long id,
                              String nombre,
                              Integer manaMax) {

    public Medium aModelo() {
        Medium medium = new Medium(this.nombre, this.manaMax);
        return medium;
    }
}
