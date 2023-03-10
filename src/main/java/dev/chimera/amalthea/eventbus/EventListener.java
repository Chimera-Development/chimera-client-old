package dev.chimera.amalthea.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//Le funny listener (I am in deep psychological torment)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventListener {
    String id();
    String[] runBefore() default {};
    String[] runAfter() default {};
}







