package com.difusal.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.difusal.logic.Cell;
import com.difusal.logic.Snake;

public class SampleCanvas extends View {
    private Paint paint;
    private Snake snake;

    public SampleCanvas(Context context, Snake snake) {
        super(context);

        paint = new Paint();
        this.snake = snake;
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawSnake(canvas);
    }

    private void drawSnake(Canvas canvas) {
        paint.setColor(Color.BLACK);

        int r = snake.getCellsRadius();
        int d = 2 * r;

        for (Cell cell : snake.getCells()) {
            Point p = cell.getLocation();

            canvas.drawCircle(r + p.x * d, r + p.y * d, r, paint);
        }
    }
}
