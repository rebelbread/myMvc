package annotation;

import java.lang.annotation.*;

/**
 *
 * @Description:
 * @author zhiwj
 * @date 2019-11-08 15:10
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {
    String value();
}
