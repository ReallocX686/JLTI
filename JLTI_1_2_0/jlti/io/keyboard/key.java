package jlti.io.keyboard;

public enum key {
    VK2_NONE(-1, -2),
    
    VK_ENTER(13),
    VK_ESC(27),
    VK2_BACKSPACE(8, 127),
    VK_TAB(9),

    VK2_ESC_CODE2(79, 91),
    VK_ESC_CODE2_HOME(72),
    VK_ESC_CODE2_END(52),
    VK_ESC_CODE2_LEFT(68),
    VK_ESC_CODE2_RIGHT(67),
    VK_ESC_CODE2_DOWN(66),
    VK_ESC_CODE2_UP(65),
    
    VK_ESC_CODE2_F1(80),
    VK_ESC_CODE2_F2(81),
    VK_ESC_CODE2_F3(82),
    VK_ESC_CODE2_F4(83),
    VK2_ESC_CODE2_F5(49, 15),
    VK2_ESC_CODE2_F6(49, 17),
    VK2_ESC_CODE2_F7(49, 18),
    VK2_ESC_CODE2_F8(49, 19),
    VK2_ESC_CODE2_F9(49, 20),
    VK2_ESC_CODE2_F10(49, 21),
    VK2_ESC_CODE2_F11(49, 23),
    VK2_ESC_CODE2_F12(49, 24);

    private final int out;
    private final int out_2;
    private final int out_3;

    private key(int i) {
        this.out = i;
        this.out_2 = i;
        this.out_3 = i;
    }
    
    private key(int i, int ii) {
        this.out = i;
        this.out_2 = ii;
        this.out_3 = ii;
    }

    private key(int i, int ii, int iii) {
        this.out = i;
        this.out_2 = ii;
        this.out_3 = iii;
    }

    public int outInt() {
        return out;
    }

    public int outInt2() {
        return out_2;
    }

    public int outInt3() {
        return out_3;
    }
}