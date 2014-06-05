package com.difusal.logic;

import android.graphics.Color;
import android.graphics.Point;

public class GreenApple extends Apple {
    public GreenApple(Point fieldDimensions, Snake snake, int radius) {
        super(fieldDimensions, snake, radius, 10, Color.GREEN);
    }
}
