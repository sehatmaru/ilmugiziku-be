package xcode.marsiajar.shared.refs;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface TimeLimitRefs {
    int TIME_LIMIT_UKOM = 180;
    int TIME_LIMIT_SKB = 100;
}
