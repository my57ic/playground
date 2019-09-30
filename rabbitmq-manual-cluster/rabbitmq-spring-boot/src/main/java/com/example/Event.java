package com.example;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;
import java.util.UUID;

final class Event {

    private final UUID uuid;
    private final String body;

    @JsonCreator
    public Event(String body) {
        this.body = body;
        this.uuid = UUID.randomUUID();
    }

    public String getBody() {
        return body;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "Event{" +
                "uuid=" + uuid +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return uuid.equals(event.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
