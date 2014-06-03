package com.difusal.snake;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.difusal.logic.Direction;
import com.difusal.logic.Snake;

public class MainActivity extends ActionBarActivity implements SwipeInterface {
    SampleCanvas drawView;
    private Snake snake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        snake = new Snake();

        drawView = new SampleCanvas(this, snake);
        setContentView(drawView);
        ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
        drawView.setOnTouchListener(activitySwipeDetector);
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
        snake.move(Direction.UP);
        drawView.invalidate();
    }

    @Override
    public void top2bottom(View v) {
        snake.move(Direction.DOWN);
        drawView.invalidate();
    }

    @Override
    public void left2right(View v) {
        snake.move(Direction.RIGHT);
        drawView.invalidate();
    }

    @Override
    public void right2left(View v) {
        snake.move(Direction.LEFT);
        drawView.invalidate();
    }
}
