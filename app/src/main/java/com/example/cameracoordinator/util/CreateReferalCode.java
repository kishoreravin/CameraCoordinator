package com.example.cameracoordinator.util;

import java.util.Random;

public class CreateReferalCode {

    int numeral;
    String alpha;

    public static String getReferalCode(){
        int first3Digits = new Random().nextInt(889)+111;
        int asciiNumber1 = new Random().nextInt(27)+64;
        int asciiNumber2 = new Random().nextInt(27)+64;

        StringBuilder sb = new StringBuilder();
        sb.append((char)asciiNumber1);
        sb.append(first3Digits);
        sb.append("_");
        sb.append((char)asciiNumber2);

        return sb.toString();
    }
}
