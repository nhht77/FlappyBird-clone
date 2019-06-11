package flappy.bird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import java.util.Random;

public class GameView extends View {

    Handler handler;
    Runnable runnable;
    final int UPDATE_MILIS=30;
    Bitmap background;
    Bitmap topTube, bottomTube;
    Display display;
    Point point;
    int dWidth, dHeight;
    Rect rect;
    Bitmap[] birds;
    int birdFrame = 0;
    int velocity=0, gravity=3;
    int birdX, birdY;
    boolean gameState = false;
    int gap = 500;
    int minTubeOffset, maxTubeOffset;
    int numberOfTubes = 4;
    int distanceBetweenTubes;
    int[] tubeX = new int[numberOfTubes];
    int[] topTubeY = new int[numberOfTubes];
    Random random;
    int tubeVelocity = 8;

//     Dummy comment
    public GameView(Context context){
        super(context);
        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run(){
                invalidate();
            }
        };
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        topTube    = BitmapFactory.decodeResource(getResources(), R.drawable.tubedown);
        bottomTube = BitmapFactory.decodeResource(getResources(), R.drawable.tubeup);
        display    = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point      = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0, 0, dWidth, dHeight);
        birds = new Bitmap[3];
        birds[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bird_upflap);
        birds[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bird_midflap);
        birds[2] = BitmapFactory.decodeResource(getResources(), R.drawable.bird_downflap);
        birdX = dWidth/2 - birds[0].getWidth()/2;
        birdY = dHeight/2 - birds[0].getHeight()/2;
        distanceBetweenTubes = dWidth*3/4;
        minTubeOffset = gap/2;
        maxTubeOffset = dHeight - minTubeOffset - gap;
        random  = new Random();
        for(int i=0; i <numberOfTubes; i++){
            tubeX[i]   = dWidth + i*distanceBetweenTubes;
            topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(background, null, rect, null);
        if(birdFrame == 0){
            birdFrame = 1;
        } else if ( birdFrame == 1){
            birdFrame = 2;
        } else if (birdFrame == 2) {
            birdFrame = 0;
        }

        if(gameState) {
            if (birdY < dHeight - birds[0].getHeight() || velocity < 0) {
                velocity += gravity;
                birdY += velocity;
            }
            for(int i=0; i<numberOfTubes; i++){
                tubeX[i] -= tubeVelocity;
                if(tubeX[i] < -topTube.getWidth()){
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset +1);
                }
                canvas.drawBitmap(topTube, tubeX[i], topTubeY[i] - topTube.getHeight(), null);
                canvas.drawBitmap(bottomTube, tubeX[i], topTubeY[i] + gap, null);
            }
        }
        canvas.drawBitmap(birds[birdFrame], birdX, birdY, null);
        handler.postDelayed(runnable, UPDATE_MILIS);

    }

    public boolean onTouchEvent(MotionEvent event){


        int action =  event.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            velocity = -30;
            gameState = true;
        }

        return true;
    }
}
