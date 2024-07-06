package io.github.qiangyt.common.json.modules;

import java.net.URI;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.github.qiangyt.common.json.JacksonDeserializer;
import io.github.qiangyt.common.json.JacksonSerializer;
import jakarta.annotation.Nonnull;

public class UriModule {

    @Nonnull
    public static SimpleModule build(boolean dump) {
        var r = new SimpleModule();
        r.addSerializer(URI.class, new Serializer(dump));
        r.addDeserializer(URI.class, new Deserializer());
        return r;
    }

    public static class Serializer extends JacksonSerializer<URI> {

        public Serializer(boolean dump) {
            super(dump);
        }

        @Override
        protected void dump(URI value, @Nonnull JsonGenerator gen) throws Exception {
            gen.writeString(value.toString());
        }
    }

    public static class Deserializer extends JacksonDeserializer<URI> {

        @Override
        protected URI deserialize(@Nonnull String text) throws Exception {
            return new URI(text);
        }
    }

}
