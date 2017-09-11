package com.roussau.spaceassault;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.lang.Math;

class Player extends GameObject {
    private int speed;
    private Bitmap shipLeft, shipRight, shipStraight, ship, healthPoint;
    private long score;
    private long startTime;
    public int healthPoints;
    Paint paint;

    public Player(Context context) {
        this.width = Settings.SHIP_WIDTH;
        this.height = Settings.SHIP_HEIGHT;
        this.healthPoints = Settings.STARTING_HEALTH;
        this.x = Settings.SHIP_START_POS_X;
        this.y = Settings.SHIP_START_POS_Y;
        this.speed = Settings.SHIP_SPEED;

        shipStraight = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        shipStraight = Bitmap.createScaledBitmap(shipStraight, width, height, true);

        shipLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_left);
        shipLeft = Bitmap.createScaledBitmap(shipLeft, width, height, true);

        shipRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_right);
        shipRight = Bitmap.createScaledBitmap(shipRight, width, height, true);

        healthPoint = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        healthPoint = Bitmap.createScaledBitmap(healthPoint, Settings.HEALTH_POINT_WIDTH, Settings.HEALTH_POINT_HEIGHT, true);

        ship = shipStraight;
        paint = new Paint();
    }


    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        if (elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }
    }

    public void draw(Canvas canvas) {
        Rect fromRect1 = new Rect(0, 0, width, height);
        Rect toRect1 = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(this.ship, fromRect1, toRect1, paint);
    }

    public void move(int xpos) {
        xpos -= 75;

        if (xpos <= x + speed  && xpos >= x - speed) {
            stay();
        }
        else if (xpos < x && x > 0) {
            x -= speed;
            x = Math.max(-75, x);
            ship = shipLeft;
        }
        else if (xpos > x && x < Settings.SCREEN_WIDTH - 75) {
            x += speed;
            x = Math.min(Settings.SCREEN_WIDTH - 75, x);
            ship = shipRight;
        }
    }

    public void stay() {
        ship = shipStraight;
    }

    public long getScore() {
        return score;
    }

    public void printHealthPoints(Canvas canvas) {
        for (int i = 1; i <= healthPoints; ++i) {
            Rect fromRect1 = new Rect(0, 0, Settings.HEALTH_POINT_WIDTH, Settings.HEALTH_POINT_HEIGHT);
            Rect toRect1 = new Rect((i - 1 )* Settings.HEALTH_POINT_WIDTH + i * Settings.HEALTH_POINT_OFFSET_X, Settings.HEALTH_POINT_OFFSET_Y,
                    i * Settings.HEALTH_POINT_WIDTH + i * Settings.HEALTH_POINT_OFFSET_X,
                    Settings.HEALTH_POINT_OFFSET_Y + Settings.HEALTH_POINT_HEIGHT);
            canvas.drawBitmap(this.healthPoint, fromRect1, toRect1, paint);
        }
    }

    @Override
    public Rect getRectangle() {
        return new Rect(x + 5, y + 3, x + width - 5, y + height / 2 + 7);
    }
}
