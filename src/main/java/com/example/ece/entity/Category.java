package com.example.ece.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    ELECTRONICS, FURNITURE, CLOTHES, FOODS, BOOKS;
    @JsonCreator
    public static Category fromString(String value) {
        return Category.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toString() {
        return name();
    }
}
