package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.ReporteSantuarioMasCorruptoDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.exception.Response;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/ubicacion")
public class UbicacionController {
    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService){ this.ubicacionService = ubicacionService; }

    @GetMapping("/{id}")
    @Operation(summary = "Recupera una Ubicacion del sistema")
    public ResponseEntity<UbicacionDTO> getUbicacionById(@PathVariable Long id){
        Ubicacion ubicacion = this.ubicacionService.recuperar(id);
        return ResponseEntity.ok(UbicacionDTO.desdeModelo(ubicacion));
    }

    @GetMapping("/todos")
    @Operation(summary = "Recupera todas las Ubicaciones del sistema")
    public Set<UbicacionDTO> getAllUbicaciones(){
        return this.ubicacionService.recuperarTodos().stream()
                .map(UbicacionDTO::desdeModelo).collect(Collectors.toSet());
    }

    @PostMapping
    @Operation(summary = "Crea una Ubicacion en el sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUbicacion(@RequestBody UbicacionDTO ubicacionDTO){
        this.ubicacionService.crear(ubicacionDTO.aModelo());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una Ubicacion en el sistema")
    public ResponseEntity<?> updateUbicacion(@PathVariable Long id, @RequestBody UbicacionDTO newDataUbic) {
        if(newDataUbic.nombre() == null || newDataUbic.tipoUbicacion() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Faltan campos obligatorios"));
        }
        Ubicacion ubicacion = newDataUbic.aModeloUpdate();

        this.ubicacionService.actualizar(ubicacion,id);
        return ResponseEntity.status(HttpStatus.OK).body(ubicacion);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una Ubicacion del sistema")
    public void deleteUbicacion(@PathVariable Long id){
        this.ubicacionService.eliminar(id);
    }

    @GetMapping("/{id}/espiritus")
    @Operation(summary = "Recupera los Espiritus del Sistema en la Ubicacion")
    public Set<EspirituDTO> espiritusEn(@PathVariable Long id){
        return this.ubicacionService.espiritusEn(id).stream().map(EspirituDTO::desdeModelo).collect(Collectors.toSet());
    }

    @GetMapping("/{id}/mediumSolos")
    @Operation(summary = "Recupera los Medium solos del Sistema en la Ubicacion")
    public Set<MediumDTO> mediumSinEspiritusEn(@PathVariable Long id){
        return this.ubicacionService.mediumsSinEspiritusEn(id).stream().map(MediumDTO::desdeModelo).collect(Collectors.toSet());
    }

    @GetMapping()
    @Operation(summary = "Recupera reporte del santuario mas corrupto")
    public ResponseEntity<?> reporteSantuarioMasCorrupto(){
        ReporteSantuarioMasCorrupto ubiCorrupta = this.ubicacionService.santuarioCorrupto();
        return ResponseEntity.ok(ReporteSantuarioMasCorruptoDTO.desdeModelo(ubiCorrupta));
    }
}

