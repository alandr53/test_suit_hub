package com.mb.util;
import java.security.SecureRandom;
import java.util.Random;

public class IdGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!%#";
    private static final int CHARACTERS_LENGTH = CHARACTERS.length();
    private static final Random RANDOM = new SecureRandom();
    public static String makeId(int length) {
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            result.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS_LENGTH)));
        }

        String addRandomValue = generateRandomValue(15);
        return result.toString() + addRandomValue;
    }

    private static String generateRandomValue(int length) {
        StringBuilder randomValue = new StringBuilder(length);

        while (randomValue.length() < length) {
            randomValue.append(Long.toString(RANDOM.nextLong(), 36));
        }

        return randomValue.substring(0, length);
    }
}
