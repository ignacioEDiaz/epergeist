package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;

public record CondicionDTO (Evaluacion evaluacion, int nivel, String habilidad){

    public static CondicionDTO desdeModelo(Condicion condicion) {
        return new CondicionDTO(
                condicion.getEvaluacion(),
                condicion.getNivel(),
                condicion.getHabilidad().getNombre());
    }

    public Condicion aModelo(CondicionDTO condicionDTO) {
        return new Condicion(this.evaluacion, this.nivel);
    }

    public Condicion aModelo() {
        return new Condicion(this.evaluacion, this.nivel);
    }
}

