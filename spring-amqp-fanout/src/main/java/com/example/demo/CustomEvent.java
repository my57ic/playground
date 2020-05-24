package com.example.demo;

import java.util.Objects;

class CustomEvent {

    private final String message;

    public CustomEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CustomEvent{" +
                "message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomEvent that = (CustomEvent) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
