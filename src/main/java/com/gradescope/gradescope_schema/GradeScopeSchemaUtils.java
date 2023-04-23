package com.gradescope.gradescope_schema;

import java.util.Collection;

public class GradeScopeSchemaUtils {
    public static boolean isNotNull(Object o) {
        return o != null;
    }

    public static boolean isDefined(Object o) {
        return isNotNull(o);
    }

    public static boolean isDefined(GradeScopeOutputFormat gof) {
        return isNotNull(gof) && gof != GradeScopeOutputFormat.undefined;
    }

    public static boolean isDefined(GradeScopeVisibility gv) {
        return isNotNull(gv) && gv != GradeScopeVisibility.undefined;
    }

    public static boolean isDefined(GradeScopeStatus gs) {
        return isNotNull(gs) && gs != GradeScopeStatus.undefined;
    }

    public static boolean isDefined(String s) {
        return isNotNull(s) && !s.isEmpty() && !s.isBlank();
    }

    public static <T> boolean isDefined(T[] arr) {
        return isNotNull(arr) && arr.length > 0;
    }

    public static <T> boolean isDefined(Collection<T> c) {
        return isNotNull(c) && c.size() > 0;
    }
}
