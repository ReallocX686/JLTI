package jlti.util.Color;

import jlti.util.Color.ConvertColor;

public enum colorANSI {
    BLACK(0),
    RED(1),
    GREEN(2),
    YELLOW(3),
    BLUE(4),
    PURPLE(5),
    LIGHT_BLUE(6),
    WHITE(7),
    customColor(0) {
        @Override
        public colorANSI set(int i) {
            this.color = i;
            return this;
        }

        @Override
        public colorANSI setRGB(int r, int g, int b) {
            this.color =  ConvertColor.convertRGBinANSI(r, g, b);
            return this;
        }
    };

    public colorANSI set(int i) {
        return this;
    }

    public colorANSI setRGB(int r, int g, int b) {
        return this;
    }

    protected int color;
    colorANSI(int i) {
        color = i;
    }

    public int readColor() {
        return color;
    }
}