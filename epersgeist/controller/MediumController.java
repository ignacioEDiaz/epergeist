package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.CoordenadaDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumUpdateDTO;
import ar.edu.unq.epersgeist.exception.Response;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import ar.edu.unq.epersgeist.utility.PrtgAtckExitosoConcretoImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medium")
public class MediumController {
    private final MediumService mediumService;
    private final UbicacionService ubicacionService;

    public MediumController(MediumService mediumService, UbicacionService ubicacionService) {
        this.mediumService = mediumService;
        this.ubicacionService = ubicacionService;
    }

    @PutMapping("/descansar/{mediumId}")
    @Operation(summary = "Descansa el Medium y sus Espiritus")
    public ResponseEntity<?> descansar(@PathVariable Long mediumId) {
        this.mediumService.descansar(mediumId);
        return ResponseEntity.ok("Ejecucion exitosa");
    }

    @PutMapping("/{mediumId}/invocar/{espirituId}")
    @Operation(summary = "Invoca un Espiritu a la ubicacion del Medium")
    public ResponseEntity<?> invocar(@PathVariable Long mediumId,@PathVariable Long espirituId){
        Espiritu espiritu = this.mediumService.invocar(mediumId,espirituId);
        return ResponseEntity.status(HttpStatus.OK).body(EspirituDTO.desdeModelo(espiritu));
    }

    @PutMapping("/{mediumId}/mover")
    @Operation(summary = "Establece la ubicacion a un medium y todos sus espiritus")
    public ResponseEntity<?> mover(@PathVariable Long mediumId,@RequestBody CoordenadaDTO coordenadaDTO) {
        this.mediumService.mover(mediumId, coordenadaDTO.getLongitud(),  coordenadaDTO.getLatitud());
        return ResponseEntity.status(HttpStatus.OK).body(MediumDTO.desdeModelo(mediumService.recuperar(mediumId)));
    }

    @PostMapping
    @Operation(summary = "Crea un medium en el sistema")
    public ResponseEntity<?> saveMedium(@RequestBody MediumDTO mediumDTO) {
        Medium nuevoMedium = mediumDTO.aModelo();
        this.mediumService.crear(nuevoMedium,mediumDTO.ubicacionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(MediumDTO.desdeModelo(nuevoMedium));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recupera un Medium del sistema")
    public ResponseEntity<MediumDTO> recuperarMedium(@PathVariable Long id) {
        Medium medium = this.mediumService.recuperar(id);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un Medium del sistema")
    public ResponseEntity<?> actualizarMedium(@RequestBody MediumUpdateDTO mediumUpdateDTO, @PathVariable Long id) {
        if (mediumUpdateDTO.nombre() == null || mediumUpdateDTO.manaMax() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Faltan campos obligatorios"));
        }
        Medium medium = mediumUpdateDTO.aModelo();
        this.mediumService.actualizar(medium,id);
        return ResponseEntity.status(HttpStatus.OK).body(MediumDTO.desdeModelo(medium));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un medium del sistema")
    public void eliminarMedium(@PathVariable Long id) {
        this.mediumService.eliminar(id);
    }

    @GetMapping
    @Operation(summary = "Recupera todos los mediums del sistema")
    public Set<MediumDTO> recuperarTodos() {
        return this.mediumService.recuperarTodos().stream()
                .map(MediumDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}/espiritus")
    @Operation(summary = "Recupera todos los espiritus de un medium")
    public Set<EspirituDTO> espiritus(@PathVariable Long id) {
        return this.mediumService.espiritus(id).stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @PutMapping("/{mediumExorcistaId}/exorcisa/{mediumAExorcisarId}")
    @Operation(summary = "Un medium exorcisa a otro")
    public ResponseEntity<?> exorcizar(@PathVariable Long mediumExorcistaId,@PathVariable Long mediumAExorcisarId) {
        this.mediumService.exorcizar(mediumExorcistaId,mediumAExorcisarId,new PrtgAtckExitosoConcretoImpl());
        return ResponseEntity.ok("Exorcismo realizado");
    }

}

