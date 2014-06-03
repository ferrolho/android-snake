package com.difusal.logic;

import android.graphics.Point;

import java.util.ArrayDeque;

public class Snake {
    private ArrayDeque<Cell> cells;
    private Direction direction;
    private int life;

    public Snake() {
        // clear cells container
        cells = new ArrayDeque<Cell>();

        addCell(1, 1);
        direction = Direction.DOWN;
        life = 100;
    }

    public void move() {
        move(direction);
    }

    public void move(Direction direction) {
        this.direction = direction;

        Point head = cells.getFirst().getLocation();
        switch (direction) {
            case UP:
                cells.addFirst(new Cell(head.x, head.y - 1));
                break;
            case DOWN:
                cells.addFirst(new Cell(head.x, head.y + 1));
                break;
            case LEFT:
                cells.addFirst(new Cell(head.x - 1, head.y));
                break;
            case RIGHT:
                cells.addFirst(new Cell(head.x + 1, head.y));
                break;
        }
    }

    public void addCell(int x, int y) {
        cells.addLast(new Cell(x, y));
    }

    public ArrayDeque<Cell> getCells() {
        return cells;
    }

    public int getCellsRadius() {
        return cells.getFirst().getRadius();
    }

    public boolean isDead() {
        return life == 0;
    }
}
