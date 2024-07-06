package io.github.qiangyt.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import jakarta.annotation.Nonnull;

public abstract class JacksonDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
        var text = p.getValueAsString();
        if (text == null) {
            return null;
        }

        try {
            return deserialize(text);
        } catch (RuntimeException | IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalError(ex);
        }
    }

    @Nonnull
    protected T deserialize(@Nonnull String text) throws Exception {
        throw new InternalError("deserialization is NOT supported");
    }

}
