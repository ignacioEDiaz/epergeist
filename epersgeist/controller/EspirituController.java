package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.exception.*;
import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/espiritu")
public class EspirituController {
    private final EspirituService espirituService;
    private final UbicacionService ubicacionService;

    public EspirituController(EspirituService espirituService, UbicacionService ubicacionService) {
        this.espirituService = espirituService;
        this.ubicacionService = ubicacionService;
    }

    @PostMapping
    @Operation(summary = "Crea un espiritu en el sistema")
    public ResponseEntity<?> saveEspiritu(@RequestBody EspirituDTO espirituDTO) {
        Espiritu nuevoEspiritu = espirituDTO.aModeloCreate();
        this.espirituService.crear(nuevoEspiritu,espirituDTO.ubicacionId());

        return ResponseEntity.status(HttpStatus.CREATED).body(EspirituDTO.desdeModelo(nuevoEspiritu));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recupera un espiritu del sistema")
    public ResponseEntity<EspirituDTO> getEspirituById(@PathVariable Long id) {
        Espiritu espiritu   = this.espirituService.recuperar(id);

        return ResponseEntity.ok(EspirituDTO.desdeModelo(espiritu));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un espiritu del sistema")
    public ResponseEntity<?>  updateEspiritu(@PathVariable Long id, @RequestBody EspirituDTO espirituDTO) {
        if (espirituDTO.nombre() == null || espirituDTO.tipo() == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Faltan campos obligatorios"));
        }

        Espiritu espirituActualizar = espirituDTO.aModeloUpdate(id);
        this.espirituService.actualizarController(espirituActualizar);
        return ResponseEntity.status(HttpStatus.OK).body(espirituActualizar);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un espiritu del sistema")
    public void deleteEspiritu(@PathVariable Long id) {
        this.espirituService.eliminar(id);
    }

    @GetMapping
    @Operation(summary = "Recupera todos los espiritus en el sistema")
    public Set<EspirituDTO> getAllEspiritus() {
        return this.espirituService.recuperarTodos().stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @PutMapping("/{espirituId}/conectar/{mediumId}")
    @Operation(summary = "Conecta un espiritu con un Medium")
    public ResponseEntity<String> conectar(@PathVariable Long espirituId, @PathVariable Long mediumId) {
        this.espirituService.conectar(espirituId, mediumId);
        return ResponseEntity.ok("Conexión exitosa");
    }

    @GetMapping("/demoniacos")
    @Operation(summary = "Recupera todos los espiritus tipo Demonio del sistema")
    public List<EspirituDTO> espiritusDemoniacos() {
        return this.espirituService.espiritusDemoniacos().stream()
                .map(EspirituDTO::desdeModelo)
                .toList();
    }

    @GetMapping("/obtener/demoniacos")
    @Operation(summary = "Recupera todos los espiritus tipo Demonio del sistema usando un paginado")
    public ResponseEntity<?> obtenerEspiritusDemoniacos(@RequestParam Direccion direccion, @RequestParam int pagina, @RequestParam int cantidadPorPagina) {
        return ResponseEntity.status(HttpStatus.OK).body(espirituService.espiritusDemoniacos(direccion, pagina - 1, cantidadPorPagina).stream()
                .map(EspirituDTO::desdeModelo)
                .toList());
    }
    @PutMapping("/{dominanteId}/dominar/{dominadoId}")
    @Operation(summary= "El espíritu dominante agrega a sus dominados el espíritu a dominar")
    public ResponseEntity<String> dominar(@PathVariable Long dominanteId, @PathVariable Long dominadoId) {
        espirituService.dominar(dominanteId, dominadoId);

        return ResponseEntity.ok("la dominacion ha sido exitosa");
    }
}

