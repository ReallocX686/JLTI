package jlti.io;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TOut {
    protected Terminal tml;
    protected NonBlockingReader reader;
    protected boolean[] conf_1 = new boolean[32];
    private int width_main = 0;
    private int height_main = 0;
    private int mode_IO = 1;

    private final ConcurrentLinkedQueue<Integer> asyncKeyQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Integer> asyncKeyQueue_2 = new ConcurrentLinkedQueue<>();
    private Thread inputListenerThread;
    private volatile boolean isRunning = true;

    private void __init() {
        conf_1[0] = true; // system
        conf_1[1] = false; // jansi
        conf_1[2] = false; // jna
        conf_1[3] = false; // dumb
    }

    private void _terminal() {
        try {
            System.setProperty("org.jline.terminal.escapetimeouts", "25"); 
            tml = TerminalBuilder.builder()
            .system(conf_1[0])
            .jansi(conf_1[1])
            .jna(conf_1[2])
            .dumb(conf_1[3])
            .build();
            tml.enterRawMode();
            tml.puts(org.jline.utils.InfoCmp.Capability.cursor_invisible);
            tml.puts(org.jline.utils.InfoCmp.Capability.keypad_xmit);
            tml.puts(org.jline.utils.InfoCmp.Capability.cursor_invisible);
            tml.flush();
            reader = tml.reader();

            startAsyncInputListener();

        } catch (IOException e) {
            System.out.println("\033[91mError TOut.java _init > \033[0m\n" + e);
            System.exit(0);
        }
    }

    private void startAsyncInputListener() {
        inputListenerThread = new Thread(() -> {
            while (isRunning) {
                try {
                    int byteRead = tml.reader().read();

                    if (byteRead != -2) {
                        asyncKeyQueue.add(byteRead);
                        asyncKeyQueue_2.add(byteRead);
                    }
                } catch (IOException e) {
                    break;
                }
            }
        });
        
        inputListenerThread.setDaemon(true);
        inputListenerThread.setName("Gilfoyle-InputListener-Thread");
        inputListenerThread.start();
    }

    protected void _init(int i) {
        mode_IO = i;
        if (i == 1) {
            __init();
            _terminal();
            width_main = tml.getWidth();
            height_main = tml.getHeight();
        } else if (i == 2) {
            width_main = 30;
            height_main = 20;
        }
        print("\033[2J\033[H");
    }

    public int getWidth() {
        return width_main;
    }

    public int getHeight() {
        return height_main;
    }

    protected void setCursorOn(boolean i) {
        if (i) {
            tml.puts(org.jline.utils.InfoCmp.Capability.cursor_normal);
        } else {
            tml.puts(org.jline.utils.InfoCmp.Capability.cursor_invisible);
        }
    }

    protected void print(String text) {
        if (mode_IO == 1) {
            tml.writer().print(text);
        } else {
            System.out.print(text);
        }
    }

    protected void printf(String text, Object... objm) {
        if (mode_IO == 1) {
            tml.writer().printf(text, objm);
        } else {
            System.out.printf(text, objm);
        }
    }

    protected void flush() {
        if (mode_IO == 1) {
            tml.writer().flush();
        } else {
            System.out.flush();
        }
    }

    protected String input() {
        try (Scanner cs = new Scanner(System.in)) {
            return cs.nextLine();
        } catch (Exception e) {
            return "null";
        }
    }

    protected int codeIsPressed(long max_time) {
        try {
            Integer firstByte = asyncKeyQueue.poll();

            if (firstByte == null) {
                return -1;
            }

            if (firstByte == 27) {
                Thread.sleep(15);
            }

            return firstByte;

        } catch (InterruptedException e) {
            return -1;
        }
    }

    protected int[] codeIsPressedReadAll() {
        try {
            int[] out = new int[20];
            out[0] = -2;
            int i = 0;

            while (true) {
                Integer firstByte = asyncKeyQueue_2.poll();

                if (firstByte == null) {
                    break;
                }

                if (i < 20) {
                    out[i] = firstByte;
                    i++;
                } else {
                    asyncKeyQueue_2.clear();
                    break;
                }

                if (firstByte == 27) {
                    Thread.sleep(15);
                }
            }

            return out;
        } catch (InterruptedException e) {
            return new int[] {-2};
        }
    }
}
