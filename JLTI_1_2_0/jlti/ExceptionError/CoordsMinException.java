package jlti.ExceptionError;

public class CoordsMinException extends RuntimeException {
    public CoordsMinException() {
        super();
    }

    public CoordsMinException(String i) {
        super("\033[0m ERROR CoordsMinException IN");
    }
}