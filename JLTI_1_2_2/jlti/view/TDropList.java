package jlti.view;

import jlti.ExceptionError.CoordsMinException;
import jlti.annotation.Overload;
import jlti.util.Color.colorANSI;

public class TDropList implements TView {
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

    public TDropList() {
        args = new Object[8];
        args[0] = new Runnable[] {() -> {}, () -> {}, () -> {}, () -> {}, () -> {}}; //isPressedClick, isEventClick, isLeaveClick, isEventWhile, isPressedWhile
        args[1] = new int[] {0, 0};
        //args[2] = true; // if clicked ERR
        args[3] = null; // list str
        args[4] = true; //onf
        args[5] = 0; // current list cursor
        args[6] = false;
        args[7] = true;// if clicked
        width = 5;
        height = 1;
        fg = 7;
        bg = 0;
        x = 0;
        y = 0;
    }

    public static class createList {
        public String[] list;

        public createList(int size) {
            this.list = new String[size];
        }

        public void add(String text, int i) {
            this.list[i] = text;
        }

        public String[] readList() {
            return this.list;
        }
    }

    /**
     * setAll
     * @param x coordinate x
     * @param y coordinate y
     * @param x2 coordinate x for controller keyboard
     * @param y2 coordinate y for controller keyboard
     * @param width width the widget
     * @param bg Background color the widget
     * @param fg Foreground color the widget
     * @param list list drop {@link createList}
     */
    public void setAll(int x, int y, int x2, int y2, int width, colorANSI bg, colorANSI fg, createList list) {
        if (x + 1 < 1 || y + 1 < 1) {
            throw new CoordsMinException("TDropList <X> <Y>");
        }
        this.x = x + 1;
        this.y = y + 1;
        this.width = width;
        this.bg = bg.readColor();
        this.fg = fg.readColor();
        this.args[1] = new int[] {x2, y2};
        this.args[3] = list.readList();
        reset = true;
    }

    /**
     * read position in integer
     * @return id current
     */
    public int readPositionInt() {
        return (int)args[5];
    }

    /**
     * read position in string
     * @return string current
     */
    public String readPositionString() {
        final String[] list = (String[])args[3];
        return list[(int)args[5]];
    }

    /**
     * set list drop
     * @param list {@link createList}
     */
    public void setListDrop(createList list) {
        this.args[3] = list.readList();
        this.args[5] = 0;
    }

    /**
     * the function ON/OFF the TDropList in main ui
     * <p>ON/OFF click by the TDropList</p>
     * @param i true/false
     */
    public void setOnf(boolean i) {
        this.args[4] = i;
    }

    /**
     * is pressed while
     * <p>if cursor in the widget to call</p>
     * <p>while the condition is true to lambda call</p>
     * @param fn lambda fn
     */
    public void isPressedWhile(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[0];
        args1[4] = fn;
        reset = true;
    }

    /**
     * is the enter while
     * <p>if cursor aimed is the widget to call fn</p>
     * <p>while the condition is true to lambda call</p>
     * @param fn lambda fn
     */
    public void isEnterWhile(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[0];
        args1[3] = fn;
        reset = true;
    }

    /**
     * is pressed click
     * <p>if click is button up, down to call fn</p>
     * <p>while the condition is true to lambda call</p>
     * @param fn lambda fn
     */
    public void isPressedClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[0];
        args1[0] = fn;
        reset = true;
    }

    /**
     * is the enter click
     * <p>if cursor aimed is the widget to call fn one</p>
     * @param fn lambda fn
     */
    public void isEnterClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[0];
        args1[1] = fn;
        reset = true;
    }

    /**
     * is leave click
     * <p>if cursor unaimed is the widget to call fn one</p>
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
     * the function for settings coordinate X the widget
     * @param x int
     */
    public void setX(int x) {
        if (x + 1 < 1) {
            throw new CoordsMinException("TDropList <Y>");
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
            throw new CoordsMinException("TDropList <Y>");
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
        return 9;
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