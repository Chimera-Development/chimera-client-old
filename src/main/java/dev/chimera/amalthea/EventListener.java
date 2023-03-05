package dev.chimera.amalthea;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



//Le funny listener (I am in deep psychological torment)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventListener {
    public String tag() default "";

    public String id();

    public int priority() default Priority.MEDIUM;

    public String[] dependencies() default {};

}







