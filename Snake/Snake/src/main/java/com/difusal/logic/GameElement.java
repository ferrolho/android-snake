package com.difusal.logic;

import android.graphics.Point;

public class GameElement {
    protected Point location;
    protected int radius;

    public GameElement(int radius) {
        location = new Point();
        this.radius = radius;
    }

    public Point getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }
}
