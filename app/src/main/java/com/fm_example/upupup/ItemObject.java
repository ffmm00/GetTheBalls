package com.fm_example.upupup;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ItemObject {
    private double mLeft;
    private double mTop;
    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap;

    public ItemObject(double left, double top, int width, int heigth, Bitmap bitmap) {
        setLocate(left, top);
        this.mWidth = width;
        this.mHeight = heigth;
        this.mBitmap = bitmap;
    }

    public void setLocate(double left, double top) {
        this.mLeft = left;
        this.mTop = top;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, (int) getLeft(), (int) getTop(), null);
    }


    public void move(double left, double top) {
        this.mLeft = left + getLeft();
        this.mTop = top + getTop();
    }

    public double getLeft() {
        return mLeft;
    }

    public double getRight() {
        return mLeft + mWidth;
    }

    public double getTop() {
        return mTop;
    }

    public double getButton() {
        return mTop + mHeight;
    }

    public int getRadian() {
        return mWidth/2;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getCenterX() {
        return ((int) getLeft() + mWidth / 2);
    }

    public int getCenterY() {
        return (int) getTop() + mHeight / 2;
    }


}
