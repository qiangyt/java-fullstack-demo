package io.github.qiangyt.common.json.modules;

import java.io.File;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.github.qiangyt.common.json.JacksonDeserializer;
import io.github.qiangyt.common.json.JacksonSerializer;
import jakarta.annotation.Nonnull;

public class FileModule {

    @Nonnull
    public static SimpleModule build(boolean dump) {
        var r = new SimpleModule();
        r.addSerializer(File.class, new Serializer(dump));
        r.addDeserializer(File.class, new Deserializer());
        return r;
    }

    public static class Serializer extends JacksonSerializer<File> {

        public Serializer(boolean dump) {
            super(dump);
        }

        @Override
        protected void dump(File value, @Nonnull JsonGenerator gen) throws Exception {
            gen.writeString(value.getPath());
        }
    }

    public static class Deserializer extends JacksonDeserializer<File> {

        @Override
        protected File deserialize(@Nonnull String text) throws Exception {
            return new File(text);
        }
    }

}
