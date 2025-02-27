package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Habilidad;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record HabilidadDTO (String nombre, Set<CondicionDTO> condiciones) {

    public static HabilidadDTO desdeModelo(Habilidad habilidad) {
        Set<CondicionDTO> condicionesDTO = habilidad.getCondiciones() != null ?
                habilidad.getCondiciones().stream().map(CondicionDTO::desdeModelo).collect(Collectors.toSet())
                : new HashSet<>();
        return new HabilidadDTO(habilidad.getNombre(), condicionesDTO);
    }

    public Habilidad aModelo() {
        Set<Condicion> condicionesModelo = this.condiciones() != null ?
                this.condiciones().stream().map(CondicionDTO::aModelo).collect(Collectors.toSet())
                : new HashSet<>();
        Habilidad h = new Habilidad(this.nombre());
        h.setCondiciones(condicionesModelo);
        return h;

    }
}