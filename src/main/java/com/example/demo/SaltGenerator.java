package com.example.demo;

import java.util.Random;

public class SaltGenerator {
    private static final String[] WORDS = {"sk", "ghs", "wk", "tkf", "fo"};

    public static String generateSalt() {
        Random random = new Random();
        int randomIndex = random.nextInt(WORDS.length);
        String selectedWord = WORDS[randomIndex];

        // 대소문자를 무작위로 혼합
        StringBuilder salt = new StringBuilder();
        for (char c : selectedWord.toCharArray()) {
            if (random.nextBoolean()) {
                salt.append(Character.toUpperCase(c));
            } else {
                salt.append(Character.toLowerCase(c));
            }
        }

        return salt.toString();
    }

    public static void main(String[] args) {
        String salt = generateSalt();
        System.out.println("Generated Salt: " + salt);
    }
}
