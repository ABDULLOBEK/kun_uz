package com.example.util;

import java.util.Random;

public class RandomUtil {
    public static String getRandomSmsCode(){
        Random random = new Random();
        return String.valueOf(random.nextInt(10000,99999));
    }
}
