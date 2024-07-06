package io.github.qiangyt.common.json.modules;

import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.github.qiangyt.common.json.JacksonDeserializer;
import io.github.qiangyt.common.json.JacksonSerializer;
import jakarta.annotation.Nonnull;

public class DateModule {

    @Nonnull
    public static SimpleModule build(boolean dump) {
        var r = new SimpleModule();
        r.addSerializer(Date.class, new Serializer(dump));
        r.addDeserializer(Date.class, new Deserializer());
        return r;
    }

    public static class Serializer extends JacksonSerializer<Date> {

        public Serializer(boolean dump) {
            super(dump);
        }

        @Override
        protected void dump(Date value, @Nonnull JsonGenerator gen) throws Exception {
            gen.writeNumber(value.getTime());
        }
    }

    public static class Deserializer extends JacksonDeserializer<Date> {

        @Override
        protected Date deserialize(@Nonnull String text) throws Exception {
            return new Date(Long.valueOf(text));
        }
    }

}
