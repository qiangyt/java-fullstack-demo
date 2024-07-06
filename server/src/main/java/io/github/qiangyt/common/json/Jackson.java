package io.github.qiangyt.common.json;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.qiangyt.common.json.modules.DateModule;
import io.github.qiangyt.common.json.modules.FileModule;
import io.github.qiangyt.common.json.modules.InstantModule;
import io.github.qiangyt.common.json.modules.UriModule;
import io.github.qiangyt.common.json.modules.UrlModule;
import io.github.qiangyt.common.misc.StringHelper;
import jakarta.annotation.Nonnull;
import lombok.Getter;

@Getter
// @ThreadSafe
public class Jackson {

    @Nonnull
    public static final Jackson DEFAULT = new Jackson(buildDefaultMapper(false));

    @Nonnull
    public static final Jackson DUMP = new Jackson(buildDefaultMapper(true));

    public final ObjectMapper mapper;

    public Jackson(@Nonnull ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Nonnull
    public static ObjectMapper buildDefaultMapper(boolean dump) {
        var r = new ObjectMapper();
        initDefaultMapper(r, dump);
        return r;
    }

    public static void initDefaultMapper(@Nonnull ObjectMapper mapper, boolean dump) {
        mapper.registerModule(FileModule.build(dump));
        mapper.registerModule(DateModule.build(dump));
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(InstantModule.build(dump));
        mapper.registerModule(UriModule.build(dump));
        mapper.registerModule(UrlModule.build(dump));

        mapper.setSerializationInclusion(Include.NON_NULL);

        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, false);
        mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, false);
        mapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, false);
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true);
        mapper.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false);
        mapper.configure(DeserializationFeature.WRAP_EXCEPTIONS, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, false);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false);
        mapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, false);
        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        mapper.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, true);

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);
        mapper.configure(SerializationFeature.WRAP_EXCEPTIONS, true);
        mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, true);
        mapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false);
        mapper.configure(SerializationFeature.CLOSE_CLOSEABLE, false);
        mapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE, true);
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, true);
        mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);
        mapper.configure(SerializationFeature.WRITE_ENUM_KEYS_USING_INDEX, false);
        // mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        // mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
        mapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false);
        // mapper.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, false);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
        mapper.configure(SerializationFeature.EAGER_SERIALIZER_FETCH, true);
        mapper.configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, false);

    }

    public void registerModule(@Nonnull com.fasterxml.jackson.databind.Module module) {
        getMapper().registerModule(module);
    }

    public <T> T from(String text, @Nonnull Class<T> clazz) {
        if (StringHelper.isBlank(text)) {
            return null;
        }

        try {
            return getMapper().readValue(text, clazz);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    public <T> T from(InputStream stream, @Nonnull Class<T> clazz) {
        if (stream == null) {
            return null;
        }

        try {
            return getMapper().readValue(stream, clazz);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    public <T> T from(ByteBuffer buf, @Nonnull Class<T> clazz) {
        if (buf == null) {
            return null;
        }

        try {
            if (!buf.hasArray()) {
                var bytes = new byte[buf.remaining()];
                buf.get(bytes);
                return getMapper().readValue(bytes, clazz);
            }

            final var offset = buf.arrayOffset();
            return getMapper().readValue(buf.array(), offset + buf.position(), buf.remaining(), clazz);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    public <T> T from(byte[] bytes, @Nonnull Class<T> clazz) {
        if (bytes == null) {
            return null;
        }

        try {
            return getMapper().readValue(bytes, clazz);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    public <T> T from(String text, @Nonnull TypeReference<T> typeReference) {
        if (StringHelper.isBlank(text)) {
            return null;
        }

        try {
            return getMapper().readValue(text, typeReference);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    public <T> T from(InputStream stream, @Nonnull TypeReference<T> typeReference) {
        if (stream == null) {
            return null;
        }

        try {
            return getMapper().readValue(stream, typeReference);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    public <T> T from(ByteBuffer buf, @Nonnull TypeReference<T> typeReference) {
        if (buf == null) {
            return null;
        }

        try {
            if (!buf.hasArray()) {
                var bytes = new byte[buf.remaining()];
                buf.get(bytes);
                return getMapper().readValue(bytes, typeReference);
            }

            var offset = buf.arrayOffset();
            return getMapper().readValue(buf.array(), offset + buf.position(), buf.remaining(), typeReference);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    public <T> T from(byte[] bytes, @Nonnull TypeReference<T> typeReference) {
        if (bytes == null) {
            return null;
        }

        try {
            return getMapper().readValue(bytes, typeReference);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    public String pretty(Object obj) {
        return toString(obj, true);
    }

    public String toString(Object obj) {
        return toString(obj, false);
    }

    public byte[] toBytes(Object obj) {
        return toBytes(obj, false);
    }

    public ByteBuffer toByteBuffer(Object obj) {
        return toByteBuffer(obj, false);
    }

    public String toString(Object obj, boolean pretty) {
        if (obj == null) {
            return null;
        }

        try {
            if (pretty) {
                return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
            return getMapper().writeValueAsString(obj);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    public byte[] toBytes(Object obj, boolean pretty) {
        if (obj == null) {
            return null;
        }
        return toByteBuffer(obj, pretty).array();
    }

    public ByteBuffer toByteBuffer(Object obj, boolean pretty) {
        if (obj == null) {
            return null;
        }

        try {
            var buf = new ByteArrayBuilder();

            if (pretty) {
                getMapper().writerWithDefaultPrettyPrinter().writeValue(buf, obj);
            } else {
                getMapper().writeValue(buf, obj);
            }

            return ByteBuffer.wrap(buf.getCurrentSegment(), 0, buf.getCurrentSegmentLength());
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }
}
