package com.kevin.mycircleprogress.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/6/24.
 */
public class DashedLine extends View {
    public DashedLine(Context context) {
        super(context);
    }

    public DashedLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DashedLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(android.R.color.black));
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, 900);
        PathEffect effects = new DashPathEffect(new float[]{4, 4, 4, 4}, 2);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}
