package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.CondicionDTO;
import ar.edu.unq.epersgeist.controller.dto.HabilidadDTO;
import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import ar.edu.unq.epersgeist.servicios.HabilidadService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/habilidad")
public class HabilidadController {

    private final HabilidadService habilidadService;

    public HabilidadController(HabilidadService habilidadService) {
        this.habilidadService = habilidadService;
    }

    @GetMapping("/todas")
    @Operation(summary = "Recupera todas las Habilidades creadas al momento.")
    public Set<HabilidadDTO> getAllAbilities(){
        return this.habilidadService.recuperarTodas().stream().map(HabilidadDTO::desdeModelo)
                .collect(Collectors.toSet());
    }


    @PostMapping
    @Operation(summary = "Crea una habilidad en el sistema")
    public ResponseEntity<?> crearHabilidad(@RequestBody HabilidadDTO habilidadDTO) {
        this.habilidadService.crear(habilidadDTO.aModelo());
        return ResponseEntity.status(HttpStatus.CREATED).body(habilidadDTO);
    }

    @PutMapping("/{nombreHabilidadOrigen}/descubre/{nombreHabilidadDestino}")
    @Operation(summary = "Crea una relacion entre 2 Habilidades")
    public ResponseEntity<?> descubrirHabilidad(@PathVariable String nombreHabilidadOrigen, @PathVariable String nombreHabilidadDestino, @RequestBody CondicionDTO condicionDTO) {
        this.habilidadService.descubrirHabilidad(nombreHabilidadOrigen, nombreHabilidadDestino, condicionDTO.aModelo());
        return ResponseEntity.status(HttpStatus.CREATED).body("Relacion Creada.");
    }

    @PatchMapping("/evolucionar/{id}")
    @Operation(summary = "Realiza una mutacion sobre un Espiritu ")
    public ResponseEntity<?> evolucionar(@PathVariable Long id) {
        this.habilidadService.evolucionar(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Mutacion realizada con Exito");
    }

    @GetMapping("/{habilidad}/conectadas")
    @Operation(summary = "Recupera todas las habilidades a las cuales puede mutar la habilidad")
    public Set<HabilidadDTO> habilidadesConectadas(@PathVariable String habilidad) {
        return this.habilidadService.habilidadesConectadas(habilidad).stream().map(HabilidadDTO::desdeModelo).collect(Collectors.toSet());
    }

    @GetMapping("/espiritu/{espirituId}/habilidadesPosibles")
    @Operation(summary = "Recupera todas las habilidades a las cuales puede mutar un espiritu")
    public ResponseEntity<Set<HabilidadDTO>> habilidadesPosibles(@PathVariable Long espirituId) {
        Set<Habilidad> hPosibles = habilidadService.habilidadesPosibles(espirituId);
        Set<HabilidadDTO> hPosiblesDTO = hPosibles.stream().map(HabilidadDTO::desdeModelo).collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK).body(hPosiblesDTO);
    }

    @GetMapping("/{origen}/camino/{destino}")
    @Operation(summary = "Recupera el camino de habilidades  mas corto,de la habilidad origen hasta la habilidad destino")
    public ResponseEntity<Set<HabilidadDTO>> caminoMasRentable(@PathVariable String origen,@PathVariable String destino,@RequestBody Set<Evaluacion> evaluaciones){
        List<Habilidad> camino = this.habilidadService.caminoMasRentable(origen,destino,evaluaciones);
        Set<HabilidadDTO> caminoDTO = camino.stream().map(HabilidadDTO::desdeModelo).collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK).body(caminoDTO);
    }

    @GetMapping("/{espirituId}/{nombreHabilidad}")
    @Operation(summary = "Devuelve el camino que mas mutaciones puede tomar el espiritu")
    public Set<HabilidadDTO> caminoMasMutable(@PathVariable Long espirituId, @PathVariable String nombreHabilidad) {
        return this.habilidadService.caminoMasMutable(espirituId, nombreHabilidad).stream()
                .map(HabilidadDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{espirituId}/caminoMenosMutable/{nombreHabilidad}")
    @Operation(summary = "Devuelve el camino que menoss puede tomar el espiritu")
    public Set<HabilidadDTO> caminoMenosMutable(@PathVariable Long espirituId, @PathVariable String nombreHabilidad) {
        return this.habilidadService.caminoMenosMutable(espirituId, nombreHabilidad).stream()
                .map(HabilidadDTO::desdeModelo)
                .collect(Collectors.toSet());
    }
}
