package com.adevinta.leaderboard.types.valueobject;

abstract class StringValueObject {
    protected String value;

    public StringValueObject(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String toString() {
        return value;
    }
}
