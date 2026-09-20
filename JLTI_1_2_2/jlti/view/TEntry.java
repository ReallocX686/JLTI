package jlti.view;

import java.util.function.Consumer;
import jlti.ExceptionError.CoordsMinException;
import jlti.annotation.Overload;
import jlti.util.Color.colorANSI;

public class TEntry implements TView {
    protected boolean reset = false;
    protected String text = "";
    protected int fg, bg;
    protected int x, y;
    protected Object[] args;
    protected int width;
    protected Runnable[] argsRun = new Runnable[] {() -> {}, () -> {}, () -> {}}; //isEnterClick, isLeaveClick, isEnterWhile
    protected Consumer<Character> isPressedCharClickRun = (charOut) -> {};
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

    public TEntry() {
        args = new Object[11];
        args[0] = 10; // max width
        args[1] = new int[] {0, 0}; // coords in entity_event
        args[2] = -1;
        args[3] = argsRun;
        args[4] = 1; //input in
        args[5] = false; // on input
        args[6] = 7; //color cursor
        args[7] = isPressedCharClickRun;
        args[8] = -1; // max print char if -1 to not barrier else msx int
        args[9] = false; // reset if text
        args[10] = true; //onf
        fg = 7;
        bg = 0;
        x = 0;
        y = 0;
        width = 10;
    }

    /**
     * the function for settings all param
     * @param text text at create the widget
     * @param x coordinate x
     * @param y coordinate y
     * @param x2 coordinate x for controller keyboard
     * @param y2 coordinate y for controller keyboard
     * @param bg Background color the widget
     * @param fg Foreground color the widget
     * @param width width the widget
     * @param max_width_text max add char in the widget
     */
    public void setAll(String text, int x, int y, int x2, int y2, colorANSI bg, colorANSI fg, int width, int max_width_text) {
        if (x + 1 < 1 || y + 1 < 1) {
            throw new CoordsMinException("TEntry <X> <Y>");
        }
        this.text = text;
        this.fg = fg.readColor();
        this.bg = bg.readColor();
        this.x = x + 1;
        this.y = y + 1;
        this.width = width;
        this.args[8] = max_width_text;
        args[0] = width;
        args[1] = new int[] {x2, y2};
        reset = true;
    }

    /**
     * the function ON/OFF the TEntry in main ui
     * <p>ON/OFF click by the TEntry</p>
     * @param i true/false
     */
    public void setOnf(boolean i) {
        args[10] = i;
    }

    /**
     * the function for settings max add char in the widget
     * @param i int max width
     */
    public void setMaxLenText(int i) {
        this.args[8] = i;
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
     * the function for settings background color the cursor
     * @param bg colorANSI
     */
    public void setBgCursor(colorANSI bg) {
        args[6] = bg.readColor();
    }

    /**
     * the function for add a lambda function
     * <p>while cursor aimed is the widget to call fn</p>
     * <p>if the condition is true to lambda call, args (current char click)</p>
     * @param fn lambda fn [(charOut) -> {method}]
     */
    public void isPressedCharClick(Consumer<Character> fn) {
        args[7] = fn;
        reset = true;
    }

    /**
     * is the enter while
     * <p>while a cursor is the widget to the lambda call</p>
     * @param fn lambda fn
     */
    public void isEnterWhile(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[3];
        args1[2] = fn;
        reset = true;
    }

    /**
     * is the enter click
     * <p>if cursor aimed is the widget to call fn</p>
     * @param fn lambda fn
     */
    public void isEnterClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[3];
        args1[0] = fn;
        reset = true;
    }

    /**
     * is leave click
     * <p>if cursor unaimed is the widget to call fn</p>
     * @param fn lambda fn
     */
    public void isLeaveClick(Runnable fn) {
        Runnable[] args1 = (Runnable[])args[3];
        args1[1] = fn;
        reset = true;
    }

    /**
     * the function for setting text
     * @param text string text
     */
    public void setText(String text) {
        this.args[9] = true;
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
            throw new CoordsMinException("TEntry <X>");
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
            throw new CoordsMinException("TEntry <Y>");
        }
        this.y = y + 1;
        reset = true;
    }

    /**
     * the function for settings width the widget
     * @param w width
     */
    public void setWidth(int w) {
        this.width = w;
        args[0] = w;
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
        return 3;
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
        return 0;

    }
}