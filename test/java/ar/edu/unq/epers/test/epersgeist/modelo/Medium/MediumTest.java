package ar.edu.unq.epers.test.epersgeist.modelo.Medium;

import ar.edu.unq.epersgeist.exception.EspirituNoLibreException;
import ar.edu.unq.epersgeist.exception.MoverMediumException;
import ar.edu.unq.epersgeist.exception.UbicacionNulaException;
import ar.edu.unq.epersgeist.modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {

    private Ubicacion ubicacion;
    private Medium medium;

    @BeforeEach
    void setUp() {
        ubicacion = new Ubicacion("ubicacion1");
    }

    @Test
    void testCrearExitoso(){
        medium = new Medium("Mago",100,100,ubicacion);

        assertEquals(medium.getNombre(),"Mago");
        assertEquals(medium.getManaMax(),100);
        assertEquals(medium.getMana(),100);
        assertEquals(medium.getUbicacion(), ubicacion);
    }
    @Test
    void testSetManaMax(){
        medium = new Medium("Mago",100,100,ubicacion);

        medium.setManaMax(1000);
        assertEquals(medium.getManaMax(),1000);

        medium.setManaMax(-1000);
        assertEquals(medium.getManaMax(),100);
    }
    @Test
    void testSetMana(){
        medium = new Medium("Mago",100,100,ubicacion);

        medium.setMana(0);
        assertEquals(medium.getMana(),0);

        medium.setMana(1000);
        assertEquals(medium.getMana(),100);

        medium.setMana(-100);
        assertEquals(medium.getMana(),0);

    }
    @Test
    void testSeaumentaManaAunMedium() {
        this.medium = new Medium("medium1", 100, 30, ubicacion);
        medium.aumentarMana(50);
        assertEquals(80, medium.getMana());
    }

    @Test
    void testSeaumentaElMaximoPermitidoYQuedaEnEse() {
        this.medium = new Medium("medium1", 100, 30, ubicacion);
        medium.aumentarMana(1000);
        assertEquals(medium.getManaMax(), medium.getMana());
    }

    @Test
    void testSePasaUNNumeroNegativoAAumetarManaYEsteQuedaEnMinimo() {
        this.medium = new Medium("medium1", 100, 30, ubicacion);
        medium.aumentarMana(-1000);
        assertEquals(0, medium.getMana());
    }

    @Test
    void testConectarseAEspirituExitoso() {
        Medium medium = new Medium("Medium 1", 100, 50, new Ubicacion("Salón"));
        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL, 50, "Espiritu 1", new Ubicacion("Bosque"), 80);

        medium.conectarseAEspiritu(espiritu);

        assertTrue(medium.getEspiritus().contains(espiritu));
        assertEquals(medium, espiritu.getMedium());
    }

    @Test
    void testConectarseAEspirituNoLibre() {
        Medium medium = new Medium("Medium 1", 100, 50, new Ubicacion("Salón"));
        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL, 50, "Espiritu 1", new Ubicacion("Bosque"), 80);
        espiritu.conectarConMedium(new Medium("Otro Medium", 80, 40, new Ubicacion("Otra ubicacion")));

        assertThrows(EspirituNoLibreException.class, () -> {
            medium.conectarseAEspiritu(espiritu);
        });
    }

    @Test
    void testDesconectarse() {
        Medium medium = new Medium("Medium 1", 100, 50, new Ubicacion("Salón"));
        Espiritu espiritu = new Espiritu(TipoEspiritu.DEMONIO, 50, "Espiritu 1", new Ubicacion("Bosque"), 80);
        medium.conectarseAEspiritu(espiritu);

        medium.desconectarse(espiritu);

        assertFalse(medium.getEspiritus().contains(espiritu));
    }

    @Test
    void testCheckearSiTieneAngelesConAngeles() {
        Espiritu angel = new Espiritu(TipoEspiritu.ANGEL, 50, "Angel 1", new Ubicacion("Bosque"), 80);
        medium = new Medium("Medium 1", 100, 50, new Ubicacion("Salón"));
        medium.getEspiritus().add(angel);

        assertDoesNotThrow(() -> medium.checkearSiTieneAngeles());
    }

    @Test
    void testCheckearSiTieneAngelesSinAngeles() {
        Espiritu demonio = new Espiritu(TipoEspiritu.DEMONIO, 50, "Demonio 1", new Ubicacion("Bosque"), 80);
        medium = new Medium("Medium 1", 100, 50, new Ubicacion("Salón"));
        medium.getEspiritus().add(demonio);

        assertFalse(medium.checkearSiTieneAngeles());
    }

    @Test
    void testDesconectarEspirituNoConectado() {
        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL, 50, "Angel 1", new Ubicacion("Bosque"), 80);
        medium = new Medium("Medium 1", 100, 50, new Ubicacion("Salón"));

        medium.desconectarse(espiritu);

        assertFalse(medium.getEspiritus().contains(espiritu));
    }

    @Test
    void testConectarEspirituConManaMaximo() {
        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL, 50, "Angel 1", new Ubicacion("Bosque"), 80);
        medium = new Medium("Medium 1", 100, 100, new Ubicacion("Salón"));

        medium.conectarseAEspiritu(espiritu);

        assertTrue(medium.getEspiritus().contains(espiritu));
    }

    @Test
    void testDesconectarEspirituEnOtraUbicacion() {
        Espiritu espiritu = new Espiritu(TipoEspiritu.DEMONIO, 50, "Demonio 1", new Ubicacion("Cueva"), 80);
        medium = new Medium("Medium 1", 100, 50, new Ubicacion("Ciudad"));

        medium.desconectarse(espiritu);

        assertFalse(medium.getEspiritus().contains(espiritu));
    }

    @Test
    void noSePuedeCrearUnMediumSinUbicacion() {
        assertThrows(UbicacionNulaException.class, () -> {new Medium("Mago", 100, 100, null);});
    }

    @Test
    void noSePuedeModificarLaUbicacionDeUnMediumPorUnaUbicacionNula() {
        medium = new Medium("Mago",100,100,ubicacion);
        assertThrows(UbicacionNulaException.class, () -> {medium.setUbicacion(null);});
    }

    @Test
    void cuandoSeMueveUnMedium_TodosSusEspiritusTambien() {
        medium = new Medium("Mago",100,100,ubicacion);
        Espiritu espiritu = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu1", ubicacion, 10);

        medium.conectarseAEspiritu(espiritu);
        Ubicacion otraUbicacion = new Ubicacion("cementerio", TipoUbicacion.CEMENTERIO,0);
        medium.mover(otraUbicacion);

        assertEquals("cementerio", medium.getUbicacion().getNombre());
        assertEquals("cementerio", espiritu.getUbicacion().getNombre());
    }

    @Test
    void cuandoUnMediumSeQuiereMover_YAlgunoDeSusEspiritusNoPoseenEnergiaSuficiente_NoPodraHacerlo() {
        medium = new Medium("Mago",100,100,ubicacion);
        Espiritu espiritu = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu1", ubicacion, 5);

        medium.conectarseAEspiritu(espiritu);
        Ubicacion otraUbicacion = new Ubicacion("santuario", TipoUbicacion.SANTUARIO,0);

        assertThrows(MoverMediumException.class,()-> medium.mover(otraUbicacion));
    }

    @Test
    void cuandoUnMediumSeMueveALaUbicacionALaQueEsta_NingunoDeSusEspiritusPierdeEnergia() {
        Ubicacion ubicacionActual = new Ubicacion("ubicacionActual",TipoUbicacion.SANTUARIO,100);
        medium = new Medium("Mago",100,100,ubicacionActual);
        Espiritu espiritu = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu1", ubicacionActual, 5);
        medium.conectarseAEspiritu(espiritu);

        medium.mover(ubicacionActual);

        assertEquals(5, espiritu.getEnergia());
    }
}