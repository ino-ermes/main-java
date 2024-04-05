package util;

import java.text.Normalizer;

public class StringUtils {

    public static String inverse(String input) {
        int len = input.length();
        char buffer[] = new char[len];
        for (int i = 0; i != len; ++i) {
            buffer[i] = input.charAt(len - i - 1);
        }
        return new String(buffer);
    }

    public static String upperCase(String input) {
        int len = input.length();
        char buffer[] = new char[len];
        for (int i = 0; i != len; ++i) {
            buffer[i] = Character.toUpperCase(input.charAt(i));
        }
        return new String(buffer);
    }

    public static String lowerCase(String input) {
        int len = input.length();
        char buffer[] = new char[len];
        for (int i = 0; i != len; ++i) {
            buffer[i] = Character.toLowerCase(input.charAt(i));
        }
        return new String(buffer);
    }

    public static String oppsite(String input) {
        int len = input.length();
        char buffer[] = new char[len];
        for (int i = 0; i != len; ++i) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                buffer[i] = Character.toLowerCase(c);
            } else if (Character.isLowerCase(c)) {
                buffer[i] = Character.toUpperCase(c);
            } else {
                buffer[i] = c;
            }
        }
        return new String(buffer);
    }

    public static int frequency(String input, char c) {
        input = removeAccent(input);
        int len = input.length();
        int freq = 0;
        for (int i = 0; i != len; ++i) {
            if (input.charAt(i) == c) {
                ++freq;
            }
        }
        return freq;
    }

    public static String removeAccent(String input) {
        String regex = "\\p{InCombiningDiacriticalMarks}+";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        return temp.replaceAll(regex, "").replaceAll("đ", "d").replaceAll("Đ", "D");
    }
}
