package ar.edu.unq.epers.test.epersgeist.service.Espiritu;

import ar.edu.unq.epersgeist.EpersgeistApplication;
import ar.edu.unq.epersgeist.exception.*;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDao;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.EspirituMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.MediumMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.UbicacionMongoDAO;
import ar.edu.unq.epersgeist.persistencia.dao.mongoDB.mongoDTOs.EspirituMongoDTO;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.MediumServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.UbicacionServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = EpersgeistApplication.class)
public class EspirituServiceTest {

    @Autowired
    private EspirituServiceImpl espirituService;
    @Autowired
    private MediumServiceImpl mediumService;
    @Autowired
    private UbicacionServiceImpl ubicacionService;

    @Autowired
    private UbicacionDAO ubicacionDAO;
    @Autowired
    private EspirituDAO espirituDAO;
    @Autowired
    private MediumDao mediumDao;

    @Autowired private EspirituMongoDAO espirituMongoDAO;
    @Autowired private UbicacionMongoDAO ubicacionMongoDAO;
    @Autowired private MediumMongoDAO mediumMongoDAO;

    private Espiritu espiritu,espirituNull;

    private Ubicacion ubicacion, ubicCementerio;
    private Medium maguito;
    private Espiritu chispita;

    @BeforeEach
    void setUp() {
        ubicacion = new Ubicacion("ubicacion1", TipoUbicacion.SANTUARIO,10);
        ubicacionService.crear(ubicacion);

        ubicCementerio = new Ubicacion("ubicacionCementerio", TipoUbicacion.CEMENTERIO,10);
        ubicacionService.crear(ubicCementerio);

        espiritu = new Espiritu(TipoEspiritu.ANGEL,0,"Tyrael",ubicacion);
        chispita = new Espiritu(TipoEspiritu.ANGEL, 40, "Pika",ubicacion);
        maguito = new Medium("Mago",110,110,ubicacion);
    }

    @Test
    void seActualizaUnEspiritu(){
        espirituService.crear(espiritu);

        Espiritu espirituRecuperado = espirituService.recuperar(espiritu.getId());

        espirituRecuperado.setTipo(TipoEspiritu.DEMONIO);
        espirituRecuperado.setNombre("Asmodan");
        espirituRecuperado.setNivelDeConexion(50);
        espirituRecuperado.setEnergia(30);

        espirituService.actualizar(espirituRecuperado);

        Espiritu espirituRec = espirituService.recuperar(espirituRecuperado.getId());

        assertEquals(espirituRecuperado.getNombre(), espirituRec.getNombre());
        assertEquals(espirituRecuperado.getTipo(), espirituRec.getTipo());
        assertEquals(espirituRecuperado.getNivelDeConexion(), espirituRec.getNivelDeConexion());
    }

    @Test
    void seQuiereActualizarUnEspirituQueNoSePersiste(){
        espiritu.setTipo(TipoEspiritu.DEMONIO);
        espiritu.setNombre("Asmodan");
        espiritu.setNivelDeConexion(50);

        assertThrows(EntidadNoExisteEnBDException.class, () -> espirituService.actualizar(espiritu));
    }
    @Test
    void testActualizarPeroConEspirituIdInexistente(){
        espiritu.setId(1L);
        espiritu.setNombre("Patron");
        espiritu.setEnergia(100);

        assertThrows(EntidadNoExisteEnBDException.class,()->espirituService.actualizar(espiritu));

    }

    @Test
    void EspirituCrearOk(){
        Espiritu angel = new Espiritu(TipoEspiritu.ANGEL,0,"Tyrael",ubicacion,180);
        Espiritu demonio = new Espiritu(TipoEspiritu.DEMONIO,0,"Valak",ubicacion,202);

        espirituService.crear(angel);
        espirituService.crear(demonio);

        Espiritu angelRec = espirituService.recuperar(angel.getId());
        Espiritu demonioRec = espirituService.recuperar(demonio.getId());

        assertEquals(angelRec.getNombre(),angel.getNombre());
        assertEquals(angelRec.getNivelDeConexion(),angel.getNivelDeConexion());
        assertEquals(angelRec.getTipo(),angel.getTipo());
        assertEquals(angelRec.getEnergia(),angel.getEnergia());

        assertEquals(demonioRec.getNombre(),demonio.getNombre());
        assertEquals(demonioRec.getNivelDeConexion(),demonio.getNivelDeConexion());
        assertEquals(demonioRec.getTipo(),demonio.getTipo());
        assertEquals(demonioRec.getEnergia(),demonio.getEnergia());
    }

    @Test
    void EspirituRecuperarOK(){
        Espiritu angel = new Espiritu(TipoEspiritu.ANGEL,0,"Tyrael",ubicacion,340);
        Espiritu demonio = new Espiritu(TipoEspiritu.DEMONIO,0,"Valak",ubicacion,45);

        espirituService.crear(angel);
        espirituService.crear(demonio);

        Espiritu angelRec = espirituService.recuperar(angel.getId());
        Espiritu demonioRec = espirituService.recuperar(demonio.getId());

        assertEquals(angelRec.getTipo(),angel.getTipo());
        assertEquals(angelRec.getNivelDeConexion(),angel.getNivelDeConexion());
        assertEquals(angelRec.getNombre(),angel.getNombre());

        assertEquals(demonioRec.getTipo(),demonio.getTipo());
        assertEquals(demonioRec.getNivelDeConexion(),demonio.getNivelDeConexion());
        assertEquals(demonioRec.getNombre(),demonio.getNombre());
    }

    @Test
    void EspirituRecuperarError(){
        Espiritu demonio = new Espiritu(TipoEspiritu.DEMONIO,0,"Valakk",ubicacion,300);

        assertThrows(EntidadNoExisteEnBDException.class,()-> espirituService.recuperar(demonio.getId()));
    }

    @Test
    void testEspirituRecuperarPeroConIdInexistente(){

        assertThrows(EntidadNoExisteEnBDException.class,()-> espirituService.recuperar(100L));
    }

    @Test
    void testSeEliminaUnEspirituYEsteYaNoSePersiste(){
        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL,0,"espiritu1",ubicacion);
        espirituService.crear(espiritu);
        espirituService.eliminar(espiritu.getId());
        assertFalse(espirituService.recuperarTodos().contains(espiritu));
    }
    @Test
    void testEliminarEspirituPeroConIdNull(){

        assertThrows(EntidadNoExisteEnBDException.class,()-> espirituService.eliminar(espiritu.getId()));
    }
    @Test
    void testEliminarEspirituPeroEsNull(){

        assertThrows(NullPointerException.class,()-> espirituService.eliminar(espirituNull.getId()));
    }

    @Test
    void testEliminarEspirituPeroConIdInexistente(){
        assertThrows(EntidadNoExisteEnBDException.class,() -> espirituService.eliminar(100L));
    }

    @Test
    void testRecuperarEspiritusOk(){
        Espiritu angel = new Espiritu(TipoEspiritu.ANGEL,0,"Tyrael",ubicacion);
        Espiritu demonio = new Espiritu(TipoEspiritu.DEMONIO,0,"Valak",ubicacion);

        espirituService.crear(angel);
        espirituService.crear(demonio);
        List<Espiritu> listaTest = espirituService.recuperarTodos();

        assertEquals(listaTest.size(),2);
        assertFalse(listaTest.isEmpty());
    }

    @Test
    void testRecuperarEspiritusConListaVacia(){
        List<Espiritu> listaTest = espirituService.recuperarTodos();

        assertEquals(0, listaTest.size());
        assertTrue(listaTest.isEmpty());
    }

    @Test
    void espiritusDemoniacosPaginadosAscendentemente() {
        Espiritu espiritu1 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu1", ubicacion, 8);
        Espiritu espiritu2 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu2", ubicacion, 7);
        espirituService.crear(espiritu1);
        espirituService.crear(espiritu2);

        List<Espiritu> espiritus = espirituService.espiritusDemoniacos(Direccion.ASCENDENTE, 0, 2);

        assertEquals(2, espiritus.size());
        assertEquals(espiritus.getFirst().getNombre(), espiritu2.getNombre());
    }

    @Test
    void espiritusDemoniacosPaginadosDescendentemente() {
        Espiritu espiritu1 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu1", ubicacion, 8);
        Espiritu espiritu2 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu2", ubicacion, 7);
        espirituService.crear(espiritu1);
        espirituService.crear(espiritu2);

        List<Espiritu> espiritus = espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 0, 2);

        assertEquals(2, espiritus.size());
        assertEquals(espiritus.getFirst().getNombre(), espiritu1.getNombre());
    }

    @Test
    void espiritusDemoniacosPaginadosDescendentementeError() {
        Espiritu espiritu1 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu1", ubicacion, 8);
        Espiritu espiritu2 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu2", ubicacion, 7);
        espirituService.crear(espiritu1);
        espirituService.crear(espiritu2);

        assertThrows(NumeroDePaginaMenorACeroException.class,  () -> espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, -5, 2));
    }

    @Test
    void espiritusDemoniacosOk(){
        Espiritu angel    = new Espiritu(TipoEspiritu.ANGEL,0,"angel",ubicacion,250);
        Espiritu angel2   = new Espiritu(TipoEspiritu.ANGEL,0,"angel2",ubicacion,400);
        Espiritu angel3   = new Espiritu(TipoEspiritu.ANGEL,0,"ange3",ubicacion,10);
        Espiritu angel4   = new Espiritu(TipoEspiritu.ANGEL,0,"angel4",ubicacion,450);

        Espiritu demonio  = new Espiritu(TipoEspiritu.DEMONIO,0,"demonio",ubicacion,100);
        Espiritu demonio2 = new Espiritu(TipoEspiritu.DEMONIO,0,"demonio2",ubicacion,65);
        Espiritu demonio3 = new Espiritu(TipoEspiritu.DEMONIO,0,"demonio3",ubicacion,1000);
        Espiritu demonio4 = new Espiritu(TipoEspiritu.DEMONIO,0,"demonio4",ubicacion,-780);

        espirituService.crear(angel);
        espirituService.crear(angel2);
        espirituService.crear(angel3);
        espirituService.crear(angel4);

        espirituService.crear(demonio);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);
        espirituService.crear(demonio4);

        List<Espiritu> demoniacos = espirituService.espiritusDemoniacos();

        assertEquals( 4, demoniacos.size());
        assertEquals( 100, demoniacos.get(0).getEnergia());
        assertEquals( 100, demoniacos.get(1).getEnergia());
        assertEquals( 65, demoniacos.get(2).getEnergia());
        assertEquals( 0, demoniacos.get(3).getEnergia());
    }

    @Test
    void testConectarExitoso(){
        maguito.setCoordenada(new GeoJsonPoint(1,1));
        chispita.setCoordenada(new GeoJsonPoint(1,1));
        mediumService.guardarConUbi(maguito);
        espirituService.guardarConUbi(chispita);

        mediumService.invocar(maguito.getId(), chispita.getId());
        Medium mediumTest = espirituService.conectar(chispita.getId(), maguito.getId());
        Espiritu espirituTest = espirituService.recuperar(chispita.getId());

        assertEquals(mediumTest.getEspiritus().size(),1);
        assertEquals(espirituTest.getNivelDeConexion(), 60);
    }

    @Test
    void testConectarExitosoPeroElEspirituNoSuperaLos100DeNivelDeConexion(){
        chispita.setNivelDeConexion(100);
        maguito.setCoordenada(new GeoJsonPoint(1,1));
        chispita.setCoordenada(new GeoJsonPoint(1,1));
        mediumService.guardarConUbi(maguito);
        espirituService.guardarConUbi(chispita);

        mediumService.invocar(maguito.getId(), chispita.getId());
        espirituService.conectar(chispita.getId(), maguito.getId());
        Espiritu espirituTest = espirituService.recuperar(chispita.getId());

        assertEquals(espirituTest.getNivelDeConexion(), 100);
    }

    @Test
    void testConectarPeroEspirituYMediumAmbosPoseenUbicacionesDistintas(){
        Ubicacion ubicacion1 = new Ubicacion("Llanura", TipoUbicacion.SANTUARIO,10);
        ubicacionService.crear(ubicacion1);
        chispita.setUbicacion(ubicacion1);
        mediumService.crear(maguito);
        espirituService.crear(chispita);

        assertThrows(DistintaUbicacionException.class,() -> espirituService.conectar(chispita.getId(), maguito.getId()));
    }

    @Test
    void testConectarPeroEspirituNoEstaLibre(){
        Medium brujo = new Medium("Brujo",200,150,ubicacion);
        chispita.setMedium(brujo);
        chispita.setUbicacion(ubicacion);
        mediumService.crear(maguito);
        mediumService.crear(brujo);
        espirituService.crear(chispita);

        assertThrows(EspirituNoLibreException.class,() -> espirituService.conectar(chispita.getId(), maguito.getId()));
    }

    @Test
    void espiritusDemoniacosPaginados_PeroSePideUnaPaginaQueNoEsLaInicial() {
        Espiritu espiritu1 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu1", ubicacion, 1);
        Espiritu espiritu2 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu2", ubicacion, 2);
        espirituService.crear(espiritu1);
        espirituService.crear(espiritu2);

        Espiritu espiritu3 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu3", ubicacion, 3);
        Espiritu espiritu4 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu4", ubicacion, 4);
        espirituService.crear(espiritu3);
        espirituService.crear(espiritu4);

        List<Espiritu> espiritus = espirituService.espiritusDemoniacos(Direccion.ASCENDENTE, 1, 2);

        assertEquals(2, espiritus.size());
        assertEquals(espiritus.getFirst().getNombre(), espiritu3.getNombre());
    }

    @Test
    void espiritusDemoniacosPaginados_AUnaPaginaQueNoTieneEspiritus() {
        Espiritu espiritu1 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu1", ubicacion, 6);
        Espiritu espiritu2 = new Espiritu(TipoEspiritu.DEMONIO, 0, "espiritu2", ubicacion, 5);
        espirituService.crear(espiritu1);
        espirituService.crear(espiritu2);

        List<Espiritu> espiritus = espirituService.espiritusDemoniacos(Direccion.ASCENDENTE, 2, 2);

        assertTrue(espiritus.isEmpty());
    }

    @Test
    void espiritusDemoniacosPaginados_PeroNoHayEspiritusPersistidos() {
        List<Espiritu> espiritus = espirituService.espiritusDemoniacos(Direccion.ASCENDENTE, 0, 2);
        assertTrue(espiritus.isEmpty());
    }

    @Test
    void espiritusDemoniacosPaginados_SePideUnaCantidadDeEspiritusNegativaPorPagina() {
        assertThrows(CantidadPorPaginaMenorACeroException.class,  () -> espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 0, -2));
    }

    @Test
    void testConectarPeroElEspirituNoEstaPersistido(){
        mediumService.crear(maguito);

        assertThrows(EntidadNoExisteEnBDException.class,() -> espirituService.conectar(chispita.getId(), maguito.getId()));
    }

    @Test
    void testConectarPeroElIdEspirituEsInexistente(){
        mediumService.crear(maguito);
        assertThrows(EntidadNoExisteEnBDException.class,() -> espirituService.conectar(1000L, maguito.getId()));
    }

    @Test
    void testConectarPeroElMediumNoEstaPersistido(){
        espirituService.crear(chispita);

        assertThrows(EntidadNoExisteEnBDException.class,() -> espirituService.conectar(chispita.getId(), maguito.getId()));
    }

    @Test
    void testConectarPeroElIdMediumEsInexistente(){
        espirituService.crear(chispita);
        assertThrows(EntidadNoExisteEnBDException.class,()-> espirituService.conectar(chispita.getId(),1000L));
    }

    @Test
    void cuandoUnEspirituSeConectaAUnMediumSinMana_() {
        ubicacion = new Ubicacion("Llanura", TipoUbicacion.SANTUARIO,10);
        ubicacionService.crear(ubicacion);

        Medium medium = new Medium("medium1",100,0,ubicacion);
        Espiritu espiritu1 = new Espiritu(TipoEspiritu.ANGEL,40,"Valak",ubicacion,100);

        mediumService.crear(medium);
        espirituService.crear(espiritu1);

        espirituService.conectar(espiritu1.getId(), medium.getId());

        Medium mediumTest = mediumService.recuperar(medium.getId());
        Espiritu espirituTest = espirituService.recuperar(espiritu1.getId());

        assertEquals("medium1", espirituTest.getMedium().getNombre());
        assertTrue(mediumTest.getEspiritus().stream().allMatch(espiritu -> espiritu.getNombre().equals("Valak")));
        assertEquals(1, mediumTest.getEspiritus().size());
        assertEquals(40, espirituTest.getNivelDeConexion());
    }

    @Test
    void PedirEspíritusDemoniacosPeroNingunoEsPersistido(){
        List<Espiritu> demoniacos = espirituService.espiritusDemoniacos();

        assertEquals( 0, demoniacos.size());
    }

    @Test
    void testSeEliminaUnEspirituYEsteSeEncuentraEnEstadoBorradoEnBD(){
        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL,0,"espiritu1",ubicacion);
        espirituService.crear(espiritu);
        espirituService.eliminar(espiritu.getId());

        assertFalse(espirituService.recuperarTodos().contains(espiritu));
        assertThrows(EntidadNoExisteEnBDException.class, () -> espirituService.recuperar(espiritu.getId()));
    }

    @Test
    void testElEspirituSeEncuentraEnEstadoNoBorradoAlSerCreado(){
        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL,0,"espiritu1",ubicacion);
        espirituService.crear(espiritu);
        Espiritu espirituRecuperado = espirituService.recuperar(espiritu.getId());

        assertFalse(espirituRecuperado.isDeleted());
    }

    @Test
    void testSeCreanSeisEspiritusYTodosSeEncuentranComoNoBorrados(){
        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL,0,"espiritu1",ubicacion);
        Espiritu espiritu2 = new Espiritu(TipoEspiritu.DEMONIO,0,"espiritu2",ubicacion);
        Espiritu espiritu3 = new Espiritu(TipoEspiritu.DEMONIO,0,"espiritu3",ubicacion);
        Espiritu espiritu4 = new Espiritu(TipoEspiritu.ANGEL,0,"espiritu4",ubicacion);
        Espiritu espiritu5 = new Espiritu(TipoEspiritu.ANGEL,0,"espiritu5",ubicacion);
        Espiritu espiritu6 = new Espiritu(TipoEspiritu.DEMONIO,0,"espiritu6",ubicacion);

        espirituService.crear(espiritu);
        espirituService.crear(espiritu2);
        espirituService.crear(espiritu3);
        espirituService.crear(espiritu4);
        espirituService.crear(espiritu5);
        espirituService.crear(espiritu6);

        Espiritu espirituRecuperado1 = espirituService.recuperar(espiritu.getId());
        Espiritu espirituRecuperado2 = espirituService.recuperar(espiritu2.getId());
        Espiritu espirituRecuperado3 = espirituService.recuperar(espiritu3.getId());
        Espiritu espirituRecuperado4 = espirituService.recuperar(espiritu4.getId());
        Espiritu espirituRecuperado5 = espirituService.recuperar(espiritu5.getId());
        Espiritu espirituRecuperado6 = espirituService.recuperar(espiritu6.getId());

        assertEquals(6, this.espirituService.recuperarTodos().size());
        assertFalse(espirituRecuperado1.isDeleted());
        assertFalse(espirituRecuperado2.isDeleted());
        assertFalse(espirituRecuperado3.isDeleted());
        assertFalse(espirituRecuperado4.isDeleted());
        assertFalse(espirituRecuperado5.isDeleted());
        assertFalse(espirituRecuperado6.isDeleted());

    }

    @Test
    void testElEspirituEsBorradoEIntentaConectarseConElMedium(){
        Espiritu espiritu = new Espiritu(TipoEspiritu.ANGEL,0,"espiritu1",ubicacion);
        Medium medium = new Medium("medium1",100,0,ubicacion);
        espirituService.crear(espiritu);
        mediumService.crear(medium);

        espirituService.eliminar(espiritu.getId());

        assertThrows(EntidadNoExisteEnBDException.class,() -> espirituService.conectar(espiritu.getId(), medium.getId()));
    }

    @Test
    void seCreanCuatroDemoniosSeBorranDosYSeRecuperanTodosLosDemoniosYDosPermancenEnModoBorrado(){
        Espiritu demonio  = new Espiritu(TipoEspiritu.DEMONIO,0,"demonio",ubicacion,100);
        Espiritu demonio2 = new Espiritu(TipoEspiritu.DEMONIO,0,"demonio2",ubicacion,65);
        Espiritu demonio3 = new Espiritu(TipoEspiritu.DEMONIO,0,"demonio3",ubicacion,1000);
        Espiritu demonio4 = new Espiritu(TipoEspiritu.DEMONIO,0,"demonio4",ubicacion,-780);

        espirituService.crear(demonio);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);
        espirituService.crear(demonio4);

        espirituService.eliminar(demonio.getId());
        espirituService.eliminar(demonio4.getId());
        List<Espiritu> demoniacos = espirituService.espiritusDemoniacos();


        assertEquals( 2, demoniacos.size());
        assertThrows(EntidadNoExisteEnBDException.class, () -> espirituService.recuperar(demonio.getId()));
        assertThrows(EntidadNoExisteEnBDException.class, () -> espirituService.recuperar(demonio4.getId()));
    }

    @Test
    void seCreaUnEspirituConLaHabilidadMaterializacionComoDefault(){
        Espiritu demonio  = new Espiritu(TipoEspiritu.DEMONIO,0,"demonio",ubicacion,100);
        espirituService.crear(demonio);

        Set<String> habilidades = espirituService.recuperarHabilidadesDe(demonio.getId());

        assertTrue(habilidades.contains("Materializacion"));
        assertEquals(1, habilidades.size());
    }

    @Test
    void seCreaUnEspirituConLaHabilidadMaterializacionComoDefaultYSeLeAgreganDosMas() {
        Espiritu demonio = new Espiritu(TipoEspiritu.DEMONIO, 0, "demonio", ubicacion, 100);
        espirituService.crear(demonio);
        demonio.setHabilidades("Mutacion");
        demonio.setHabilidades("Ilusion");
        espirituService.actualizar(demonio);

        Set<String> habilidades = espirituService.recuperarHabilidadesDe(demonio.getId());

        assertTrue(habilidades.contains("Materializacion"));
        assertTrue(habilidades.contains("Mutacion"));
        assertTrue(habilidades.contains("Ilusion"));
        assertEquals(3, habilidades.size());
    }

    @Test
    void crearMongoEspiritu() {
        Espiritu demonio = new Espiritu.Builder()
                                .nombre("mago")
                                .ubicacion(new Ubicacion("cueva",TipoUbicacion.SANTUARIO,100))
                                .build();
        demonio.setCoordenada(new GeoJsonPoint(34.665,9.342));
        espirituMongoDAO.save(EspirituMongoDTO.desdeModelo(demonio));

    }
    @Test
    void recuperarConUbiOK(){

        Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion,100);
        Espiritu dominado = new Espiritu(TipoEspiritu.ANGEL, 30, "Dominado", ubicacion,100);
        GeoJsonPoint coordenada1 = new GeoJsonPoint(-73.968285, 40.785091);
        GeoJsonPoint coordenada2 = new GeoJsonPoint(-73.968281, 40.785093);
        dominante.setCoordenada(coordenada1);
        dominado.setCoordenada(coordenada2);
        espirituService.guardarConUbi(dominante);
        espirituService.guardarConUbi(dominado);

        Espiritu eact= espirituService.recuperarConUbi(dominante.getId());
        assertEquals(eact.getCoordenada().getX(),-73.968285);
    }
    @Test
    void espirituDominacionExitosa2() {
        Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion,100);
        Espiritu dominado = new Espiritu(TipoEspiritu.ANGEL, 30, "Dominado", ubicacion,40);
        GeoJsonPoint coordenada1 = new GeoJsonPoint(-73.9830773868208,40.76792653782013);
        GeoJsonPoint coordenada2 = new GeoJsonPoint(-73.96444062736852, 40.79230325236276);
        dominante.setCoordenada(coordenada1);
        dominado.setCoordenada(coordenada2);
        espirituService.guardarConUbi(dominante);
        espirituService.guardarConUbi(dominado);
        espirituService.dominar(dominante.getId(), dominado.getId());
        Espiritu dominadoActualizado = espirituService.recuperarConUbi(dominado.getId());

        Long duenio  = espirituDAO.getDominante(dominadoActualizado.getId());
        assertEquals(duenio,dominante.getId());
    }
    @Test
    void espirituDominacionFallaAlQuererDominarASuDominante() {
        Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion,49);
        Espiritu dominado = new Espiritu(TipoEspiritu.ANGEL, 30, "Dominado", ubicacion,40);
        GeoJsonPoint coordenada1 = new GeoJsonPoint(-73.9830773868208,40.76792653782013);
        GeoJsonPoint coordenada2 = new GeoJsonPoint(-73.96444062736852, 40.79230325236276);
        dominante.setCoordenada(coordenada1);
        dominado.setCoordenada(coordenada2);
        espirituService.guardarConUbi(dominante);
        espirituService.guardarConUbi(dominado);
        espirituService.dominar(dominante.getId(), dominado.getId());

        Assertions.assertThrows(DominacionImposibleException.class, () ->
                espirituService.dominar(dominado.getId(),dominante.getId())
        );
    }
    @Test
    void espirituDominacionFallidaDistanciaMayor5Km() {
        Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion, 100);
        Espiritu dominado = new Espiritu(TipoEspiritu.ANGEL, 30, "Dominado", ubicacion, 10);
        GeoJsonPoint coordenada1 = new GeoJsonPoint(-73.983077, 40.767926);
        GeoJsonPoint coordenada2 = new GeoJsonPoint(-4.023080, 50.804303);
        dominante.setCoordenada(coordenada1);
        dominado.setCoordenada(coordenada2);

        espirituService.guardarConUbi(dominante);
        espirituService.guardarConUbi(dominado);

        Assertions.assertThrows(DominacionInvalidoException.class, () ->
                espirituService.dominar(dominante.getId(), dominado.getId())
        );
    }

    @Test
    void espirituDominacionFallidaDistanciaMenor2Km() {
        Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion, 100);
        Espiritu dominado = new Espiritu(TipoEspiritu.ANGEL, 30, "Dominado", ubicacion, 40);
        GeoJsonPoint coordenada1 = new GeoJsonPoint(-73.968285, 40.785091);
        GeoJsonPoint coordenada2 = new GeoJsonPoint(-73.968281, 40.785093);
        dominante.setCoordenada(coordenada1);
        dominado.setCoordenada(coordenada2);

        espirituService.guardarConUbi(dominante);
        espirituService.guardarConUbi(dominado);

        Assertions.assertThrows(DominacionInvalidoException.class, () ->
                espirituService.dominar(dominante.getId(), dominado.getId())
        );
    }

    @Test
    void espirituDominacionFallidaDominanteNoExisteEnBase() {
        Espiritu dominado = new Espiritu(TipoEspiritu.ANGEL, 30, "Dominado", ubicacion, 100);
        GeoJsonPoint coordenadaDominado = new GeoJsonPoint(-73.968285, 40.785091);
        dominado.setCoordenada(coordenadaDominado);
        espirituService.guardarConUbi(dominado);

        Assertions.assertThrows(EntidadNoExisteEnBDException.class, () ->
                espirituService.dominar(999L, dominado.getId())
        );
    }

    @Test
    void espirituDominacionFallidaDominadoNoExisteEnBase() {
        Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion, 100);
        GeoJsonPoint coordenadaDominante = new GeoJsonPoint(-73.968285, 40.785091);
        dominante.setCoordenada(coordenadaDominante);
        espirituService.guardarConUbi(dominante);

        Assertions.assertThrows(EntidadNoExisteEnBDException.class, () ->
                espirituService.dominar(dominante.getId(), 999L)
        );
    }
    @Test
    void espirituDominacionFallidaEspirituYaDominado() {
        Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion, 100);
        Espiritu dominado = new Espiritu(TipoEspiritu.ANGEL, 30, "Dominado", ubicacion, 40);
        GeoJsonPoint coordenada1 = new GeoJsonPoint(-73.9830773868208,40.76792653782013);
        GeoJsonPoint coordenada2 = new GeoJsonPoint(-73.96444062736852, 40.79230325236276);
        dominante.setCoordenada(coordenada1);
        dominado.setCoordenada(coordenada2);

        dominado.setEnergia(30);

        espirituService.guardarConUbi(dominante);
        espirituService.guardarConUbi(dominado);

        espirituService.dominar(dominante.getId(), dominado.getId());

        Assertions.assertThrows(EspirituDominadoException.class, () ->
                espirituService.dominar(dominante.getId(), dominado.getId())
        );
    }
    @Test
    void espirituDominacionFallidaPorEnergia() {
        Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion, 100);
        Espiritu dominado = new Espiritu(TipoEspiritu.ANGEL, 50, "Dominado", ubicacion, 100);
        GeoJsonPoint coordenadaDominante = new GeoJsonPoint(-73.968285, 40.785091);
        GeoJsonPoint coordenadaDominado = new GeoJsonPoint(-73.968280, 40.785092);
        dominante.setCoordenada(coordenadaDominante);
        dominado.setCoordenada(coordenadaDominado);

        dominado.setEnergia(50);

        espirituService.guardarConUbi(dominante);
        espirituService.guardarConUbi(dominado);

        Assertions.assertThrows(DominacionInvalidoException.class, () ->
                espirituService.dominar(dominante.getId(), dominado.getId())
        );
    }
    @Test
    void masDeUnEspirituADistanciadeDominarPeroSolamenteSeleccionaALDominado(){

            Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion,100);
            Espiritu dominado = new Espiritu(TipoEspiritu.ANGEL, 30, "Dominado", ubicacion,40);
            Espiritu noDominado = new Espiritu(TipoEspiritu.ANGEL, 30, "noDominado", ubicacion,0);
        GeoJsonPoint coordenada1 = new GeoJsonPoint(-73.9830773868208,40.76792653782013);
        GeoJsonPoint coordenada2 = new GeoJsonPoint(-73.96444062736852, 40.79230325236276);
        GeoJsonPoint coordenada3 = new GeoJsonPoint(-73.9830773868208,40.76792653782013);
            dominante.setCoordenada(coordenada1);
            dominado.setCoordenada(coordenada2);
            noDominado.setCoordenada(coordenada3);
            espirituService.guardarConUbi(dominante);
            espirituService.guardarConUbi(dominado);
            espirituService.guardarConUbi(noDominado);
            espirituService.dominar(dominante.getId(), dominado.getId());
            Espiritu dominadoActualizado = espirituService.recuperarConUbi(dominado.getId());

            Long duenio  = espirituDAO.getDominante(dominadoActualizado.getId());
            assertEquals(duenio,dominante.getId());

    }

    @Test
    void espirituSinDominarSeConectaConMedium() {
        Espiritu espirituSinDominar = new Espiritu(TipoEspiritu.ANGEL, 30, "Sin Dominar", ubicacion,40);
        GeoJsonPoint coordenada2 = new GeoJsonPoint(-73.96444062736852, 40.79230325236276);
        espirituSinDominar.setCoordenada(coordenada2);
        espirituService.guardarConUbi(espirituSinDominar);
        mediumService.crear(maguito);

        List<Espiritu> espiritusDominantesDe = espirituService.recuperarDominantesDe_(espirituSinDominar.getId());

        assertTrue(espiritusDominantesDe.isEmpty());

    }


    @Test
    void espirituDominadoNoSePuedeConectarConMedium() {
        Espiritu dominante = new Espiritu(TipoEspiritu.DEMONIO, 100, "Dominante", ubicacion,100);
        Espiritu espirituSinDominar = new Espiritu(TipoEspiritu.ANGEL, 30, "Sin Dominar", ubicacion,40);

        GeoJsonPoint coordenada1 = new GeoJsonPoint(-73.9830773868208,40.76792653782013);
        GeoJsonPoint coordenada2 = new GeoJsonPoint(-73.96444062736852, 40.79230325236276);
        dominante.setCoordenada(coordenada1);
        espirituSinDominar.setCoordenada(coordenada2);
        espirituService.guardarConUbi(espirituSinDominar);
        espirituService.guardarConUbi(dominante);
        mediumService.crear(maguito);

        espirituService.dominar(dominante.getId(),espirituSinDominar.getId());

        assertThrows(EspirituDominadoException.class, ()->espirituService.conectar(espirituSinDominar.getId(), maguito.getId()));
    }

    @AfterEach
    void cleanup() {
        espirituDAO.deleteAll();
        mediumDao.deleteAll();
        ubicacionDAO.deleteAll();

        espirituMongoDAO.deleteAll();
        mediumMongoDAO.deleteAll();
        ubicacionMongoDAO.deleteAll();
    }
}
