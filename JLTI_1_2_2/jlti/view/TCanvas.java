package jlti.view;

import jlti.ExceptionError.CoordsMinException;
import jlti.annotation.Overload;
import jlti.util.Color.colorANSI;
import jlti.util.text.CharLine;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class TCanvas implements TView {
    protected boolean reset = false;
    protected String text = "";
    protected int fg, bg;
    protected int x, y;
    protected int width, height;
    protected Object[] args;
    protected int ids_main;
    protected boolean OnfMain = true;

    private Map<Object[], String> tags = new HashMap<>();

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

    public TCanvas() {
        args = new Object[3];
        args[0] = 0; //on window [0 - off window and text, 1 - on window, 2 - on window and text]
        args[1] = 0; // style line [0 - standart line cube, 1 - line cube 2, 2 - line cube and corner no cube]
        args[2] = new ArrayList<>(); // list canvas {[type(1), x, y, y2, p1bg, p2fg], [type(2), x, y, text, bg ,fg], [type(3, 4), x, y, width, bg, fg, styleLine]}
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
    public enum canvasStyle {
        line(0),
        lineDv(1),
        lineAndCorner(2),
        line2(3);

        private final int out;
        canvasStyle(int i) {
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
    public enum canvasWindowOnf {
        off(0),
        window(1),
        windowAndText(2);

        private final int out;
        canvasWindowOnf(int i) {
            out = i;
        }

        public int readInt() {
            return out;
        }
    }

    /**
     * the function for settings all param
     * @param text name window
     * @param x coordinate x
     * @param y coordinate y
     * @param bg Background color the widget
     * @param fg Foreground color the widget
     * @param width width the widget
     * @param height height the widget
     * @param windowon settings on/off window and text
     * @param styleLine settings style line window
     */
    public void setAll(String text, int x, int y, colorANSI bg, colorANSI fg, int width, int height, canvasWindowOnf windowon, canvasStyle styleLine) {
        if (x + 1 < 1 || y + 1 < 1) {
            throw new CoordsMinException("TCanvas <X> <Y>");
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
     * the function for createPixelLine
     * @param x ot x
     * @param y ot y
     * @param x2 do x
     * @param y2 do y
     * @param bg Background color
     */
    public void createPixelLine(int x, int y, int x2, int y2, colorANSI bg) {
        final int dx = Math.abs(x2 - x);
        final int dy = Math.abs(y2 - y);
        
        int sx = (x < x2) ? 1 : -1;
        int sy = (y < y2) ? 1 : -1;
        
        int err = dx - dy;
        int xc = x;
        int yc = y;
        
        while (true) {
            createPixel(xc, yc, bg);
            
            if (xc == x2 && yc == y2) {
                break;
            }
            
            int e2 = 2 * err;
            
            if (e2 > -dy) {
                err = err - dy;
                xc += sx;
            }
            
            if (e2 < dx) {
                err = err + dx;
                yc += sy; 
            }
        }
    }

    /**
     * the function for createPixelRectangle
     * @param x ot x
     * @param y ot y
     * @param width width the rectangle
     * @param height height the rectangle
     * @param bg Background color
     * @param fg Foreground color
     * @param ramcOn frame on/off
     */
    public void createPixelRectangle(int x, int y, int width, int height, colorANSI bg, colorANSI fg, boolean ramcOn) {
        for (int x1 = x; x1 < x + width; x1++) {
            for (int y1 = y; y1 < y + height; y1++) {
                createPixel(x1, y1, bg);
            }
        }
        if (ramcOn) {
            for (int x1 = x; x1 < x + width; x1++) {
                createPixel(x1, y, fg);
            }
            for (int x1 = x; x1 < x + width; x1++) {
                createPixel(x1, y + height - 1, fg);
            }
            for (int y1 = y; y1 < y + height; y1++) {
                createPixel(x, y1, fg);
            }
            for (int y1 = y; y1 < y + height; y1++) {
                createPixel(x + width - 1, y1, fg);
            }
        }

        reset = true;
    }

    /**
     * the function for set permission image
     * off - standart permission image
     * SET - sett permission
     */
    public enum setSizeImage {
        SET(-1, -1) {
            @Override
            public int[] off() {
                return new int[] {-1, -1};
            }

            @Override
            public int[] size(int x, int y) {
                return new int[] {x, y};
            }
        };

        public int[] size(int x, int y) {
            return new int[] {-1, -1};
        }

        public int[] off() {
            return new int[] {-1, -1};
        }

        private final int xx, yy;
        setSizeImage(int x, int y) {
            this.xx = x;
            this.yy = y;
        }

        public int[] read() {
            return new int[] {xx, yy};
        }
    }

    /**
     * the function for createImage
     * @param path path image
     * @param x0 coordinate x
     * @param y0 coordinate y
     * @param sizes {@link setSizeImage}
     */
    public void createImage(String path, int x0, int y0, int[] sizes) {
        final int maxWidth = sizes[0];
        final int maxHeight = sizes[1];
        final boolean flg1 = maxWidth == -1 && maxHeight == -1 ? false : true;
        try {
            File f = new File(path);
            BufferedImage img = ImageIO.read(f);

            final int width = img.getWidth();
            final int height = img.getHeight();

            for (int x = 0; x < width; x++) {
                if (x > maxWidth && flg1) break;
                for (int y = 0; y < height; y++) {
                    if (y > maxHeight && flg1) break;
                    int color = img.getRGB(x, y);

                    int alpha = (color >> 24) & 0xff;
                    int red   = (color >> 16) & 0xff;
                    int green = (color >> 8)  & 0xff;
                    int blue  =  color        & 0xff;

                    createPixel(x0 + x, y0 + y, colorANSI.customColor.setRGB(red, green, blue));
                }
            }
        } catch (IOException e) {
            System.out.println("\033[H\033[0m not image in canvas");
            System.exit(0);
        }
    }

    /**
     * the function for createPixel
     * @param x coordinate x
     * @param y coordinate y
     * @param bg Background color
     */
    public void createPixel(int x, int y, colorANSI bg) {
        final ArrayList<Object[]> list = (ArrayList<Object[]>)args[2];
        final int x1 = x;
        int y1;
        Object[] ch = null;
        boolean flg1 = false;
        if (y % 2 == 0) {
            flg1 = true;
            y1 = (int)(y / 2);
        } else {
            y1 = (int)(y / 2);
        }
        if (y < 0) {
            flg1 = false;
            y1 = 1;
        }

        int ii = 0;
        boolean flg = false;
        for (Object[] i : list) {
            if ((int)i[0] == 1) {
                if ((int)i[1] == x && (int)i[2] == y1) {
                    ch = i;
                    list.remove(ii);
                    flg = true;
                    break;
                }
            }
            ii++;
        }

        if (flg) {
            if (flg1) {
                ((ArrayList<Object[]>)args[2]).add(new Object[] {1, x, y1, y, ch[4], bg.readColor()});
            } else {
                ((ArrayList<Object[]>)args[2]).add(new Object[] {1, x, y1, y, bg.readColor(), ch[5]});
            }
        } else {
            if (flg1) {
                ((ArrayList<Object[]>)args[2]).add(new Object[] {1, x, y1, y, this.bg, bg.readColor()});
            } else {
                ((ArrayList<Object[]>)args[2]).add(new Object[] {1, x, y1, y, bg.readColor(), this.bg});
            }
        }

        reset = true;
    }

    /**
     * the function for createText
     * @param x coordinate x
     * @param y coordinate y
     * @param text text
     * @param bg Background color text
     * @param fg Foreground color text
     */
    public void createText(int x, int y, String text, colorANSI bg, colorANSI fg) {
        ((ArrayList<Object[]>)args[2]).add(new Object[] {2, x, y, text, bg.readColor(), fg.readColor()});
    }

    /**
     * the function for createTxLineH
     * <p>text line horizontal</p>
     * @param x coordinate x
     * @param y coordinate y
     * @param width width line
     * @param bg Background color line
     * @param fg Foreground color line
     * @param style style line
     */
    public void createTxLineH(int x, int y, int width, colorANSI bg, colorANSI fg, CharLine.Type style) {
        ((ArrayList<Object[]>)args[2]).add(new Object[] {3, x, y, width, bg.readColor(), fg.readColor(), style});
    }

    /**
     * the function for createTxLineV
     * <p>text line vertical</p>
     * @param x coordinate x
     * @param y coordinate y
     * @param height width line
     * @param bg Background color line
     * @param fg Foreground color line
     * @param style style line
     */
    public void createTxLineV(int x, int y, int height, colorANSI bg, colorANSI fg, CharLine.Type style) {
        final Object[] out = new Object[] {4, x, y, height, bg.readColor(), fg.readColor(), style};
        ((ArrayList<Object[]>)args[2]).add(out);
    }

    public void cleanCanvas() {
        args[2] = new ArrayList<>();
        reset = true;
    }

    /**
     * the function for set style line the canvas
     * @param i {@link canvasWindowOnf}
     */
    public void setStyleLine(canvasWindowOnf i) {
        args[1] = i.readInt();
        reset = true;
    }

    /**
     * the function for set style window the canvas
     * @param i {@link canvasStyle}
     */
    public void setWindowOnf(canvasStyle i) {
        args[0] = i.readInt();
        reset = true;
    }

    /**
     * the function for set width for the widget
     * @param i width
     */
    public void setWidth(int i) {
        width = i;
        reset = true;
    }

    /**
     * the function for set height for the widget
     * @param i height
     */
    public void setHeight(int i) {
        height = i;
        reset = true;
    }

    /**
     * the function for set text for the widget
     * @param text text string
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
     * the function for set coordinate x for the widget
     * @param x coordinate x
     */
    public void setX(int x) {
        if (x + 1 < 1) {
            throw new CoordsMinException("TCanvas <X>");
        }
        this.x = x + 1;
        reset = true;
    }

    /**
     * the function for set coordinate y for the widget
     * @param y coordinate y
     */
    public void setY(int y) {
        if (y + 1 < 1) {
            throw new CoordsMinException("TCanvas <Y>");
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
        return 6;
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