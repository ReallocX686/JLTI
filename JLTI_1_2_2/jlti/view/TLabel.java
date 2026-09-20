package jlti.view;

import jlti.ExceptionError.CoordsMinException;
import jlti.annotation.Overload;
import jlti.util.Color.colorANSI;

public class TLabel implements TView {
    protected boolean reset = false;
    protected String text = "";
    protected int fg, bg;
    protected int x, y;
    protected int width, height;
    protected Object[] args;
    protected int ids_main;
    protected boolean OnfMain = true;

    /**
     * the function for settings on render the widget
     * @param i boolean
     */
    @Override
    public void setOnfMain(boolean i) {
        this.OnfMain = i;
    }

    @Override
    public boolean getOnfMain() {
        return this.OnfMain;
    }

    public TLabel() {
        args = new Object[2];
        args[0] = -1; //max len char
        fg = 7;
        bg = 0;
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }

    /**
     * the function for settings all param
     * @param text text
     * @param x coordinate x
     * @param y coordinate y
     * @param bg Background color the widget
     * @param fg Foreground color the widget
     * @param width width the widget
     * @param max_len width text in the widget
     */
    public void setAll(String text, int x, int y, colorANSI bg, colorANSI fg, int width, int max_len) {
        if (x + 1 < 1 || y + 1 < 1) {
            throw new CoordsMinException("TLabel <X> <Y>");
        }
        this.text = text;
        this.fg = fg.readColor();
        this.bg = bg.readColor();
        this.x = x + 1;
        this.y = y + 1;
        this.width = width;
        args[0] = max_len;
        reset = true;
    }

    /**
     * the function for settings max width text in the widget
     * @param i width
     */
    public void setMaxLenText(int i) {
        args[0] = i;
        reset = true;
    }

    /**
     * the function for settings width the widget
     * @param i width
     */
    public void setWidth(int i) {
        width = i;
        reset = true;
    }

    /**
     * the function for settings height the widget
     * @param i height
     */
    public void setHeight(int i) {
        height = i;
        reset = true;
    }

    /**
     * the function for set text
     * @param text string text
     */
    public void setText(String text) {
        this.text = text;
        reset = true;
    }

    /**
     * the function for settings a background color
     * @param bg int
     */
    @Overload
    public void setBg(int bg) {
        this.bg = bg;
        reset = true;
    }

    /**
     * the function for settings a foreground color
     * @param fg int
     */
    @Overload
    public void setFg(int fg) {
        this.fg = fg;
        reset = true;
    }

    /**
     * the function for settings a background color
     * @param bg colorANSI
     */
    @Overload
    public void setBg(colorANSI bg) {
        this.bg = bg.readColor();
        reset = true;
    }

    /**
     * the function for settings a foreground color
     * @param fg colorANSI
     */
    @Overload
    public void setFg(colorANSI fg) {
        this.fg = fg.readColor();
        reset = true;
    }

    /**
     * the function for settings coordinate x the widget
     * @param x coordinate x
     */
    public void setX(int x) {
        if (x + 1 < 1) {
            throw new CoordsMinException("TLabel <X>");
        }
        this.x = x + 1;
        reset = true;
    }

    /**
     * the function for settings coordinate y the widget
     * @param y coordinate y
     */
    public void setY(int y) {
        if (y + 1 < 1) {
            throw new CoordsMinException("TLabel <Y>");
        }
        this.y = y + 1;
        reset = true;
    }

    @Override
    public void setIds(int i) {
        this.ids_main = i;
    }

    @Override
    public int getIds() {
        return this.ids_main;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public boolean getRes() {
        return reset;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getFg() {
        return fg;
    }

    @Override
    public int getBg() {
        return bg;
    }

    @Override
    public void setRes(boolean i) {
        reset = i;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}