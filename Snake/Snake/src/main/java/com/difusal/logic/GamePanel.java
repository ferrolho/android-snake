package com.difusal.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.difusal.snake.ActivitySwipeDetector;
import com.difusal.snake.SwipeInterface;

import java.util.LinkedList;
import java.util.Queue;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, SwipeInterface {
    private static final String TAG = GamePanel.class.getSimpleName();

    private Context context;
    private MainThread thread;
    private Paint paint;
    private int tickCounter;
    private Queue<Direction> directionsQueue;

    private Point fieldDimensions;
    private int cellsDiameter, cellsRadius;
    private Snake snake;
    private Apple apple;

    private String highScoreKey = "highScore";
    private long highScore;
    private boolean highScoreUpdated;

    public GamePanel(Context context) {
        super(context);

        // save context (necessary to save high score)
        this.context = context;

        // add the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // set on touch listener
        setOnTouchListener(new ActivitySwipeDetector(this));

        // create the game loop thread
        thread = new MainThread(getHolder(), this);

        // create paint
        paint = new Paint();

        // create directions queue
        directionsQueue = new LinkedList<Direction>();

        // make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    /**
     * Game initialize method.
     */
    private void initGame() {
        // reset tick counter
        tickCounter = 0;

        // reset directions queue
        directionsQueue.clear();

        Log.d("SnakeView", "View width: " + getWidth());
        Log.d("SnakeView", "View height: " + getHeight());

        // initialize game board and game elements radius
        int fieldWidth = 20;
        cellsDiameter = getWidth() / fieldWidth;
        cellsRadius = cellsDiameter / 2;
        int fieldHeight = getHeight() / cellsDiameter;
        fieldDimensions = new Point(fieldWidth, fieldHeight);

        Log.d("MainActivity", "Cell Diameter: " + cellsDiameter);
        Log.d("MainActivity", "Field Dimensions: " + fieldWidth + "x" + fieldHeight);

        // create snake
        snake = new Snake(cellsRadius);

        // create apple
        apple = new GreenApple(fieldDimensions, snake, cellsRadius);

        // reset highScoreUpdated flag
        highScoreUpdated = false;

        // load high score
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        highScore = sharedPref.getLong(highScoreKey, 0);
    }

    /**
     * Game update method.
     */
    public void update() {
        // increment tick counter
        tickCounter++;

        // if snake is alive
        if (tickCounter % snake.getMoveDelay() == 0) {
            // set snake direction
            if (!directionsQueue.isEmpty())
                snake.setDirection(directionsQueue.poll());

            // check if snake hit any wall
            checkIfSnakeHitAnyWall();

            if (!snake.isDead()) {
                // move the snake
                snake.move();

                // check if snake ate apple
                checkIfSnakeAteApple();
            } else {
                // if high score hasn't been updated
                if (!highScoreUpdated) {
                    Log.d(TAG, "Updating high score");

                    saveHighScore();
                    highScoreUpdated = true;
                }
            }
        }
    }

    @Override
    public void bottom2top(View v) {
        if (snake.getDirection() == Direction.LEFT || snake.getDirection() == Direction.RIGHT)
            snake.setDirection(Direction.UP);
    }

    @Override
    public void top2bottom(View v) {
        if (snake.getDirection() == Direction.LEFT || snake.getDirection() == Direction.RIGHT)
            snake.setDirection(Direction.DOWN);
    }

    @Override
    public void left2right(View v) {
        if (snake.getDirection() == Direction.UP || snake.getDirection() == Direction.DOWN)
            snake.setDirection(Direction.RIGHT);
    }

    @Override
    public void right2left(View v) {
        if (snake.getDirection() == Direction.UP || snake.getDirection() == Direction.DOWN)
            snake.setDirection(Direction.LEFT);
    }

    @Override
    public void onClick(View v, int x, int y) {
        // if snake is dead
        if (snake.isDead()) {
            Log.d(TAG, "Starting new game");

            initGame();
        } else {
            Direction direction;

            if (snake.getDirection() == Direction.LEFT || snake.getDirection() == Direction.RIGHT) {
                // if snake is moving horizontally

                // if touch anywhere above of the snake head
                if (y < snake.getHead().getLocation().y * cellsDiameter)
                    // move snake up
                    direction = Direction.UP;
                else
                    // move snake down
                    direction = Direction.DOWN;

            } else {
                // if snake is moving vertically

                // if touch anywhere left of the snake head
                if (x < snake.getHead().getLocation().x * cellsDiameter)
                    // move snake left
                    direction = Direction.LEFT;
                else
                    // move snake right
                    direction = Direction.RIGHT;
            }

            // add direction to queue of directions to be applied to the snake
            directionsQueue.add(direction);
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

            // increase snake size
            snake.incSize();

            // increase snake speed
            snake.increaseSpeed();

            // generate new apple
            apple.newRandomLocation(fieldDimensions, snake);

            // update score
            snake.incScore(apple.getScore());

            // update high score
            if (snake.getScore() > highScore)
                highScore = snake.getScore();
        }
    }

    private void saveHighScore() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(highScoreKey, highScore);
        editor.commit();
    }

    /**
     * Game draw method.
     */
    public void render(Canvas canvas) {
        // draw background
        drawBackground(canvas);

        // draw board limits
        drawBoardLimits(canvas);

        // draw apple
        drawApple(canvas);

        // draw snake
        drawSnake(canvas);

        // display score
        drawScore(canvas);

        // if snake is dead
        if (snake.isDead())
            drawGameOverMessage(canvas);
    }

    private void drawBackground(Canvas canvas) {
        int bgColor = snake.isDead() ? Color.RED : Color.LTGRAY;
        paint.setColor(bgColor);

        canvas.drawRect(0, 0, fieldDimensions.x * cellsDiameter, fieldDimensions.y * cellsDiameter, paint);
    }

    private void drawBoardLimits(Canvas canvas) {
        paint.setColor(Color.DKGRAY);
        for (int i = 0; i < fieldDimensions.y; i++) {
            // fill first and last cell
            canvas.drawRect(0, i * cellsDiameter, cellsDiameter, (i + 1) * cellsDiameter, paint);
            canvas.drawRect((fieldDimensions.x - 1) * cellsDiameter, i * cellsDiameter, getWidth(), (i + 1) * cellsDiameter, paint);

            // if first line, draw every cell
            if (i == 0)
                for (int j = 0; j < fieldDimensions.x; j++)
                    canvas.drawRect(j * cellsDiameter, i * cellsDiameter, (j + 1) * cellsDiameter, (i + 1) * cellsDiameter, paint);

            // if last line, draw every cell with y correction
            if (i == fieldDimensions.y - 1)
                for (int j = 0; j < fieldDimensions.x; j++)
                    canvas.drawRect(j * cellsDiameter, i * cellsDiameter, (j + 1) * cellsDiameter, getHeight(), paint);
        }
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
        String[] text = new String[]{"Best: " + highScore, "Score: " + snake.getScore()};

        int textSize = 3 * cellsDiameter / 2;
        int leftPadding = textSize / 4;

        for (int i = 0; i < text.length; i++) {
            paint.setTextSize(textSize);
            paint.setColor(Color.BLACK);
            canvas.drawText(text[i], leftPadding + 2, (i + 1) * textSize + 2, paint);
            paint.setColor(Color.YELLOW);
            canvas.drawText(text[i], leftPadding, (i + 1) * textSize, paint);
        }
    }

    private void drawGameOverMessage(Canvas canvas) {
        String[] text = new String[]{"Game Over.", "Click to restart."};

        int textSize = 3 * cellsDiameter / 2;
        int leftPadding = textSize / 4;
        int topPadding = textSize * 5;

        for (int i = 0; i < text.length; i++) {
            paint.setTextSize(textSize);
            paint.setColor(Color.BLACK);
            canvas.drawText(text[i], leftPadding + 2, (i + 1) * textSize + topPadding + 2, paint);
            paint.setColor(Color.YELLOW);
            canvas.drawText(text[i], leftPadding, (i + 1) * textSize + topPadding, paint);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // if first time thread starts
        if (thread.getState() == Thread.State.TERMINATED)
            thread = new MainThread(getHolder(), this);

        MainThread.setRunning(true);
        thread.start();

        // initialize game
        initGame();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "Surface is being destroyed");

        // tell the thread to shut down and wait for it to finish. this is a clean shutdown
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }

        Log.d(TAG, "Thread was shut down cleanly");
    }
}
