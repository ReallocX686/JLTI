package jlti.util;

import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {
    public static void sleepml(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(i);
        } catch (InterruptedException e) {

        }
    }

    public static void sleeps(int i) {
        try {
            TimeUnit.SECONDS.sleep(i);
        } catch (InterruptedException e) {

        }
    }

    public static void ThreadSleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {

        }
    }

    public static String timeFormat(String format_time) {
        LocalDateTime t1 = LocalDateTime.now();
        DateTimeFormatter t2 = DateTimeFormatter.ofPattern(format_time);
        return t1.format(t2);
    }
}