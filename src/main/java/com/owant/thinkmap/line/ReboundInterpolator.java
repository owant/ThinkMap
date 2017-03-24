package com.owant.thinkmap.line;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class ReboundInterpolator implements Interpolator {
    /**
     * Value at which the interpolation changes from decelerate to bounce
     */
    private static final float UNION = 0.2f;

    public ReboundInterpolator() {
    }

    public ReboundInterpolator(Context context, AttributeSet attrs) {
    }

    @Override
    public float getInterpolation(float input) {
        if (input < UNION) {
            // Decelerate interpolator
            float value = mapRange(input, 0f, UNION, 0f, 1f);
            return getDecelerateInterpolation(value);
        } else {
            // Bounce interpolator
            float value = mapRange(input, UNION, 1f, 0f, 1f);
            return 1f - getBounceInterpolation(value);
        }
    }

    /**
     * @param input Input value
     * @return Output value
     * @see {@link android.view.animation.DecelerateInterpolator}
     */
    private float getDecelerateInterpolation(float input) {
        return 1.0f - (1.0f - input) * (1.0f - input);
    }

    /**
     * @param t Input value
     * @return Output value
     * @see {@link android.view.animation.BounceInterpolator}
     */
    private float getBounceInterpolation(float t) {
        // Change for the interpolation ends in 1.0
        if (t == 1f) return 1f;
        // _b(t) = t * t * 8
        // bs(t) = _b(t) for t < 0.3535
        // bs(t) = _b(t - 0.54719) + 0.7 for t < 0.7408
        // bs(t) = _b(t - 0.8526) + 0.9 for t < 0.9644
        // bs(t) = _b(t - 1.0435) + 0.95 for t <= 1.0
        // b(t) = bs(t * 1.1226)
        t *= 1.1226f;
        if (t < 0.3535f) return bounce(t);
        else if (t < 0.7408f) return bounce(t - 0.54719f) + 0.7f;
        else if (t < 0.9644f) return bounce(t - 0.8526f) + 0.9f;
        else return bounce(t - 1.0435f) + 0.95f;
    }

    private static float bounce(float t) {
        return t * t * 8.0f;
    }

    /**
     * Maps value which is in the range fromLow and fromHigh to it's corresponding value in the range toLow, toHigh
     *
     * @param value    Input value to convert
     * @param fromLow  Lowest value in the range of input value
     * @param fromHigh Highest value in the range of input value
     * @param toLow    Lowest value in the range of output value
     * @param toHigh   Highest value in the range of output value
     * @return Output value converted to the range
     * @see <a href="http://gist.github.com/Tylos/30e157bf42d655419164">Tylos / Map Range</a>
     */
    private static float mapRange(float value, float fromLow, float fromHigh, float toLow, float toHigh) {
        return toLow + (((value - fromLow) / (fromHigh - fromLow)) * (toHigh - toLow));
    }
}
