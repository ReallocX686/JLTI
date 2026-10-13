package jlti.io.keyboardOutput;

public interface keyboardListener {
    void PressedDoView(int[] code);
    void PressedEndView(int[] code);
    void WhileDoView(int[] code);
    void WhileEndView(int[] code);
}