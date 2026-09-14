package jlti.app;

public class InputMode {
    /**
     * 1 OFF - off keyboard
     * <p>2 - main controller by goto is widget</p>
     * <p>3 - for a TEntry</p>
     * <p>4 - for a TEntryText</p>
     * <p>5 - for a TScorllBar</p>
     * <p>6 - for a TDropList</p>
     */
    public enum Modes {
        OFF(-1),
        InputMain(0),
        InputT1(1),
        InputT2(2),
        InputTSB(3),
        InputTDL(4);

        private final int out;
        Modes(int i) {
            this.out = i;
        }

        public int readInt() {
            return out;
        }

        public static Modes readM0(int i) {
            for (Modes ii: Modes.values()) {
                if (ii.readInt() == i) {
                    return ii;
                }
            }
            return null;
        }
    }

    public static boolean equalsMode(Modes i, Modes ii) {
        if (i.readInt() == ii.readInt()) {
            return true;
        }
        return false;
    }
}
