package annotation;

import java.lang.annotation.*;

/**
 *
 * @Description:
 * @author zhiwj
 * @date 2019-11-08 15:11
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowrited {
    String value();
}
