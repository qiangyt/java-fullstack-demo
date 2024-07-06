package io.github.qiangyt.common.misc;

import java.util.Collection;
import java.util.Objects;

import com.google.common.collect.Lists;

/**
 * Utility class for string operations
 */
public class StringHelper {

    /** toString for array */
    public static <T> String toString(T[] array) {
        if (array == null) {
            return null;
        }
        return Lists.newArrayList(array).toString();
    }

    /**
     * Join multiple strings with a specified delimiter
     *
     * @param delimiter
     * @param texts
     *            Multiple strings to be concatenated
     */
    public static <T> String join(String delimiter, Collection<T> texts) {
        return join(delimiter, texts.toArray(new String[texts.size()]));
    }

    /**
     * toString for array with a specified delimiter
     *
     * @param delimiter
     * @param array
     *            array
     */
    public static <T> String join(String delimiter, T[] array) {
        var r = new StringBuilder(array.length * 64);
        var isFirst = true;
        for (var obj : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                r.append(delimiter);
            }
            r.append(Objects.toString(obj));
        }
        return r.toString();
    }

    /**
     * determine if a string is null or consists entirely of whitespace characters i
     */
    public static boolean isBlank(String str) {
        return (str == null || str.length() == 0 || str.trim().length() == 0);
    }

    /**
     * determine it is not null and not entirely composed of whitespace characters
     */
    public static boolean notBlank(String str) {
        return !isBlank(str);
    }

}
