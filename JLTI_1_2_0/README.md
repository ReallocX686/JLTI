# JLTI (java legacy terminal interface)

create graphics interface on java terminal

## information and version
* **version:** 1.2.0
* **date create:** 27.08.2026
* **status:** release - 31.08.2026

## download
setting and configuration file pom.xml

//JLIT dir in src/main/java

set configuration in file `pom.xml`

1:dependencies
```xml

  <dependencies>
    <dependency>
        <groupId>org.jline</groupId>
        <artifactId>jline</artifactId>
        <version>3.25.1</version>
    </dependency>
    <dependency>
        <groupId>org.fusesource.jansi</groupId>
        <artifactId>jansi</artifactId>
        <version>2.4.1</version>
    </dependency>
  </dependencies>
```

2:properties
```xml
 <properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
  </properties>
```

## Usage

## widget:
  * **TButton**
  * **TEntry**
  * **TEntryText**
  * **TCanvas**
  * **TFrame**
  * **TLabel**
  * **TScrollBar**
  * **TDropList**
  * **TText**
  * **TSwitchButton**
  
  interface - TView

```java
package com.example;

import jlti.app.interfaceAppMain;
import jlti.util.Time;
import jlti.view.TView;
import jlti.view.TButton;
import jlti.view.TLabel;
import jlti.view.TText;
import jlti.view.TEntry;
import jlti.view.TEntryText;
import jlti.view.TFrame;
import jlti.view.TCanvas;
import jlti.util.Color.ColorViewText;
import jlti.util.Color.colorANSI;
import jlti.util.text.CharLine;
import jlti.view.TSwitchButton;
import jlti.view.TScrollBar;
import jlti.view.TDropList;
import jlti.io.keyboardOutput.keyboardListener;
import jlti.io.keyboardOutput.keyboardCode;

public class App {
    public static void main(String[] args) {
        t1 t = new t1();
    }
}

class t1 {
    public interfaceAppMain app;
    public TView[] list_1;

    public int onTextScrollBarDownAuto = 0;
    public TEntryText onTextScrollBarDownAuto_EntryText;

    public t1() {
        app = new interfaceAppMain();
        app.init(128, 10, interfaceAppMain.modeIO.GPM);

        list_1 = new TView[16];

        TLabel timelabel = new TLabel();
        timelabel.setAll("-", 0, 4, colorANSI.BLUE, colorANSI.WHITE, 20, 20);
        app.add(timelabel, 40);
        list_1[10] = timelabel;

        app.later(() -> {
            timelabel.setText(Time.timeFormat("HH:mm:ss"));
        }, 40, 35, 0);

        TLabel lbl = new TLabel();
        lbl.setAll("%test!!", 1, 0, colorANSI.WHITE, colorANSI.BLUE, 10, 10);
        app.add(lbl, 0);

        keyboardListener keyListener = new keyboardListener() {
            @Override
            public void PressedDoView(int[] key) {
                if (keyboardCode.equals(key, keyboardCode.mixChar(keyboardCode.F1, keyboardCode.F2, keyboardCode.setChar('f')))) {
                    timelabel.setText("tab + f");
                }
            }

            @Override
            public void PressedEndView(int[] code) {

            }

            @Override
            public void WhileDoView(int[] code) {

            }

            @Override
            public void WhileEndView(int[] code) {

            }
        };

        app.setKeyboardListener(keyListener);

        list_1[0] = lbl;
        TLabel lbl2 = new TLabel();
        lbl2.setAll("test", 1, 2, colorANSI.WHITE, colorANSI.BLUE, 10, -1);
        app.add(lbl2, 1);
        list_1[1] = lbl2;
        TButton btn = new TButton();
        btn.setAll("%btn", 10, 0, 0, 0, 10, 1, colorANSI.BLUE, colorANSI.WHITE, -1);
        btn.isPressedClick(() -> {
            lbl.setText("0000");
            lbl.setBg(0);
            lbl.setFg(7);
            app.setReset(2);
            app.shutDownNow();
        });
        btn.isLeaveClick(() -> {
            TButton b = (TButton)app.getClass(3);
            app.setOnfView(true, b);
        });
        app.add(btn, 2);
        list_1[2] = btn;
        TButton btn2 = new TButton();
        btn2.setAll("btn 2", 10, 5, 0, 1, 7, 1, colorANSI.BLUE, colorANSI.WHITE, -1);
        btn2.isPressedClick(() -> {
            lbl.setText("1111"); lbl.setBg(7);lbl.setBg(0);
            lbl.setFg(7);
            //app.del(7);
            app.setReset(2);
            btn2.setOnf(false);
            ((TEntry)app.getClass(5)).setOnf(false);
        });
        app.add(btn2, 3);
        list_1[3] = btn2;
        TButton btn3 = new TButton();
        btn3.setAll("btn 3", 20, 0, 1, 0, 10, 3, colorANSI.BLUE, colorANSI.WHITE, -1);
        btn3.isPressedClick(() -> {
            lbl2.setText("CLICK");
            lbl2.setBg(4);
            app.setCursorCoordKeyboard(0, 0);
            app.setReset(2);
            //app.del(2); run (error not) +
        });
        btn3.isEnterClick(() -> {
            lbl2.setText("Enter");
            lbl2.setBg(2);
        });
        btn3.isLeaveClick(() -> {
            lbl2.setText("Leave");
            lbl2.setBg(1);
        });
        btn3.isEnterWhile(() -> {
            lbl.setText("ENTER W");
            lbl.setBg(1);
            lbl.setFg(7);
        });
        app.add(btn3, 4);
        list_1[4] = btn3;
        /*
        new Thread(() -> {
            while (true) {
                lbl2.setBg(7);
                Time.sleepml(10000);
                final TEntry m = (TEntry)app.getClass(6);
                m.setText("non");
                try {
                    final TEntryText m2 = (TEntryText)app.getClass(7);
                    m2.setText("\nnon-----------------------------\n");
                } catch (Exception e) {

                }
            }
        }).start();
        */

        TEntry ent = new TEntry();
        ent.setAll("fffds", 5, 10, 0, 2, colorANSI.BLUE, colorANSI.WHITE, 10, 5);
        ent.isPressedCharClick((charOut) -> {
            lbl2.setText("-" + charOut);
            lbl2.setBg(3);
            lbl2.setFg(0);
        });
        ent.isEnterClick(() -> {
            app.setReset(2);
        });
        ent.isLeaveClick(() -> {
            app.setReset(2);
            lbl2.setBg(1);
        });
        ent.isEnterWhile(() -> {
            lbl.setBg(5);
        });
        app.add(ent, 5);
        list_1[5] = ent;

        TEntry ent2 = new TEntry();
        ent2.setAll("entry2 test 11111", 15, 10, 2, 0, colorANSI.BLUE, colorANSI.WHITE, 10, -1);
        app.add(ent2, 9);
        list_1[9] = ent2;

        ColorViewText color1 = new ColorViewText(1);
        color1.add(0, 10, colorANSI.BLUE, colorANSI.WHITE, 0);

        TEntryText entt = new TEntryText();
        entt.setAll("text 1\ntext 2\ntext 3\n", 40, 2, 0, 3, 13, 10, colorANSI.GREEN, colorANSI.BLACK);
        entt.setBgFg(color1);
        entt.setBgCursor(colorANSI.RED);
        app.add(entt, 7);
        list_1[7] = entt;

        TText text1 = new TText();
        text1.setAll("text 1\ntext 2\ntext 3\n", 40, 15, 10, 10, colorANSI.RED, colorANSI.BLACK);
        app.add(text1, 8);
        list_1[8] = text1;

        new Thread(() -> {
            Time.sleepml(5000);
            //((TEntryText)app.getClass(7)).setOnf(false);
        }).start();
        
        /*
        ColorViewText color1 = new ColorViewText(1);
        color1.add(0, 0, 4, 7, 0);
        
        TEntryText t = new TEntryText();
        t.setAll("text\n", 1, 1, 0, 0, 4, 100, 30, color1);
        t.setBgCursor(1);
        app.add(t, 0);
        */

        TFrame frame1 = new TFrame();
        frame1.setAll("frame", 39, 1, colorANSI.BLUE, colorANSI.WHITE, 14, 11, TFrame.frameWindowOnf.windowAndText, TFrame.frameStyle.line2);
        app.add(frame1, 6);
        list_1[6] = frame1;

        final int[] i1 = {0};

        TButton clearAll = new TButton();
        clearAll.setAll("clear test", 0, 25, 0, 4, 10, 1, colorANSI.BLUE, colorANSI.WHITE, -1);
        app.add(clearAll, 20);

        TButton newAll = new TButton();
        newAll.setAll("new all", 15, 25, 1, 4, 10, 1, colorANSI.BLUE, colorANSI.WHITE, -1);
        app.add(newAll, 21);

        newAll.isLeaveClick(() -> {
            app.del(20);
            app.del(21);
            app.setCursorCoordKeyboard(0, 0);
        });

        newAll.isPressedClick(() -> {
            for (int i = 0; i < 11; i++) {
                app.add(list_1[i], i);
            }
        });

        clearAll.isPressedClick(() -> {
            for (int i = 0; i < 11; i++) {
                app.del(i);
                System.out.println("\033[HCLEAR");
                app.setReset(2);
            }
        });

        //app.del(20);
        //app.del(21);

        TCanvas canvas = new TCanvas();
        canvas.setAll("canvas", 55, 2, colorANSI.BLUE, colorANSI.WHITE, 20, 20, TCanvas.canvasWindowOnf.windowAndText, TCanvas.canvasStyle.line2);
        app.add(canvas, 10);

        canvas.createPixel(0, 1, colorANSI.customColor.setRGB(75, 198, 99));
        canvas.createPixel(0, 2, colorANSI.customColor.set(200));
        canvas.createPixel(20, 0, colorANSI.RED);
        {
            int ii000 = 0;
            for (colorANSI i : colorANSI.values()) {
                canvas.createPixel(ii000, 0, i);
                ii000++;
            }
        }

        canvas.createPixelRectangle(0, 5, 5, 5, colorANSI.RED, colorANSI.GREEN, true);
        canvas.createPixelLine(0, 0, 25, 2, colorANSI.RED);
        canvas.createPixelLine(10, 0, 11, 41, colorANSI.GREEN);
        canvas.createText(0, 17, CharLine.LineV(CharLine.Type.I) + "text", colorANSI.WHITE, colorANSI.BLUE);
        canvas.createTxLineH(18, 5, 5, colorANSI.GREEN, colorANSI.WHITE, CharLine.Type.II);
        canvas.createTxLineV(17, 18, 5, colorANSI.WHITE, colorANSI.GREEN, CharLine.Type.III);

        canvas.createImage("src/main/resources/image/img1.png", 0, 8, TCanvas.setSizeImage.SET.off());

        TSwitchButton swb2 = new TSwitchButton();
        swb2.setAll(2, 20, 4, 0, colorANSI.BLUE, colorANSI.WHITE, 'N', 'F');
        app.add(swb2, 13);
        TSwitchButton swb = new TSwitchButton();
        swb.setAll(0, 20, 3, 0, colorANSI.BLUE, colorANSI.WHITE, 'I', 'O');
        swb.isPressedClick(() -> {
            swb2.Click();
        });
        swb.isEnterClick(() -> {
            swb2.setBg(colorANSI.RED);
        });
        swb.isLeaveClick(() -> {
            swb2.setBg(colorANSI.GREEN);
        });
        swb.Click();
        app.add(swb, 12);

        TLabel lbl001 = new TLabel();
        lbl001.setAll("-", 30, 1, colorANSI.BLUE, colorANSI.WHITE, 5, 4);
        app.add(lbl001, 25);

        TButton btn01 = new TButton();
        btn01.setAll("btn4", 30, 0, 0, 2, 5, 1, colorANSI.BLUE, colorANSI.RED, -1);
        btn01.isPressedClick(() -> {
            lbl001.setText("111");
        });
        app.add(btn01, 14);

        btn01.Click();

        TScrollBar b1 = new TScrollBar();
        b1.setAll(0, 22, 5, 0, 10, TScrollBar.TypeOrient.HORIZONTAL, colorANSI.BLUE, colorANSI.WHITE, 50);
        b1.setCurrentCursor(0);
        b1.isEnterClick(() -> {
            lbl.setText("ENTER TSB");
        });
        b1.isLeaveClick(() -> {
            lbl.setText("LEAVE TSB");
        });
        b1.isPressedClick(() -> {
            lbl2.setText("pressed TSB");
        });
        b1.isPressedWhile(() -> {
            entt.setText(String.format("test\nTSB\n%d\n", b1.readPosition()));
        });
        app.add(b1, 30);
        entt.setText("test\ngtrg\ngfre\n");

        TScrollBar b2 = new TScrollBar();
        b2.setAll(30, 10, 6, 0, 10, TScrollBar.TypeOrient.VERTICAL, colorANSI.BLUE, colorANSI.WHITE, 1);
        b2.setCurrentCursor(0);
        app.add(b2, 31);

        TDropList td = new TDropList();
        TDropList.createList listTd = new TDropList.createList(3);
        listTd.add("m1", 0);
        listTd.add("m2", 1);
        listTd.add("m3", 2);
        td.setAll(1, 18, 7, 0, 5, colorANSI.BLUE, colorANSI.WHITE, listTd);
        td.isPressedWhile(() -> {
            text1.setText(String.format("TDL\nint -> %d\nstr -> %s", td.readPositionInt(), td.readPositionString()));
        });
        app.add(td, 32);

        entt.insertText(String.format("tml size\nwidth = %d\nheight = %d\n", app.getWidth(), app.getHeight()));

        TButton onTextScrollBarDownAuto = new TButton();
        onTextScrollBarDownAuto.setAll("text auto", 0, 7, 0, 5, 10, 1, colorANSI.BLUE, colorANSI.WHITE, -1);
        app.add(onTextScrollBarDownAuto, 41);
        onTextScrollBarDownAuto.isPressedClick(() -> {
            onTextScrollBarDownAuto_test_on();
        });

        onTextScrollBarDownAuto_EntryText = new TEntryText();
        onTextScrollBarDownAuto_EntryText.setAll("", 90, 0, 50, 50, 10, 5, colorANSI.BLUE, colorANSI.WHITE);
        onTextScrollBarDownAuto_EntryText.setOnfAutoDown(true);
        app.add(onTextScrollBarDownAuto_EntryText, 42);

        app.setOnfView(false, lbl);
        app.setOnfView(false, btn2);

        app.run(2);
    }

    public void onTextScrollBarDownAuto_test_on() {
        onTextScrollBarDownAuto = 0;
        app.later(() -> {
            onTextScrollBarDownAuto_test_on_2();
        }, 50, 1);
    }

    public void onTextScrollBarDownAuto_test_on_2() {
        if (onTextScrollBarDownAuto != 20) {
            onTextScrollBarDownAuto++;
            onTextScrollBarDownAuto_EntryText.insertText(String.format("%d\n", onTextScrollBarDownAuto));
        } else {
            onTextScrollBarDownAuto = 0;
            app.laterDel(1);
        }
    }
}
```

## Authors
* **ReallocX686** - Lead Developer
