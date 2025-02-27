package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.SnapshotDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.exception.Response;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/estadistica")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    public EstadisticaController(EstadisticaService estadisticaService){
        this.estadisticaService = estadisticaService;
    }

    @PostMapping("/crear/snapshot")
    @Operation(summary = "Crea una Snapshot con fecha del dia actual.")
    public ResponseEntity<?> crearSnapshot(){
        this.estadisticaService.crearSnapshot();
        return ResponseEntity.status(HttpStatus.CREATED).body("La Snapshot fue creada con exito!");
    }

    @GetMapping("/recuperar/snapshot/{fecha}")
    @Operation(summary = "Recupera la Snapshot dada una fecha. Formato Fecha [yyyy-mm-dd]")
    public ResponseEntity<?> obtenerSnapshot(@PathVariable("fecha") LocalDate fecha){
        Snapshot snapshot = this.estadisticaService.obtenerSnapshot(fecha);
        if(snapshot == null || fecha == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("No existe un Snapshot con la fecha solicitada."));
        }
        return ResponseEntity.ok(SnapshotDTO.desdeModelo(snapshot));
    }

    @GetMapping("/espirituMasDominante/{fechaInicio}/{fechaFin}")
    @Operation(summary = "Recupera el espiritu con mayor espiritus dominados dentro de las dos fechas dadas.")
    public ResponseEntity<?> espirituMasDominante(@PathVariable("fechaInicio") LocalDate fechaInicio,@PathVariable("fechaFin") LocalDate fechaFin) {
        Espiritu espiritu = this.estadisticaService.espirituMasDominante(fechaInicio, fechaFin);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(espiritu));
    }

    @GetMapping("/ubicacionMasDominada/{fechaInicio}/{fechaFin}")
    @Operation(summary = "Obtiene la ubicacion con mayor diferencia de angeles dominados sobre demoniacos. Formato Fecha [yyyy-mm-dd]")
    public ResponseEntity<?> ubicacionMasDominada(@PathVariable("fechaInicio") LocalDate fechaInicio, @PathVariable("fechaFin") LocalDate fechaFin) {
        Ubicacion ubicacionMasDominada = estadisticaService.ubicacionMasDominada(fechaInicio, fechaFin);
        return ResponseEntity.ok(UbicacionDTO.desdeModelo(ubicacionMasDominada));
    }
}
