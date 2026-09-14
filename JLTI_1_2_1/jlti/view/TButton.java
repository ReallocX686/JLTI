package jlti.view;

import jlti.ExceptionError.CoordsMinException;
import jlti.util.Color.colorANSI;
import jlti.annotation.Overload;

public class TButton implements TView {
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

    public TButton() {
        args = new Object[5];
        args[0] = new Runnable[] {() -> {}, () -> {}, () -> {}, () -> {}}; //isPressedClick, isEventClick, isLeaveClick, isEventWhile
        args[1] = new int[] {0, 0};
        args[2] = 1; // if clicked
        args[3] = -1;
        args[4] = true;
        width = 5;
        height = 1;
        fg = 7;
        bg = 0;
        x = 0;
        y = 0;
    }

    /**
     * the function for settings all param
     * @param text text in button
     * @param x coordinate x
     * @param y coordinate y
     * @param x2 coordinate x for controller keyboard
     * @param y2 coordinate y for controller keyboard
     * @param width width the widget
     * @param height height the widget
     * @param bg Background color the widget
     * @param fg Foreground color the widget
     * @param max_len max width text in the widget
     */
    public void setAll(String text, int x, int y, int x2, int y2, int width, int height, colorANSI bg, colorANSI fg, int max_len) {
        if (x + 1 < 1 || y + 1 < 1) {
            throw new CoordsMinException("TButton <X> <Y>");
        }
        this.x = x + 1;
        this.y = y + 1;
        this.width = width;
        this.height = height;
        this.bg = bg.readColor();
        this.fg = fg.readColor();
        this.text = text;
        this.args[1] = new int[] {x2, y2};
        this.args[3] = max_len;
        reset = true;
    }

    /**
     * art click
     */
    public void Click() {
        if ((boolean)args[4]) {
            ((Runnable[])args[0])[0].run();
        }
    }

    /**
     * the function ON/OFF the button in main ui
     * <p>ON/OFF click by the button</p>
     * @param i true/false
     */
    public void setOnf(boolean i) {
        this.args[4] = i;
    }

    /**
     * the function for settings variable max width text in the button
     * @param i int
     */
    public void setMaxLenText(int i) {
        args[3] = i;
        reset = true;
    }

    /**
     * while a cursor is the widget to the lambda call
     * @param fn lambda fn
     */
    public void isEnterWhile(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[0];
        args1[3] = fn;
        reset = true;
    }

    /**
     * while a cursor is the widget and click to the lambda call
     * @param fn lambda fn
     */
    public void isPressedClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[0];
        args1[0] = fn;
        reset = true;
    }

    /**
     * while a cursor is the widget to the lambda call one
     * @param fn lambda fn
     */
    public void isEnterClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[0];
        args1[1] = fn;
        reset = true;
    }

    /**
     * while a cursor not is the widget to the lambda call one
     * @param fn lambda fn
     */
    public void isLeaveClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[0];
        args1[2] = fn;
        reset = true;
    }

    /**
     * the function for settings coordinate X for controller keyboard
     * @param x int
     */
    public void setCollumn(int x) {
        int[] args1 = (int[])args[1];
        args1[0] = x;
        reset = true;
    }

    /**
     * the function for settings coordinate Y for controller keyboard
     * @param y int
     */
    public void setRow(int y) {
        int[] args1 = (int[])args[1];
        args1[1] = y;
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
            throw new CoordsMinException("TButton <Y>");
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
            throw new CoordsMinException("TButton <Y>");
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
        return 2;
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