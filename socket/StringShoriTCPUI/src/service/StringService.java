package service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import util.StringUtils;

public class StringService {
    public static String stringProcesses(String input) {
        List<String> vowels = Arrays.asList("a", "i", "u", "e", "o");
        return ("Ket qua xu ly chuoi\n" +
                "Dao nguoc: " + StringUtils.inverse(input) + "\n" +
                "In hoa: " + StringUtils.upperCase(input) + "\n" +
                "In thuong: " + StringUtils.lowerCase(input) + "\n" +
                "Hoa thuong: " + StringUtils.oppsite(input) + "\n" +
                "So tu: " + input.length() + "\n" +
                "Nguyen am: " +
                vowels.stream()
                        .map(vowel -> vowel + "-" + StringUtils.frequency(input, vowel.charAt(0)))
                        .collect(Collectors.joining(", ")));
    }
}
