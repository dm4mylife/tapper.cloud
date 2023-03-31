package tests;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TakeOrCompareScreenshots {
    boolean isTakeScreenshot() default true;

}
