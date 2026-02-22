package com.cetim.labs.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.cetim.labs.serializer.OSStatusDeserializer;
import com.cetim.labs.serializer.StatusSerializer;

@JsonSerialize(using = StatusSerializer.class)
@JsonDeserialize(using = OSStatusDeserializer.class)
public enum SStatus {
    NOUVEAU("Nouveau"),
    ACCEPTE("Accepté"),
    DISPATCHER("encoure de traitement"),
    REVISER("pour reviser"),
    TERMINE("Terminé"),
    ANNULE("Annulé"),
    EN_ATTENTE("En attente"),
    REJETE("Rejeté");

    private final String displayName;

    SStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SStatus fromString(String text) {
        if (text == null || text.isEmpty()) {
            return NOUVEAU;
        }
        for (SStatus status : SStatus.values()) {
            if (status.name().equalsIgnoreCase(text) || status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return NOUVEAU; // Default value
    }
} 