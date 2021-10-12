package xcode.ilmugiziku.shared.refs;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface PaymentStatusRefs {
    int PENDING = 1;
    int EXPIRED = 2;
    int SUCCESS = 3;
}
