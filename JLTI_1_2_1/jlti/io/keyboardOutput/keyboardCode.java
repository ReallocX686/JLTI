package jlti.io.keyboardOutput;

/**
 * The class ansi code the keyboard
 */
public class keyboardCode {
    public final static int[] BACKSPACE = {127};
    public final static int[] TAB       = {9};
    public final static int[] ENTER     = {13};
    public final static int[] SPACE     = {32};
    public final static int[] ESC       = {27};

    public final static int[] UP        = {27, 91, 65};
    public final static int[] DOWN      = {27, 91, 66};
    public final static int[] RIGHT     = {27, 91, 67};
    public final static int[] LEFT      = {27, 91, 68};

    public static int[] HOME;
    public static int[] END;

    static {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            HOME = new int[] {27, 91, 49, 126};
            END  = new int[] {27, 91, 52, 126};
        } else {
            HOME = new int[] {27, 91, 72};
            END  = new int[] {27, 91, 70};
        }
    }

    public final static int[] INSERT    = {27, 91, 50, 126};
    public final static int[] DELETE    = {27, 91, 51, 126};
    public final static int[] PAGE_UP   = {27, 91, 53, 126};
    public final static int[] PAGE_DOWN = {27, 91, 54, 126};

    public final static int[] F1        = {27, 79, 80};
    public final static int[] F2        = {27, 79, 81};
    public final static int[] F3        = {27, 79, 82};
    public final static int[] F4        = {27, 79, 83};
    public final static int[] F5        = {27, 91, 49, 53, 126};
    public final static int[] F6        = {27, 91, 49, 55, 126};
    public final static int[] F7        = {27, 91, 49, 56, 126};
    public final static int[] F8        = {27, 91, 49, 57, 126};
    public final static int[] F9        = {27, 91, 50, 48, 126};
    public final static int[] F10       = {27, 91, 50, 49, 126};
    public final static int[] F11       = {27, 91, 50, 51, 126};
    public final static int[] F12       = {27, 91, 50, 52, 126};

    /**
     * The function mixChar
     * <p>mixChar(setChar('F'), ...)</p>
     * @param arrays
     * @return list the key
     */
    public static int[] mixChar(int[]... arrays) {
        int size = 0;
        for (int i = 0; i < arrays.length; i++) {
            size = size + arrays[i].length;
        }
        int[] out = new int[size];
        int outi = 0;

        for (int i = 0; i < arrays.length; i++) {
            for (int ii = 0; ii < arrays[i].length; ii++) {
                out[outi] = arrays[i][ii];
                outi++;
            }
        }

        return out;
    }

    /**
     * the function for set char in list the key
     * <p>setChar('F')</p>
     * @param i
     * @return
     */
    public static int[] setChar(char i) {
        return new int[] {(int)i};
    }

    /**
     * the function is operator key buttons
     * <p>equals(key, setChar('F'))</p>
     * <p>equals(key, mixChar(TAB, setChar('F')))</p>
     * @param ic
     * @param code
     * @return true/false
     */
    public static boolean equals(int[] ic, int[] code) {
        final int size = ic.length;
        final int sizeCode = code.length;

        boolean out = false;
        for (int i = 0; i < size; i++) {
            if (i < sizeCode) {
                if (ic[i] == code[i] && (ic[i] != 0 && code[i] != 0)) {
                    out = true;
                } else {
                    out = false;
                }
            } else {
                break;
            }
        }
        return out;
    }
}