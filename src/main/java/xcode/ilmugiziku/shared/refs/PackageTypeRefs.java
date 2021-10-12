package xcode.ilmugiziku.shared.refs;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface PackageTypeRefs {
    int UKOM_NEWBIE = 1;
    int UKOM_EXPERT = 2;
    int SKB_NEWBIE = 3;
    int SKB_EXPERT = 4;
}