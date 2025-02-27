package ar.edu.unq.epers.test.epersgeist.modelo.Condicion;

import ar.edu.unq.epersgeist.exception.ValoresParaEvaluacionInvalidos;
import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CondicionTest {

    private Condicion condicion;

    @Test
    void NoSePuedeCrearUnaCondicionConNivelesMenoresOIgualesA0() {
        assertThrows(ValoresParaEvaluacionInvalidos.class, () -> condicion = new Condicion(Evaluacion.EXORCISMOS_EVITADOS, 0));
        assertThrows(ValoresParaEvaluacionInvalidos.class, () -> condicion = new Condicion(Evaluacion.EXORCISMOS_RESUELTOS, 0));
        assertThrows(ValoresParaEvaluacionInvalidos.class, () -> condicion = new Condicion(Evaluacion.NIVEL_DE_CONEXION, 0));
    }

    @Test
    void NoSePuedeCrearUnaCondicionConNivelesMayoresA100() {
        assertThrows(ValoresParaEvaluacionInvalidos.class, () -> condicion = new Condicion(Evaluacion.NIVEL_DE_CONEXION, 180));
        assertThrows(ValoresParaEvaluacionInvalidos.class, () -> condicion = new Condicion(Evaluacion.CANTIDAD_DE_ENERGIA, 180));
    }
}
