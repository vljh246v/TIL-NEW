package com.demo.cookbook.ch02;

public class Ch02 {

    // 2-22
    public void ch2_22() {
        int myInt = 3;
        long myLong = myInt;
    }

    // 2-23
    public void ch2_23() {
        Integer myInteger = 3;
//        Long myWrappedLong = myInteger;
        Long myWrappedLong = myInteger.longValue();
        myWrappedLong = Long.valueOf(myInteger);
    }

}
