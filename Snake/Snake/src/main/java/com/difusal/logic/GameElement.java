package com.difusal.logic;

import android.graphics.Point;

public class GameElement {
    protected Point location;
    protected int radius;

    public GameElement() {
        location = new Point();
        radius = 5;
    }

    public Point getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }
}
