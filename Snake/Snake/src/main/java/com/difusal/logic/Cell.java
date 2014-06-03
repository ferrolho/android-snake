package com.difusal.logic;

import android.graphics.Point;

public class Cell {
    private Point location;
    private int radius;

    public Cell(int x, int y) {
        location = new Point(x, y);
        radius = 5;
    }

    public Point getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }
}
