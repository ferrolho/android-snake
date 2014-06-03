package com.difusal.snake;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.difusal.logic.Direction;
import com.difusal.logic.Snake;

public class MainActivity extends ActionBarActivity implements SwipeInterface {
    private RefreshHandler mRedrawHandler;
    private Snake snake;
    SampleCanvas drawView;
    private long lastMoveTime;

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            updateGame();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    public void initGame() {
        // create handler
        mRedrawHandler = new RefreshHandler();

        // create snake
        snake = new Snake();

        // create canvas
        drawView = new SampleCanvas(this, snake);
        setContentView(drawView);
        ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
        drawView.setOnTouchListener(activitySwipeDetector);

        // initialize last move time
        lastMoveTime = 0;

        // update game
        updateGame();
    }

    public void updateGame() {
        long currentTime = System.currentTimeMillis();

        // if it is time to move the snake
        if (currentTime - lastMoveTime > snake.getMoveDelay()) {
            // move the snake
            snake.move();

            // redraw view
            drawView.invalidate();

            // update last move time
            lastMoveTime = currentTime;
        }

        mRedrawHandler.sleep(snake.getMoveDelay());
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
