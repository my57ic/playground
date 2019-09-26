package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

class SomeEvent {

    private UUID uuid;
    private String message;

    @JsonCreator
    public SomeEvent(@JsonProperty("message") String message) {
        this.uuid = UUID.randomUUID();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SomeEvent someEvent = (SomeEvent) o;
        return uuid.equals(someEvent.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "SomeEvent{" +
                "uuid=" + uuid +
                ", message='" + message + '\'' +
                '}';
    }
}
