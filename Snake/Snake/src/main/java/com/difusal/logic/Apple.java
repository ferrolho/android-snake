package com.difusal.logic;

import android.graphics.Point;
import android.util.Log;

import java.util.Random;

public class Apple extends GameElement {
    private int score;
    private int color;

    public Apple(Point fieldDimensions, Snake snake, int radius, int score, int color) {
        super(radius);
        newRandomLocation(fieldDimensions, snake);
        this.score = score;
        this.color = color;
    }

    public void newRandomLocation(Point fieldDimensions, Snake snake) {
        Random random = new Random();
        Point p = new Point();

        boolean valid;
        do {
            valid = true;

            p.x = random.nextInt(fieldDimensions.x - 2) + 1;
            p.y = random.nextInt(fieldDimensions.y - 2) + 1;

            for (Cell cell : snake.getCells())
                if (cell.getLocation().equals(p))
                    valid = false;
        } while (!valid);

        Log.d("GameElement", "New apple at: " + p.x + ", " + p.y);
        location = p;
    }

    public int getScore() {
        return score;
    }

    public int getColor() {
        return color;
    }
}
