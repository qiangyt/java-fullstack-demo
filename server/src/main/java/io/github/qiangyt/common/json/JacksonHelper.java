package io.github.qiangyt.common.json;

import java.io.InputStream;

import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.Nonnull;

public class JacksonHelper {

    @Nonnull
    public static final Jackson DEFAULT = Jackson.DEFAULT;

    public static String pretty(Object object) {
        return DEFAULT.pretty(object);
    }

    public static String to(Object object) {
        return DEFAULT.toString(object, false);
    }

    public static <T> T from(String json, @Nonnull Class<T> clazz) {
        return DEFAULT.from(json, clazz);
    }

    public static <T> T from(String json, @Nonnull TypeReference<T> typeReference) {
        return DEFAULT.from(json, typeReference);
    }

    public static <T> T from(InputStream json, @Nonnull Class<T> clazz) {
        return DEFAULT.from(json, clazz);
    }

    public static <T> T from(InputStream json, @Nonnull TypeReference<T> typeReference) {
        return DEFAULT.from(json, typeReference);
    }

}
