package com.fm_example.upupup;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ItemMove extends ItemObject {

    private double rot;
    private double rotSave;
    private double rad;
    private double x;
    private double y;
    private final int CIRCLESIZE = 20;
    protected static double SpeedX;
    protected static double SpeedY;
    private boolean a;

    public ItemMove(int left, int top, int width, int height, Bitmap bitmap, double SpeedX, double SpeedY) {
        super(left, top, width, height, bitmap);
        setSpped(SpeedX, SpeedY);
    }


    //円回転
    public void Circle(double speed, double size, int plusMinus) {
        rot += speed;
        rad = Math.toRadians(rot);
        super.move(Math.cos(rad) * size, Math.sin(rad) * size * plusMinus);
    }

    //左右
    public void RandL(double speed,double size) {
        rot += speed;
        rad = Math.toRadians(rot);
        super.move(Math.cos(rad) * size, 0);
    }

    //斜め
    public void shoot(double SpeedX, double SpeedY) {
        super.move(SpeedX, SpeedY);
    }

    public void setSpped(double xSpeed, double ySpeed) {
        this.SpeedX = xSpeed;
        this.SpeedY = ySpeed;
    }

}