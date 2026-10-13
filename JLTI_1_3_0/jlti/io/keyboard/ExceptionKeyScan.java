package jlti.io.keyboard;

public class ExceptionKeyScan extends RuntimeException {
    public ExceptionKeyScan(int i) {
        super("\033[91m ERROR keyBoard scan2 args 1, all args " + i + "\033[0m");
        System.exit(0);
    }
}