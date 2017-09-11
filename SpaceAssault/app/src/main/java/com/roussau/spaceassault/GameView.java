package com.roussau.spaceassault;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class GameView extends SurfaceView implements Runnable {

    // Backgrounds
    private ArrayList<Background> backgrounds;

    // Player
    private Player player;
    boolean actionFlag = false;
    private int touchEventXPos;
    private long lastScore = 0;
    private long lastSpawnTime = 0;
    private long lastTakenDamageTime = 0;

    // Asteroids
    private ArrayList<Asteroid> asteroids;

    // Game thread
    private volatile boolean running;
    private Thread gameThread = null;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    // Holds a reference to the Activity
    private Context context;

    // Control the fps
    long fps = 60;

    // Screen resolution
    private int screenWidth;
    private int screenHeight;

    // Random number generator
    Random random = new Random();

    GameView(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.context = context;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        surfaceHolder = getHolder();
        paint = new Paint();

        player = new Player(context);

        asteroids = new ArrayList<>();
        backgrounds = new ArrayList<>();

        backgrounds.add(new Background(this.context, screenWidth, screenHeight, "star_background1", 25));
        backgrounds.add(new Background(this.context, screenWidth, screenHeight, "star_background2", 85));
        backgrounds.add(new Background(this.context, screenWidth, screenHeight, "star_background3", 177));
        backgrounds.add(new Background(this.context, screenWidth, screenHeight, "star_background4", 180));
    }

    @Override
    public void run() {
        while (running) {
            long startFrameTime = System.currentTimeMillis();

            if (lastSpawnTime + Settings.SPAWN_PERIOD < startFrameTime) {
                spawnAsteroid();
                lastSpawnTime = startFrameTime;
            }

            update();
            draw();

            // Calculate the fps this frame
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;

            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
        for (Background bg : backgrounds) {
            bg.update(fps);
        }

        if (actionFlag) {
            player.move(touchEventXPos);
        }
        else {
            player.stay();
        }

        player.update();

        // Remove out of bounds asteroids
        ArrayList<Asteroid> deleteCandidates = new ArrayList<>();
        for (Asteroid asteroid : asteroids) {
            if (asteroid.getY() > (player.getY() + player.getHeight() / 2)) {
                deleteCandidates.add(asteroid);
            }
        }
        for (Asteroid asteroid : deleteCandidates) {
            asteroids.remove(asteroid);
        }

        if (lastTakenDamageTime + Settings.IMMUNITY_TIME < System.currentTimeMillis()) {
            checkCollisions();
        }

        if (lastScore + Settings.SCORE_PER_LEVEL < player.getScore()) {
            lastScore = player.getScore();
            increaseLevel();
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Draw the background parallax
            drawBackground(0, 255);
            drawBackground(1, 204);
            drawBackground(2, 152);
            drawBackground(3, 255);

            // Draw player's spaceship
            player.draw(canvas);

            // Draw asteroids
            for (Asteroid asteroid : asteroids) {
                asteroid.draw(canvas);
            }

            // Draw player's score
            paint.setTextSize(45);
            paint.setAlpha(200);
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawText("Score: " + player.getScore(), Settings.SCORE_X_POS, Settings.SCORE_Y_POS, paint);

            // Draw player's health points
            player.printHealthPoints(canvas);

            // Unlock and draw the scene
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawBackground(int position, int alpha) {
        Background bg = backgrounds.get(position);

        Rect fromRect1 = new Rect(0, 0, bg.width, bg.height - bg.yClip);
        Rect toRect1 = new Rect(0, bg.yClip, bg.width, bg.height);

        Rect fromRect2 = new Rect(0, bg.height - bg.yClip, bg.width, bg.height);
        Rect toRect2 = new Rect(0, 0, bg.width, bg.yClip);

        paint.setAlpha(alpha);

        if (!bg.reversedFirst) {
            canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
        } else {
            canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
        }
    }

    // Clean up our thread if the game is stopped
    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {}
    }

    // Make a new thread and start it
    // Execution moves to our run method
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchEventXPos = (int) event.getX();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            actionFlag = true;
            return true;
        }
        else if (action == MotionEvent.ACTION_UP) {
            actionFlag = false;
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void checkCollisions() {
        Rect playerRect = player.getRectangle();

        for (Asteroid asteroid : asteroids) {
            Rect asteroidRect = asteroid.getRectangle();

            if (playerRect.intersect(asteroidRect)) {
                player.healthPoints--;
                lastTakenDamageTime = System.currentTimeMillis();

                if (player.healthPoints <= 0) {
                    int score = (int) player.getScore();
                    Intent intent = new Intent().setClass(getContext(), ResultActivity.class);
                    intent.putExtra("SCORE", score);
                    ((Activity) getContext()).startActivity(intent);

                }

                return;
            }
        }
    }

    public void increaseLevel() {
        Settings.ASTEROID_SPEED = (int)((float)Settings.ASTEROID_SPEED * 1.45);
        Settings.ASTEROID_TYPE_MODIFIER = Math.max(10, Settings.ASTEROID_TYPE_MODIFIER + 1);
    }

    public void spawnAsteroid() {
        int startX = random.nextInt() % Settings.SCREEN_WIDTH;
        int type = random.nextInt() % 10;

        if (type < Settings.ASTEROID_TYPE_MODIFIER) {
            type = 1;
        }
        else {
            type = 0;
        }

        asteroids.add(new Asteroid(this.context, type, Settings.ASTEROID_SPEED, startX));
    }
}
