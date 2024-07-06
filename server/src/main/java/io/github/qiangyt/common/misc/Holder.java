package io.github.qiangyt.common.misc;

/**
 *
 * @author
 *
 */
public class Holder<T> {

    private volatile T data;

    public Holder() {
        this(null);
    }

    public Holder(T data) {
        this.data = data;
    }

    public static <T> Holder<T> of(T data) {
        return new Holder<>(data);
    }

    public synchronized void set(T newData) {
        this.data = newData;
    }

    public synchronized T get() {
        return this.data;
    }

}
