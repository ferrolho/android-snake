package com.difusal.logic;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayDeque;

public class Snake {
    private int radius;
    private ArrayDeque<Cell> cells;
    private Cell previousTail;

    public Direction getDirection() {
        return direction;
    }

    private Direction direction;
    private int moveDelay;
    private int life;
    private int score;

    public Snake(int radius) {
        this.radius = radius;

        // clear cells container
        cells = new ArrayDeque<Cell>();

        cells.addLast(new Cell(1, 1, radius));
        direction = Direction.RIGHT;
        moveDelay = 200;
        life = 100;
        score = 0;
    }

    public void move() {
        // get snake head location
        Point head = getHead().getLocation();

        // add a new cell in front of the head in the current direction
        switch (direction) {
            case UP:
                cells.addFirst(new Cell(head.x, head.y - 1, radius));
                break;
            case DOWN:
                cells.addFirst(new Cell(head.x, head.y + 1, radius));
                break;
            case LEFT:
                cells.addFirst(new Cell(head.x - 1, head.y, radius));
                break;
            case RIGHT:
                cells.addFirst(new Cell(head.x + 1, head.y, radius));
                break;
        }

        // log head current location
        head = getHead().getLocation();
        Log.v("Snake", "Head at: " + head.x + ", " + head.y);

        // remove last cell and temporarily save it
        previousTail = cells.removeLast();

        checkIfAteItself();
    }

    private void checkIfAteItself() {
        for (Cell cell : cells)
            if (cell != getHead() && cell.getLocation().equals(getHead().getLocation()))
                kill();
    }

    public void incSize() {
        cells.addLast(new Cell(previousTail));
    }

    public ArrayDeque<Cell> getCells() {
        return cells;
    }

    public Cell getHead() {
        return cells.getFirst();
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getMoveDelay() {
        return moveDelay;
    }

    public void increaseSpeed() {
        if (moveDelay > 50)
            moveDelay -= 10;
    }

    public boolean isDead() {
        return life == 0;
    }

    public void kill() {
        life = 0;
    }

    public int getScore() {
        return score;
    }

    public boolean ate(Apple apple) {
        return getHead().getLocation().equals(apple.getLocation());
    }

    public void incScore(int score) {
        this.score += score;
    }
}
