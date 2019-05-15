package com.kr.md5;

import java.io.*;
import java.security.*;


public final class Main {
    private static final double COEF = 1.0E-6;

    public static void main(String... args) {
        if (args.length < 1) {
            System.out.println("Missing parameter");
            return;
        }

        var file = new File(args[0]);
        var md = new MD();
        var time = System.nanoTime();
        try {
            var simpleResult = md.simpleCount(file);
            var nextTime = System.nanoTime();
            System.out.println("Simple counting time in milliseconds is " + (nextTime - time) * COEF);
            var complexResult = md.forkJoinCount(file);
            System.out.println("Fork/Join counting time in milliseconds is " + (System.nanoTime() - nextTime) * COEF);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 is not available");
        } catch (IOException e) {
            System.out.println(e.getCause() + " " + e.getMessage());
        }
    }
}
