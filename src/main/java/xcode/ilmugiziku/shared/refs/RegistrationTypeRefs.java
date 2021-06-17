package xcode.ilmugiziku.shared.refs;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface RegistrationTypeRefs {
    int EMAIL = 1;
    int GOOGLE_FB = 2;
}
