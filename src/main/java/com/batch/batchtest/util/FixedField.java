package com.batch.batchtest.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FixedField {
    int length();

    char padChar() default ' ';

    Align align() default Align.LEFT;

    String pattern() default "";

    enum Align {
        LEFT,
        RIGHT
    }
}
