package com.kevin.mycircleprogress;

import android.graphics.Color;
import android.util.LruCache;

/**
 * @author XieJiaHua create on 2016/8/23.(lylwo317@gmail.com)
 */
public class GradientUtil
{
	private static final LruCache<Integer, int[]> cubicGradientColors = new LruCache<>(10);
	private static final LruCache<Integer, float[]> cubicGradientPositions = new LruCache<>(10);

	private GradientUtil()
	{
	}

	public static float[] makeStopColorsPositions(float baseColorPosition, int numStops)
	{
		int cacheKeyHash = (int)(baseColorPosition*100);
		cacheKeyHash = 31 * cacheKeyHash + numStops;

		float[] posAndColors1 = cubicGradientPositions.get(cacheKeyHash);
		if (posAndColors1 != null)
		{
			return posAndColors1;
		}
		numStops = Math.max(numStops, 3);

		final float[] positions = new float[numStops];

		for (int i = 0; i < numStops; i++)
		{

			if (i == 0)
			{
				positions[i] = 0;
			}else if (i == 1)
			{
				positions[i] = baseColorPosition;
			}else if (i == numStops - 1)
			{
				positions[i] = 1;
			}else
			{
				positions[i] = (1 - baseColorPosition)*(i-1) /(numStops-2) + baseColorPosition;
			}
		}

		cubicGradientPositions.put(cacheKeyHash, positions);

		return positions;
	}

	public static int[] makeCubicGradientStopColors(int baseColor, int numStops)
	{

		int cacheKeyHash = baseColor;
		cacheKeyHash = 31 * cacheKeyHash + numStops;

		int[] stopColorsCache = cubicGradientColors.get(cacheKeyHash);
		if (stopColorsCache != null)
		{
			return stopColorsCache;
		}
		numStops = Math.max(numStops, 3);

		final int[] stopColors = new int[numStops];

		int red = Color.red(baseColor);
		int green = Color.green(baseColor);
		int blue = Color.blue(baseColor);
		int alpha = Color.alpha(baseColor);

		for (int i = 0; i < numStops; i++)
		{

			float x = i * 1f / (numStops - 1);
			float opacity = MathUtil.constrain(0, 1, (float) Math.pow(x, 3));

			stopColors[i] = Color.argb((int) (alpha * opacity), red, green, blue);
		}


		cubicGradientColors.put(cacheKeyHash, stopColors);

        return stopColors;
	}
}
