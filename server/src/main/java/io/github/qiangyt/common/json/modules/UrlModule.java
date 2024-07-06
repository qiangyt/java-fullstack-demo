package io.github.qiangyt.common.json.modules;

import java.net.URL;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.github.qiangyt.common.json.JacksonDeserializer;
import io.github.qiangyt.common.json.JacksonSerializer;
import jakarta.annotation.Nonnull;

public class UrlModule {

    @Nonnull
    public static SimpleModule build(boolean dump) {
        var r = new SimpleModule();
        r.addSerializer(URL.class, new Serializer(dump));
        r.addDeserializer(URL.class, new Deserializer());
        return r;
    }

    public static class Serializer extends JacksonSerializer<URL> {

        public Serializer(boolean dump) {
            super(dump);
        }

        @Override
        protected void dump(URL value, @Nonnull JsonGenerator gen) throws Exception {
            gen.writeString(value.toString());
        }
    }

    public static class Deserializer extends JacksonDeserializer<URL> {

        @Override
        protected URL deserialize(@Nonnull String text) throws Exception {
            return new URL(text);
        }

    }

}
