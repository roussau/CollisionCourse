package com.roussau.spaceassault;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Asteroid extends GameObject {
    private int type;
    private int speed;
    private Paint paint;
    Bitmap bitmap;

    public Asteroid(Context context, int type, int speed, int startX) {
        paint = new Paint();
        this.type = type;
        this.speed = speed;

        if (type == 0) {
            this.width = Settings.ASTEROID_SMALL_WIDTH;
            this.height = Settings.ASTEROID_SMALL_HEIGHT;
            this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.meteor_small);
            this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        }
        else {
            this.width = Settings.ASTEROID_BIG_WIDTH;
            this.height = Settings.ASTEROID_BIG_HEIGHT;
            this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.meteor_big);
            this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        this.x = startX;
        this.y = -height;
    }

    public void draw(Canvas canvas) {
        Rect fromRect1 = new Rect(0, 0, width, height);
        Rect toRect1 = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bitmap, fromRect1, toRect1, paint);
        y += speed;
    }
}
