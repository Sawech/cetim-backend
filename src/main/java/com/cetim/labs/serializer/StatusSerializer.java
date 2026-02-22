package com.cetim.labs.serializer;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.cetim.labs.model.SStatus;

public class StatusSerializer extends JsonSerializer<SStatus> {
    @Override
    public void serialize(SStatus value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeString("NOUVEAU");
        } else {
            gen.writeString(value.getDisplayName());
        }
    }
} 