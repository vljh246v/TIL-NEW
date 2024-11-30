package ch02;

import java.util.HashSet;
import java.util.Set;

public class RuntimeConstantPoolOOM_1 {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        short i = 0;

        while(true) {
            set.add(String.valueOf(i++).intern());
        }
    }
}
