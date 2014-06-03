package com.difusal.snake;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.difusal.logic.Direction;
import com.difusal.logic.Snake;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity implements SwipeInterface {
    private Timer timer;
    private Snake snake;
    SampleCanvas drawView;

    public void initGame() {
        // create snake
        snake = new Snake();

        // create canvas
        drawView = new SampleCanvas(this, snake);
        setContentView(drawView);
        ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
        drawView.setOnTouchListener(activitySwipeDetector);

        // create timer
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }
        }, 0, snake.getMoveDelay());
    }

    /**
     * This method is called directly by the timer
     * and runs in the same thread as the timer.
     */
    private void timerMethod() {
        updateGame();

        // call the method that will work with the UI
        this.runOnUiThread(timerTick);
    }

    private Runnable timerTick = new Runnable() {
        /**
         * This method runs in the same thread as the UI.
         * Do something to the UI thread here.
         */
        @Override
        public void run() {
            // redraw view
            drawView.invalidate();
        }
    };

    public void updateGame() {
        // move the snake
        snake.move();

        if (snake.getHead().getLocation().y > 50)
            snake.incSize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // initialize game
        initGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void bottom2top(View v) {
        snake.setDirection(Direction.UP);
        drawView.invalidate();
    }

    @Override
    public void top2bottom(View v) {
        snake.setDirection(Direction.DOWN);
        drawView.invalidate();
    }

    @Override
    public void left2right(View v) {
        snake.setDirection(Direction.RIGHT);
        drawView.invalidate();
    }

    @Override
    public void right2left(View v) {
        snake.setDirection(Direction.LEFT);
        drawView.invalidate();
    }
}
