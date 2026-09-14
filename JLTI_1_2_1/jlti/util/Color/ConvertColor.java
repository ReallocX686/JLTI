package jlti.util.Color;

import jlti.ExceptionError.ErrorColor;

public class ConvertColor {
    public static int convertRGBinANSI(int r, int g, int b) {
        if ((r < 0 || r > 255) || (g < 0 || g > 255) || (b < 0 || b > 255)) {
            throw new ErrorColor("error convertor rgb in ansi CcolorRGB");
        }
        int rr = (r * 5 + 127) / 255;
        int gg = (g * 5 + 127) / 255;
        int bb = (b * 5 + 127) / 255;
        return 16 + (36 * rr) + (6 * gg) + bb;
    }
}