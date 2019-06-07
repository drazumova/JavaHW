package com.hw.junit;

public class Main {

    public static void main(String... args) {
        if (args.length > 2 || args.length < 1) {
            System.out.println("Invalide input format\n Should be <path to root of package> (default - current) <path to tests directory>");
        }
        var tester = new TestRunner();
        if (args.length == 2) {
            tester.test(args[0], args[1]);
        } else {
            tester.test("", args[0]);
        }
    }
}
