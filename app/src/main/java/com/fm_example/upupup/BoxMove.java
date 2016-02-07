package com.fm_example.upupup;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;;import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoxMove extends ItemObject {
    public static int mScore;
    private SoundPool mSoundPool;
    private int mGetBall;
    private short[] mScoreList = {10, 10, 10, 10, 20, 20, 20, 20, 20, 20, 30, 30, 30, 30, 40, 40, 40, 55, 55, 55, 65, 70};
    private double mWidthAd;
    private double mHeightAd;

    public BoxMove(int left, int top, int width, int height, Bitmap bitmap, Context con) {
        super(left, top, width, height, bitmap);
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mGetBall = mSoundPool.load(con, R.raw.getball, 2);
        mWidthAd = Adjust.getWidthAdjust(MainActivity.disp.getWidth()) * 20;
        mHeightAd = Adjust.getHeightAdjust(MainActivity.disp.getHeight()) * 20;

    }

    public void ballCatch(List list, int scoreNum, Context con) {
        Iterator<ItemMove> ballList = list.iterator();
        while (ballList.hasNext()) {
            ItemMove ball = ballList.next();
            if (this.getLeft() + mWidthAd < ball.getRight() &&
                    this.getTop() + mHeightAd * 3 < ball.getButton() &&
                    this.getRight() - mWidthAd > ball.getLeft() &&
                    this.getButton() - mHeightAd / 2 > ball.getTop() &&
                    !ballTouchLeft(list) &&
                    !ballTouchRight(list) &&
                    !ballTouchUnder(list) &&
                    !ballTouchLeftUp(list) &&
                    !ballTouchRightUp(list)) {
                mScore += mScoreList[scoreNum];
                mSoundPool.play(mGetBall, 1.0f, 1.0f, 0, 0, 1.0f);
                ballList.remove();
            }
        }
    }

    public boolean ballTouchLeft(List list) {
        Iterator<ItemMove> ballList = list.iterator();
        while (ballList.hasNext()) {
            ItemMove ball = ballList.next();
            if (this.getLeft() < ball.getCenterX() + ball.getRadian() &&
                    this.getTop() < ball.getButton() &&
                    this.getLeft() > ball.getLeft() &&
                    this.getButton() > ball.getTop()) {
                return true;
            }
        }
        return false;
    }

    public boolean ballTouchRight(List list) {
        Iterator<ItemMove> ballList = list.iterator();
        while (ballList.hasNext()) {
            ItemMove ball = ballList.next();
            if (this.getRight() < ball.getRight() &&
                    this.getTop() < ball.getButton() &&
                    this.getRight() > ball.getCenterX() - ball.getRadian() &&
                    this.getButton() > ball.getTop()) {
                return true;
            }
        }
        return false;
    }

    public boolean ballTouchUnder(List list) {
        Iterator<ItemMove> ballList = list.iterator();
        while (ballList.hasNext()) {
            ItemMove ball = ballList.next();
            if (this.getLeft() < ball.getRight() &&
                    this.getCenterY() < ball.getButton() &&
                    this.getRight() > ball.getLeft() &&
                    this.getButton() > ball.getCenterY() - ball.getRadian() &&
                    !ballTouchLeft(list) &&
                    !ballTouchRight(list)) {
                return true;
            }
        }
        return false;
    }


    public boolean ballTouchLeftUp(List list) {
        Iterator<ItemMove> ballList = list.iterator();
        while (ballList.hasNext()) {
            ItemMove ball = ballList.next();
            if (this.getLeft() + mWidthAd > ball.getLeft() &&
                    this.getTop() < ball.getButton() &&
                    this.getLeft() < ball.getRight() &&
                    this.getTop() + mWidthAd > ball.getCenterY()) {
                return true;
            }
        }
        return false;
    }

    public boolean ballTouchRightUp(List list) {
        Iterator<ItemMove> ballList = list.iterator();
        while (ballList.hasNext()) {
            ItemMove ball = ballList.next();
            if (this.getRight() < ball.getRight() &&
                    this.getTop() < ball.getButton() &&
                    this.getRight() - mWidthAd > ball.getLeft() &&
                    this.getTop() + mWidthAd > ball.getCenterY()) {
                return true;
            }
        }
        return false;
    }


}
