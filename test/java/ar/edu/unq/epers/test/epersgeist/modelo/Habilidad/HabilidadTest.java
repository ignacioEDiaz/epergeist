package ar.edu.unq.epers.test.epersgeist.modelo.Habilidad;

import ar.edu.unq.epersgeist.exception.HabilidadNoSeRelacionaConsigoMismaException;
import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HabilidadTest {

    private Condicion condicion;
    private Habilidad habilidad;
    private Habilidad habilidad2;

    @BeforeEach
    void setUp() {
        habilidad = new Habilidad("habilidad1");
        habilidad2 = new Habilidad("habilidad2");
        condicion = new Condicion(Evaluacion.CANTIDAD_DE_ENERGIA, 30, habilidad2);
    }

    @Test
    void cuandoSeLeAgregaUnaCondicionAUnaHabiliad_EstaPasaraAFormarParteDeSusCondiciones() {
        habilidad.agregarCondicion(condicion);

        assertTrue(habilidad.getCondiciones().contains(condicion));
    }

    @Test
    void unaHabilidadNoSePuedeRelacionarConsigoMisma() {
        condicion = new Condicion(Evaluacion.CANTIDAD_DE_ENERGIA, 30, habilidad);

        assertThrows(HabilidadNoSeRelacionaConsigoMismaException.class, () -> {habilidad.agregarCondicion(condicion);});
    }
}
