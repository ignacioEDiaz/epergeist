package ar.edu.unq.epers.test.epersgeist.modelo.Ubicacion;

import ar.edu.unq.epersgeist.exception.EntidadNombreInvalidoException;
import ar.edu.unq.epersgeist.exception.ExcesoDeEnergiaPermitidaException;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UbicacionTest {

    private Ubicacion ubicacion;

    @Test
    void testCrearUbicacionExitosa(){

        ubicacion = new Ubicacion("Bosque");

        assertEquals(ubicacion.getNombre(),"Bosque");
    }
    @Test
    void testCrearUbicacionPeroConNombreNull(){

        assertThrows(EntidadNombreInvalidoException.class, () -> {
            ubicacion = new Ubicacion(null);
        });
    }
    @Test
    void testCrearUbicacionPeroConNombreVacio(){

        assertThrows(EntidadNombreInvalidoException.class, () -> {
            ubicacion = new Ubicacion("");
        });
    }
    @Test
    void testCrearUbicacionPeroConNombreVacioPorEspacios(){

        assertThrows(EntidadNombreInvalidoException.class, () -> {
            ubicacion = new Ubicacion("   ");
        });
    }

    @Test
    void seIntentaCrearUnaUbicacionConEnergiaMayorACien(){
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setNombre("Norte");

        assertThrows(ExcesoDeEnergiaPermitidaException.class, () -> ubicacion.setEnergia(1000));
    }

    @Test
    void seIntentaCrearUnaUbicacionConEnergiaMenorACero(){
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setNombre("Norte");

        assertThrows(ExcesoDeEnergiaPermitidaException.class, () -> ubicacion.setEnergia(-5));
    }
}
