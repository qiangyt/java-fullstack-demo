package io.github.qiangyt.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import jakarta.annotation.Nonnull;
import lombok.Getter;

@Getter
public abstract class JacksonSerializer<T> extends JsonSerializer<T> {

    final boolean dump;

    protected JacksonSerializer(boolean dump) {
        this.dump = dump;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {

        if (value == null) {
            gen.writeNull();
        } else {
            try {
                if (isDump()) {
                    dump(value, gen);
                } else {
                    serialize(value, gen);
                }
            } catch (RuntimeException | IOException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new InternalError(ex);
            }
        }
    }

    @Nonnull
    protected void dump(@Nonnull T value, @Nonnull JsonGenerator gen) throws Exception {
        throw new InternalError("serialization is NOT supported");
    }

    @Nonnull
    protected void serialize(@Nonnull T value, @Nonnull JsonGenerator gen) throws Exception {
        dump(value, gen);
    }

}
