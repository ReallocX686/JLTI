package jlti.ExceptionError;

public class MaxLenTextException extends RuntimeException {
    public MaxLenTextException() {
        super();
    }

    public MaxLenTextException(String text) {
        super("\033[0m" + text);
    }
}