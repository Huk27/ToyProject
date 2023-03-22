package com.Jinhyy.util;

public class Is {
    public static boolean Negative(Number number) {
        if ( number != null && number.intValue() < 0 ) {
            return true;
        }

        return false;
    }
}
