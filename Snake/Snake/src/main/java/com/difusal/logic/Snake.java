package com.difusal.logic;

import android.graphics.Point;

import java.util.ArrayDeque;

public class Snake {
    private ArrayDeque<Cell> cells;
    private Direction direction;
    private int moveDelay;
    private int life;

    public Snake() {
        // clear cells container
        cells = new ArrayDeque<Cell>();

        addCell(1, 1);
        direction = Direction.DOWN;
        moveDelay = 100;
        life = 100;
    }

    public void move() {
        // get snake head location
        Point head = cells.getFirst().getLocation();

        // add a new cell in front of the head in the current direction
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

        // remove last cell
        cells.removeLast();
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

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getMoveDelay() {
        return moveDelay;
    }

    public boolean isDead() {
        return life == 0;
    }
}
