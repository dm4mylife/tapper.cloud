package common;


public @interface ConfigDriver {

    String[] type() default {"desktop"};
    String[] browserSize() default {"1920x1080"};



}


