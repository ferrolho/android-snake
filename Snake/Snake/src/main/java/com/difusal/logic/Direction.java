package com.difusal.logic;

public enum Direction {
    RIGHT(0), UP(1), LEFT(2), DOWN(3);

    private final int value;

    private Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
