package annotation;

import java.lang.annotation.*;

/**
 *
 * @Description:
 * @author zhiwj
 * @date 2019-11-08 15:10
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();
}
