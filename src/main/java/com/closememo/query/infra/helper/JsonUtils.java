package com.closememo.query.infra.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JsonUtils {

  private static final ObjectMapper MAPPER;

  static {
    MAPPER = new ObjectMapper();

    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDateTime.class,
        new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
    javaTimeModule.addDeserializer(LocalDateTime.class,
        new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
    javaTimeModule.addSerializer(ZonedDateTime.class,
        new ZonedDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    javaTimeModule.addDeserializer(ZonedDateTime.class,
        InstantDeserializer.ZONED_DATE_TIME);

    MAPPER.registerModule(javaTimeModule);
    MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  private JsonUtils() {
    throw new UnsupportedOperationException();
  }

  public static ObjectMapper getMapper() {
    return MAPPER;
  }

  public static <T> T fromJson(String json, Class<T> clazz) {
    if (json == null) {
      return null;
    }

    try {
      return MAPPER.readValue(json, clazz);
    } catch (IOException e) {
      throw new JsonDecodeException(e);
    }
  }

  public static <T> T fromJson(String json, TypeReference<T> typeReference) {
    if (json == null) {
      return null;
    }

    try {
      return MAPPER.readValue(json, typeReference);
    } catch (IOException e) {
      throw new JsonDecodeException(e);
    }
  }

  public static String toJson(final Object object) {
    try {
      return MAPPER.writeValueAsString(object);
    } catch (IOException e) {
      throw new JsonEncodeException(e);
    }
  }

  public static class JsonEncodeException extends RuntimeException {

    public JsonEncodeException(Throwable cause) {
      super(cause);
    }
  }

  public static class JsonDecodeException extends RuntimeException {

    public JsonDecodeException(Throwable cause) {
      super(cause);
    }
  }
}
