package ar.edu.unq.epersgeist.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int responseCode;
    private String message;
    private String descripcion;

    public ErrorResponse(int responseCode,String message, String descripcion) {
        this.responseCode = responseCode;
        this.message      = message;
        this.descripcion  = descripcion;
    }
}
