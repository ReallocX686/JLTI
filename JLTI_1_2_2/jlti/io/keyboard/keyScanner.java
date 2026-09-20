package jlti.io.keyboard;

import org.jline.utils.NonBlockingReader;

public class keyScanner {
    public static boolean keyScanAnd2(NonBlockingReader reader, int keyPressedInt, key i) throws Exception {
        try {
            switch (i.outInt()) {
                case 49:
                case 50:
                    StringBuilder sb = new StringBuilder();
                    sb.append((char)keyPressedInt);
                    int next;
                    while ((next = reader.read(50)) != 126 && next > 0) {
                        sb.append((char)next);
                    }
                    String seq = sb.toString();
                    if (Integer.valueOf(seq) == i.outInt2()) {
                        return true;
                    }
                    break;
            }
        } catch (Exception e) {
            
        }
        return false;
    }

    public static boolean keyScan2(int keyPressedInt, key i) throws ExceptionKeyScan {
        if (i.outInt() == i.outInt2()) {
            throw new ExceptionKeyScan(2);
        } else {
            if (keyPressedInt == i.outInt() || keyPressedInt == i.outInt2()) {
                return true;
            }
        }
        return false;
    }

    public static boolean keyScan(int keyPressedInt, key i) {
        if (keyPressedInt == i.outInt()) {
            return true;
        }
        return false;
    }
}