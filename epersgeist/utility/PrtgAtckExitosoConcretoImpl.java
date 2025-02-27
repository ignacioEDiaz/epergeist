package ar.edu.unq.epersgeist.utility;


public class PrtgAtckExitosoConcretoImpl implements PorcentajeAtaqueExitoso {

    int porcentaje = 70;

    @Override
    public int porcentajeAtaqueExitoso(Integer nivelDeConexion, Integer puntosPorEspirituDominado) {
        return porcentaje;
    }

    @Override
    public void setPorcentajeExitoso(Integer valor) {
        this.porcentaje = valor;
    }
}
