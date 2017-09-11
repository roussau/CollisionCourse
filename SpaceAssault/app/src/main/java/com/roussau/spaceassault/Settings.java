package com.roussau.spaceassault;

public final class Settings {
    // Variables that affect player's experience
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static int SHIP_WIDTH;
    public static int SHIP_HEIGHT;
    public static int SHIP_SPEED;
    public static int SHIP_START_POS_X;
    public static int SHIP_START_POS_Y;
    public static int ASTEROID_SPEED;
    public static int ASTEROID_SMALL_WIDTH;
    public static int ASTEROID_SMALL_HEIGHT;
    public static int ASTEROID_BIG_WIDTH;
    public static int ASTEROID_BIG_HEIGHT;
    public static final int STARTING_HEALTH = 3;
    public static int HEALTH_POINT_WIDTH;
    public static int HEALTH_POINT_HEIGHT;
    public static int HEALTH_POINT_OFFSET_X;
    public static int HEALTH_POINT_OFFSET_Y;
    public static final int SCORE_PER_LEVEL = 250;
    public static final int IMMUNITY_TIME = 750;
    public static int ASTEROID_TYPE_MODIFIER = 2;
    public static final int SPAWN_PERIOD = 190;
    public static int SCORE_X_POS;
    public static int SCORE_Y_POS;

    public static void setScreenWidth(int sw) {
        SCREEN_WIDTH = sw;
    }

    public static void setScreenHeight(int sh) {
        SCREEN_HEIGHT = sh;
    }

    public static void init() {
        SHIP_WIDTH = SCREEN_WIDTH / 10 + 9;
        SHIP_HEIGHT = SCREEN_HEIGHT / 15 + 3;
        SHIP_SPEED = 59;
        SHIP_START_POS_X = SCREEN_WIDTH / 2;
        SHIP_START_POS_Y = SCREEN_HEIGHT - SHIP_HEIGHT - 20;
        ASTEROID_SPEED = 55;
        ASTEROID_SMALL_WIDTH = SCREEN_WIDTH / 12;
        ASTEROID_SMALL_HEIGHT = ASTEROID_SMALL_WIDTH + 1;
        ASTEROID_BIG_WIDTH = SCREEN_WIDTH / 7;
        ASTEROID_BIG_HEIGHT = ASTEROID_BIG_WIDTH - 30;
        HEALTH_POINT_WIDTH = SCREEN_WIDTH / 20 + 10;
        HEALTH_POINT_HEIGHT = HEALTH_POINT_WIDTH + 10;
        HEALTH_POINT_OFFSET_X = HEALTH_POINT_WIDTH / 2 + 12;
        HEALTH_POINT_OFFSET_Y = SCREEN_HEIGHT / 30 - 12;
        SCORE_X_POS = SCREEN_WIDTH - SCREEN_WIDTH / 4 - 25;
        SCORE_Y_POS = SCREEN_HEIGHT / 30 + HEALTH_POINT_WIDTH - 20;
    }
}
