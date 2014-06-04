package com.difusal.logic;

import android.graphics.Point;

public class Apple extends GameElement {
    private int score;
    private int color;

    public Apple(Point fieldDimensions, int radius, int score, int color) {
        super(radius);
        newRandomLocation(fieldDimensions);
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
