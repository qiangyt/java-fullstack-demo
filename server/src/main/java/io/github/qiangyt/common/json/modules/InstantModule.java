package io.github.qiangyt.common.json.modules;

import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.github.qiangyt.common.json.JacksonDeserializer;
import io.github.qiangyt.common.json.JacksonSerializer;
import jakarta.annotation.Nonnull;

public class InstantModule {

    @Nonnull
    public static SimpleModule build(boolean dump) {
        var r = new SimpleModule();
        r.addSerializer(Instant.class, new Serializer(dump));
        r.addDeserializer(Instant.class, new Deserializer());
        return r;
    }

    public static class Serializer extends JacksonSerializer<Instant> {

        public Serializer(boolean dump) {
            super(dump);
        }

        @Override
        protected void dump(Instant value, @Nonnull JsonGenerator gen) throws Exception {
            gen.writeString(value.toString());
        }
    }

    public static class Deserializer extends JacksonDeserializer<Instant> {

        @Override
        protected Instant deserialize(@Nonnull String text) throws Exception {
            return Instant.parse(text);
        }
    }

}
