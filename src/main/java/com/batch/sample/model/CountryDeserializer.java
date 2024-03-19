package com.batch.sample.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import com.batch.sample.config.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class CountryDeserializer implements Deserializer<Country> {

	ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapperInstance();

    @Override
    public Country deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            } else {
                return objectMapper.readValue(data, Country.class);
            }
        } catch (SerializationException | IOException e) {
            throw new SerializationException("Error serializing user", e);
        }
    }
}