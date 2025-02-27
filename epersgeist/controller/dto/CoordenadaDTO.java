package ar.edu.unq.epersgeist.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CoordenadaDTO {
    private Double latitud;
    private Double longitud;

    public CoordenadaDTO(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
