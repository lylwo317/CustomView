package com.kevin.mycircleprogress.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by XieJiaHua on 2016/7/14.
 */
public class ListViewDrawable extends Drawable {

    private Paint linePaint;

    public ListViewDrawable() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(3*3);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect myRect = getBounds();
        canvas.drawLine(myRect.left+10f*3,myRect.top,myRect.left+10f*3,myRect.bottom,linePaint);
    }

    public void setColor(int color){
        linePaint.setColor(color);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
