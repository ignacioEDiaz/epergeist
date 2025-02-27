package ar.edu.unq.epersgeist.modelo;


import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReporteSantuarioMasCorrupto {

    private String nombreSantuario;
    private Medium mediumConMasDemonios;
    private int demoniosTotal;
    private int demoniosLibres;

    public ReporteSantuarioMasCorrupto(){
        this.nombreSantuario = null;
        this.mediumConMasDemonios = null;
        this.demoniosTotal = 0;
        this.demoniosLibres = 0;
    }


    public ReporteSantuarioMasCorrupto(String nombreSantuario, Medium mediumConMasDemonios,
                                       int demoniosTotal, int demoniosLibres) {

        this.nombreSantuario = nombreSantuario;
        this.mediumConMasDemonios = mediumConMasDemonios;
        this.demoniosTotal = demoniosTotal;
        this.demoniosLibres = demoniosLibres;
    }

    public Medium getMediumConMasDemonios() {
        return mediumConMasDemonios;
    }

    public void setMediumConMasDemonios(Medium mediumConMasDemonios) {
        this.mediumConMasDemonios = mediumConMasDemonios;
    }

    public String getNombreSantuario() {
        return nombreSantuario;
    }

    public void setNombreSantuario(String nombreSantuario) {
        this.nombreSantuario = nombreSantuario;
    }

    public int getDemoniosTotal() {
        return demoniosTotal;
    }

    public void setDemoniosTotal(int demoniosTotal) {
        this.demoniosTotal = demoniosTotal;
    }

    public int getDemoniosLibres() {
        return demoniosLibres;
    }

    public void setDemoniosLibres(int demoniosLibres) {
        this.demoniosLibres = demoniosLibres;
    }
}
