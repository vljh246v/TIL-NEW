package com.demo.cookbook.ch05;

import java.util.Arrays;

// 5-1
public class Ch05 {
    public static void main(String[] args) {
        String[] strings = new String[4];
        strings[0] = "an";
        strings[1] = "array";
        strings[2] = "of";
        strings[3] = "strings";

        System.out.println(Arrays.toString(strings));

        strings = "an array of strings".split(" ");
        System.out.println(Arrays.toString(strings));
    }
}
