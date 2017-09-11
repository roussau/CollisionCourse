package com.roussau.spaceassault;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

class Background {

    Bitmap bitmap;
    Bitmap bitmapReversed;

    int width;
    int height;
    boolean reversedFirst;
    private float speed;
    int yClip;

    Background(Context context, int screenWidth, int screenHeight, String bitmapName, float s) {
        int resID = context.getResources().getIdentifier(bitmapName, "drawable", context.getPackageName());

        bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, true);

        yClip = 0;

        reversedFirst = false;
        speed = s;
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        //Create a mirror image of the background (vertical flip)
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        bitmapReversed = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    void update(long fps) {
        yClip += speed / fps;
        if (yClip >= height) {
            yClip = 0;
            reversedFirst = !reversedFirst;
        }
    }
}
