package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.EntidadNombreInvalidoException;
import ar.edu.unq.epersgeist.exception.ExcesoDeEnergiaPermitidaException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

@Getter @Setter @NoArgsConstructor @ToString
@Entity @EqualsAndHashCode
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false, length = 500, unique = true)
    @Setter(AccessLevel.NONE)
    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoUbicacion tipo;

    @Column(nullable = false)
    private Integer energia;

    @Transient private GeoJsonPolygon coordenada;

    public Ubicacion(String nombre) {
        this.nombre = nombreValido(nombre);
    }

    public Ubicacion(String nombre, Integer energia) {
        this.nombre = nombreValido(nombre);
        this.energia = verificarEnergia(energia);
    }

    public Ubicacion(String nombre, TipoUbicacion tipoUbicacion, Integer energia) {
        this.nombre = nombreValido(nombre);
        this.tipo = tipoUbicacion;
        this.energia = verificarEnergia(energia);
    }

    public void setNombre(String nombre){
        this.nombre = nombreValido(nombre);
    }

    public void setEnergia(int energia) {this.energia = verificarEnergia(energia); }

    private String nombreValido(String nombre){
        if (nombre == null || nombre.isEmpty() || nombre.isBlank()){
            throw new EntidadNombreInvalidoException("Entidad " + this.getClass().getSimpleName() + " nombre vacio o nulo");
        }
        return nombre;
    }

    public boolean permiteTipoDe(String tipo) {
        return this.tipoPermitido().equals(tipo);
    }

    private String tipoPermitido(){
        if(this.tipo.equals(TipoUbicacion.SANTUARIO)){
            return "ANGEL";
        }else{ return "DEMONIO";}
    }

    private int verificarEnergia(int energia){
        if(energia >= 0 && energia <= 100){
            return energia;
        }
        else {
            throw new ExcesoDeEnergiaPermitidaException(energia);
        }
    }

    @NoArgsConstructor
    public static class Builder {
        Ubicacion ubicacion = new Ubicacion();

        public Ubicacion.Builder id(Long id) {
            this.ubicacion.setId(id);
            return this;
        }

        public Ubicacion.Builder nombre(String nombre) {
            this.ubicacion.setNombre(nombre);
            return this;
        }

        public Ubicacion.Builder energia(Integer energia) {
            this.ubicacion.setEnergia(energia);
            return this;
        }

        public Ubicacion.Builder tipo(TipoUbicacion tipo) {
            this.ubicacion.setTipo(tipo);
            return this;
        }

        public Ubicacion.Builder coordenada(GeoJsonPolygon  coordenada) {
            this.ubicacion.setCoordenada(coordenada);
            return this;
        }

        public Ubicacion build() {
            return ubicacion;
        }
    }
}
