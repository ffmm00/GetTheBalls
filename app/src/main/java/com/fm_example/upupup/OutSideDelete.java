package com.fm_example.upupup;

import java.util.Iterator;
import java.util.List;

public class OutSideDelete {

    public static void outsideDelete(List list, int width, int height) {
        Iterator<ItemMove> ballList = list.iterator();
        while (ballList.hasNext()) {
            ItemMove ball = ballList.next();
            if (ball.getRight() < -ball.getWidth() * 2 ||
                    ball.getRight() > width + ball.getWidth() * 2 ||
                    ball.getButton() < -ball.getWidth() * 3 ||
                    ball.getTop() > height + ball.getWidth() * 2) {
                ballList.remove();
            }
        }


    }

}
