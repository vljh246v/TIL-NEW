package com.demo.cookbook.ch03;

public class Ch03 {
    public static void main(String[] args) {
        // 3-28
        System.out.println(MySingleton.INSTANCE.myFunction());
        System.out.println(MySingleton.INSTANCE.getMyProperty());
    }
}
