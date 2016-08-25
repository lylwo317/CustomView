package com.kevin.mycircleprogress.view;

import java.text.DecimalFormat;

import com.kevin.mycircleprogress.GradientUtil;
import com.kevin.mycircleprogress.R;
import com.kevin.mycircleprogress.Utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by XieJiaHua on 2016/6/22.
 */
public class LandscapeDriveSpeedDialView extends View
{

	private final DecimalFormat decimalFormat;
	private final Drawable background;
	private final int backgroundPadding;
	private int default_score_text_color = Color.parseColor("#f9f9f9");
	private int default_description_text_color = Color.BLACK;
	private int progress_color = Color.parseColor("#20eefc");
	private int speed_line_color = Color.parseColor("#ff0000");

	private String description_text;
	private float default_score = 30;

	private float speed_text_size;
	private float description_text_size;

	private int first_circle_color;
	private int scale_color;
	private int current_speed_line_color;
	private int speed_text_color;
	private int description_text_color;

	private RectF outRect = new RectF();
	private RectF progressOutArcRect = new RectF();
	private RectF progressInnerArcRect = new RectF();
	private Rect textBounds = new Rect();

	private Paint descriptionTextPaint;
	private Paint speedTextPaint;
	private Paint firstCirclePaint;
	private Paint scaleCirclePaint;
	private Paint scaleCircleBluePaint;
	private Paint currentSpeedLinePaint;

	private TextPaint unitTextPaint;
	private TextPaint timeTextPaint;
	private Paint outProgressPaint;
	private Paint innerProgressPaint;
	private Paint speedLinePaint;
	private float outCircleRadius;
	private float scaleCircleRadius;
	private float innerProgressCircleRadius;
	private float currentSpeedLineRadius;

	private float currentSpeedLineLength;

	private float cx;
	private float cy;

	// 得分
	private float currentSpeed =90;
	private float descriptionMarginBottom;
	private float scoreTextMarginBottom;
	private float totalDegrees;

	private float startOffsetDegrees;
	private ValueAnimator animator;
	private String unit_text;
	private float unit_text_size;
	private int unit_text_color;
	private int time_text_color;
	private float time_text_size;
	private float unit_text_MarginBottom;
	private float time_text_MarginBottom;

	private String driveDuration="00:00:00";


	public LandscapeDriveSpeedDialView(Context context)
	{
		this(context, null);
	}

	public LandscapeDriveSpeedDialView(Context context, AttributeSet attrs)
	{
		this(context, attrs, R.attr.DriveScoreCircleProgressStyle);
	}

	public LandscapeDriveSpeedDialView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		animator = ValueAnimator.ofFloat(this.currentSpeed, this.currentSpeed);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				LandscapeDriveSpeedDialView.this.currentSpeed = (float) animation.getAnimatedValue();
				postInvalidate();
			}
		});
		outRect = new RectF();

		totalDegrees = 16 * 360f / 24;
		startOffsetDegrees = 10f * 360f / 24;

		decimalFormat = new DecimalFormat("00");

		background = getResources().getDrawable(R.drawable.bg_speed_landscape);

		backgroundPadding = (int)Utils.dp2px(getResources(), 4f);

		TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DriveScoreCircleProgress,
				defStyleAttr, R.style.DriveScoreCircleProgress);
		initAttribute(attributes);
		attributes.recycle();
		initPaint();
	}

	private void initAttribute(TypedArray attributes)
	{

		speed_text_color = default_score_text_color;
		speed_text_size = Utils.dp2px(getResources(),39f);

		description_text = "即时速度";
		description_text_size = Utils.dp2px(getResources(), 12f);
		description_text_color = Color.parseColor("#808080");
		descriptionMarginBottom = Utils.dp2px(getResources(), 65f);

		unit_text = "km/h";
		unit_text_size = Utils.dp2px(getResources(), 12f);
		unit_text_color = Color.parseColor("#f9f9f9");
		unit_text_MarginBottom = Utils.dp2px(getResources(), 26f);


		time_text_size = Utils.dp2px(getResources(), 16f);
		time_text_color = Color.parseColor("#20eefc");
		time_text_MarginBottom = Utils.dp2px(getResources(), 40f);

		int padding = (int)Utils.dp2px(getResources(), 3f);

		setPadding(padding,padding,padding,padding);

		scale_color = Color.rgb(80, 80, 80);
	}

	private void initPaint()
	{
		descriptionTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		descriptionTextPaint.setColor(description_text_color);
		descriptionTextPaint.setTextSize(description_text_size);
		descriptionTextPaint.setTextAlign(Paint.Align.CENTER);

		unitTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		unitTextPaint.setColor(unit_text_color);
		unitTextPaint.setTextSize(unit_text_size);
		unitTextPaint.setTextAlign(Paint.Align.CENTER);


		timeTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		timeTextPaint.setColor(time_text_color);
		timeTextPaint.setTextSize(time_text_size);
		timeTextPaint.setTextAlign(Paint.Align.CENTER);

		speedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		speedTextPaint.setColor(speed_text_color);
		speedTextPaint.setTextSize(speed_text_size);
		//speedTextPaint.setTextAlign(Paint.Align.CENTER);

		firstCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		firstCirclePaint.setStyle(Paint.Style.FILL);
		firstCirclePaint.setStrokeWidth(Utils.dp2px(getResources(), 0.3f));

		scaleCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		scaleCirclePaint.setColor(scale_color);
		scaleCirclePaint.setStyle(Paint.Style.STROKE);
		scaleCirclePaint.setStrokeWidth(Utils.dp2px(getResources(), 1.3f));

		scaleCircleBluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		scaleCircleBluePaint.setColor(progress_color);
		scaleCircleBluePaint.setStyle(Paint.Style.STROKE);
		scaleCircleBluePaint.setStrokeWidth(Utils.dp2px(getResources(), 1.3f));

		currentSpeedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		currentSpeedLinePaint.setColor(current_speed_line_color);
		currentSpeedLinePaint.setStyle(Paint.Style.STROKE);
		currentSpeedLinePaint.setStrokeWidth(Utils.dp2px(getResources(), 0.7f));

		outProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		outProgressPaint.setColor(progress_color);
		outProgressPaint.setStyle(Paint.Style.STROKE);
		outProgressPaint.setStrokeWidth(Utils.dp2px(getResources(), 2));

		innerProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		innerProgressPaint.setColor(Color.GREEN);
		innerProgressPaint.setStyle(Paint.Style.FILL);

		speedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		speedLinePaint.setStrokeWidth(Utils.dp2px(getResources(), 2f));
		speedLinePaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		outRect.set(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		background.setBounds(0, 0, getWidth(), getHeight());
		background.draw(canvas);
		drawClockScale(canvas, scaleCircleRadius);

		canvas.save();
		canvas.rotate(startOffsetDegrees, cx, cy);
		canvas.drawArc(progressOutArcRect, 0, getProgressAngel(), false, outProgressPaint);
		canvas.drawArc(progressInnerArcRect, 0, getProgressAngel(), true, innerProgressPaint);
		canvas.restore();

        //canvas.drawText(String.valueOf((int)currentSpeed), cx, getHeight() / 2, speedTextPaint);

		drawTextCentred(canvas, speedTextPaint, String.valueOf((int)currentSpeed), cx, cy);
		canvas.drawText(unit_text, cx, getHeight() - unit_text_MarginBottom, unitTextPaint);

		drawLine(canvas, currentSpeedLineRadius);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		outCircleRadius = (Math.min(getWidth(), getHeight())) / 2f;
		cx = outRect.width() / 2;
		cy = outRect.height() / 2;

		scaleCircleRadius = outCircleRadius - Utils.dp2px(getResources(), 3.5f)-getPaddingLeft();

		currentSpeedLineLength = Utils.dp2px(getResources(), 13) + getPaddingLeft();
		currentSpeedLineRadius = outCircleRadius- currentSpeedLineLength;

		float startX = getWidth() /2f;
		float startY = 0;// y坐标即园中心点的y坐标-半径

		float stopX1 = startX;
		float stopY1 = getHeight() / 2f - currentSpeedLineRadius;

		speedLinePaint.setShader(new LinearGradient(stopX1, stopY1, startX, startY, new int[]{Color.parseColor("#00ff0000"),Color.parseColor("#ffff0000"),Color.parseColor("#ffff0000")}, new float[]{0,0.7f,1}, Shader.TileMode.CLAMP));


		int outArcStrokeWidth = (int) (outProgressPaint.getStrokeWidth() / 2f);
		int innerArcStrokeWidth = (int) (innerProgressPaint.getStrokeWidth() / 2f);

		progressOutArcRect.set(outArcStrokeWidth + getPaddingLeft(), outArcStrokeWidth + getPaddingLeft(), outCircleRadius * 2 - outArcStrokeWidth - getPaddingLeft(), outCircleRadius * 2 - outArcStrokeWidth - getPaddingLeft());

		progressInnerArcRect.set(progressOutArcRect.left + outArcStrokeWidth + innerArcStrokeWidth, progressOutArcRect.top + outArcStrokeWidth + innerArcStrokeWidth, progressOutArcRect.right - innerArcStrokeWidth - outArcStrokeWidth, progressOutArcRect.bottom - innerArcStrokeWidth - outArcStrokeWidth);

		innerProgressCircleRadius = progressInnerArcRect.width() / 2;

		updateProgressPaint();
	}

	public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy)
	{
		paint.getTextBounds(text, 0, text.length(), textBounds);
		canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
	}

	/**
	 * 绘制刻度线（总共24条） 从正上方，即12点处开始绘制一条直线，后面的只是旋转一下画布角度即可
	 * 
	 * @param canvas
	 */
	private void drawClockScale(Canvas canvas, float radius)
	{
		// 先保存一下当前画布的状态，因为后面画布会进行旋转操作，而在绘制完刻度后，需要恢复画布状态
		canvas.save();
		// 计算12点处刻度的开始坐标
		float startX = getWidth() / 2f;
		float startY = getHeight() / 2f - radius;// y坐标即园中心点的y坐标-半径
		canvas.rotate(startOffsetDegrees + 90, cx, cy);
		// 计算12点处的结束坐标
		float stopX = startX;
		int stopY1 = (int) (startY + Utils.dp2px(getResources(), 2.3f));// 整点处的线长度为30
		// 计算画布每次旋转的角度
		float degree = 360f / 24;
		for (int i = 0; i < 24; i++)
		{
			if (i > 16)
			{
				canvas.rotate(degree, getWidth() / 2f, getHeight() / 2f);// 以圆中心进行旋转
				continue;
			}

			if (degree * i <= getProgressAngel())
			{
				canvas.drawLine(startX, startY, stopX, stopY1, scaleCircleBluePaint);// 绘制整点长的刻度
			}
			else
			{
				canvas.drawLine(startX, startY, stopX, stopY1, scaleCirclePaint);// 绘制整点长的刻度
			}
			canvas.rotate(degree, getWidth() / 2f, getHeight() / 2f);// 以圆中心进行旋转
		}
		// 绘制完后，记得把画布状态复原
		canvas.restore();
	}

	private void drawLine(Canvas canvas, float radius)
	{
		// 先保存一下当前画布的状态，因为后面画布会进行旋转操作，而在绘制完刻度后，需要恢复画布状态
		canvas.save();
		// 计算12点处刻度的开始坐标
		float startX = getWidth() /2f;
		float startY = 0;// y坐标即园中心点的y坐标-半径

		float stopX1 = startX;
		float stopY1 = getHeight() / 2f - radius;

		// 计算画布每次旋转的角度
		float degree = getProgressAngel();

		canvas.rotate(degree + startOffsetDegrees + 90, getWidth() / 2f, getHeight() / 2f);// 以圆中心进行旋转
		canvas.drawLine(startX, startY, stopX1, stopY1, speedLinePaint);// 绘制整点长的刻度
		canvas.restore();
	}

	public float getCurrentSpeed()
	{
		return currentSpeed;
	}

	private void updateProgressPaint()
	{
		int[] stopColors = GradientUtil.makeCubicGradientStopColors(Color.argb(127, 32, 238, 252), 15);
		float[] stopColorsPositions = GradientUtil.makeStopColorsPositions(0.7f, 15);
		RadialGradient mRadialGradient = new RadialGradient(cx, cy, innerProgressCircleRadius, stopColors,
				stopColorsPositions, Shader.TileMode.CLAMP);
		innerProgressPaint.setShader(mRadialGradient);
	}

	private float getProgressAngel()
	{
		return currentSpeed / 240 * totalDegrees;
	}

	public void setSpeed(float speed)
	{
		if (animator != null)
		{
			animator.cancel();
			animator.setFloatValues(this.currentSpeed, speed);
			animator.setDuration(800);
			animator.start();
		}
	}

	public void setCurrentTime(long millisecond)
	{
		long HH = millisecond / (60*60*1000);
		long mm = (millisecond % (60*60*1000))/(60*1000);
		long ss = (millisecond % (60 * 60 * 1000)) % (60 * 1000) / 1000;

		driveDuration = decimalFormat.format(HH) + ":" + decimalFormat.format(mm) + ":" + decimalFormat.format(ss);

		invalidate();
	}

}
