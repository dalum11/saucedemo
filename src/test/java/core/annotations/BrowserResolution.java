package core.annotations;

import core.config.BrowserOrientation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BrowserResolution {
    BrowserOrientation value() default BrowserOrientation.STANDARD;
}
