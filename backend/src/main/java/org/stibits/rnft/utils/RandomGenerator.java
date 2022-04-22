package org.stibits.rnft.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class RandomGenerator {
    private final int DEFAULT_LENGTH = 25;
    private String ascii;

    public String generateRandomStr() {
        return this.generateRandomStr(DEFAULT_LENGTH);
    }

    public String generateRandomStr(int len) {
        String allowedChars = this.getAsciiChars();
        String generated = "";

        for (int i = 0; i < len; i++) {
            int index = (int) Math.floor(Math.random() * allowedChars.length());
            generated += allowedChars.charAt(index);
        }

        return generated;
    }

    private String getAsciiChars() {
        if (ascii != null && ascii.length() > 0)
            return ascii;

        int charsCount = (int) ('z' - 'a') + 1;
        ascii = "";

        for (int i = 0; i < charsCount; i++) {
            char c = (char) ('a' + i);
            String ch = "" + c;
            ascii += ch + ch.toUpperCase();

            if (i < 10)
                ascii += Integer.toString(i).charAt(0);
        }

        return ascii;
    }

    public int randomInteger (int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
