package com.gianvittorio.reactor.util;

import java.util.concurrent.TimeUnit;

public interface CommonUtil {
    static void delay(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
