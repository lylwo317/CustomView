package com.kevin.mycircleprogress.view;

import java.text.DecimalFormat;

import com.kevin.mycircleprogress.R;
import com.kevin.mycircleprogress.Utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by XieJiaHua on 2016/6/22.
 */
public class CompassDialView extends View
{

	private final Drawable background;
	private final DecimalFormat decimalFormat;
	private int direction_text_color = Color.parseColor("#20eefc");
	private int degree_text_color = Color.parseColor("#f9f9f9");

	private Paint gravityIndicatorPaint;
	private Paint degreeTextPaint;
	private float cx;

	private float cy;
	private ValueAnimator animator;

	private float currentRotate =0;
	private String currentDirection="";

	private TextPaint directionTextPaint;


	public CompassDialView(Context context)
	{
		this(context, null);
	}

	public CompassDialView(Context context, AttributeSet attrs)
	{
		this(context, attrs, R.attr.DriveScoreCircleProgressStyle);
	}

	public CompassDialView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		animator = ValueAnimator.ofFloat(this.currentRotate, this.currentRotate);
		animator.setDuration(800);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				CompassDialView.this.currentRotate = (float)animation.getAnimatedValue();

				if (0==currentRotate)
				{
					currentDirection = "N";
				}else if (0<currentRotate&& currentRotate<90)
				{
					currentDirection = "NE";
				}else if (90 == currentRotate)
				{
					currentDirection = "E";
				}else if (90 < currentRotate && currentRotate < 180)
				{
					currentDirection = "ES";
				}else if (currentRotate == 180)
				{
					currentDirection = "S";
				}else if (180 < currentRotate && currentRotate < 270)
				{
					currentDirection = "SW";
				}
				else if (270 == currentRotate)
				{
					currentDirection = "W";
				}else
				{
					currentDirection = "WN";
				}

				postInvalidate();
			}
		});

		decimalFormat=new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.

		background = getResources().getDrawable(R.drawable.compass_bg);

		initPaint();
	}

	private void initPaint()
	{

		directionTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		directionTextPaint.setColor(direction_text_color);
		directionTextPaint.setStyle(Paint.Style.STROKE);
		directionTextPaint.setTextAlign(Paint.Align.CENTER);
		directionTextPaint.setTextSize(Utils.dp2px(getResources(), 9f));

		degreeTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		degreeTextPaint.setColor(degree_text_color);
		degreeTextPaint.setStyle(Paint.Style.STROKE);
		degreeTextPaint.setTextAlign(Paint.Align.CENTER);
		degreeTextPaint.setTextSize(Utils.dp2px(getResources(), 13f));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.save();
		canvas.rotate(-currentRotate, cx, cy);
		if (background != null)
		{
			background.setBounds(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
			background.draw(canvas);
		}
		canvas.restore();


		canvas.drawText(currentDirection, getWidth() / 2, getHeight() - Utils.dp2px(getResources(), 47), directionTextPaint);
		canvas.drawText(decimalFormat.format(currentRotate)+"°",getWidth()/2,getHeight() - Utils.dp2px(getResources(),31),degreeTextPaint);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		cx = getWidth() / 2;
		cy = getHeight() / 2;
	}

	public void setRotate(float rotate)
	{
		animator.cancel();
		animator.setFloatValues(currentRotate, rotate);
		animator.start();
	}
}
