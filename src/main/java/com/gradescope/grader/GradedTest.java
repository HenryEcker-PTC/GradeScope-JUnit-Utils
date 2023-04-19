package com.gradescope.grader;

import com.gradescope.gradescope_schema.GradeScopeOutputFormat;
import com.gradescope.gradescope_schema.GradeScopeStatus;
import com.gradescope.gradescope_schema.GradeScopeVisibility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface GradedTest {
    double max_score();

    GradeScopeStatus status() default GradeScopeStatus.undefined;

    String name();

    GradeScopeOutputFormat name_format() default GradeScopeOutputFormat.undefined;

    String number() default "";

    GradeScopeOutputFormat output_format() default GradeScopeOutputFormat.undefined;

    String[] tags() default {};

    GradeScopeVisibility visibility() default GradeScopeVisibility.undefined;
}
