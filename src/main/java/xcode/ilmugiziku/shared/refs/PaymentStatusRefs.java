package xcode.ilmugiziku.shared.refs;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface PaymentStatusRefs {
    String SUCCEEDED = "SUCCEEDED";
    String PENDING = "PENDING";
    String FAILED = "FAILED";
    String VOIDED = "VOIDED";
    String REFUNDED = "REFUNDED";
}
