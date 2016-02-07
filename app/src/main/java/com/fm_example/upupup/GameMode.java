package com.fm_example.upupup;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMode extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private int mWidth;
    private int mHeight;
    private SurfaceHolder mHolder;

    private boolean mIsAttached;
    private Thread mThread;
    private Canvas mCanvas = null;
    private Paint mPaint = null;
    private Random mRand;

    private Bitmap mBitmapPlayer;
    private Bitmap mBitmapBall;
    private Bitmap mBackGround;
    private BoxMove mPlayer;
    private Bitmap mBitmapButton;
    private ItemMove mBall;
    private ItemObject mBackMenu;
    private MediaPlayer mStageBgm;
    private double mHeightAdjust;
    private double mWidthAdjust;

    private boolean mIsClear;

    SharedPreferences.Editor etest2;

    private double ref;
    private double ref2;
    private int mLaunchX;
    private boolean mIsBack;
    private int mBoxNumber = -1;
    private int mCircleNumber;
    private boolean mIsTouch;
    private double mTouchX;
    private double mTouchY;
    private Path mMenuZone;
    private Region mRegionMenuZone;
    private View decor;

    private List<ItemMove> mBallList = new ArrayList<ItemMove>();
    //
    private double[] mBallSpeedX = {0, 0, 0, 0, 1.7, -1.7, 2.2, -2.2, 0, 0, -3, 2.6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private double[] mBallSpeedY = {3, 5, 7, 7, 5, 5, 6.5, 6.5, 8.5, 10, 7, 7.5, 2.6, 2, 2.5, 2.5, 2.5, 2, 2, 2, 4.6, 3};
    private double[] mCircleSpeed = {1.5, 1.2, 3.5, 4, 4, 3.5, 3.5, 3.5, 4.5, 5};
    private double[] mCircleSize = {7, 10.5, 12, 14, 14, 10, 10, 10, 14, 12};
    private int[] mCirclePlusMinus = {-1, 1, 0, 0, 0, 1, -1, 1, 0, 1};
    private int[] mShootVariety = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 1, 1, 1, 2, 1};
    //0=Normal,1=Circle,2=RandL;
    private int[] mLaunchWidth = {0, 0, 0, 0, 2, 1, 2, 1, 0, 0, 1, 2, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3};
    //0=Normal,1=Right,2=Left,3=Circle


    public GameMode(Context context) {
        super(context);
        etest2 = MainActivity.preftest2.edit();


        ((Activity) getContext()).setContentView(this);
        decor = ((Activity) getContext()).getWindow().getDecorView();
        decor.setSystemUiVisibility(SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);

        mHolder = getHolder();
        mHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);

        mWidth = getWidth();
        mHeight = getHeight();

        mHeightAdjust = Adjust.getHeightAdjust(mHeight);
        mWidthAdjust = Adjust.getWidthAdjust(mWidth);


        Resources rsc = getResources();

        mBitmapPlayer = BitmapFactory.decodeResource(rsc, R.drawable.box);
        mBitmapPlayer = Bitmap.createScaledBitmap(mBitmapPlayer, mWidth / 4,
                mHeight / 8, false);


        mBitmapBall = BitmapFactory.decodeResource(rsc, R.drawable.ball);
        mBitmapBall = Bitmap.createScaledBitmap(mBitmapBall, mWidth / 8,
                mWidth / 8, false);

        mBitmapButton = BitmapFactory.decodeResource(rsc, R.drawable.backmenu);
        mBitmapButton = Bitmap.createScaledBitmap(mBitmapButton, mWidth / 3, mBitmapBall.getWidth(), false);


        mBackGround = BitmapFactory.decodeResource(rsc, R.drawable.background);

        mStageBgm = MediaPlayer.create(getContext(), R.raw.bgm);
        this.mStageBgm.setLooping(true);
        mStageBgm.start();

        mRand = new Random();

        newPlayer();
        backMenu();

        newBall();

        mIsAttached = true;
        mThread = new Thread(this);
        mThread.start();

    }

    @Override
    public void run() {
        while (mIsAttached) {
            drawGameBoard();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            mTouchX = event.getX();
            mTouchY = event.getY();

            mPlayer.setLocate(mTouchX - mBitmapPlayer.getWidth() / 2, mTouchY - mBitmapPlayer.getHeight());

            //左
            if (mTouchX < mBitmapPlayer.getWidth() / 2) {
                mPlayer.setLocate(0, mTouchY - mBitmapPlayer.getHeight());
            }

            //右
            if (mTouchX + mBitmapPlayer.getWidth() / 2 > mWidth) {
                mPlayer.setLocate(mWidth - mBitmapPlayer.getWidth(), mTouchY - mBitmapPlayer.getHeight());
            }

            //上
            if (mTouchY < mHeight - mHeight / 3 + mBall.getWidth()) {
                mPlayer.setLocate(mTouchX - mBitmapPlayer.getWidth() / 2, mHeight - mHeight / 3 - mBitmapPlayer.getHeight() + mBall.getWidth());
                //左上
                if (mTouchX < mBitmapPlayer.getWidth() / 2)
                    mPlayer.setLocate(0, mHeight - mHeight / 3 - mBitmapPlayer.getHeight() + mBall.getWidth());
                //右上
                if (mTouchX + mBitmapPlayer.getWidth() / 2 > mWidth)
                    mPlayer.setLocate(mWidth - mBitmapPlayer.getWidth(), mHeight - mHeight / 3 - mBitmapPlayer.getHeight() + mBall.getWidth());
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mPlayer.move(0, 0);
            }

            //メニューに戻る
            if (mTouchY <= mBitmapBall.getWidth() && mTouchX <= mBitmapButton.getWidth()) {
                mIsBack = true;
                mStageBgm.stop();
                ((Activity) getContext()).finish();
            }

            if (mTouchY <= mHeight - mHeight / 3 + mBall.getWidth()) {
                mTouchY = mHeight - mHeight / 3 + mBall.getWidth();
            }

        }
        return true;
    }


    public void drawGameBoard() {
        if (mIsClear || mIsBack)
            return;

        mPlayer.ballCatch(mBallList, mBoxNumber, getContext());
        OutSideDelete.outsideDelete(mBallList, mWidth, mHeight);


        if (mBallList.isEmpty()) {
            if (mBoxNumber > 11) {
                mCircleNumber++;
            }
            newBall();
            mIsTouch = false;
        }


        mBall.shoot(wAdjust(ItemMove.SpeedX), hAdjust(ItemMove.SpeedY));

        if (mShootVariety[mBoxNumber] == 2 && !mIsTouch) {
            mBall.RandL(mCircleSpeed[mCircleNumber], hAdjust((hAdjust(mCircleSize[mCircleNumber]))));
        }
        if (mShootVariety[mBoxNumber] == 1 && !mIsTouch) {
            mBall.Circle(mCircleSpeed[mCircleNumber], hAdjust(hAdjust(mCircleSize[mCircleNumber])), mCirclePlusMinus[mCircleNumber]);
        }

        if (mPlayer.ballTouchLeft(mBallList)) {
            mBall.setSpped(-Math.abs(mTouchX - ref + wAdjust(4)), hAdjust(8));
            mIsTouch = true;
        }


        if (mPlayer.ballTouchRight(mBallList)) {
            mBall.setSpped(Math.abs(mTouchX - ref + wAdjust(4)), hAdjust(8));
            mIsTouch = true;
        }

        if (mPlayer.ballTouchUnder(mBallList)) {
            mBall.setSpped(0, hAdjust(8) + (mTouchY - ref2) / 2);
            mIsTouch = true;
        }

        if (mPlayer.ballTouchLeftUp(mBallList)) {
            mBall.setSpped(0, -(Math.abs(ref2 - mTouchY) + hAdjust(6)));
            mIsTouch = true;
        }

        if (mPlayer.ballTouchRightUp(mBallList)) {
            mBall.setSpped(0, -(Math.abs(ref2 - mTouchY) + hAdjust(6)));
            mIsTouch = true;
        }


        for (ItemMove itemmove : mBallList) {
            if (itemmove != null) {
                itemmove.move(wAdjust(itemmove.SpeedX), hAdjust(itemmove.SpeedY));
            }
        }

        ref2 = mTouchY;
        ref = mTouchX;


        mCanvas = getHolder().lockCanvas();
        if (mCanvas != null) {
            mCanvas.drawBitmap(mBackGround, 0, 0, mPaint);

            MenuBar();

            if (mIsClear) {
                gameEnd();
            }

            mPlayer.draw(mCanvas);
            mBackMenu.draw(mCanvas);

            for (ItemMove itemmove : mBallList) {
                mCanvas.drawBitmap(mBitmapBall, (int) itemmove.getLeft(), (int) itemmove.getTop(), null);
            }

            getHolder().unlockCanvasAndPost(mCanvas);


        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        mStageBgm.stop();
        bitmapDestroy(mBitmapPlayer);
        bitmapDestroy(mBitmapBall);
        bitmapDestroy(mBackGround);
        bitmapDestroy(mBitmapButton);
        mIsAttached=false;
        while(mThread.isAlive());
    }

    private void bitmapDestroy(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }


    private void newPlayer() {
        mPlayer = new BoxMove(getWidth() / 2, mHeight - mBitmapPlayer.getHeight() - 1, mBitmapPlayer.getWidth(), mBitmapPlayer.getHeight(),
                mBitmapPlayer, getContext());
        BoxMove.mScore = 0;
        mIsBack = false;
        mIsClear = false;
    }

    private void newBall() {
        mBoxNumber++;

        if (mBoxNumber == mBallSpeedX.length) {
            mIsClear = true;
            mCircleNumber = mCircleSpeed.length - 1;
            mBoxNumber = mBallSpeedX.length - 1;
        }


        //0=Normal,1=Right,2=Left,3=Circle
        switch (mLaunchWidth[mBoxNumber]) {
            case 0: {
                mLaunchX = mRand.nextInt(mWidth - mBitmapBall.getWidth() * 2) + mBitmapBall.getWidth();
            }
            break;
            case 2: {
                mLaunchX = mRand.nextInt(mWidth / 2);
            }
            break;
            case 1: {
                mLaunchX = mRand.nextInt(mWidth / 2) + mWidth / 2;
            }
            break;
            case 3: {
                mLaunchX = mRand.nextInt(mWidth / 3) + mBitmapBall.getWidth() * 2;
            }
            break;
        }

        mBall = new ItemMove(mLaunchX, 0, mBitmapBall.getWidth(), mBitmapBall.getHeight(),
                mBitmapBall, mBallSpeedX[mBoxNumber], mBallSpeedY[mBoxNumber]);
        mBallList.add(mBall);
    }

    private void backMenu() {
        mBackMenu = new ItemObject(0, 0, mBitmapButton.getWidth(), mBitmapButton.getHeight(), mBitmapButton);

    }

    private void MenuBar() {

        mMenuZone = new Path();
        mRegionMenuZone = new Region();

        mMenuZone.addRect(0, 0, mWidth, mBall.getWidth(), Path.Direction.CW);
        mRegionMenuZone.setPath(mMenuZone, mRegionMenuZone);

        mPaint.setColor(Color.WHITE);
        mCanvas.drawPath(mMenuZone, mPaint);
        String msg = "得点　　" + BoxMove.mScore;
        mPaint.setTextSize((int) wAdjust(80));
        mPaint.setColor(Color.BLACK);
        mCanvas.drawText(msg, mWidth / 2, mBall.getWidth() / 2, mPaint);
    }

    private void gameEnd() {
        if (MainActivity.preftest2.getInt("save", 0) < BoxMove.mScore) {
            etest2.putInt("save", BoxMove.mScore);
            etest2.apply();
        }
        mBitmapBall.eraseColor(Color.TRANSPARENT);
        String msg = "ゲームクリア";
        mPaint.setTextSize((int) wAdjust(90));
        mPaint.setColor(Color.BLACK);
        mCanvas.drawText(msg, mWidth / 2 - mBitmapBall.getWidth() * 3 / 2, mHeight / 2, mPaint);
    }

    private double hAdjust(double base) {
        return base * mHeightAdjust;
    }

    private double wAdjust(double base) {
        return base * mWidthAdjust;
    }

}
