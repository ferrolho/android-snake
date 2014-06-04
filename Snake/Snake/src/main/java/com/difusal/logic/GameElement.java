package com.difusal.logic;

import android.graphics.Point;
import android.util.Log;

import java.util.Random;

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

    public void newRandomLocation(Point fieldDimensions) {
        Random random = new Random();
        int x = random.nextInt(fieldDimensions.x - 2) + 1;
        int y = random.nextInt(fieldDimensions.y - 2) + 1;

        Log.d("GameElement", "New apple at: " + x + ", " + y);
        location = new Point(x, y);
    }

    public int getRadius() {
        return radius;
    }
}
