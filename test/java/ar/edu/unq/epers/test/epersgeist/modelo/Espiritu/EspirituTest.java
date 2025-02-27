package ar.edu.unq.epers.test.epersgeist.modelo.Espiritu;

import ar.edu.unq.epersgeist.exception.UbicacionNulaException;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.utility.PorcentajeAtaqueExitoso;
import ar.edu.unq.epersgeist.utility.PrtgAtckExitosoConcretoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class EspirituTest {

    private Espiritu espiritu,espiritu2;
    private Medium medium;
    private Ubicacion ubicacion;
    PorcentajeAtaqueExitoso porcentajeAtaqueExitoso;

    @BeforeEach
    public void setup() {
        ubicacion = new Ubicacion("Ubicacion 1");
        espiritu = new Espiritu(TipoEspiritu.DEMONIO, 50, "Espiritu Maligno", ubicacion,50);
        espiritu2 = new Espiritu(TipoEspiritu.ANGEL, 50, "Espiritu Luminoso", ubicacion,50);
        medium = new Medium("medium1", 100, 50, ubicacion);
        porcentajeAtaqueExitoso = new PrtgAtckExitosoConcretoImpl();
    }

    @Test
    public void testCrearEspiritu() {
        assertEquals("Espiritu Maligno", espiritu.getNombre());
        assertEquals(TipoEspiritu.DEMONIO, espiritu.getTipo());
        assertEquals(ubicacion, espiritu.getUbicacion());
        assertEquals(50, espiritu.getNivelDeConexion());
        assertEquals(50, espiritu.getEnergia());
    }
    @Test
    public void testCrearEspirituPeroConNivelDeConexionMayorA100EnConstructor(){

        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL,1000,"EspirituMaxNivel",ubicacion, 0);

        assertEquals(espiritu.getNivelDeConexion(),100);
    }

    @Test
    public void testCrearEspirituPeroConNivelDeConexionEnNegativoEnConstructor(){

        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL,-1000,"EspirituMinNivel",ubicacion, 0);

        assertEquals(espiritu.getNivelDeConexion(),0);
    }
    @Test
    public void testCrearEspirituPeroConEnergiaMayorA100EnConstructor(){

        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL, 50,"EspirituMaxEnergia",ubicacion, 1000);

        assertEquals(espiritu.getEnergia(),100);
    }
    @Test
    public void testCrearEspirituPeroConEnergiaNegativaEnConstructor(){

        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL, 50,"EspirituMaxEnergia",ubicacion, -1000);

        assertEquals(espiritu.getEnergia(),0);
    }
    @Test
    public void testSetNivelDeConexion(){

        espiritu.setNivelDeConexion(10);
        assertEquals(espiritu.getNivelDeConexion(),10);

        espiritu.setNivelDeConexion(1000);
        assertEquals(espiritu.getNivelDeConexion(),100);

        espiritu.setNivelDeConexion(-1000);
        assertEquals(espiritu.getNivelDeConexion(),0);
    }
    @Test
    public void testSetEnergia(){

        espiritu.setEnergia(10);
        assertEquals(espiritu.getEnergia(),10);

        espiritu.setEnergia(1000);
        assertEquals(espiritu.getEnergia(),100);

        espiritu.setEnergia(-1000);
        assertEquals(espiritu.getEnergia(),0);
    }
    @Test
    public void testAumentarConexion() {
        espiritu.aumentarConexion(medium);
        assertEquals(60, espiritu.getNivelDeConexion());
        espiritu.aumentarConexion(medium);
        assertEquals(70, espiritu.getNivelDeConexion());

        espiritu.aumentarConexion(medium);
        espiritu.aumentarConexion(medium);
        espiritu.aumentarConexion(medium);
        assertEquals(100, espiritu.getNivelDeConexion());
    }

    @Test
    public void testConectarConMedium() {
        espiritu.conectarConMedium(medium);
        assertEquals(medium, espiritu.getMedium());
    }

    @Test
    public void testAtacar() {
        espiritu2.atacar(this.espiritu,this.porcentajeAtaqueExitoso);
        assertEquals(25,espiritu.getEnergia());
    }

    @Test
    public void testAtacarEspirituConEnergiaCeroSigueEnCero(){
        espiritu.setEnergia(0);
        espiritu2.atacar(this.espiritu,this.porcentajeAtaqueExitoso);
        assertEquals(espiritu.getEnergia(),0);
    }


    @Test
    public void testRecibirAtaque() {
        Espiritu angel = new Espiritu(TipoEspiritu.ANGEL, 50, "Espiritu Angel", ubicacion);

        espiritu.recibirAtaque(angel);
        assertEquals(espiritu.getEnergia(), 25);

        angel.setNivelDeConexion(100);
        espiritu.recibirAtaque(angel);
        assertEquals(0, espiritu.getEnergia());
    }
    @Test
    public void testRecibirAtaquePeroEspirituYaTieneEnergiaCeroYSeguiEsteEnCero(){
        Espiritu angel = new Espiritu(TipoEspiritu.ANGEL, 50, "Espiritu Angel", ubicacion);
        espiritu.setEnergia(0);
        espiritu.recibirAtaque(angel);
        assertEquals(espiritu.getEnergia(),0);

    }

    @Test
    public void testDesconectarsePeroElEspirituNoEstaConectadoPorLoQueNoFalla(){

        espiritu.desconectarse();
        assertNull(espiritu.getMedium());
        assertEquals(0,espiritu.getNivelDeConexion());
    }
    @Test
    public void testEstaLibre() {
        assertTrue(espiritu.estaLibre());
        espiritu.conectarConMedium(medium);
        assertFalse(espiritu.estaLibre());
    }

    @Test
    public void testSerInvocadoPorMedium() {
        espiritu.serInvocadoPorMedium(medium);
        assertEquals("Ubicacion 1", espiritu.getUbicacion().getNombre());
    }

    @Test
    void noSePuedeCrearUnEspirituSinUbicacion() {
        assertThrows(UbicacionNulaException.class, () -> {new Espiritu(TipoEspiritu.ANGEL, 50, "Espiritu Angel", null);});
    }

    @Test
    void noSePuedeModificarLaUbicacionDeUnEspirituPorUnaUbicacionNula() {
        assertThrows(UbicacionNulaException.class, () -> {espiritu.setUbicacion(null);});
    }

    @Test
    void cuandoUnEspirituMutaAUnaHabilidad_EsaHabilidadFormaraParteDeSusHabilidades() {
        Habilidad habilidadAMutar = new Habilidad("habilidad1");
        Set<Habilidad> habilidadesAMutar = new HashSet<>();
        habilidadesAMutar.add(habilidadAMutar);

        espiritu.mutar(habilidadesAMutar);

        assertTrue(habilidadesAMutar.contains(habilidadAMutar));
    }

}