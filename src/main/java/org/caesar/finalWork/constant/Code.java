package org.caesar.finalWork.constant;

public enum Code {
    OK(200),
    REQUEST_ERROR(400),
    NOT_FIND(404),
    RESPONSE_ERROR(500);

    private final int value;

    Code(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Code=" + value;
    }
}
