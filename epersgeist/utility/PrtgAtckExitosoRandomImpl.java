package ar.edu.unq.epersgeist.utility;

public class PrtgAtckExitosoRandomImpl implements PorcentajeAtaqueExitoso {

    @Override
    public int porcentajeAtaqueExitoso(Integer nivelDeConexion, Integer puntosPorEspirituDominado) {
        return (int) (Math.random() * 10) + 1 + nivelDeConexion + puntosPorEspirituDominado;
    }

    @Override
    public void setPorcentajeExitoso(Integer valor) {}
}
