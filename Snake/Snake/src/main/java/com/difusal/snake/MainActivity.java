package com.difusal.snake;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.difusal.logic.GamePanel;
import com.difusal.logic.MainThread;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // request to turn the title OFF
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        // make it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
        //setContentView(R.layout.activity_main);

        // set our GamePanel as the View
        FrameLayout gamePanel = (FrameLayout) findViewById(R.id.gamePanel);
        gamePanel.addView(new GamePanel(this));
        */

        setContentView(new GamePanel(this));

        Log.d(TAG, "View added");
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
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "Pausing");
        MainThread.setRunning(false);
        super.onPause();
    }
}
