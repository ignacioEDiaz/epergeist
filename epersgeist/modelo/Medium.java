package ar.edu.unq.epersgeist.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import ar.edu.unq.epersgeist.exception.*;
import ar.edu.unq.epersgeist.utility.PorcentajeAtaqueExitoso;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Medium implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false, length = 500,  unique = true)
    private String nombre;

    @OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @OrderBy("nombre ASC")
    private Set<Espiritu> espiritus = new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    private @NonNull Ubicacion ubicacion;

    private Integer manaMax;
    private Integer mana;

    @Transient private PorcentajeAtaqueExitoso prctAtckExitoso;
    @Transient private Integer puntosPorEspirituDominado;

    @Transient private GeoJsonPoint coordenada;

    public Medium(String nombre, Integer manaMax, Integer mana) {
        this.nombre = nombre;
        this.setManaMax(manaMax);
        this.setMana(mana);
    }

    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion) {
        if (ubicacion == null) {
            throw new UbicacionNulaException("La ubicación no puede ser nula");
        }
        this.nombre = nombre;
        this.setManaMax(manaMax);
        this.setMana(mana);
        this.ubicacion = ubicacion;
        this.prctAtckExitoso = null;
    }

    public Medium(String nombre, Integer manaMax) {
        this.nombre = nombre;
        this.setManaMax(manaMax);
    }

    public Set<Espiritu> getEspiritus(){
        return this.espiritus == null ? null : this.espiritus.stream().filter(e -> !e.isDeleted()).collect(Collectors.toSet());
    }

    public void setMana(Integer mana) {
        this.mana = Math.toIntExact(Math.clamp(mana, 0, this.manaMax));
    }

    public void setManaMax(Integer manaMax) {
        this.manaMax = manaMax >= 100 ? manaMax : 100;
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        if (!espiritu.estaLibre())
            throw new EspirituNoLibreException(espiritu);
        this.espiritus.add(espiritu);
        espiritu.conectarConMedium(this);
    }

    public void desconectarse(Espiritu espiritu) {
        this.espiritus.remove(espiritu);
    }


    public boolean checkearSiTieneAngeles() {
        return this.getEspiritus().stream().anyMatch(e -> e.getTipo() == TipoEspiritu.ANGEL && !e.isDeleted());
    }

    public Espiritu invocarEspiritu(Espiritu espiritu) {
        if (this.getMana() < 10) {
            return espiritu;
        }
        if (!espiritu.estaLibre()) {
            throw new EspirituNoLibreException(espiritu);
        }
        this.reducirMana(10);
        espiritu.serInvocadoPorMedium(this);
        return espiritu;
    }

    private void reducirMana(int manaAReducir) {
        this.setMana(this.getMana() - manaAReducir);
    }

    public boolean mismaUbicacion(Espiritu espiritu) {
        return espiritu.getUbicacion().equals(this.ubicacion);
    }

    public Integer getPorcentajeMana() {
        return (int) (this.mana * 0.20);
    }

    public void aumentarMana(Integer mana) {
        this.mana = (mana < 0)
                ? Math.max(this.mana + mana, 0)
                : Math.min(this.mana + mana, this.manaMax);
    }

    public void descansar() {
        if(ubicacion.getTipo() == TipoUbicacion.SANTUARIO){
            this.mana += sumarHastaManaMaxSantuario();
            aumentarEnergiaDelTipo();
        }

        if(ubicacion.getTipo() == TipoUbicacion.CEMENTERIO){
            this.mana += sumarHastaManaMaxCementerio();
            aumentarEnergiaDelTipo();
        }
    }

    private int sumarHastaManaMaxSantuario(){
        int energiaBonus = (int) (ubicacion.getEnergia() * (float) 1.5);
        return (this.mana + energiaBonus < this.manaMax) ? energiaBonus : manaMax;
    }

    private int sumarHastaManaMaxCementerio(){
        int energiaBonus = ubicacion.getEnergia() / 2;
        return (this.mana + energiaBonus < this.manaMax) ? energiaBonus : manaMax;
    }

    private void aumentarEnergiaDelTipo(){
        for (Espiritu dmn : this.espiritus){
                dmn.subirEnergiaPorUbicacion();
        }
    }

    public void setUbicacion(Ubicacion ubicacion) {
        if (ubicacion == null) {
            throw new UbicacionNulaException("La ubicación no puede ser nula");
        }
        this.ubicacion = ubicacion;
    }

    public void exorcizarA(Medium mediumAExorcizar) {
        List<Espiritu> angelesDelExorcista = this.listaEspiritusAngeles();
        List<Espiritu> demoniosDelExorcizado = mediumAExorcizar.listaEspiritusDemoniacos();
        for (Espiritu angelExorcista : angelesDelExorcista) {
            this.exorcizaALosDemonios(angelExorcista, demoniosDelExorcizado, mediumAExorcizar);
        }
    }

    private void exorcizaALosDemonios(Espiritu angelExorcista, List<Espiritu> demoniosDelExorcizado,Medium mediumAExorcizar) {
        for (Espiritu demonioAExorcizar : demoniosDelExorcizado) {
            this.elAngel_ExorcizaAlDemonio_(angelExorcista, demonioAExorcizar);
            mediumAExorcizar.checkearLiberacionDemoniaca(demonioAExorcizar);
        }
    }

    private void checkearLiberacionDemoniaca(Espiritu demonioAExorcizar) {
        if (demonioAExorcizar.getMedium() == null || demonioAExorcizar.getEnergia() == 0) {
            demonioAExorcizar.desconectarse();
        }
    }

    private boolean sePuedeExorcizar(Espiritu angelExorcista, Espiritu demonioAExorcizar) {
        return angelExorcista.getEnergia() >= 10 && demonioAExorcizar.getEnergia() > 0;
    }

    private void elAngel_ExorcizaAlDemonio_(Espiritu angelExorcista, Espiritu demonioAExorcizar) {
        while (this.sePuedeExorcizar(angelExorcista, demonioAExorcizar)) {
            angelExorcista.atacar(demonioAExorcizar, this.prctAtckExitoso);
        }
    }

    public List<Espiritu> listaEspiritusAngeles() {
        return this.espiritus.stream().filter(e -> e.getTipo() == TipoEspiritu.ANGEL).toList();
    }

    public List<Espiritu> listaEspiritusDemoniacos() {
        return this.espiritus.stream().filter(e -> e.getTipo() == TipoEspiritu.DEMONIO).toList();
    }

    public void mover(Ubicacion ubicacion) {
        if (this.getUbicacion().getNombre().equals(ubicacion.getNombre()))
            return;

        if (!todosSusEspiritusPuedenMoverseA(ubicacion))
            throw new MoverMediumException("Alguno de sus espiritus no posee energia suficiente");

        setUbicacion(ubicacion);
        this.getEspiritus().forEach(e -> e.mover(ubicacion));
    }

    private boolean todosSusEspiritusPuedenMoverseA(Ubicacion ubicacion) {
        return this.getEspiritus().stream().allMatch(e -> e.puedeMoverseA(ubicacion));
    }

    public boolean puedeInvocarA(Espiritu espiritu) {
        String tipo = espiritu.getTipo().toString();
        return this.ubicacion.permiteTipoDe(tipo);
    }

    public Espiritu obtenerEspirituPorNombre(String nombre){
        return this.getEspiritus().stream()
                   .filter(espiritu -> espiritu.getNombre().equals(nombre))
                   .findFirst()
                   .orElse(null);
    }

    @NoArgsConstructor
    public static class Builder {
        Medium medium = new Medium();

        public Medium.Builder id(Long id) {
            this.medium.setId(id);
            return this;
        }

        public Medium.Builder espiritus(Set<Espiritu> espiritus) {
            this.medium.setEspiritus(espiritus);
            return this;
        }

        public Medium.Builder mana(Integer mana) {
            this.medium.setMana(mana);
            return this;
        }

        public Medium.Builder manaMax(Integer manaMax) {
            this.medium.setManaMax(manaMax);
            return this;
        }

        public Medium.Builder ubicacion(Ubicacion ubicacion) {
            this.medium.setUbicacion(ubicacion);
            return this;
        }

        public Medium.Builder nombre(String nombre) {
            this.medium.setNombre(nombre);
            return this;
        }

        public Medium.Builder coordenada(GeoJsonPoint coordenada) {
            this.medium.setCoordenada(coordenada);
            return this;
        }

        public Medium build() {
            return medium;
        }
    }
}
