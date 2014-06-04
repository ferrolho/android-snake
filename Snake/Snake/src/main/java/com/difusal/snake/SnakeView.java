package com.difusal.snake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.difusal.logic.Apple;
import com.difusal.logic.Cell;
import com.difusal.logic.Direction;
import com.difusal.logic.GreenApple;
import com.difusal.logic.Snake;

import java.util.Timer;
import java.util.TimerTask;

public class SnakeView extends View implements SwipeInterface {
    private int width, height;
    private Paint paint;
    private Point fieldDimensions;
    private Snake snake;
    private Apple apple;
    private int cellsDiameter, cellsRadius;

    public SnakeView(Context context, final TimerTask timerTask) {
        super(context);

        paint = new Paint();

        // set on touch listener
        setOnTouchListener(new ActivitySwipeDetector(this));

        // wait for view to be created in order to use width and height
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                //now we can retrieve the width and height
                width = getWidth();
                height = getHeight();

                Log.d("SnakeView", "View width: " + width);
                Log.d("SnakeView", "View height: " + height);

                // initialize game board and game elements radius
                int fieldWidth = 20;
                cellsDiameter = width / fieldWidth;
                cellsRadius = cellsDiameter / 2;
                int fieldHeight = height / cellsDiameter;
                fieldDimensions = new Point(fieldWidth, fieldHeight);

                Log.d("MainActivity", "Cell Diameter: " + cellsDiameter);
                Log.d("MainActivity", "Field Dimensions: " + fieldWidth + "x" + fieldHeight);

                startNewGame();

                // create timer
                Timer timer = new Timer();
                timer.schedule(timerTask, 0, snake.getMoveDelay());

                // important step not to keep receiving callbacks: remove this listener
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public Snake getSnake() {
        return snake;
    }

    public void startNewGame() {
        // create snake
        snake = new Snake(cellsRadius);

        // create apple
        apple = new GreenApple(fieldDimensions, cellsRadius);
    }

    public void updateGame() {
        // check if snake hit any wall
        checkIfSnakeHitAnyWall();

        if (!snake.isDead()) {
            // move the snake
            snake.move();

            // check if snake ate apple
            checkIfSnakeAteApple();
        }
    }

    private void checkIfSnakeHitAnyWall() {
        // get snake head location
        Point head = snake.getHead().getLocation();

        switch (snake.getDirection()) {
            case UP:
                if (head.y == 1)
                    snake.kill();
                break;
            case DOWN:
                if (head.y == fieldDimensions.y - 2)
                    snake.kill();
                break;
            case LEFT:
                if (head.x == 1)
                    snake.kill();
                break;
            case RIGHT:
                if (head.x == fieldDimensions.x - 2)
                    snake.kill();
                break;
        }
    }

    public void checkIfSnakeAteApple() {
        if (snake.ate(apple)) {
            Log.d("Snake", "Apple has been eaten");

            // update score
            snake.incScore(apple.getScore());

            // generate new apple
            apple.newRandomLocation(fieldDimensions);

            // increase snake size
            snake.incSize();

            // increase snake speed
            snake.increaseSpeed();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // draw background
        paint.setColor(Color.LTGRAY);
        if (snake.isDead())
            paint.setColor(Color.RED);
        canvas.drawRect(0, 0, fieldDimensions.x * cellsDiameter, fieldDimensions.y * cellsDiameter, paint);

        // draw board limits
        paint.setColor(Color.DKGRAY);
        for (int i = 0; i < fieldDimensions.y; i++) {
            // fill first and last cell
            canvas.drawRect(0, i * cellsDiameter, cellsDiameter, (i + 1) * cellsDiameter, paint);
            canvas.drawRect((fieldDimensions.x - 1) * cellsDiameter, i * cellsDiameter, width, (i + 1) * cellsDiameter, paint);

            // if first line, draw every cell
            if (i == 0)
                for (int j = 0; j < fieldDimensions.x; j++)
                    canvas.drawRect(j * cellsDiameter, i * cellsDiameter, (j + 1) * cellsDiameter, (i + 1) * cellsDiameter, paint);

            // if last line, draw every cell with y correction
            if (i == fieldDimensions.y - 1)
                for (int j = 0; j < fieldDimensions.x; j++)
                    canvas.drawRect(j * cellsDiameter, i * cellsDiameter, (j + 1) * cellsDiameter, height, paint);
        }

        // draw apple
        drawApple(canvas);

        // draw snake
        drawSnake(canvas);

        // display score
        drawScore(canvas);
    }

    private void drawApple(Canvas canvas) {
        Point p = apple.getLocation();

        paint.setColor(Color.BLACK);
        canvas.drawCircle(cellsRadius + p.x * cellsDiameter, cellsRadius + p.y * cellsDiameter, cellsRadius, paint);

        paint.setColor(apple.getColor());
        canvas.drawCircle(cellsRadius + p.x * cellsDiameter, cellsRadius + p.y * cellsDiameter, (float) (cellsRadius - cellsRadius / 4.0), paint);
    }

    private void drawSnake(Canvas canvas) {
        paint.setColor(Color.BLACK);

        for (Cell cell : snake.getCells()) {
            Point p = cell.getLocation();
            canvas.drawCircle(cellsRadius + p.x * cellsDiameter, cellsRadius + p.y * cellsDiameter, cellsRadius, paint);
        }
    }

    private void drawScore(Canvas canvas) {
        int textSize = 3 * cellsDiameter / 2;

        paint.setTextSize(textSize);
        paint.setColor(Color.BLACK);
        canvas.drawText("Score: " + snake.getScore(), textSize / 4 + 2, textSize + 2, paint);

        paint.setColor(Color.YELLOW);
        canvas.drawText("Score: " + snake.getScore(), textSize / 4, textSize, paint);
    }

    @Override
    public void bottom2top(View v) {
        snake.setDirection(Direction.UP);
        invalidate();
    }

    @Override
    public void top2bottom(View v) {
        snake.setDirection(Direction.DOWN);
        invalidate();
    }

    @Override
    public void left2right(View v) {
        snake.setDirection(Direction.RIGHT);
        invalidate();
    }

    @Override
    public void right2left(View v) {
        snake.setDirection(Direction.LEFT);
        invalidate();
    }

    @Override
    public void onClick(View v) {
        if (snake.isDead())
            startNewGame();
    }
}
