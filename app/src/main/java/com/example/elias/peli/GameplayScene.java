package com.example.elias.peli;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.sql.Time;
import java.util.Timer;

public class GameplayScene implements Scene {

    private Rect r = new Rect();
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private long gameOverTime;
    private Context context;

    private MainThread mainThread;


    private OrientationData orientationData;
    private long frameTime;



    public GameplayScene(MainThread thread, Context context) {

    player =new RectPlayer(new Rect(100,100,200,200),Color.rgb(255,0,0));
    playerPoint =new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
    player.update(playerPoint);

    obstacleManager = newObstacleManager();
    mainThread = thread;

    orientationData = new OrientationData();
    orientationData.register();
    frameTime = System.currentTimeMillis();
    this.context = context;
}
    public ObstacleManager newObstacleManager(){
        return new ObstacleManager(300,600,80,Color.BLUE);
    }

    public void reset () {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = newObstacleManager();
        movingPlayer = false;
    }

    @Override
        public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;

    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY()))
                    movingPlayer = true;
                if (gameOver && System.currentTimeMillis() - gameOverTime >= 1000) {
                    killRunning();
                    Timer t = new Timer();
                    try {
                        t.wait(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent i = new Intent(context, ScoreScreen.class);
                    i.putExtra("Score", obstacleManager.getScore());
                    context.startActivity(i);
                   /* reset();
                    gameOver = false;
                    orientationData.newGame();*/
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (!gameOver && movingPlayer)
                    playerPoint.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    private void killRunning() {
            mainThread.setRunning(false);
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.rgb(255,255,255));

        player.draw(canvas);
        obstacleManager.draw(canvas);
        if (gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(200);
            paint.setColor(Color.RED);
            drawCenterText(canvas, paint, "Hävisit pelin");

        }

    }

    @Override
    public void update(){
        if (!gameOver) {
            if (frameTime < Constants.INIT_TIME)
            frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if (orientationData.getOrientation() != null && orientationData.getOrientation() != null) {
                float pitch = orientationData.getOrientation() [1] - orientationData.getStartOrientation() [1];
                float roll = orientationData.getOrientation() [2] - orientationData.getStartOrientation() [2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH/1000f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT/1000f;

                playerPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed * elapsedTime) > 5 ? ySpeed * elapsedTime : 0;
            }
            if (playerPoint.x < 0)
                playerPoint.x = 0;
            else if (playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;

            if (playerPoint.y < 0)
                playerPoint.y = 0;
            else if (playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            player.update(playerPoint);
            obstacleManager.update();

            if (obstacleManager.playerCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }

    }
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
    private void returnMenu() {


    }
}
