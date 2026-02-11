package fr.kainovaii.obsidian.core.web.route.methods;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GET
{
    String value();
    String name() default "";
}