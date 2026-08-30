package jlti.view;

import jlti.ExceptionError.CoordsMinException;
import jlti.annotation.Overload;
import jlti.util.Color.colorANSI;

public class TFrame implements TView {
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

    public TFrame() {
        args = new Object[2];
        args[0] = 0; //on window [0 - off window and text, 1 - on window, 2 - on window and text]
        args[1] = 0; // style line [0 - standart line cube, 1 - line cube 2, 2 - line cube and corner no cube]
        fg = 7;
        bg = 0;
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }

    /**
     * line - one line
     * the line dv - 2 line
     * lineAndCorner - line and corner style
     * line2 - bold lines
     */
    public enum frameStyle {
        line(0),
        lineDv(1),
        lineAndCorner(2),
        line2(3);

        private final int out;
        private frameStyle(int i) {
            out = i;
        }

        public int readInt() {
            return out;
        }
    }

    /**
     * off - window and text
     * window - on window
     * windowAndText - on all
     */
    public enum frameWindowOnf {
        off(0),
        window(1),
        windowAndText(2);

        private final int out;
        private frameWindowOnf(int i) {
            out = i;
        }

        public int readInt() {
            return out;
        }
    }

    /**
     * the function for settings all param
     * @param text text
     * @param x coordinate x
     * @param y coordinate y
     * @param bg Background color the widget
     * @param fg Foreground color the widget
     * @param width width the widget
     * @param height height the widget
     * @param windowon style the window widget {@link frameWindowOnf}
     * @param styleLine style line the window widget {@link frameStyle}
     */
    public void setAll(String text, int x, int y, colorANSI bg, colorANSI fg, int width, int height, frameWindowOnf windowon, frameStyle styleLine) {
        if (x + 1 < 1 || y + 1 < 1) {
            throw new CoordsMinException("TFrame <X> <Y>");
        }
        this.text = text;
        this.fg = fg.readColor();
        this.bg = bg.readColor();
        this.x = x + 1;
        this.y = y + 1;
        this.width = width;
        this.height = height;
        args[0] = windowon.readInt();
        args[1] = styleLine.readInt();
        reset = true;
    }

    /**
     * the function for settings style window line
     * @param i {@link frameStyle}
     */
    public void setStyleLine(frameStyle i) {
        args[1] = i.readInt();
        reset = true;
    }

    /**
     * the function for settings window the widget
     * @param i {@link frameWindowOnf}
     */
    public void setWindowOnf(frameWindowOnf i) {
        args[0] = i.readInt();
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
     * the function for set text in the widget
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
            throw new CoordsMinException("TFrame <X>");
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
            throw new CoordsMinException("TFrame <Y>");
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
        return 5;
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