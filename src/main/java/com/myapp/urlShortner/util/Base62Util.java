package com.myapp.urlShortner.util;

public class Base62Util {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = 62;

    public static String encode(long value) {
        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % BASE)));
            value /= BASE;
        }

        return sb.reverse().toString();
    }

    public static long decode(String str) {
        long value = 0;

        for (char c : str.toCharArray()) {
            value = value * BASE + BASE62.indexOf(c);
        }

        return value;
    }
}
