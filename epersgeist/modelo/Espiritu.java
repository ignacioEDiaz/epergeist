package ar.edu.unq.epersgeist.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ar.edu.unq.epersgeist.utility.PorcentajeAtaqueExitoso;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.edu.unq.epersgeist.exception.UbicacionNulaException;
import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Getter @Setter @NoArgsConstructor @ToString
@Entity
public class Espiritu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoEspiritu tipo;

    @Column(nullable = false, length = 500, unique = true)
    private String nombre;

    @Column(nullable = false)
    private Integer energia;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name="medium_id")
    private Medium medium;

    @ManyToOne
    @JoinColumn(name="ubicacion_id")
    @ToString.Exclude
    @NonNull
    private Ubicacion ubicacion;

    private Integer nivelDeConexion;

    @ToString.Exclude
    @NonNull
    private int exorcismosResueltos;

    @ToString.Exclude
    @NonNull
    private int exorcismosEvitados;

    @ElementCollection(fetch = FetchType.EAGER)
    @NonNull
    private Set<String> habilidades = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @NonNull
    private Set<Long> dominados = new HashSet<>();

    @Transient private GeoJsonPoint coordenada;
    @Transient Integer bonificacion = 0;
    @Transient private List<Espiritu> espiritusDominados = new LinkedList<>();

    public Espiritu(String nombre, TipoEspiritu tipo,Ubicacion ubicacion) {
        this.nombre          = nombre;
        this.tipo            = tipo;
        this.ubicacion       = validarUbicacion(ubicacion);
        this.energia         = 1;
        this.nivelDeConexion = 0;
        this.deleted         = false;
        this.habilidades.add("Materializacion");
    }

    public Espiritu(String nombre, TipoEspiritu tipo) {
        this.nombre          = nombre;
        this.tipo            = tipo;
        this.ubicacion       = validarUbicacion(ubicacion);
        this.energia         = 1;
        this.nivelDeConexion = 0;
        this.deleted         = false;
        this.habilidades.add("Materializacion");
    }

    public Espiritu( String nombre,TipoEspiritu tipo, Ubicacion ubicacion, Integer energia) {
        this.nombre          = nombre;
        this.tipo            = tipo;
        this.ubicacion       = validarUbicacion(ubicacion);
        this.nivelDeConexion = 0;
        this.deleted         = false;
        this.habilidades.add("Materializacion");

        this.setEnergia(energia);
    }

    public Espiritu(TipoEspiritu tipo, Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        this.tipo      = tipo;
        this.nombre    = nombre;
        this.ubicacion = validarUbicacion(ubicacion);
        this.energia   = 1;
        this.deleted   = false;
        this.habilidades.add("Materializacion");

        this.setNivelDeConexion(nivelDeConexion);
    }

    public Espiritu(TipoEspiritu tipo, Integer nivelDeConexion, String nombre, Ubicacion ubicacion, Integer energia) {
        this.tipo      = tipo;
        this.nombre    = nombre;
        this.ubicacion = validarUbicacion(ubicacion);
        this.deleted   = false;
        this.habilidades.add("Materializacion");

        this.setEnergia(energia);
        this.setNivelDeConexion(nivelDeConexion);
    }

    public Espiritu(String nombre, TipoEspiritu tipo, int energia, int nivelDeConexion) {
        this.tipo      = tipo;
        this.nombre    = nombre;
        this.energia   = energia;
        this.deleted   = false;
        this.habilidades.add("Materializacion");

        this.setNivelDeConexion(nivelDeConexion);
    }

    public Espiritu(String nombre, int energia, int nivelDeConexion,TipoEspiritu tipo,int exorcismosResueltos,int exorcismosEvitados) {
        this.nombre              = nombre;
        this.energia             = energia;
        this.nivelDeConexion     = nivelDeConexion;
        this.tipo                = tipo;
        this.exorcismosResueltos = exorcismosResueltos;
        this.exorcismosEvitados  = exorcismosEvitados;
        this.deleted             = false;
        this.habilidades.add("Materializacion");
    }

    public void aumentarConexion(Medium medium) {
        Integer nivelConexionActual = this.nivelDeConexion + medium.getPorcentajeMana();
        this.setNivelDeConexion(nivelConexionActual);
    }

    public void conectarConMedium(Medium medium){
        this.medium = medium;
    }

    public void atacar(Espiritu demonioAExorcizar, PorcentajeAtaqueExitoso ataque){
        if(this.esAtaqueExitoso(ataque)){
            this.energia -=10;
            demonioAExorcizar.recibirAtaque(this);
            this.exorcismosResueltos += 1;
        }
        else {
            this.energia -= 10;
            demonioAExorcizar.incExorcismosEvitados();
        }
    }

    private void incExorcismosEvitados(){
        this.exorcismosEvitados += 1;
    }

    private boolean esAtaqueExitoso(PorcentajeAtaqueExitoso ataque) {
        return (this.energia>=10 && ataque.porcentajeAtaqueExitoso(nivelDeConexion,this.bonificacion) > 66);

    }

    public void recibirAtaque(Espiritu angel){
        int danio = angel.getNivelDeConexion() / 2;
        this.energia = Math.max(this.energia - danio, 0);

    }

    public void desconectarse() {
            nivelDeConexion = 0;
            energia = 0;
            deleted = true;
    }

    public boolean estaLibre() {
        return this.medium == null;
    }

    public void serInvocadoPorMedium(Medium medium) {
        this.setUbicacion(medium.getUbicacion());
    }

    public void setNivelDeConexion(Integer nivelDeConexion){
        this.nivelDeConexion = Math.max(0, Math.min(nivelDeConexion, 100));
    }

    public void setEnergia(Integer energia){
        this.energia = Math.max(0, Math.min(energia, 100));
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = validarUbicacion(ubicacion);
    }

    private Ubicacion validarUbicacion(Ubicacion ubicacion){
        if (ubicacion == null)
            throw new UbicacionNulaException("La ubicación no puede ser nula");
        return  ubicacion;
    }

    public void mover(Ubicacion ubicacion) {
        this.setUbicacion(ubicacion);
        this.disminuirEnergiaPorMovimiento(ubicacion);
    }

    private void disminuirEnergiaPorMovimiento(Ubicacion ubicacion) {
        if (this.esUnSantuarioYSoyUnDemonio(ubicacion)) {
            this.disminuirEnergia(10);
        } else if (this.esUnCementerioYSoyUnAngel(ubicacion)) {
            this.disminuirEnergia(5);
        }
    }

    public boolean puedeMoverseA(Ubicacion ubicacion) {
        if (this.esUnSantuarioYSoyUnDemonio(ubicacion)) {
            return this.getEnergia() >= 10;
        } else if (this.esUnCementerioYSoyUnAngel(ubicacion)) {
            return this.getEnergia() >= 5;
        }
        return this.getEnergia() >= 0;
    }

    private boolean esUnSantuarioYSoyUnDemonio(Ubicacion ubicacion){
        return ubicacion.getTipo() == TipoUbicacion.SANTUARIO && this.getTipo() == TipoEspiritu.DEMONIO;
    }

    private boolean esUnCementerioYSoyUnAngel(Ubicacion ubicacion){
        return ubicacion.getTipo() == TipoUbicacion.CEMENTERIO && this.getTipo() == TipoEspiritu.ANGEL;
    }

    private void disminuirEnergia(Integer cantidad) {
        this.energia -= cantidad;
    }

    public void subirEnergiaPorUbicacion() {
        if(this.tipo.equals(TipoEspiritu.ANGEL) && this.ubicacion.getTipo().equals(TipoUbicacion.SANTUARIO))
            this.setEnergia(this.getEnergia() + this.getUbicacion().getEnergia());

        if(this.tipo.equals(TipoEspiritu.DEMONIO) && this.ubicacion.getTipo().equals(TipoUbicacion.CEMENTERIO))
            this.setEnergia(this.getEnergia() + this.getUbicacion().getEnergia());
    }

    public void mutar(Set<Habilidad> habilidadesAMutar) {
        for (Habilidad h : habilidadesAMutar) {
            this.habilidades.add(h.getNombre());
        }
    }

    public void setHabilidades(String newHabilidad){
        habilidades.add(newHabilidad);
    }

    public void addDominado(Long espirituId){
        this.dominados.add(espirituId);
    }

    public void addEspirituDominado(Espiritu espiritu){
        this.espiritusDominados.add(espiritu);
    }

    @NoArgsConstructor
    public static class Builder {
        Espiritu espiritu = new Espiritu();

        public Builder id(Long id) {
            espiritu.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            espiritu.nombre = nombre;
            return this;
        }

        public Builder energia(int energia) {
            espiritu.energia = energia;
            return this;
        }

        public Builder nivelDeConexion(int nivelDeConexion) {
            espiritu.nivelDeConexion = nivelDeConexion;
            return this;
        }

        public Builder tipo(TipoEspiritu tipo) {
            espiritu.tipo = tipo;
            return this;
        }

        public Builder deleted(boolean deleted) {
            espiritu.deleted = deleted;
            return this;
        }

        public Builder habilidades(Set<String> habilidades) {
            espiritu.habilidades.addAll(habilidades);
            return this;
        }

        public Builder exorcismosResueltos(int exorcismosResueltos) {
            espiritu.exorcismosResueltos = exorcismosResueltos;
            return this;
        }

        public Builder exorcismosEvitados(int exorcismosEvitados) {
            espiritu.exorcismosEvitados = exorcismosEvitados;
            return this;
        }

        public Builder medium(Medium medium) {
            espiritu.medium = medium;
            return this;
        }

        public Builder ubicacion(Ubicacion ubicacion) {
            espiritu.ubicacion = ubicacion;
            return this;
        }

        public Builder coordenada(GeoJsonPoint coordenada) {
            espiritu.coordenada = coordenada;
            return this;
        }

        public Builder dominados(Set<Long> dominados) {
            espiritu.dominados = dominados;
            return this;
        }

        public Espiritu build() {
            espiritu.getHabilidades().add("Materializacion");
            return espiritu;
        }
    }


}