package com.hyd;

public class Greetings {

    public static void main(String[] args) {
        String name = args.length == 0 ? "Anonymous" : args[0];
        System.out.println("Greetings, " + name + "!");
    }
}
