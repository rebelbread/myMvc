package annotation;

import java.lang.annotation.*;

/**
 *
 * @Description:
 * @author zhiwj
 * @date 2019-11-08 15:04
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String value();
}
