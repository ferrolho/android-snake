package com.difusal.logic;

import android.graphics.Point;

public class Cell extends GameElement {
    public Cell(int x, int y) {
        location = new Point(x, y);
    }

    public Cell(Cell cell) {
        location = new Point(cell.location);
    }
}
