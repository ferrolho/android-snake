package com.difusal.logic;

public class Apple extends GameElement {
    private int score;
    private int color;

    public Apple(int fieldWidth, int fieldHeight, int radius, int score, int color) {
        super(radius);
        newRandomLocation(fieldWidth, fieldHeight);
        this.score = score;
        this.color = color;
    }

    public int getScore() {
        return score;
    }

    public int getColor() {
        return color;
    }
}
