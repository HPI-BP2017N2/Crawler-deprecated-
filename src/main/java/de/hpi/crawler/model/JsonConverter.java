package de.hpi.crawler.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;

public class JsonConverter {

    @Getter(AccessLevel.PRIVATE) private static final ObjectMapper mapper = new ObjectMapper();

    static {
        getMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    private JsonConverter() {
        throw new IllegalStateException();
    }

    public static <T> T readJavaObjectFromInputStream(InputStream inputStream, Class<T> javaObjectType) throws IOException {
        return getMapper().readValue(inputStream, javaObjectType);
    }

    public static String getJsonStringForJavaObject(Object javaObject) throws JsonProcessingException {
        return getMapper().writeValueAsString(javaObject);
    }

}
