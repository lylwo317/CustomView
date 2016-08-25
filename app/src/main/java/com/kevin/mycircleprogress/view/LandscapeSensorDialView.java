package com.kevin.mycircleprogress.view;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.kevin.mycircleprogress.R;
import com.kevin.mycircleprogress.Utils;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by XieJiaHua on 2016/6/22.
 */
public class LandscapeSensorDialView extends View
{

	private final Drawable background;
	private final ValueAnimator animatorY;
	private final AnimatorSet animSet;
	private final ValueAnimator animatorG;
	private final DecimalFormat decimalFormat;
	private int gravity_indicator_color = Color.parseColor("#20eefc");

	private Paint gravityIndicatorPaint;
	private Paint gravityTextPaint;
	private float cx;

	private float cy;
	private ValueAnimator animatorX;

	private float maxValue = 1000;
	private float radiusMaxValue = 1000;
	private float gravityIndicatorRadius;
	private float newXValue = -1000;
	private float newYValue = -1000;
	private float currentXValue = -1000;
	private float currentYValue = -1000;
	private float currentGValue = 1000;
	private float newGValue = 1000;
	private float circleRadius;

	private float gravityCircleHigh;
	private float gravityTextHigh;

	Timer timer = new Timer();
	TimerTask task = new TimerTask()
	{

		@Override
		public void run()
		{
			handler.post(new Runnable()
			{
				@Override
				public void run()
				{
					setCurrentPosition(0, 0, 0);
				}
			});
		}
	};

	Handler handler = new Handler();/*.postDelayed(new Runnable()
	{
		@Override
		public void run()
		{

		}
	}, 1000);*/

	private int overTime = 1000;

	public LandscapeSensorDialView(Context context)
	{
		this(context, null);
	}

	public LandscapeSensorDialView(Context context, AttributeSet attrs)
	{
		this(context, attrs, R.attr.DriveScoreCircleProgressStyle);
	}

	public LandscapeSensorDialView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		animatorX = ValueAnimator.ofFloat(this.currentXValue, this.newXValue);
		animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				timer.cancel();
				timer.purge();
				timer.schedule(task, 10000);
				LandscapeSensorDialView.this.currentXValue = (float) animation.getAnimatedValue();
				postInvalidate();
			}
		});

		animatorY = ValueAnimator.ofFloat(this.currentYValue, this.newYValue);
		animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				LandscapeSensorDialView.this.currentYValue = (float) animation.getAnimatedValue();
			}
		});

		animatorG = ValueAnimator.ofFloat(this.currentGValue, this.newGValue);
		animatorG.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				LandscapeSensorDialView.this.currentGValue = (float) animation.getAnimatedValue();
			}
		});

		animSet = new AnimatorSet();
		animSet.setDuration(800);
		animSet.play(animatorX).with(animatorY).with(animatorG);

		radiusMaxValue = (float) Math.sqrt(Math.pow(maxValue, 2) * 2);

		decimalFormat = new DecimalFormat("0.0");// 构造方法的字符格式这里如果小数不足2位,会以0补足.

		gravityCircleHigh = Utils.dp2px(getResources(), 68f);
		gravityTextHigh = Utils.dp2px(getResources(), 11f);

		background = getResources().getDrawable(R.drawable.bg_sensor_landscape);


		initPaint();
	}

	private void initPaint()
	{
		gravityIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		gravityIndicatorPaint.setColor(gravity_indicator_color);
		gravityIndicatorPaint.setStyle(Paint.Style.FILL);

		gravityTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		gravityTextPaint.setColor(gravity_indicator_color);
		gravityTextPaint.setStyle(Paint.Style.STROKE);
		gravityTextPaint.setTextAlign(Paint.Align.CENTER);
		gravityTextPaint.setTextSize(Utils.dp2px(getResources(), 11f));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) gravityCircleHigh, MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (gravityCircleHigh + gravityTextHigh),
				MeasureSpec.EXACTLY);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		if (background != null)
		{
			background.setBounds(getPaddingLeft(), (int) (getPaddingTop() + gravityTextHigh),
					getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
			background.draw(canvas);
		}

		canvas.drawText(decimalFormat.format(currentGValue) + "G", getWidth() / 2,
				gravityTextHigh - Utils.dp2px(getResources(), 1f), gravityTextPaint);

		canvas.drawCircle(getCurrentCx(), getCurrentCy(), gravityIndicatorRadius, gravityIndicatorPaint);
	}

	private float getCurrentCx()
	{
		return cx - currentXValue / radiusMaxValue * circleRadius;
	}

	private float getCurrentCy()
	{
		return cy + currentYValue / radiusMaxValue * circleRadius;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		gravityIndicatorRadius = Utils.dp2px(getResources(), 4);
		circleRadius = getWidth() / 2f - getPaddingLeft();
		cx = getWidth() / 2;
		cy = getWidth() / 2 + gravityTextHigh;
	}

	public void setCurrentPosition(float xValue, float yValue, float gValue)
	{
		animSet.cancel();
		this.newXValue = xValue;
		this.newYValue = yValue;
		this.newGValue = gValue;
		animatorX.setFloatValues(currentXValue, newXValue);
		animatorY.setFloatValues(currentYValue, newYValue);
		animatorG.setFloatValues(currentGValue, newGValue);
		animSet.start();
	}

}
