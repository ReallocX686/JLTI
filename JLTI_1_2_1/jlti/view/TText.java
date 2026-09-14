package jlti.view;

import jlti.annotation.Overload;
import jlti.util.Color.InterfaceColorViewText;
import jlti.util.Color.ColorViewText;
import jlti.ExceptionError.CoordsMinException;
import jlti.util.Color.colorANSI;

public class TText implements TView {
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

    public TText() {
        args = new Object[2];
        width = 5;
        height = 2;
        fg = 7;
        bg = 0;
        x = 0;
        y = 0;
    }

    /**
     * the function for settings all param
     * @param text text
     * @param x coordinate x
     * @param y coordinate y
     * @param width width the widget
     * @param height height the widget
     * @param bg Background color the widget
     * @param fg Foreground color the widget
     */
    public void setAll(String text, int x, int y, int width, int height, colorANSI bg, colorANSI fg) {
        if (x + 1 < 1 || y + 1 < 1) {
            throw new CoordsMinException("TText <X> <Y>");
        }
        ColorViewText cc = new ColorViewText(1);
        cc.add(0, 2, bg, fg, 0);
        this.args[0] = cc.returnFgBg();
        this.text = text;
        this.x = x + 1;
        this.y = y + 1;
        this.width = width;
        this.height = height;
        this.bg = bg.readColor();
        this.fg = fg.readColor();
        reset = true;
    }

    /**
     * the function for settings text
     * @param text text string
     */
    public void setText(String text) {
        this.text = text;
        reset = true;
    }

    /**
     * the function for insert text in the widget
     * @param text string text
     */
    public void insertText(String text) {
        this.text = this.text = text;
        reset = true;
    }

    /**
     * the function for settings width the widget
     * @param width int
     */
    public void setWidth(int width) {
        this.width = width;
        reset = true;
    }

    /**
     * the function for settings height the widget
     * @param height int
     */
    public void setHeight(int height) {
        this.height = height;
        reset = true;
    }

    /**
     * the function for settings coordinate X the widget
     * @param x int
     */
    public void setX(int x) {
        if (x + 1 < 1) {
            throw new CoordsMinException("TText <X>");
        }
        this.x = x + 1;
        reset = true;
    }

    /**
     * the function for settings coordinate Y the widget
     * @param y int
     */
    public void setY(int y) {
        if (y + 1 < 1) {
            throw new CoordsMinException("TText <Y>");
        }
        this.y = y + 1;
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
     * the function for settings a background color
     * @param bg colorANSI
     */
    @Overload
    public void setBg(colorANSI bg) {
        this.bg = bg.readColor();
        reset = true;
    }

    /**
     * the function for settings style color the widget
     * @param fgbg {@link InterfaceColorViewText}
     */
    public void setBgFg(InterfaceColorViewText fgbg) {
        this.args[0] = fgbg.returnFgBg();
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
        return 1;
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
        return 0;
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