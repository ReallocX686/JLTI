package jlti.view;

import jlti.ExceptionError.CoordsMinException;
import jlti.annotation.Overload;
import jlti.util.Color.colorANSI;

public class TSwitchButton implements TView {
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

    public TSwitchButton() {
        args = new Object[7];
        args[0] = new char[] {'0', '1'};
        args[1] = new int[] {0, 0};
        args[2] = null;
        args[3] = true; // onf clicked
        args[4] = true; //if enter on btn
        args[5] = false; //switch
        args[6] = new Runnable[] {() -> {}, () -> {}, () -> {}, () -> {}}; //isPressedClick, isEventClick, isLeaveClick, isEventWhile
        width = 1;
        height = 1;
        fg = 7;
        bg = 0;
        x = 0;
        y = 0;
    }

    /**
     * the function for settings all param
     * @param x coordinate x
     * @param y coordinate y
     * @param x2 coordinate x for controller keyboard
     * @param y2 coordinate y for controller keyboard
     * @param bg Background color the widget
     * @param fg Foreground color the widget
     * @param ifOn if the switch is true to print char on
     * @param ifOff if the switch is false to print char off
     */
    public void setAll(int x, int y, int x2, int y2, colorANSI bg, colorANSI fg, char ifOn, char ifOff) {
        if (x + 1 < 1 || y + 1 < 1) {
            throw new CoordsMinException("TSwitchButton <X> <Y>");
        }
        this.x = x + 1;
        this.y = y + 1;
        this.bg = bg.readColor();
        this.fg = fg.readColor();
        this.args[0] = new char[] {ifOn, ifOff};
        this.args[1] = new int[] {x2, y2};
        reset = true;
    }

    /**
     * the function for art click
     */
    public void Click() {
        final boolean i = (boolean)args[5];
        ((Runnable[])args[6])[0].run();
        if (i) {
            args[5] = false;
        } else {
            args[5] = true;
        }
        reset = true;
    }

    /**
     * the function for art settings on/off
     * @param i true/false
     */
    public void setClickType(boolean i) {
        ((Runnable[])args[6])[0].run();
        args[5] = i;
        reset = true;
    }

    /**
     * the function for read type on/off
     * @return true/false
     */
    public boolean readOnf() {
        return (boolean)args[5];
    }

    /**
     * the function for settings char ON
     * @param i char ON
     */
    public void setCharOn(char i) {
        char[] c = (char[])args[0];
        c[0] = i;
        reset = true;
    }

    /**
     * the function for settings char OFF
     * @param i char OFF
     */
    public void setCharOff(char i) {
        char[] c = (char[])args[0];
        c[1] = i;
        reset = true;
    }

    /**
     * the function ON/OFF the TSwitchButton in main ui
     * <p>ON/OFF click by the TSwitchButton</p>
     * @param i true/false
     */
    public void setOnf(boolean i) {
        this.args[3] = i;
        reset = true;
    }

    /**
     * while a cursor is the widget to the lambda call
     * @param fn lambda fn
     */
    public void isEnterWhile(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[6];
        args1[3] = fn;
        reset = true;
    }

    /**
     * while a cursor is the widget and click to the lambda call
     * @param fn lambda fn
     */
    public void isPressedClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[6];
        args1[0] = fn;
        reset = true;
    }

    /**
     * while a cursor is the widget to the lambda call one
     * @param fn lambda fn
     */
    public void isEnterClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[6];
        args1[1] = fn;
        reset = true;
    }

    /**
     * while a cursor not is the widget to the lambda call one
     * @param fn lambda fn
     */
    public void isLeaveClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[6];
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
     * the function for settings coordinate X the widget
     * @param x int
     */
    public void setX(int x) {
        if (x + 1 < 1) {
            throw new CoordsMinException("TSwitchButton <Y>");
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
            throw new CoordsMinException("TSwitchButton <Y>");
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
        return 7;
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