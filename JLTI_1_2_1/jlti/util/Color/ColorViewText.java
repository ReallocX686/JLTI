package jlti.util.Color;

import jlti.ExceptionError.ErrorAdd;

/**
 * the class for a widget as TEntryText, TText
 */
public class ColorViewText implements InterfaceColorViewText {
    protected int[][] fgbg;
    protected int size_main;

    public ColorViewText(int size) {
        fgbg = new int[size][4];
        size_main = size;
    }

    /**
     *
     * @param ot ot char (ot 5 char)
     * @param do_ do char (do 10 char)
     * @param bg Background color char
     * @param fg Foreground color char
     * @param ids ids
     */
    public void add(int ot, int do_, colorANSI bg, colorANSI fg, int ids) {
        if (ids < size_main) {
            fgbg[ids] = new int[] {ot, do_, fg.readColor(), bg.readColor()};
        } else {
            throw new ErrorAdd("ERROR ColorViewText add");
        }
    }

    /**
     * the function for delete instruction
     * @param ids ids
     */
    public void del(int ids) {
        if (ids < size_main) {
            fgbg[ids] = null;
        } else {
            throw new ErrorAdd("ERROR ColorViewText add");
        }
    }

    @Override
    public int[][] returnFgBg() {
        return fgbg;
    }
}