package jlti.app;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import jlti.ExceptionError.ErrorAdd;
import jlti.ExceptionError.ErrorDel;
import jlti.ExceptionError.ErrorLater;
import jlti.ExceptionError.ErrorSetSizeListException;
import jlti.ExceptionError.MaxLenTextException;
import jlti.annotation.Overload;
import jlti.io.TOut;
import jlti.io.keyboard.key;
import jlti.io.keyboard.keyScanner;
import jlti.io.keyboardOutput.keyboardListener;
import jlti.util.Time;
import jlti.util.text.CharLine;
import jlti.view.TButton;
import jlti.view.TDropList;
import jlti.view.TEntry;
import jlti.view.TEntryText;
import jlti.view.TScrollBar;
import jlti.view.TSwitchButton;
import jlti.view.TView;

/**
 * main class view and handle all a widget iu
 */
public class interfaceAppMain extends TOut {
    private keyboardListener mainKeyboardListener;
    private Object[][] laterEvents;
    private boolean run_on = true;
    private int sleep_while = 10, sleep_plus = 0;
    private long max_time_limit_scanner = 0L;
    private TView[] entity;
    private TView viewClassOld = null;
    private int[][] entity_event;
    private int reset = 0;
    private int max_entity = 128;
    private final int[] typeListViewOnEvent = new int[] {2, 3, 4, 7, 8, 9};
    private int collumn_main = 0, old_collumn_main = 0;//x
    private int row_main = 0, old_row_main = 0;//y
    private int mode_input = 0; //0, 1, 2, 3, 4
    private boolean enterOn = false;
    private int collumn_input_mode_1 = 0, max_input_mode_1 = 0, min_input_mode_1 = 0;
    private boolean cursorOnf = false;
    private int lenViewEnter = 0;
    private int main_size = 0;
    private modeIO modeInputView = modeIO.GPM;

    //entry, entryText
    private int input_mode_1_x_cursor = 0;
    private int input_mode_1_current_time_sleep = 0;
    private int input_mode_1_current_collumn_x_in = 0;
    private int input_mode_1_current_collumn_x_in_2 = 0;
    private int input_mode_1_max_collumn_x_in = 0;
    private int input_mode_1_max_width_text = -1;
    private StringBuilder input_mode_1_text;

    //entryText
    private int input_mode_2_y_cursor = 0;
    private int max_input_mode_2_y = 0;
    private int min_input_mode_2_y = 0;
    private int row_input_mode_2 = 0;
    private int input_mode_2_current_row_y_in = 0;
    private int input_mode_2_max_row_y_in = 0;
    private int input_mode_2_current_row_y_in_2 = 0;
    private int input_mode_2_max_height_text = 0;
    private String input_mode_2_text;
    private StringBuilder input_mode_2_text_2;
    private boolean input_mode_2_outOnMain = true;

    public enum modeIO {
        GPM(1),
        LIT(2);

        private final int out;
        modeIO(int i) {
            out = i;
        }

        public int readInt() {
            return out;
        }
    }

    /**
     *
     * @param sizes size buffer iu view
     * @param time_sleep main sleep while graphics ui
     * @param mode GPM(JLine standart out)/LIT(System.out.)
     */
    public void init(int sizes, int time_sleep, modeIO mode) {
        _init(mode.readInt());
        main_size = sizes;
        entity = new TView[sizes];
        entity_event = new int[sizes][2];
        laterEvents = new Object[sizes][3];
        sleep_while = time_sleep;
        max_time_limit_scanner = (long)time_sleep + 1;
    }

    /**
     * the function for settings a meaning
     * @param t type set
     * <p>view -> realloc size list a view</p>
     * <p>later -> realloc size list a later</p>
     * <p>sleep_plus -> set delay plus</p>
     * @param s  meaning
     * <p>variable integer</p>
     */
    public void setVar(String t, int s) {
        switch (t) {
            case "view":
                entity = new TView[s];
                entity_event = new int[s][2];
                break;
            case "later":
                laterEvents = new Object[s][3];
                break;
            case "sleep_plus":
                sleep_plus = s;
                break;
            default:
                throw new ErrorSetSizeListException();
        }
    }

    /**
     * the function for create after in main thread
     * @param fn lambda function
     * @param timeml delay is ml/s
     * @param ids ids
     */
    @Overload("later 001")
    public void later(Runnable fn, int timeml, int ids) {
        if (ids < laterEvents.length) {
            laterEvents[ids] = new Object[] {fn, timeml, 0};
        } else {
            throw new ErrorLater("\033[2J\033[HERROR add later");
        }
    }

    /**
     * the function for create after in main thread
     * @param fn lambda function
     * @param timeml delay is ml/s
     * @param currentTime current delay is ml/s
     * @param ids ids
     */
    @Overload("later 002")
    public void later(Runnable fn, int timeml, int currentTime, int ids) {
        if (ids < laterEvents.length) {
            laterEvents[ids] = new Object[] {fn, timeml, currentTime};
        } else {
            throw new ErrorLater("\033[2J\033[HERROR add later");
        }
    }

    /**
     * the later delete
     * @param ids ids
     */
    public void laterDel(int ids) {
        if (ids < laterEvents.length) {
            laterEvents[ids] = null;
        } else {
            throw new ErrorLater("\033[2J\033[HERROR del later");
        }
    }

    /**
     * set ON/OFF the view for graphics
     * @param onf true/false
     * @param clazz a view
     */
    public void setOnfView(boolean onf, TView clazz) {
        final int ids = clazz.getIds();
        final Object[] args = clazz.getArgs();
        clazz.setOnfMain(onf);
        if (onf) {
            if (typeViewEvent(clazz.getType())) {
                entity_event[ids] = (int[])args[1];
            }
        } else {
            if (typeViewEvent(clazz.getType())) {
                entity_event[ids] = null;
            }
        }
    }

    private boolean typeViewEvent(int type) {
        for (int i = 0; i < typeListViewOnEvent.length; i++) {
            if (type == typeListViewOnEvent[i]) {
                return true;
            }
        }
        return false;
    }

    private int getIdsInEntityEvent(int x, int y) {
        for (int i = 0; i < entity_event.length; i++) {
            if (entity_event[i][0] == x && entity_event[i][1] == y) {
                return i;
            }
        }
        return -1;
    }

    /**
     * the function for add a view ui
     * @param clazz
     * @param ids
     */
    public void add(TView clazz, int ids) {
        if (ids < max_entity) {
            clazz.setIds(ids);
            entity[ids] = clazz;
            boolean f1 = false;
            for (int i = 0; i < typeListViewOnEvent.length; i++) {
                if (clazz.getType() == typeListViewOnEvent[i]) {
                    cursorOnf = true;
                    lenViewEnter++;
                    Object[] args = clazz.getArgs();
                    int[] args1 = (int[])args[1];
                    entity_event[ids] = new int[] {args1[0], args1[1]};
                    f1 = false;
                    break;
                } else {
                    f1 = true;
                }
            }
            if (f1) {
                entity_event[ids] = null;
            }
            reset = 2;
        } else {
            throw new ErrorAdd("\033[2J\033[HERROR add, view");
        }
    }

    /**
     * the function for delete a view ui
     * <p>By a widget this class</p>
     * @param clazz
     */
    @Overload("del 001")
    public void del(TView clazz) {
        final int types = clazz.getType();
        for (int i = 0; i < typeListViewOnEvent.length; i++) {
            if (types == typeListViewOnEvent[i]) {
                entity_event[i] = null;
                lenViewEnter--;
                break;
            }
        } 
        for (int i = 0; i < entity.length; i++) {
            if (entity[i] == clazz) {
                entity[i] = null;
                reset = 2;
                break;
            }
        }
        if (lenViewEnter < 1) {
            cursorOnf = false;
        }
    }

    /**
     * the function for delete a view ui
     * <p>By a widget this ids</p>
     * @param ids
     */
    @Overload("del 002")
    public void del(int ids) {
        if (ids < max_entity) {
            final TView clazz = entity[ids];
            for (int i = 0; i < entity.length; i++) {
                if (entity[i] == clazz) {
                    if (clazz != null) {
                        final int types = clazz.getType();
                        for (int ii = 0; ii < typeListViewOnEvent.length; ii++) {
                            if (types == typeListViewOnEvent[ii]) {
                                entity_event[ids] = new int[] {0, 0};
                                lenViewEnter--;
                                break;
                            }
                        }
                    }
                    entity[i] = null;
                    reset = 2;
                    break;
                }
            }
            if (lenViewEnter < 1) {
                cursorOnf = false;
            }
        } else {
            throw new ErrorDel("\033[HERROR del");
        }
    }

    /**
     * the function for a find widget By to ids
     * @param ids
     * @return viewList
     */
    public TView getClass(int ids) {
        return entity[ids];
    }

    /**
     * the function for a find widget By to class
     * @param clazz
     * @return int
     */
    public int getIdClass(TView clazz) {
        for (int i = 0; i < entity.length; i++) {
            if (entity[i] == clazz) {
                return i;
            }
        }
        return -1;
    }

    /**
     * the function for find a current X cursor
     * @return int
     */
    public int returnCollumn() {
        return collumn_main;
    }

    /**
     * the function for find a current Y cursor
     * @return int
     */
    public int returnRow() {
        return row_main;
    }

    /**
     * the function for controller rendering
     * <p>int 2 - rendering all widget ui</p>
     * @param i
     */
    public void setReset(int i) {
        // i for reset
        reset = i;
    }

    /**
     * the function for add a keyboardListener
     * @param i
     */
    public void setKeyboardListener(keyboardListener i) {
        mainKeyboardListener = i;
    }

    /**
     * the function for settings a coordinate cursor (x, y)
     * <p>not unfuse</p>
     * <p>is recommented used the function {@link #setCursorCoordKeyboard}</p>
     * @param x
     * @param y
     */
    public void setCursorCoordKeyboardADM(int x, int y) {
        collumn_main = x;
        row_main = y;
    }

    /**
     * the function for settings a coordinate cursor (x, y)
     * <p>used fuse</p>
     * @param x
     * @param y
     */
    public void setCursorCoordKeyboard(int x, int y) {
        for (int i = 0; i < entity_event.length; i++) {
            if (entity_event[i] != null) {
                if (x == entity_event[i][0] && y == entity_event[i][1]) {
                    collumn_main = x;
                    row_main = y;
                    break;
                }
            }
        }
    }

    /**
     * the function for find a typeInputMode
     * @return InputMode.Modes
     */
    public InputMode.Modes typeInputMode() {
        return InputMode.Modes.readM0(this.mode_input);
    }

    /**
     * the function for settings a inputMode keyboard
     * @param i
     */
    public void setInputMode(InputMode.Modes i) {
        mode_input = i.readInt();
    }

    public void setClear() {
        printf("\033[0m\033[2J\033[H");
    }

    private void controllerKeyBoardListenerDO_VIEW() {
        if (mainKeyboardListener != null) {
            final int[] codePressed = codeIsPressedReadAll();
            mainKeyboardListener.WhileDoView(codePressed);
            if (codePressed[0] != -2) {
                mainKeyboardListener.PressedDoView(codePressed);
            }
        }
    }

    private void controllerKeyBoardListenerEND_VIEW() {
        if (mainKeyboardListener != null) {
            final int[] codePressed = codeIsPressedReadAll();
            mainKeyboardListener.WhileEndView(codePressed);
            if (codePressed[0] != -2) {
                mainKeyboardListener.PressedEndView(codePressed);
            }
        }
    }

    private int PressedInputMode_0_KeyFnMain() {
        int codePressed = -2;
        if (mode_input == 0) {
            codePressed = codeIsPressed(max_time_limit_scanner);
            if (keyScanner.keyScan2(codePressed, key.VK2_ESC_CODE2)) {
                int codePressed2 = codeIsPressed(max_time_limit_scanner);
                if (keyScanner.keyScan(codePressed2, key.VK_ESC_CODE2_UP)) { // up, y
                    for (int i = 0; i < entity_event.length; i++) {
                        if (entity_event[i] != null) {
                            if (entity_event[i][1] == row_main - 1 && entity_event[i][0] == collumn_main) {
                                row_main--;
                                break;
                            }
                        }
                    }
                } else if (keyScanner.keyScan(codePressed2, key.VK_ESC_CODE2_DOWN)) { // down, y
                    for (int i = 0; i < entity_event.length; i++) {
                        if (entity_event[i] != null) {
                            if (entity_event[i][1] == row_main + 1 && entity_event[i][0] == collumn_main) {
                                row_main++;
                                break;
                            }
                        }
                    }
                } else if (keyScanner.keyScan(codePressed2, key.VK_ESC_CODE2_LEFT)) { // left, x
                    for (int i = 0; i < entity_event.length; i++) {
                        if (entity_event[i] != null) {
                            if (entity_event[i][0] == collumn_main - 1 && entity_event[i][1] == row_main) {
                                collumn_main--;
                                break;
                            }
                        }
                    }
                } else if (keyScanner.keyScan(codePressed2, key.VK_ESC_CODE2_RIGHT)) { // right, x
                    for (int i = 0; i < entity_event.length; i++) {
                        if (entity_event[i] != null) {
                            if (entity_event[i][0] == collumn_main + 1 && entity_event[i][1] == row_main) {
                                collumn_main++;
                                break;
                            }
                        }
                    }
                }
            } else if (keyScanner.keyScan(codePressed, key.VK_ENTER)) {
                //enter
                enterOn = true;
            }
        }
        return codePressed;
    }

    private void taskHandler() {
        for (int i = 0; i < laterEvents.length; i++) {
            if (laterEvents[i] != null) {
                final Object[] args = laterEvents[i];
                if (args[0] != null && args[1] != null && args[2] != null) {
                    int currentTimeSleep = (int)args[2];
                    final int maxTimeSleep = (int)args[1];
                    if (currentTimeSleep >= maxTimeSleep) {
                        args[2] = 0;
                        ((Runnable)args[0]).run();
                    } else {
                        args[2] = currentTimeSleep + sleep_while + sleep_plus;
                    }
                }
            }
        }
    }

    /**
     * The main function for rendering all widget
     * <p>type int 2 - standart rendering</p>
     * <p>type int 1 - one rendering</p>
     * @param t
     */
    public void run(int t) {
        while (run_on) {
            taskHandler();
            if (reset == 1) {
                setClear();
            }
            controllerKeyBoardListenerDO_VIEW();
            final int codePressed = PressedInputMode_0_KeyFnMain();
            for (int i = 0; i < main_size; i++) {
                if (entity[i] != null) {
                    final TView ent = entity[i];
                    final int types = ent.getType();
                    if ((entity[i].getRes() == true || reset > 0) && ent.getOnfMain()) {
                        if (types == 0) {
                            //TLabel
                            final Object[] args = ent.getArgs();
                            final int width1 = ent.getWidth();

                            if ((int)args[0] != -1) {
                                if (ent.getText().length() > (int)args[0]) {
                                    throw new MaxLenTextException("error len text > width label in TLabel");
                                }
                            }

                            StringBuilder bufferOut = new StringBuilder();
                            bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H")
                                    .append("\033[38;5;").append(ent.getBg()).append(";48;5;").append(ent.getBg()).append("m")
                                    .append(" ".repeat(width1));

                            bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H")
                                    .append("\033[38;5;").append(ent.getFg()).append(";48;5;").append(ent.getBg()).append("m")
                                    .append(ent.getText())
                                    .append("\033[0m");

                            print(bufferOut.toString());
                        } else if (types == 1) {
                            //TText
                            StringBuilder bufferOut = new StringBuilder();
                            StringBuilder std_out = new StringBuilder();
                            std_out.append(ent.getText());
                            int i2 = 0;
                            int x0 = ent.getX();
                            int y0 = ent.getY();
                            Object[] args = ent.getArgs();
                            int[][] args1 = (int[][])args[0];
                            int sizeFGBG = args1.length;
                            final int width1 = ent.getWidth();
                            final int height1 = ent.getHeight();
                            String spaces = " ".repeat(width1);
                            
                            for (int i3 = 0; i3 < height1; i3++) {
                                bufferOut.append("\033[").append(y0).append(";").append(x0).append("H\033[38;5;").append(ent.getBg()).append(";48;5;").append(ent.getBg()).append("m").append(spaces);
                                y0++;
                            }

                            bufferOut.append("\033[0m");
                            x0 = ent.getX();
                            y0 = ent.getY();
                            bufferOut.append("\033[").append(y0).append(";").append(x0).append("H");
                            print(bufferOut.toString());
                            StringBuilder std_out_text = new StringBuilder();
                            for (int i3 = 0; i3 < ent.getText().length(); i3++) {
                                int fg = ent.getFg();
                                int bg = ent.getBg();
                                for (int i4 = 0; i4 < sizeFGBG; i4++) {
                                    if (args1[i4][0] <= i2 && i2 <= args1[i4][1]) {
                                        fg = args1[i4][2];
                                        bg = args1[i4][3];
                                        break;
                                    }
                                }

                                if (x0 == ent.getX() + ent.getWidth() || std_out.charAt(i3) == '\n') {
                                    x0 = ent.getX();
                                    y0++;
                                    std_out_text.append("\033[").append(y0).append(";").append(x0).append("H");
                                } else {
                                    std_out_text.append("\033[38;5;").append(fg).append(";48;5;").append(bg).append("m").append(std_out.charAt(i3));
                                    x0++;
                                }

                                i2++;
                            }

                            print(std_out_text.toString());
                        } else if (types == 2 && modeInputView == modeIO.GPM) {
                            //TButton
                            final Object[] args = ent.getArgs();
                            final int[] args1 = (int[])args[1];
                            entity_event[i] = new int[] {args1[0], args1[1]};
                            int x1 = ent.getX();
                            int y1 = ent.getY();
                            StringBuilder bufferOut = new StringBuilder();
                            String spaces = " ".repeat(ent.getWidth());
                            for (int h = 0; h < ent.getHeight(); h++) {
                                bufferOut.append("\033[").append(y1 + h).append(";").append(x1).append("H\033[38;5;").append(ent.getBg()).append(";48;5;").append(ent.getBg()).append("m").append(spaces);
                            }
                            if ((int)args[3] != -1) {
                                if (ent.getText().length() > (int)args[3]) {
                                    throw new MaxLenTextException("error len text > width label in TButton");
                                }
                            }
                            bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H\033[38;5;").append(ent.getFg()).append(";48;5;").append(ent.getBg()).append("m").append(ent.getText()).append("\033[0m");
                            print(bufferOut.toString());
                        } else if (types == 3 && modeInputView == modeIO.GPM) {
                            //TEntry
                            final Object[] args = ent.getArgs();
                            StringBuilder bufferOut = new StringBuilder();
                            bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H\033[38;5;").append(ent.getBg()).append(";48;5;").append(ent.getBg()).append("m").append(" ".repeat(ent.getWidth()));
                            StringBuilder text_b = new StringBuilder();
                            text_b.append(ent.getText());
                            StringBuilder text_out = new StringBuilder();
                            for (int x2 = 0; x2 < ent.getText().length(); x2++) {
                                if (x2 < ent.getWidth()) {
                                    text_out.append(text_b.charAt(x2));
                                } else {
                                    break;
                                }
                            }
                            bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H\033[38;5;").append(ent.getFg()).append(";48;5;").append(ent.getBg()).append("m").append(text_out.toString()).append("\033[0m");
                            print(bufferOut.toString());
                        } else if (types == 4 && modeInputView == modeIO.GPM) {
                            //TEntryText
                            final Object[] args = ent.getArgs();
                            StringBuilder std_out = new StringBuilder();
                            std_out.append(ent.getText());
                            int i2 = 0;
                            int x0 = ent.getX();
                            int y0 = ent.getY();
                            int[][] args1 = (int[][])args[11];
                            int sizeFGBG = args1.length;
                            StringBuilder bufferOut = new StringBuilder();
                            bufferOut.append("\033[").append(y0).append(";").append(x0).append("H\033[38;5;").append(ent.getBg()).append(";48;5;").append(ent.getBg()).append("m");
                            String spaces = " ".repeat(ent.getWidth());
                            for (int i3 = 0; i3 < ent.getHeight(); i3++) {
                                bufferOut.append("\033[").append(y0).append(";").append(x0).append("H").append(spaces);
                                y0++;
                            }
                            bufferOut.append("\033[0m");
                            x0 = ent.getX();
                            y0 = ent.getY();
                            bufferOut.append("\033[").append(y0).append(";").append(x0).append("H");
                            int[] args2 = (int[])args[12];
                            final int strXot = args2[0];
                            int strYot = 0;
                            if ((boolean)args[15]) {
                                final int len_text_m = ent.getText().split("\n").length;
                                if (len_text_m > ent.getHeight()) {
                                    strYot = len_text_m - ent.getHeight();
                                }
                            } else {
                                strYot = args2[1];
                            }
                            int strCurrentX = 0;
                            int strCurrentY = 0;
                            int y_1 = ent.getY();
                            String gtext = ent.getText();
                            StringBuilder StringTextOut = new StringBuilder();
                            int oldFG = 0;
                            int oldBG = 0;
                            for (int i3 = 0; i3 < ent.getText().length(); i3++) {
                                int fg = ent.getFg();
                                int bg = ent.getBg();
                                for (int i4 = 0; i4 < sizeFGBG; i4++) {
                                    if (args1[i4][0] <= i2 && i2 <= args1[i4][1]) {
                                        fg = args1[i4][2];
                                        bg = args1[i4][3];
                                        break;
                                    }
                                }
                                if (std_out.charAt(i3) == '\n') {
                                    x0 = ent.getX();
                                    if (strCurrentY >= strYot) {
                                        y0++;
                                    }
                                    strCurrentY++;
                                    strCurrentX = 0;
                                    StringTextOut.append("\033[").append(y0).append(";").append(x0).append("H");
                                } else {
                                    if (strCurrentY >= strYot) {
                                        if (x0 < ent.getX() + ent.getWidth() && y0 < ent.getY() + ent.getHeight()) {
                                            if (strCurrentX >= strXot) {
                                                if (fg != oldFG || bg != oldBG) {
                                                    StringTextOut.append("\033[38;5;").append(fg).append(";48;5;").append(bg).append("m");
                                                    oldBG = bg;
                                                    oldFG = fg;
                                                }
                                                StringTextOut.append(std_out.charAt(i3));
                                                x0++;
                                            }
                                            strCurrentX++;
                                        }
                                    }
                                }
                                i2++;
                            }
                            bufferOut.append(StringTextOut.toString());
                            print(bufferOut.toString());
                        } else if (types == 5) {
                            //TFrame
                            Object[] args = ent.getArgs();
                            final int onf1 = (int)args[0];
                            final int fg1 = ent.getFg();
                            final int bg1 = ent.getBg();
                            final int x1 = ent.getX();
                            final int y1 = ent.getY();
                            final int width1 = ent.getWidth();
                            final int height1 = ent.getHeight();
                            final String text1 = ent.getText();
                            final int styleLine = (int)args[1];
                            final CharLine.Type styleLine2 = switch (styleLine) {
                                case 0 -> CharLine.Type.I;
                                case 1 -> CharLine.Type.II;
                                case 3 -> CharLine.Type.III;
                                default -> CharLine.Type.I;
                            };
                            StringBuilder bufferOut = new StringBuilder();
                            int y2 = y1;
                            bufferOut.append("\033[38;5;").append(bg1).append(";48;5;").append(bg1).append("m");
                            String spaces = " ".repeat(width1);
                            for (int i2 = 0; i2 < height1; i2++) {
                                bufferOut.append("\033[").append(y2).append(";").append(x1).append("H").append(spaces);
                                y2++;
                            }
                            if (onf1 == 1 || onf1 == 2) {
                                bufferOut.append("\033[48;5;").append(bg1).append(";38;5;").append(fg1).append("m");
                                for (int i2 = 0; i2 < height1; i2++) {
                                    bufferOut.append("\033[").append(y1 + i2).append(";").append(x1).append("H").append(CharLine.LineV(styleLine2));
                                }
                                for (int i2 = 0; i2 < height1; i2++) {
                                    bufferOut.append("\033[").append(y1 + i2).append(";").append(x1 + width1).append("H").append(CharLine.LineV(styleLine2));
                                }
                                bufferOut.append("\033[").append(y1).append(";").append(x1).append("H").append(String.valueOf(CharLine.LineH(styleLine2)).repeat(width1));
                                bufferOut.append("\033[").append(y1 + height1).append(";").append(x1).append("H").append(String.valueOf(CharLine.LineH(styleLine2)).repeat(width1));
                                if (styleLine != 2) {
                                    bufferOut.append("\033[").append(y1).append(";").append(x1).append("H").append(CharLine.LineYDR(styleLine2));
                                    bufferOut.append("\033[").append(y1).append(";").append(x1 + width1).append("H").append(CharLine.LineYDL(styleLine2));
                                    bufferOut.append("\033[").append(y1 + height1).append(";").append(x1).append("H").append(CharLine.LineYUR(styleLine2));
                                    bufferOut.append("\033[").append(y1 + height1).append(";").append(x1 + width1).append("H").append(CharLine.LineYUL(styleLine2));
                                } else {
                                    bufferOut.append("\033[").append(y1).append(";").append(x1).append("H").append(CharLine.LineYDR2());
                                    bufferOut.append("\033[").append(y1).append(";").append(x1 + width1).append("H").append(CharLine.LineYDL2());
                                    bufferOut.append("\033[").append(y1 + height1).append(";").append(x1).append("H").append(CharLine.LineYUR2());
                                    bufferOut.append("\033[").append(y1 + height1).append(";").append(x1 + width1).append("H").append(CharLine.LineYUL2());
                                }
                            }
                            if (onf1 == 2) {
                                final int lenText = text1.length();
                                final int x2 = (int)(width1 / 2) - (int)(lenText / 2);
                                final int x2Out = x1 + x2;
                                bufferOut.append("\033[").append(y1).append(";").append(x2Out).append("H").append(text1);
                            }
                            print(bufferOut.toString());
                            print("\033[0m");
                        } else if (types == 6) {
                            //TCanvas
                            Object[] args = ent.getArgs();
                            final List<Object[]> listLines = (ArrayList<Object[]>)args[2];
                            final int onf1 = (int)args[0];
                            final int fg1 = ent.getFg();
                            final int bg1 = ent.getBg();
                            final int x1 = ent.getX();
                            final int y1 = ent.getY();
                            final int width1 = ent.getWidth();
                            final int height1 = ent.getHeight();
                            final String text1 = ent.getText();
                            final int styleLine = (int)args[1];
                            final CharLine.Type styleLine2 = switch (styleLine) {
                                case 0 -> CharLine.Type.I;
                                case 1 -> CharLine.Type.II;
                                case 3 -> CharLine.Type.III;
                                default -> CharLine.Type.I;
                            };
                            int x3 = x1;
                            int y3 = y1;
                            StringBuilder bufferOut = new StringBuilder();
                            int y2 = y1;
                            bufferOut.append("\033[38;5;").append(bg1).append(";48;5;").append(bg1).append("m");
                            int pad = 0;
                            if (onf1 == 1 || onf1 == 2) {
                                pad = 0;
                            } else {
                                pad = 1;
                            }
                            String spaces = " ".repeat(width1 - pad);
                            for (int i2 = 0; i2 < height1; i2++) {
                                bufferOut.append("\033[").append(y2).append(";").append(x1).append("H").append(spaces);
                                y2++;
                            }
                            if (onf1 == 1 || onf1 == 2) {
                                x3++;
                                y3++;
                                bufferOut.append("\033[48;5;").append(bg1).append(";38;5;").append(fg1).append("m");
                                for (int i2 = 0; i2 < height1; i2++) {
                                    bufferOut.append("\033[").append(y1 + i2).append(";").append(x1).append("H").append(CharLine.LineV(styleLine2));
                                }
                                for (int i2 = 0; i2 < height1; i2++) {
                                    bufferOut.append("\033[").append(y1 + i2).append(";").append(x1 + width1).append("H").append(CharLine.LineV(styleLine2));
                                }
                                bufferOut.append("\033[").append(y1).append(";").append(x1).append("H").append(String.valueOf(CharLine.LineH(styleLine2)).repeat(width1));
                                bufferOut.append("\033[").append(y1 + height1).append(";").append(x1).append("H").append(String.valueOf(CharLine.LineH(styleLine2)).repeat(width1));
                                if (styleLine != 2) {
                                    bufferOut.append("\033[").append(y1).append(";").append(x1).append("H").append(CharLine.LineYDR(styleLine2));
                                    bufferOut.append("\033[").append(y1).append(";").append(x1 + width1).append("H").append(CharLine.LineYDL(styleLine2));
                                    bufferOut.append("\033[").append(y1 + height1).append(";").append(x1).append("H").append(CharLine.LineYUR(styleLine2));
                                    bufferOut.append("\033[").append(y1 + height1).append(";").append(x1 + width1).append("H").append(CharLine.LineYUL(styleLine2));
                                } else {
                                    bufferOut.append("\033[").append(y1).append(";").append(x1).append("H").append(CharLine.LineYDR2());
                                    bufferOut.append("\033[").append(y1).append(";").append(x1 + width1).append("H").append(CharLine.LineYDL2());
                                    bufferOut.append("\033[").append(y1 + height1).append(";").append(x1).append("H").append(CharLine.LineYUR2());
                                    bufferOut.append("\033[").append(y1 + height1).append(";").append(x1 + width1).append("H").append(CharLine.LineYUL2());
                                }
                            }
                            if (onf1 == 2) {
                                final int lenText = text1.length();
                                final int x2 = (int)(width1 / 2) - (int)(lenText / 2);
                                final int x2Out = x1 + x2;
                                bufferOut.append("\033[").append(y1).append(";").append(x2Out).append("H").append(text1);
                            }
                            bufferOut.append("\033[0m\033[H");
                            for (Object[] i2 : listLines) {
                                if ((int)i2[0] == 1) {
                                    int boundY = 0;
                                    if (((int)args[0]) == 1 || ((int)args[0]) == 2) {
                                        boundY = 2;
                                    } else {
                                        boundY = 1;
                                    }
                                    if (((int)i2[1] > width1 - 2 || (int)i2[1] < 0) || ((int)i2[3] > (height1 * 2) - boundY || (int)i2[3] < 0)) {
                                    } else {
                                        boolean flg = true;
                                        if ((int)i2[3] == 1) {
                                            flg = false;
                                        }
                                        if ((int)i2[3] % 2 == 0 && flg) {
                                            bufferOut.append("\033[").append(y3 + (int)i2[2]).append(";").append(x3 + (int)i2[1]).append("H\033[38;5;").append((int)i2[5]).append(";48;5;").append((int)i2[4]).append("m").append(CharLine.BlockPL(CharLine.blockType.UP));
                                        } else {
                                            if ((int)i2[3] >= 1) {
                                                bufferOut.append("\033[").append(y3 + (int)i2[2]).append(";").append(x3 + (int)i2[1]).append("H\033[38;5;").append((int)i2[5]).append(";48;5;").append((int)i2[4]).append("m").append(CharLine.BlockPL(CharLine.blockType.UP));
                                            } else {
                                                bufferOut.append("\033[").append(y3 + (int)i2[2] - 1).append(";").append(x3 + (int)i2[1]).append("H\033[38;5;").append((int)i2[4]).append(";48;5;").append((int)i2[5]).append("m").append(CharLine.BlockPL(CharLine.blockType.UP));
                                            }
                                        }
                                    }
                                } else if ((int)i2[0] == 2) {
                                    final String text = (String)i2[3];
                                    StringBuilder textOut = new StringBuilder(text);
                                    if ((int)i2[1] + text.length() > width1) {
                                        textOut.delete(width1 - (int)i2[1] - 1, text.length());
                                    }
                                    int checkH = 0;
                                    if (((int)args[0]) == 1 || ((int)args[0]) == 2) {
                                        checkH = 1;
                                    } else {
                                        checkH = 0;
                                    }
                                    if ((int)i2[2] < height1 - checkH) {
                                        bufferOut.append("\033[").append(y3 + (int)i2[2]).append(";").append(x3 + (int)i2[1]).append("H\033[38;5;").append((int)i2[5]).append(";48;5;").append((int)i2[4]).append("m").append(textOut.toString());
                                    }
                                } else if ((int)i2[0] == 3) {
                                    if ((int)i2[2] < height1 && (int)i2[1] < width1 - 1) {
                                        int repLen = 0;
                                        if ((int)i2[3] > width1 - (int)i2[1]) {
                                            repLen = width1 - (int)i2[1];
                                        } else {
                                            repLen = (int)i2[3];
                                        }
                                        if (repLen < 0) {
                                            repLen = 0;
                                        } else {
                                            repLen = repLen - 1;
                                        }
                                        if (repLen < 0) repLen = 0;
                                        bufferOut.append("\033[").append(y3 + (int)i2[2]).append(";").append(x3 + (int)i2[1]).append("H\033[38;5;").append((int)i2[5]).append(";48;5;").append((int)i2[4]).append("m").append(String.valueOf(CharLine.LineH((CharLine.Type)i2[6])).repeat(repLen));
                                    }
                                } else if ((int)i2[0] == 4) {
                                    if ((int)i2[2] < height1 && (int)i2[1] < width1) {
                                        StringBuilder textOut = new StringBuilder();
                                        int limH = 0;
                                        if ((int)i2[2] + (int)i2[4] > height1) {
                                            limH = height1;
                                        } else {
                                            limH = (int)i2[2] + (int)i2[4];
                                        }
                                        int offH = 0;
                                        if (((int)args[0]) == 1 || ((int)args[0]) == 2) {
                                            offH = 1;
                                        } else {
                                            offH = 0;
                                        }
                                        for (int i3 = (int)i2[2]; i3 < limH - offH; i3++) {
                                            textOut.append("\033[").append(y3 + i3).append(";").append(x3 + (int)i2[1]).append("H").append(CharLine.LineV((CharLine.Type)i2[6]));
                                        }
                                        bufferOut.append("\033[").append(y3 + (int)i2[2]).append(";").append(x3 + (int)i2[1]).append("H\033[38;5;").append((int)i2[5]).append(";48;5;").append((int)i2[4]).append("m").append(textOut.toString());
                                    }
                                }
                            }
                            bufferOut.append("\033[0m");
                            print(bufferOut.toString());
                            print("\033[0m");
                        } else if (types == 7 && modeInputView == modeIO.GPM) {
                            //TSwitchButton
                            Object[] args = ent.getArgs();
                            final boolean onf1 = (boolean)args[5];
                            final char[] c1 = (char[])args[0];
                            final char cOut = onf1 ? c1[0] : c1[1];
                            StringBuilder bufferOut = new StringBuilder();
                            bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H\033[38;5;").append(ent.getFg()).append(";48;5;").append(ent.getBg()).append("m").append(cOut).append("\033[0m");
                            print(bufferOut.toString());
                        } else if (types == 8 && modeInputView == modeIO.GPM) {
                            //TScrollBar
                            Object[] args = ent.getArgs();
                            final TScrollBar.TypeOrient orient = (TScrollBar.TypeOrient)args[6];
                            int[] outm;
                            StringBuilder bufferOut = new StringBuilder();
                            switch (orient) {
                                case TScrollBar.TypeOrient.HORIZONTAL:
                                    bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H\033[38;5;").append(ent.getBg()).append(";48;5;").append(ent.getBg()).append("m");
                                    bufferOut.append(" ".repeat(ent.getWidth()));
                                    outm = STDD.out_1_scrollbar(ent.getWidth(), (int)args[2], (int)args[3]);
                                    bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX() + outm[0]).append("H\033[38;5;").append(ent.getFg()).append(";48;5;").append(ent.getBg()).append("m");
                                    bufferOut.append(String.valueOf(CharLine.Block50p()).repeat(outm[1]));
                                    break;
                                case TScrollBar.TypeOrient.VERTICAL:
                                    bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H\033[38;5;").append(ent.getBg()).append(";48;5;").append(ent.getBg()).append("m");
                                    for (int i2 = 0; i2 < ent.getWidth(); i2++) {
                                        bufferOut.append("\033[").append(ent.getY() + i2).append(";").append(ent.getX()).append("H ");
                                    }
                                    outm = STDD.out_1_scrollbar(ent.getWidth(), (int)args[2], (int)args[3]);
                                    bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H\033[38;5;").append(ent.getFg()).append(";48;5;").append(ent.getBg()).append("m");
                                    for (int i2 = outm[0]; i2 < outm[0] + outm[1]; i2++) {
                                        bufferOut.append("\033[").append((ent.getY() + ent.getWidth() - 1) - i2).append(";").append(ent.getX()).append("H").append(CharLine.Block50p());
                                    }
                                    break;
                            }
                            print(bufferOut.toString());
                            print("\033[0m");
                        } else if (types == 9 && modeInputView == modeIO.GPM) {
                            //TDropList
                            Object[] args = ent.getArgs();
                            final int wd = ent.getWidth();
                            final String[] listt = (String[])args[3];
                            StringBuilder textOut = new StringBuilder(listt[(int)args[5]]);
                            final int wdt = textOut.toString().length();
                            if (wdt > wd) {
                                textOut.delete(wd, wdt);
                            }
                            StringBuilder bufferOut = new StringBuilder();
                            bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H\033[38;5;").append(ent.getBg()).append(";48;5;").append(ent.getBg()).append("m");
                            bufferOut.append(" ".repeat(wd));
                            bufferOut.append("\033[").append(ent.getY()).append(";").append(ent.getX()).append("H\033[38;5;").append(ent.getFg()).append(";48;5;").append(ent.getBg()).append("m");
                            bufferOut.append(textOut.toString());
                            print(bufferOut.toString());
                            print("\033[0m");
                        }
                        ent.setRes(false);
                    }

                    int idsm = -1;
                    boolean flgIdsm = false;
                    if (entity_event.length > 0) {
                        for (int i2 = 0; i2 < entity_event.length; i2++) {
                            final int[] args1 = entity_event[i2];
                            if (args1 != null) {
                                if (args1[0] == collumn_main && args1[1] == row_main) {
                                    idsm = i2;
                                    flgIdsm = true;
                                    break;
                                }
                            }
                        }
                    }

                    TView ent2 = null;
                    int typeEnt2Main = -1;
                    try {
                        ent2 = entity[idsm];
                        typeEnt2Main = ent2.getType();
                    } catch (Exception e) {
                        flgIdsm = false;
                    }
                    if (flgIdsm && cursorOnf) {
                        if (ent2 != null) {
                            TView classEnt3 = null;
                            Object[] argsEnt3 = null;
                            if (viewClassOld != null) {
                                final int TypeEnt3 = viewClassOld.getType();
                                if (TypeEnt3 == 2) {
                                    argsEnt3 = ((TButton)viewClassOld).getArgs();
                                } else if (TypeEnt3 == 3) {
                                    argsEnt3 = ((TEntry)viewClassOld).getArgs();
                                } else if (TypeEnt3 == 4) {
                                    argsEnt3 = ((TEntryText)viewClassOld).getArgs();
                                } else if (TypeEnt3 == 7) {
                                    argsEnt3 = ((TSwitchButton)viewClassOld).getArgs();
                                } else if (TypeEnt3 == 8) {
                                    argsEnt3 = ((TScrollBar)viewClassOld).getArgs();
                                } else if (TypeEnt3 == 9) {
                                    argsEnt3 = ((TDropList)viewClassOld).getArgs();
                                }
                            }
                            if (viewClassOld != null) {
                                final TView classNonType = viewClassOld;
                                final int ClassType = classNonType.getType();
                                int fg = 7;
                                int bg = 0;
                                if (ClassType == 2) {
                                    final Runnable[] argsRun = (Runnable[])argsEnt3[0];
                                    if (viewClassOld != ent2) {
                                        classEnt3 = (TButton)viewClassOld;
                                        bg = ((TButton)classEnt3).getBg();
                                        fg = ((TButton)classEnt3).getFg();
                                        ((TButton)classEnt3).setFg(bg);
                                        ((TButton)classEnt3).setBg(fg);
                                        argsRun[2].run();
                                        argsEnt3[2] = 1;
                                    }
                                } else if (ClassType == 3) {
                                    final Runnable[] argsRun = (Runnable[])argsEnt3[3];
                                    if (viewClassOld != ent2) {
                                        classEnt3 = (TEntry)viewClassOld;
                                        bg = ((TEntry)classEnt3).getBg();
                                        fg = ((TEntry)classEnt3).getFg();
                                        ((TEntry)classEnt3).setFg(bg);
                                        ((TEntry)classEnt3).setBg(fg);
                                        argsRun[1].run();
                                        argsEnt3[4] = 1;
                                    }
                                } else if (ClassType == 4) {
                                    final Runnable[] argsRun = (Runnable[])argsEnt3[3];
                                    if (viewClassOld != ent2) {
                                        classEnt3 = (TEntryText)viewClassOld;
                                        bg = ((TEntryText)classEnt3).getBg();
                                        fg = ((TEntryText)classEnt3).getFg();
                                        ((TEntryText)classEnt3).setFg(bg);
                                        ((TEntryText)classEnt3).setBg(fg);
                                        argsRun[1].run();
                                        argsEnt3[4] = 1;
                                    }
                                } else if (ClassType == 7) {
                                    final Runnable[] argsRun = (Runnable[])argsEnt3[6];
                                    if (viewClassOld != ent2) {
                                        classEnt3 = (TSwitchButton)viewClassOld;
                                        bg = ((TSwitchButton)classEnt3).getBg();
                                        fg = ((TSwitchButton)classEnt3).getFg();
                                        ((TSwitchButton)classEnt3).setFg(bg);
                                        ((TSwitchButton)classEnt3).setBg(fg);
                                        argsRun[2].run();
                                        argsEnt3[4] = true;
                                    }
                                } else if (ClassType == 8) {
                                    final Runnable[] argsRun = (Runnable[])argsEnt3[0];
                                    if (viewClassOld != ent2) {
                                        classEnt3 = (TScrollBar)viewClassOld;
                                        bg = ((TScrollBar)classEnt3).getBg();
                                        fg = ((TScrollBar)classEnt3).getFg();
                                        ((TScrollBar)classEnt3).setFg(bg);
                                        ((TScrollBar)classEnt3).setBg(fg);
                                        argsRun[2].run();
                                        argsEnt3[5] = true;
                                    }
                                } else if (ClassType == 9) {
                                    final Runnable[] argsRun = (Runnable[])argsEnt3[0];
                                    if (viewClassOld != ent2) {
                                        classEnt3 = (TDropList)viewClassOld;
                                        bg = ((TDropList)classEnt3).getBg();
                                        fg = ((TDropList)classEnt3).getFg();
                                        ((TDropList)classEnt3).setFg(bg);
                                        ((TDropList)classEnt3).setBg(fg);
                                        argsRun[2].run();
                                        argsEnt3[7] = true;
                                    }
                                }
                                viewClassOld.setRes(true);
                            }

                            if (typeEnt2Main == 2) {
                                final TButton classEnt2 = (TButton)ent2;
                                final Object[] argsEnt2 = ent2.getArgs();
                                final Runnable[] argsRun = (Runnable[])argsEnt2[0];
                                final boolean onf = (boolean)argsEnt2[4];
                                ent2.setRes(true);
                                if (enterOn && onf) { // pressed click
                                    argsRun[0].run();
                                }
                                if (entity_event[idsm][0] == collumn_main && entity_event[idsm][1] == row_main) { //enter while
                                    argsRun[3].run();
                                }
                                if ((int)argsEnt2[2] == 1) { //enter
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setBg(fg);
                                    classEnt2.setFg(bg);
                                    argsRun[1].run();
                                    argsEnt2[2] = 0;
                                }
                                enterOn = false;
                                if (viewClassOld != ent2) {
                                    if (viewClassOld != null) {
                                        argsEnt3[2] = 1;
                                    }
                                }
                                if (viewClassOld != ent2) {
                                    viewClassOld = ent2;
                                }
                            } else if (typeEnt2Main == 3) {
                                final TEntry classEnt2 = (TEntry)ent2;
                                final Object[] argsEnt2 = ent2.getArgs();
                                final Runnable[] argsRun = (Runnable[])argsEnt2[3];
                                final boolean onf = (boolean)argsEnt2[10];
                                if ((boolean)argsEnt2[5] && onf) {
                                    ent2.setRes(true);
                                    final int codePressed11 = codeIsPressed(max_time_limit_scanner);
                                    if ((boolean)argsEnt2[9]) {
                                        input_mode_1_text = new StringBuilder(classEnt2.getText());
                                        collumn_input_mode_1 = ent2.getX();
                                        input_mode_1_current_collumn_x_in = 0;
                                        input_mode_1_current_collumn_x_in_2 = 0;
                                        input_mode_1_max_collumn_x_in = ent2.getText().length();
                                        max_input_mode_1 = ent2.getX() + ent2.getWidth() - 1;
                                        argsEnt2[9] = false;
                                    }

                                    if (!keyScanner.keyScan2(codePressed11, key.VK2_NONE)) {
                                        boolean flg2 = false;
                                        if (keyScanner.keyScan(codePressed11, key.VK_ENTER)) {
                                            argsRun[1].run();
                                            final int fg = ent2.getFg();
                                            final int bg = ent2.getBg();
                                            classEnt2.setBg(fg);
                                            classEnt2.setFg(bg);
                                            classEnt2.setText(input_mode_1_text.toString());
                                            argsEnt2[9] = false;
                                            mode_input = 0;
                                            enterOn = false;
                                            argsEnt2[5] = false;
                                            argsEnt2[2] = input_mode_1_current_collumn_x_in;
                                            flg2 = true;
                                        } else if (keyScanner.keyScan(codePressed11, key.VK_ESC)) {
                                            final int codePressed12 = codeIsPressed(max_time_limit_scanner);
                                            if (keyScanner.keyScan2(codePressed12, key.VK2_ESC_CODE2)) {
                                                final int codePressed13 = codeIsPressed(max_time_limit_scanner);
                                                if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_LEFT)) {
                                                    if (collumn_input_mode_1 > min_input_mode_1) {
                                                        collumn_input_mode_1--;
                                                    } else {
                                                        if (input_mode_1_current_collumn_x_in_2 > 0) {
                                                            input_mode_1_current_collumn_x_in_2--;
                                                        }
                                                    }
                                                    if (input_mode_1_current_collumn_x_in > 0) {
                                                        input_mode_1_current_collumn_x_in--;
                                                    }
                                                } else if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_RIGHT)) {
                                                    if (collumn_input_mode_1 < max_input_mode_1) {
                                                        if (input_mode_1_current_collumn_x_in < input_mode_1_max_collumn_x_in) {
                                                            collumn_input_mode_1++;
                                                        }
                                                    } else {
                                                        if (input_mode_1_current_collumn_x_in < input_mode_1_max_collumn_x_in) {
                                                            input_mode_1_current_collumn_x_in_2++;
                                                        }
                                                    }
                                                    if (input_mode_1_current_collumn_x_in < input_mode_1_max_collumn_x_in) {
                                                        input_mode_1_current_collumn_x_in++;
                                                    }
                                                }
                                            }
                                            flg2 = true;
                                        } else if (keyScanner.keyScan2(codePressed11, key.VK2_BACKSPACE)) {
                                            if (input_mode_1_current_collumn_x_in > 0) {
                                                if (collumn_input_mode_1 > min_input_mode_1 + 1) {
                                                    collumn_input_mode_1--;
                                                } else {
                                                    input_mode_1_current_collumn_x_in_2--;
                                                    if (input_mode_1_current_collumn_x_in_2 < 0) {
                                                        input_mode_1_current_collumn_x_in_2 = 0;
                                                        collumn_input_mode_1 = min_input_mode_1;
                                                    }
                                                }
                                                input_mode_1_current_collumn_x_in--;
                                                input_mode_1_max_collumn_x_in--;
                                                input_mode_1_text.deleteCharAt(input_mode_1_current_collumn_x_in);
                                            }
                                            flg2 = true;
                                        } else if (codePressed11 >= 32) {
                                            boolean flg1 = false;
                                            if (input_mode_1_max_width_text == -1) {
                                                flg1 = true;
                                            } else {
                                                if (input_mode_1_text.toString().length() < input_mode_1_max_width_text + 1) {
                                                    flg1 = true;
                                                }
                                            }
                                            if (flg1) {
                                                input_mode_1_text.insert(input_mode_1_current_collumn_x_in, (char)codePressed11);
                                                input_mode_1_current_collumn_x_in++;
                                                input_mode_1_max_collumn_x_in++;
                                                if (collumn_input_mode_1 < max_input_mode_1) {
                                                    collumn_input_mode_1++;
                                                } else {
                                                    input_mode_1_current_collumn_x_in_2++;
                                                }
                                                flg2 = true;
                                            }
                                        }
                                        if (flg2) {
                                            ((Consumer<Character>) argsEnt2[7]).accept((char) codePressed11);
                                        }
                                        if (mode_input == 1) {
                                            String output_text = input_mode_1_text.substring(input_mode_1_current_collumn_x_in_2).toString();
                                            classEnt2.setText(output_text);
                                            ent2.setRes(true);
                                            argsEnt2[9] = false;
                                        }
                                    }
                                    print("\033[" + ent2.getY() + ";" + collumn_input_mode_1 + "H\033[38;5;" + (int)argsEnt2[6] + ";48;5;" + (int)argsEnt2[6] + "m \033[0m");
                                }
                                if (enterOn && onf) {
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setBg(fg);
                                    classEnt2.setFg(bg);
                                    mode_input = 1;
                                    argsEnt2[5] = true;
                                    input_mode_1_x_cursor = 0;
                                    input_mode_1_text = new StringBuilder();
                                    input_mode_1_text.append(ent2.getText());
                                    max_input_mode_1 = ent2.getX() + ent2.getWidth() - 1;
                                    min_input_mode_1 = ent2.getX();
                                    collumn_input_mode_1 = ent2.getX();
                                    input_mode_1_current_collumn_x_in = 0;
                                    input_mode_1_max_collumn_x_in = ent2.getText().length();
                                    input_mode_1_current_collumn_x_in_2 = 0;
                                    input_mode_1_max_width_text = (int)argsEnt2[8];
                                }
                                if (entity_event[idsm][0] == collumn_main && entity_event[idsm][1] == row_main) {
                                    argsRun[2].run();
                                }
                                if ((int)argsEnt2[4] == 1) {
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setBg(fg);
                                    classEnt2.setFg(bg);
                                    argsRun[0].run();
                                    argsEnt2[4] = 0;
                                }
                                enterOn = false;
                                if (viewClassOld != ent2) {
                                    viewClassOld = ent2;
                                }
                            } else if (typeEnt2Main == 4) {
                                final TEntryText classEnt2 = (TEntryText)ent2;
                                final Object[] argsEnt2 = ent2.getArgs();
                                final Runnable[] argsRun = (Runnable[])argsEnt2[3];
                                final boolean onf = (boolean)argsEnt2[14];
                                if ((boolean)argsEnt2[5] && onf) {
                                    final int codePressed11 = codeIsPressed(max_time_limit_scanner);

                                    if ((boolean)argsEnt2[13]) {
                                        collumn_input_mode_1 = ent2.getX();
                                        input_mode_1_current_collumn_x_in = 0;
                                        input_mode_1_current_collumn_x_in_2 = 0;
                                        row_input_mode_2 = ent2.getY();
                                        input_mode_2_current_row_y_in = 0;
                                        input_mode_2_current_row_y_in_2 = 0;
                                        final String[] m1 = ent2.getText().split("\n");
                                        input_mode_2_max_row_y_in = m1.length - 1;
                                        input_mode_1_max_collumn_x_in = 0;
                                        max_input_mode_1 = ent2.getX() + ent2.getWidth() - 1;
                                        argsEnt2[13] = false;
                                        argsEnt2[12] = new int[] {input_mode_1_current_collumn_x_in_2, input_mode_2_current_row_y_in_2};
                                    }

                                    input_mode_2_text = ent2.getText();
                                    final int idsm2 = input_mode_2_current_row_y_in;
                                    String[] textList = input_mode_2_text.split("\n", -1);
                                    input_mode_2_text_2 = new StringBuilder(textList[idsm2]);
                                    input_mode_1_max_collumn_x_in = input_mode_2_text_2.toString().length();
                                    boolean flg4 = false;
                                    if (!keyScanner.keyScan2(codePressed11, key.VK2_NONE)) {
                                        boolean flg2 = false;
                                        boolean flg3 = true;
                                        flg4 = true;
                                        if (keyScanner.keyScan(codePressed11, key.VK_ENTER)) {
                                            flg3 = false;
                                            String currentLine = input_mode_2_text_2.toString();
                                            String textBeforeCursor = currentLine.substring(0, input_mode_1_current_collumn_x_in);
                                            String textAfterCursor = currentLine.substring(input_mode_1_current_collumn_x_in);

                                            String[] textList_2 = new String[textList.length + 1];
                                            for (int x3 = 0; x3 < input_mode_2_current_row_y_in; x3++) {
                                                textList_2[x3] = textList[x3];
                                            }
                                            textList_2[input_mode_2_current_row_y_in] = textBeforeCursor;
                                            textList_2[input_mode_2_current_row_y_in + 1] = textAfterCursor;
                                            for (int x3 = input_mode_2_current_row_y_in + 2; x3 < textList_2.length; x3++) {
                                                textList_2[x3] = textList[x3 - 1];
                                            }

                                            input_mode_2_current_row_y_in++;
                                            collumn_input_mode_1 = min_input_mode_1;
                                            input_mode_1_current_collumn_x_in = 0;
                                            input_mode_1_current_collumn_x_in_2 = 0;
                                            input_mode_2_max_row_y_in++;

                                            if (row_input_mode_2 < max_input_mode_2_y) {
                                                row_input_mode_2++;
                                            } else {
                                                input_mode_2_current_row_y_in_2++;
                                            }

                                            String output_text = String.join("\n", textList_2);
                                            classEnt2.setText(output_text);
                                            argsEnt2[12] = new int[] {input_mode_1_current_collumn_x_in_2, input_mode_2_current_row_y_in_2};
                                            ent2.setRes(true);
                                            argsEnt2[13] = false;
                                        } else if (keyScanner.keyScan(codePressed11, key.VK_ESC)) {
                                            final int codePressed12 = codeIsPressed(max_time_limit_scanner);
                                            if (keyScanner.keyScan2(codePressed12, key.VK2_NONE)) {
                                                //exit if click esc
                                                argsRun[1].run();
                                                final int fg = ent2.getFg();
                                                final int bg = ent2.getBg();
                                                classEnt2.setBg(fg);
                                                classEnt2.setFg(bg);
                                                argsEnt2[12] = new int[] {0, 0};
                                                mode_input = 0;
                                                enterOn = false;
                                                argsEnt2[5] = false;
                                                argsEnt2[2] = input_mode_1_current_collumn_x_in;
                                                flg2 = true;
                                            } else if (keyScanner.keyScan2(codePressed12, key.VK2_ESC_CODE2)) {
                                                final int codePressed13 = codeIsPressed(max_time_limit_scanner);
                                                if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_HOME)) {
                                                    //home
                                                    collumn_input_mode_1 = ent2.getX();
                                                    input_mode_1_current_collumn_x_in_2 = 0;
                                                    input_mode_1_current_collumn_x_in = 0;
                                                } else if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_END) && false) { // error
                                                    //end
                                                    input_mode_2_outOnMain = false;
                                                    final int textLen = input_mode_2_text_2.toString().length();
                                                    if (textLen > ent2.getWidth() - 4) {
                                                        collumn_input_mode_1 = ent2.getX() + 1;
                                                        input_mode_1_current_collumn_x_in_2 = textLen - 1;
                                                    } else {
                                                        input_mode_1_current_collumn_x_in_2 = 0;
                                                        collumn_input_mode_1 = ent2.getX() + textLen;
                                                    }
                                                    input_mode_1_current_collumn_x_in = textLen;
                                                } else if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_LEFT)) {
                                                    //left
                                                    if (collumn_input_mode_1 > min_input_mode_1) {
                                                        collumn_input_mode_1--;
                                                    } else {
                                                        if (input_mode_1_current_collumn_x_in_2 > 0) {
                                                            input_mode_1_current_collumn_x_in_2--;
                                                        }
                                                    }
                                                    if (input_mode_1_current_collumn_x_in > 0) {
                                                        input_mode_1_current_collumn_x_in--;
                                                    }
                                                } else if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_RIGHT)) {
                                                    //rigth
                                                    if (collumn_input_mode_1 < max_input_mode_1) {
                                                        if (input_mode_1_current_collumn_x_in < input_mode_1_max_collumn_x_in) {
                                                            collumn_input_mode_1++;
                                                        }
                                                    } else {
                                                        if (input_mode_1_current_collumn_x_in < input_mode_1_max_collumn_x_in) {
                                                            input_mode_1_current_collumn_x_in_2++;
                                                        }
                                                    }
                                                    if (input_mode_1_current_collumn_x_in < input_mode_1_max_collumn_x_in) {
                                                        input_mode_1_current_collumn_x_in++;
                                                    }
                                                } else if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_DOWN)) {
                                                    //down
                                                    if (idsm2 + 1 < textList.length) {
                                                        final int length_text = textList[idsm2 + 1].length();
                                                        if (length_text < input_mode_1_current_collumn_x_in) {
                                                            if (input_mode_1_current_collumn_x_in > length_text) {
                                                                final int sizesWindow = ent2.getWidth();
                                                                input_mode_1_current_collumn_x_in_2 = length_text > (int)sizesWindow / 2 ? length_text - (int)sizesWindow / 2 : 0;
                                                                if (length_text > 0) {
                                                                    collumn_input_mode_1 = min_input_mode_1 + (length_text > (int)sizesWindow / 2 ? (int)sizesWindow / 2 : length_text);
                                                                } else {
                                                                    collumn_input_mode_1 = min_input_mode_1;
                                                                }
                                                            } else {
                                                                if (length_text - input_mode_1_current_collumn_x_in_2 > 0) {
                                                                    collumn_input_mode_1 = min_input_mode_1 + (length_text - input_mode_1_current_collumn_x_in_2);
                                                                } else {
                                                                    collumn_input_mode_1 = min_input_mode_1;
                                                                    input_mode_1_current_collumn_x_in_2 = 0;
                                                                }
                                                            }
                                                            input_mode_1_current_collumn_x_in = length_text;
                                                        }
                                                    }
                                                    if (row_input_mode_2 < max_input_mode_2_y) {
                                                        if (input_mode_2_current_row_y_in < input_mode_2_max_row_y_in) {
                                                            row_input_mode_2++;
                                                        }
                                                    } else {
                                                        if (input_mode_2_current_row_y_in < input_mode_2_max_row_y_in) {
                                                            input_mode_2_current_row_y_in_2++;
                                                        }
                                                    }
                                                    if (input_mode_2_current_row_y_in < input_mode_2_max_row_y_in) {
                                                        input_mode_2_current_row_y_in++;
                                                    }
                                                } else if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_UP)) {
                                                    //up
                                                    if (idsm2 - 1 > -1) {
                                                        final int length_text = textList[idsm2 - 1].length();
                                                        if (length_text < input_mode_1_current_collumn_x_in) {
                                                            if (input_mode_1_current_collumn_x_in > length_text) {
                                                                final int sizesWindow = ent2.getWidth();
                                                                input_mode_1_current_collumn_x_in_2 = length_text > (int)sizesWindow / 2 ? length_text - (int)sizesWindow / 2 : 0;
                                                                if (length_text > 0) {
                                                                    collumn_input_mode_1 = min_input_mode_1 + (length_text > (int)sizesWindow / 2 ? (int)sizesWindow / 2 : length_text);
                                                                } else {
                                                                    collumn_input_mode_1 = min_input_mode_1;
                                                                }
                                                            } else {
                                                                if (length_text - input_mode_1_current_collumn_x_in_2 > 0) {
                                                                    collumn_input_mode_1 = min_input_mode_1 + (length_text - input_mode_1_current_collumn_x_in_2);
                                                                } else {
                                                                    collumn_input_mode_1 = min_input_mode_1;
                                                                    input_mode_1_current_collumn_x_in_2 = 0;
                                                                }
                                                            }
                                                            input_mode_1_current_collumn_x_in = length_text;
                                                        }
                                                    }
                                                    if (row_input_mode_2 > min_input_mode_2_y) {
                                                        row_input_mode_2--;
                                                    } else {
                                                        if (input_mode_2_current_row_y_in_2 > 0) {
                                                            input_mode_2_current_row_y_in_2--;
                                                        }
                                                    }
                                                    if (input_mode_2_current_row_y_in > 0) {
                                                        input_mode_2_current_row_y_in--;
                                                    }
                                                }
                                            }
                                        } else if (keyScanner.keyScan2(codePressed11, key.VK2_BACKSPACE)) {
                                            if (input_mode_1_current_collumn_x_in > 0) {
                                                if (collumn_input_mode_1 > min_input_mode_1 + 1) {
                                                    collumn_input_mode_1--;
                                                } else {
                                                    input_mode_1_current_collumn_x_in_2--;
                                                    if (input_mode_1_current_collumn_x_in_2 < 0) {
                                                        input_mode_1_current_collumn_x_in_2 = 0;
                                                        collumn_input_mode_1 = min_input_mode_1;
                                                    }
                                                }
                                                input_mode_1_current_collumn_x_in--;
                                                input_mode_1_max_collumn_x_in--;
                                                input_mode_2_text_2.deleteCharAt(input_mode_1_current_collumn_x_in);
                                            } else {
                                                if (input_mode_2_current_row_y_in > 0 && input_mode_1_current_collumn_x_in == 0) {
                                                    StringBuilder textUp = new StringBuilder(textList[input_mode_2_current_row_y_in - 1]);
                                                    int size_1 = textList[input_mode_2_current_row_y_in - 1].length();
                                                    String[] textList_2 = new String[textList.length - 1];
                                                    for (int x3 = 0; x3 < input_mode_2_current_row_y_in - 1; x3++) {
                                                        textList_2[x3] = textList[x3];
                                                    }
                                                    String textUpOld = textUp.toString();
                                                    textUp.append(input_mode_2_text_2.toString());
                                                    textList_2[input_mode_2_current_row_y_in - 1] = textUp.toString();
                                                    for (int x3 = input_mode_2_current_row_y_in; x3 < textList_2.length; x3++) {
                                                        textList_2[x3] = textList[x3 + 1];
                                                    }

                                                    if (input_mode_1_current_collumn_x_in < textUpOld.toString().length()) {
                                                        if (textUpOld.toString().length() > ent2.getWidth() - 1) {
                                                            input_mode_1_current_collumn_x_in_2 = textUpOld.toString().length() - 1;
                                                            collumn_input_mode_1 = min_input_mode_1 + 1;
                                                        } else {
                                                            collumn_input_mode_1 = min_input_mode_1 + size_1;
                                                        }
                                                    }
                                                    
                                                    input_mode_2_current_row_y_in--;
                                                    input_mode_2_max_row_y_in--;
                                                    input_mode_1_current_collumn_x_in = size_1;
                                                    input_mode_1_max_collumn_x_in = input_mode_2_text_2.toString().length();

                                                    if (row_input_mode_2 > min_input_mode_2_y) {
                                                        row_input_mode_2--;
                                                    } else {
                                                        input_mode_2_current_row_y_in_2--;
                                                    }
                                                    
                                                    String output_text = String.join("\n", textList_2);
                                                    classEnt2.setText(output_text);
                                                    argsEnt2[12] = new int[] {input_mode_1_current_collumn_x_in_2, input_mode_2_current_row_y_in_2};
                                                    ent2.setRes(true);
                                                    argsEnt2[13] = false;
                                                    flg3 = false;
                                                }
                                            }
                                            flg2 = true;
                                        } else if (codePressed11 >= 32) {
                                            if (input_mode_2_outOnMain) {
                                                boolean flg1 = false;
                                                if (input_mode_1_max_width_text == -1) {
                                                    flg1 = true;
                                                } else {
                                                    if (input_mode_2_text_2.toString().length() < input_mode_1_max_width_text + 1) {
                                                        flg1 = true;
                                                    }
                                                }
                                                if (flg1) {
                                                    input_mode_2_text_2.insert(input_mode_1_current_collumn_x_in, (char)codePressed11);
                                                    input_mode_1_current_collumn_x_in++;
                                                    input_mode_1_max_collumn_x_in++;
                                                    if (collumn_input_mode_1 < max_input_mode_1) {
                                                        collumn_input_mode_1++;
                                                    } else {
                                                        input_mode_1_current_collumn_x_in_2++;
                                                    }
                                                    flg2 = true;
                                                }
                                            } else {
                                                input_mode_2_outOnMain = true;
                                            }
                                        }
                                        if (flg2) {
                                            ((Consumer<Character>) argsEnt2[7]).accept((char) codePressed11);
                                        }
                                        if (mode_input == 2 && flg3 && flg4) {
                                            textList[idsm2] = input_mode_2_text_2.toString();
                                            String output_text = String.join("\n", textList);
                                            classEnt2.setText(output_text + "\n");
                                            argsEnt2[12] = new int[] {input_mode_1_current_collumn_x_in_2, input_mode_2_current_row_y_in_2};
                                            ent2.setRes(true);
                                            argsEnt2[13] = false;
                                        }
                                    }
                                    print("\033[" + row_input_mode_2 + ";" + collumn_input_mode_1 + "H\033[38;5;" + (int)argsEnt2[6] + ";48;5;" + (int)argsEnt2[6] + "m \033[0m");
                                }
                                if (enterOn && onf) {
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setBg(fg);
                                    classEnt2.setFg(bg);
                                    mode_input = 2;
                                    argsEnt2[5] = true;
                                    input_mode_1_x_cursor = 0;
                                    input_mode_2_y_cursor = 0;
                                    input_mode_2_text = ent2.getText();
                                    max_input_mode_1 = ent2.getX() + ent2.getWidth() - 1;
                                    max_input_mode_2_y = ent2.getY() + ent2.getHeight() - 1;
                                    min_input_mode_1 = ent2.getX();
                                    min_input_mode_2_y = ent2.getY();
                                    collumn_input_mode_1 = ent2.getX();
                                    row_input_mode_2 = ent2.getY();
                                    input_mode_1_current_collumn_x_in = 0;
                                    input_mode_2_current_row_y_in = 0;
                                    input_mode_1_max_collumn_x_in = 0;
                                    final String[] m1 = ent2.getText().split("\n");
                                    input_mode_2_max_row_y_in = m1.length - 1;
                                    input_mode_1_current_collumn_x_in_2 = 0;
                                    input_mode_2_current_row_y_in_2 = 0;
                                    input_mode_1_max_width_text = (int)argsEnt2[8];
                                    input_mode_2_max_height_text = (int)argsEnt2[9];
                                }
                                if (entity_event[idsm][0] == collumn_main && entity_event[idsm][1] == row_main) {
                                    argsRun[2].run();
                                }
                                if ((int)argsEnt2[4] == 1) {
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setBg(fg);
                                    classEnt2.setFg(bg);
                                    argsRun[0].run();
                                    argsEnt2[4] = 0;
                                }
                                enterOn = false;
                                if (viewClassOld != ent2) {
                                    viewClassOld = ent2;
                                }
                            } else if (typeEnt2Main == 7) {
                                final TSwitchButton classEnt2 = (TSwitchButton)ent2;
                                final Object[] argsEnt2 = ent2.getArgs();
                                final Runnable[] argsRun = (Runnable[])argsEnt2[6];
                                final boolean onf2 = (boolean)argsEnt2[3];
                                final boolean onf3 = (boolean)argsEnt2[5];
                                if (enterOn && onf2) {
                                    argsRun[0].run();
                                    if (onf3) {
                                        argsEnt2[5] = false;
                                    } else {
                                        argsEnt2[5] = true;
                                    }
                                }
                                if ((boolean)argsEnt2[4]) {
                                    argsRun[1].run();
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setFg(bg);
                                    classEnt2.setBg(fg);
                                    argsEnt2[4] = false;
                                }
                                if (entity_event[idsm][0] == collumn_main && entity_event[idsm][1] == row_main) {
                                    argsRun[3].run();
                                }

                                enterOn = false;
                                if (viewClassOld != ent2) {
                                    viewClassOld = ent2;
                                }
                                ent2.setRes(true);
                            } else if (typeEnt2Main == 8) {
                                final TScrollBar classEnt2 = (TScrollBar)ent2;
                                final Object[] argsEnt2 = ent2.getArgs();
                                final Runnable[] argsRun = (Runnable[])argsEnt2[0];
                                final boolean onf2 = (boolean)argsEnt2[4];
                                if ((boolean)argsEnt2[7]) {
                                    final int codePressed11 = codeIsPressed(max_time_limit_scanner);
                                    if (!keyScanner.keyScan2(codePressed11, key.VK2_NONE)) {
                                        if (keyScanner.keyScan(codePressed11, key.VK_ENTER)) {
                                            //exit
                                            final int fg = ent2.getFg();
                                            final int bg = ent2.getBg();
                                            classEnt2.setFg(bg);
                                            classEnt2.setBg(fg);
                                            mode_input = 0;
                                            argsEnt2[7] = false;
                                        } else if (keyScanner.keyScan(codePressed11, key.VK_ESC)) {
                                            final int codePressed12 = codeIsPressed(max_time_limit_scanner);
                                            if (keyScanner.keyScan2(codePressed12, key.VK2_ESC_CODE2)) {
                                                final int codePressed13 = codeIsPressed(max_time_limit_scanner);
                                                if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_DOWN) || keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_LEFT)) {
                                                    if ((int)argsEnt2[3] > 0) {
                                                        argsEnt2[3] = ((int)argsEnt2[3]) - 1;
                                                    }
                                                    argsRun[4].run();
                                                } else if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_UP) || keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_RIGHT)) {
                                                    if ((int)argsEnt2[3] < (int)argsEnt2[2]) {
                                                        argsEnt2[3] = ((int)argsEnt2[3]) + 1;
                                                    }
                                                    argsRun[4].run();
                                                }
                                            }
                                        }
                                    }
                                }
                                if (enterOn && onf2) {
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setFg(bg);
                                    classEnt2.setBg(fg);
                                    argsRun[0].run();
                                    mode_input = 3;
                                    argsEnt2[7] = true;
                                }
                                if ((boolean)argsEnt2[5]) {
                                    argsRun[1].run();
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setFg(bg);
                                    classEnt2.setBg(fg);
                                    argsEnt2[5] = false;
                                }
                                if (entity_event[idsm][0] == collumn_main && entity_event[idsm][1] == row_main) {
                                    argsRun[3].run();
                                }

                                enterOn = false;
                                if (viewClassOld != ent2) {
                                    viewClassOld = ent2;
                                }
                                ent2.setRes(true);
                            } else if (typeEnt2Main == 9) {
                                final TDropList classEnt2 = (TDropList)ent2;
                                final Object[] argsEnt2 = ent2.getArgs();
                                final Runnable[] argsRun = (Runnable[])argsEnt2[0];
                                final boolean onf2 = (boolean)argsEnt2[4];
                                if ((boolean)argsEnt2[6]) {
                                    final int codePressed11 = codeIsPressed(max_time_limit_scanner);
                                    if (!keyScanner.keyScan2(codePressed11, key.VK2_NONE)) {
                                        if (keyScanner.keyScan(codePressed11, key.VK_ENTER)) {
                                            //exit
                                            final int fg = ent2.getFg();
                                            final int bg = ent2.getBg();
                                            classEnt2.setFg(bg);
                                            classEnt2.setBg(fg);
                                            mode_input = 0;
                                            argsEnt2[6] = false;
                                        } else if (keyScanner.keyScan(codePressed11, key.VK_ESC)) {
                                            final int codePressed12 = codeIsPressed(max_time_limit_scanner);
                                            if (keyScanner.keyScan2(codePressed12, key.VK2_ESC_CODE2)) {
                                                final int codePressed13 = codeIsPressed(max_time_limit_scanner);
                                                if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_DOWN)) {
                                                    if ((int)argsEnt2[5] < ((String[])argsEnt2[3]).length - 1) {
                                                        argsEnt2[5] = ((int)argsEnt2[5]) + 1;
                                                    }
                                                    argsRun[4].run();
                                                } else if (keyScanner.keyScan(codePressed13, key.VK_ESC_CODE2_UP)) {
                                                    if ((int)argsEnt2[5] > 0) {
                                                        argsEnt2[5] = ((int)argsEnt2[5]) - 1;
                                                    }
                                                    argsRun[4].run();
                                                }
                                            }
                                        }
                                    }
                                }
                                if (enterOn && onf2) {
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setFg(bg);
                                    classEnt2.setBg(fg);
                                    argsRun[0].run();
                                    mode_input = 4;
                                    argsEnt2[6] = true;
                                }
                                if ((boolean)argsEnt2[7]) {
                                    argsRun[1].run();
                                    final int fg = ent2.getFg();
                                    final int bg = ent2.getBg();
                                    classEnt2.setFg(bg);
                                    classEnt2.setBg(fg);
                                    argsEnt2[7] = false;
                                }
                                if (entity_event[idsm][0] == collumn_main && entity_event[idsm][1] == row_main) {
                                    argsRun[3].run();
                                }

                                enterOn = false;
                                if (viewClassOld != ent2) {
                                    viewClassOld = ent2;
                                }
                                ent2.setRes(true);
                            }
                        }
                    }
                }
                old_collumn_main = collumn_main;
                old_row_main = row_main;
            }
            if (reset > 0) {
                reset--;
            }
            if (t == 0) {
                break;
            }
            controllerKeyBoardListenerEND_VIEW();
            enterOn = false;
            flush(); 
            Time.ThreadSleep(sleep_while);
        }
    }

    /**
     * the function for off a program
     */
    public void shutDownNow() {
        run_on = false;
        setCursorOn(true);
        print("\033[2J\033[H\033[0m");
        System.exit(0);
    }
}

class STDD {
    public static int[] out_1_scrollbar(int w_bar, int n_total, int n_current) {
        if (n_total <= 1) {
            return new int[] {0, w_bar};
        }
        if (n_current < 0) n_current = 0;
        if (n_current >= n_total) n_current = n_total - 1;
        int width = (int) Math.round((double) w_bar / n_total);
        if (width < 1) width = 1;
        int available_space = w_bar - width;
        int max_scroll_value = n_total - 1;
        int x_start = (n_current * available_space) / max_scroll_value;

        return new int[] {x_start, width};
    }
}