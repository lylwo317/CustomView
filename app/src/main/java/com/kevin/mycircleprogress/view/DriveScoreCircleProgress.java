package com.kevin.mycircleprogress.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.SystemClock;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.kevin.mycircleprogress.R;
import com.kevin.mycircleprogress.Utils;

/**
 * Created by XieJiaHua on 2016/6/22.
 */
public class DriveScoreCircleProgress extends View {

    private float default_description_text_size;
    private float default_score_text_size;
    private float default_description_text_margin_bottom;
    private float default_score_text_margin_bottom;

    private int default_first_circle_color = Color.rgb(221,225,234);
    private int default_second_circle_color = Color.rgb(22, 228, 156);
    private int default_third_circle_color = Color.rgb(22, 228, 156);
    private int default_score_text_color = Color.GREEN;
    private int default_description_text_color = Color.BLACK;

    private String description_text;
    private float default_score = 30;

    private float score_text_size;
    private float description_text_size;

    private int first_circle_color;
    private int second_circle_color;
    private int third_circle_color;
    private int score_text_color;
    private int description_text_color;

    private RectF outRect = new RectF();
    private RectF arcRect = new RectF();
    private Rect textBounds=new Rect();

    private Paint descriptionTextPaint;
    private Paint scoreTextPaint;
    private Paint firstCirclePaint;
    private Paint secondCirclePaint;
    private Paint thirdCirclePaint;
    private Paint progressPaint;

    private Paint scoreLinePaint;
    private float firstCircleRadius;
    private float secondCircleRadius;
    private float thirdCircleRadius;
    private float cx;
    private float cy;

    //得分
    private float score;

    private float descriptionMarginBottom;
    private float scoreTextMarginBottom;

    private float totalDegrees;
    private float startOffsetDegrees;

    public DriveScoreCircleProgress(Context context) {
        this(context, null);
    }

    public DriveScoreCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.DriveScoreCircleProgressStyle);
    }

    public DriveScoreCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_description_text_size = Utils.dp2px(getResources(), 9);
        default_score_text_size = Utils.dp2px(getResources(), 32f);
        default_description_text_margin_bottom = Utils.dp2px(getResources(), 25);
        default_score_text_margin_bottom = Utils.dp2px(getResources(), 50);
        outRect = new RectF();

        totalDegrees = 8*360f/12;
        startOffsetDegrees = 5*360f/12;

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,R.styleable.DriveScoreCircleProgress,defStyleAttr,R.style.DriveScoreCircleProgress);
        initAttribute(attributes);
        attributes.recycle();
        initPaint();
    }

    private void initAttribute(TypedArray attributes) {

        description_text = attributes.getString(R.styleable.DriveScoreCircleProgress_description_text);
        description_text_size = attributes.getDimension(R.styleable.DriveScoreCircleProgress_description_text_size, default_description_text_size);
        score_text_size = attributes.getDimension(R.styleable.DriveScoreCircleProgress_score_text_size, default_score_text_size);

        description_text_color = attributes.getColor(R.styleable.DriveScoreCircleProgress_description_text_color, default_description_text_color);
        first_circle_color = attributes.getColor(R.styleable.DriveScoreCircleProgress_first_circle_color, default_first_circle_color);
        second_circle_color = attributes.getColor(R.styleable.DriveScoreCircleProgress_second_circle_color, default_second_circle_color);
        third_circle_color = attributes.getColor(R.styleable.DriveScoreCircleProgress_third_circle_color, default_third_circle_color);
        score_text_color = attributes.getColor(R.styleable.DriveScoreCircleProgress_score_text_color, default_score_text_color);


        score = attributes.getFloat(R.styleable.DriveScoreCircleProgress_score, default_score);
        descriptionMarginBottom = attributes.getDimension(R.styleable.DriveScoreCircleProgress_description_text_margin_bottom, default_description_text_margin_bottom);
        scoreTextMarginBottom = attributes.getDimension(R.styleable.DriveScoreCircleProgress_score_text_margin_bottom, default_score_text_margin_bottom);
    }

    private void initPaint(){
        descriptionTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        descriptionTextPaint.setColor(description_text_color);
        descriptionTextPaint.setTextSize(description_text_size);
        descriptionTextPaint.setTextAlign(Paint.Align.CENTER);

        scoreTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scoreTextPaint.setColor(score_text_color);
        scoreTextPaint.setTextSize(score_text_size);
        scoreTextPaint.setTextAlign(Paint.Align.CENTER);

        firstCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstCirclePaint.setColor(first_circle_color);
        firstCirclePaint.setStyle(Paint.Style.STROKE);
        firstCirclePaint.setStrokeWidth(Utils.dp2px(getResources(), 0.3f));

        secondCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondCirclePaint.setColor(second_circle_color);
        secondCirclePaint.setStyle(Paint.Style.STROKE);
        secondCirclePaint.setStrokeWidth(Utils.dp2px(getResources(), 1.3f));

        thirdCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thirdCirclePaint.setColor(third_circle_color);
        thirdCirclePaint.setStyle(Paint.Style.STROKE);
        thirdCirclePaint.setStrokeWidth(Utils.dp2px(getResources(), 0.7f));

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(Color.BLACK);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(Utils.dp2px(getResources(), 8));

        scoreLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scoreLinePaint.setStrokeWidth(Utils.dp2px(getResources(), 2));
        scoreLinePaint.setStyle(Paint.Style.STROKE);
        scoreLinePaint.setColor(getResources().getColor(R.color.scoreLineColor));
    }

    @Override
    public void invalidate() {

        super.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        outRect.set(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.rotate(startOffsetDegrees, cx, cy);
        canvas.drawArc(arcRect, 0, score / 100 * totalDegrees, false, progressPaint);
        canvas.restore();

        drawClockScale(canvas, secondCircleRadius);

        canvas.drawCircle(cx, cy, firstCircleRadius, firstCirclePaint);//第一个圆
        canvas.drawCircle(cx, cy, secondCircleRadius, secondCirclePaint);
        canvas.drawCircle(cx, cy, thirdCircleRadius, thirdCirclePaint);

        canvas.drawText(String.valueOf((int) score), cx, getHeight() - scoreTextMarginBottom, scoreTextPaint);

        canvas.drawText(description_text, cx, getHeight() - descriptionMarginBottom, descriptionTextPaint);
        drawLine(canvas, thirdCircleRadius);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        firstCircleRadius = (Math.min(getWidth(),getHeight()) - firstCirclePaint.getStrokeWidth())/2f;
        cx = outRect.width() / 2;
        cy = outRect.height() / 2;
        updateProgressPaint();
        secondCircleRadius = firstCircleRadius - Utils.dp2px(getResources(), 3);
        thirdCircleRadius = firstCircleRadius - Utils.dp2px(getResources(), 15);
        int arcStrokeWidth = (int) (progressPaint.getStrokeWidth()/2f);
        arcRect.set(cx - secondCircleRadius + arcStrokeWidth, cy - secondCircleRadius + arcStrokeWidth, cx + secondCircleRadius - arcStrokeWidth, cy + secondCircleRadius - arcStrokeWidth);
    }

    public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy){
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    /**
     * 绘制刻度线（总共60条）
     * 从正上方，即12点处开始绘制一条直线，后面的只是旋转一下画布角度即可
     * @param canvas
     */
    private void drawClockScale(Canvas canvas,float radius) {
        //先保存一下当前画布的状态，因为后面画布会进行旋转操作，而在绘制完刻度后，需要恢复画布状态
        canvas.save();
        //计算12点处刻度的开始坐标
        float startX = getWidth() / 2;
        float startY = getHeight() / 2 - radius;//y坐标即园中心点的y坐标-半径
        //计算12点处的结束坐标
        float stopX = startX;
        int stopY1 = (int) (startY + Utils.dp2px(getResources(),6));//整点处的线长度为30
        //计算画布每次旋转的角度
        float degree = 360 / 12;
        for(int i = 0; i < 12; i++) {
            if (i == 5 || i == 6 || i == 7) {
                canvas.rotate(degree, getWidth() / 2, getHeight() / 2);//以圆中心进行旋转
                continue;
            }
            canvas.drawLine(startX, startY, stopX, stopY1, secondCirclePaint);//绘制整点长的刻度
            canvas.rotate(degree, getWidth() / 2, getHeight() / 2);//以圆中心进行旋转
        }
        //绘制完后，记得把画布状态复原
        canvas.restore();
    }


    private void drawLine(Canvas canvas,float radius) {
        //先保存一下当前画布的状态，因为后面画布会进行旋转操作，而在绘制完刻度后，需要恢复画布状态
        canvas.save();
        //计算12点处刻度的开始坐标
        float startX = getWidth() / 2;
        float startY = 0;//y坐标即园中心点的y坐标-半径

        float startX1 = startX;
        float startY1 = getHeight() / 2 - radius;

        //计算画布每次旋转的角度
        float degree = score / 100 * totalDegrees;

        canvas.rotate(degree + startOffsetDegrees + 90, getWidth() / 2, getHeight() / 2);//以圆中心进行旋转
        canvas.drawLine(startX, startY, startX1, startY1, scoreLinePaint);//绘制整点长的刻度
        canvas.restore();
    }

    public float getScore() {
        return score;
    }

    private void updateProgressPaint() {
            int[] colors = {Color.argb(40, 166, 251, 209), Color.rgb(166, 251, 209), Color.argb(0, 166, 251, 209)};
            float[] positions = {0f,score/100f*totalDegrees/360,1f};
            SweepGradient mRadialGradient = new SweepGradient(cx, cy, colors, positions);
            progressPaint.setShader(mRadialGradient);
    }

    public void setScore(float score) {
        this.score = score;
        updateProgressPaint();
        invalidate();
    }

}
