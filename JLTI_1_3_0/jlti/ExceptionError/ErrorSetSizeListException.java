package jlti.ExceptionError;

public class ErrorSetSizeListException extends RuntimeException {
    public ErrorSetSizeListException() {
        super("\033[HError set size list");
    }
}