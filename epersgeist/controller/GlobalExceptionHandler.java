package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HabilidadNoSeRelacionaConsigoMismaException.class)
    public ResponseEntity<?> handlerHabilidadNoSeRelacionaConsigoMismaExceptionException(HabilidadNoSeRelacionaConsigoMismaException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExisteEntidadConMismoNombreException.class)
    public ResponseEntity<?> handlerExisteEntidadConMismoNombreException(ExisteEntidadConMismoNombreException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHayUbicacionesMasCorruptas.class)
    public ResponseEntity<?> handlerNoHayUbicacionesMasCorruptasException(NoHayUbicacionesMasCorruptas e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntidadNoExisteEnBDException.class)
    public ResponseEntity<?> handlerEntidadNoExisteEnBDException(EntidadNoExisteEnBDException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.NOT_FOUND.value(),e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NumeroDePaginaMenorACeroException.class)
    public ResponseEntity<?> handlerNumeroDePaginaMenorACeroException(NumeroDePaginaMenorACeroException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CantidadPorPaginaMenorACeroException.class)
    public ResponseEntity<?> handlerCantidadPorPaginaMenorACeroException(CantidadPorPaginaMenorACeroException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UbicacionNulaException.class)
    public ResponseEntity<?> handlerUbicacionNulaException(UbicacionNulaException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntidadNombreInvalidoException.class)
    public ResponseEntity<?> handlerEntidadNombreInvalidoException(EntidadNombreInvalidoException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DistintaUbicacionException.class)
    public ResponseEntity<?> handlerDistintaUbicacionException(DistintaUbicacionException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HabilidadesNoConectadasException.class)
    public ResponseEntity<?> handlerHabilidadesNoConectadasException(HabilidadesNoConectadasException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MutacionImposibleException.class)
    public ResponseEntity<?> handlerMutacionImposibleException(MutacionImposibleException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DominacionInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleDominacionInvalidoException(DominacionInvalidoException e, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EspirituDominadoException.class)
    public ResponseEntity<ErrorResponse> handleEspirituDominadoException(EspirituDominadoException e, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FechasIncorrectasException.class)
    public ResponseEntity<ErrorResponse> handleFechasIncorrectasException(FechasIncorrectasException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RegistroMasDominanteException.class)
    public ResponseEntity<ErrorResponse> handleRegistroMasDominanteException(RegistroMasDominanteException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
