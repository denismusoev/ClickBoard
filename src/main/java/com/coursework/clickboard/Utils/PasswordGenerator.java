package com.coursework.clickboard.Utils;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = UPPER_CASE.toLowerCase();
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]|,./?><";

    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword(int length) {
        String mix = UPPER_CASE + LOWER_CASE + NUMBERS + SPECIAL_CHARS;
        StringBuilder password = new StringBuilder(length);

                password.append(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));
        password.append(LOWER_CASE.charAt(random.nextInt(LOWER_CASE.length())));
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

                for (int i = password.length(); i < length; i++) {
            password.append(mix.charAt(random.nextInt(mix.length())));
        }

                return shuffleString(password.toString());
    }

    private static String shuffleString(String string) {
        char[] array = string.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return new String(array);
    }
}

