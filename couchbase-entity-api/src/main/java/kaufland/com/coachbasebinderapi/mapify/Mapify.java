package kaufland.com.coachbasebinderapi.mapify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface Mapify {

    String name() default "";
}