package jlti.view;

import java.util.function.Consumer;

import jlti.annotation.Overload;
import jlti.util.Color.InterfaceColorViewText;
import jlti.ExceptionError.CoordsMinException;
import jlti.util.Color.colorANSI;
import jlti.util.Color.ColorViewText;

public class TEntryText implements TView {
    protected boolean reset = false;
    protected String text = "";
    protected int fg, bg;
    protected int x, y;
    protected Object[] args;
    protected int width, height;
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

    public TEntryText() {
        args = new Object[16];
        args[0] = 10; // max width
        args[10] = 10; //max height
        args[1] = new int[] {0, 0}; // coords in entity_event
        args[2] = -1;
        args[3] = argsRun;
        args[4] = 1; //input in
        args[5] = false; // on input
        args[6] = 7; //color cursor
        args[7] = isPressedCharClickRun;
        args[8] = -1;
        args[9] = -1;
        args[11] = new int[][] {{0, 100, 4, 7}}; // color bgfg
        args[12] = new int[] {0, 0}; // ot print x, y
        args[13] = false;
        args[14] = true;
        args[15] = false; //scrollbar auto down
        fg = 7;
        bg = 0;
        x = 0;
        y = 0;
        width = 10;
        height = 10;
    }

    /**
     * the function for settings all param
     * @param text text
     * @param x coordinate x
     * @param y coordinate y
     * @param x2 coordinate x for controller keyboard
     * @param y2 coordinate y for controller keyboard
     * @param width width the widget
     * @param height height the widget
     * @param bg Background color the widget
     * @param fg Foreground color the widget
     */
    public void setAll(String text, int x, int y, int x2, int y2, int width, int height, colorANSI bg, colorANSI fg) {
        if (x + 1 < 1 || y + 1 < 1) {
            throw new CoordsMinException("TEntryText <X> <Y>");
        }
        this.text = text;
        this.bg = bg.readColor();
        this.fg = fg.readColor();
        this.x = x + 1;
        this.y = y + 1;
        this.width = width;
        this.height = height;
        this.args[0] = width;
        this.args[10] = height;
        this.args[1] = new int[] {x2, y2};
        ColorViewText cc = new ColorViewText(1);
        cc.add(0, 2, bg, fg, 0);
        this.args[11] = cc.returnFgBg();
        reset = true;
    }

    /**
     * the function settings print ot coordinate x in the widget
     * @param i coordinate x
     */
    public void setPrintOtX(int i) {
        int[] ot = (int[])args[12];
        ot[0] = i;
        reset = true;
    }

    /**
     * the function get print ot coordinate x in the widget
     * @return coordinate x
     */
    public int getPrintOtX() {
        int[] ot = (int[])args[12];
        return ot[0];
    }

    /**
     * the function settings print ot coordinate y in the widget
     * @param i coordinate y
     */
    public void setPrintOtY(int i) {
        int[] ot = (int[])args[12];
        ot[1] = i;
        reset = true;
    }

    /**
     * the function get print ot coordinate y in the widget
     * @return coordinate y
     */
    public int getPrintOtY() {
        int[] ot = (int[])args[12];
        return ot[1];
    }

    /**
     * the function for settings color text the widget
     * @param fgbg {@link InterfaceColorViewText}
     */
    public void setBgFg(InterfaceColorViewText fgbg) {
        this.args[11] = fgbg.returnFgBg();
        reset = true;
    }

    /**
     * the function for settings on/off auto down in the widget
     * @param i true/false
     */
    public void setOnfAutoDown(boolean i) {
        args[15] = i;
    }

    /**
     * the function ON/OFF the TEntryText in main ui
     * <p>ON/OFF click by the TEntryText</p>
     * @param i true/false
     */
    public void setOnf(boolean i) {
        args[14] = i;
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
     * the function for settings background color cursor in the widget
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
     * the function for insert text in the widget
     * <p>WARNING at used the function, call function {@link #setOnfAutoDown(boolean)} args (FALSE)</p>
     * @param text string text
     */
    public void insertText(String text) {
        this.args[13] = true;
        this.text = this.text + text;
        reset = true;
    }

    /**
     * the function for insert text as in buffer
     * <p>if size buffer == 10 string => if size string > 10 to delete very old</p>
     * <p>WARNING at used the function, call function {@link #setOnfAutoDown(boolean)} args (FALSE)</p>
     * @param text insert string text
     * @param size setting size a buffer
     */
    public void insertTextAndDel(String text, int size) {
        this.args[13] = true;
        StringBuilder out = new StringBuilder();
        String[] textin = this.text.split("\n");

        if (textin.length > size) {
            final int ot = (textin.length - size);
            for (int i = ot; i < textin.length; i++) {
                out.append(textin[i] + '\n');
            }
            out.append(text);
            this.text = out.toString();
        } else {
            this.text = this.text + text;
        }

        reset = true;
    }

    /**
     * the function for set text in the widget
     * <p>WARNING at used the function, call function {@link #setOnfAutoDown(boolean)} args (FALSE)</p>
     * @param text
     */
    public void setText(String text) {
        this.args[13] = true;
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
     * the function for settings coordinate x
     * @param x coordinate x
     */
    public void setX(int x) {
        if (x + 1 < 1) {
            throw new CoordsMinException("TEntryText <X>");
        }
        this.x = x + 1;
        reset = true;
    }

    /**
     * the function for settings coordinate y
     * @param y coordinate y
     */
    public void setY(int y) {
        if (y + 1 < 1) {
            throw new CoordsMinException("TEntryText <Y>");
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

    /**
     * the function for settings height the widget
     * @param h height
     */
    public void setHeight(int h) {
        this.height = h;
        args[10] = h;
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
        return 4;
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