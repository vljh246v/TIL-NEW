package com.demo.cookbook.ch02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Ch02Test {

    // 2-14
    @Test
    void supplyAllArguments() {
        System.out.println(Ch02Kt.addProduct("Name", 5.0, "Desc"));
    }


    // 2-15
    @Test
    void checkOverloads() {
        Assertions.assertAll("overloads called from Java",
                () -> System.out.println(Ch02Kt.addProduct("Name", 5.0, "Desc")),
                () -> System.out.println(Ch02Kt.addProduct("Name", 5.0)),
                () -> System.out.println(Ch02Kt.addProduct("Name"))
        );
    }

    // 2-20
    @Test
    void checkOverloadedProductCtor() {
        Assertions.assertAll("overloads call from Java",
                () -> System.out.println(new Product("Name", 5.0, "Desc")),
                () -> System.out.println(new Product("Name", 5.0)),
                () -> System.out.println(new Product("Name"))
        );
    }
}
