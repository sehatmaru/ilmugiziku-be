package xcode.ilmugiziku.shared.refs;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface QuestionTypeRefs {
    int QUIZ = 1;
    int PRACTICE = 2;
    int TRY_OUT_UKOM = 3;
    int TRY_OUT_SKB_GIZI = 4;
}
