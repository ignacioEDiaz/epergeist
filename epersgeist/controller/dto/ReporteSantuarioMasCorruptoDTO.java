package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;

public record ReporteSantuarioMasCorruptoDTO(String nombreSantuario,
                                             Medium mediumConMasDemonios,
                                             int demoniosTotal,
                                             int demoniosLibres) {

    public static ReporteSantuarioMasCorruptoDTO desdeModelo(ReporteSantuarioMasCorrupto reporte){
        return new ReporteSantuarioMasCorruptoDTO(
                reporte.getNombreSantuario(),
                reporte.getMediumConMasDemonios(),
                reporte.getDemoniosTotal(),
                reporte.getDemoniosLibres()
        );
    }

    public ReporteSantuarioMasCorrupto aModelo(){
        return new ReporteSantuarioMasCorrupto(this.nombreSantuario, this.mediumConMasDemonios, this.demoniosTotal, this.demoniosLibres);
    }
}
