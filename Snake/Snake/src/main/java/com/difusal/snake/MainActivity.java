package com.difusal.snake;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.TimerTask;

public class MainActivity extends ActionBarActivity {
    private SnakeView snakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // create timer task
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }
        };

        // create snake view
        snakeView = new SnakeView(this, timerTask);
        setContentView(snakeView);
    }

    /**
     * This method is called directly by the timer
     * and runs in the same thread as the timer.
     */
    private void timerMethod() {
        snakeView.updateGame();

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
            snakeView.invalidate();
        }
    };

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
}
