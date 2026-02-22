package com.cetim.labs.serializer;

import java.io.IOException;

import com.cetim.labs.model.SStatus;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;



public class OSStatusDeserializer extends JsonDeserializer<SStatus> {
    @Override
    public SStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null || value.isEmpty()) {
            return SStatus.NOUVEAU;
        }
        return SStatus.fromString(value);
    }
} 