package com.owant.thinkmap.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

/**
 * Created by owant on 01/04/2017.
 */

public class GravityCompoundDrawable extends Drawable {

    // inner Drawable
    private final Drawable mDrawable;

    public GravityCompoundDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    @Override
    public int getIntrinsicWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mDrawable.getIntrinsicHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        int halfCanvas= canvas.getHeight() / 2;
        int halfDrawable = mDrawable.getIntrinsicHeight() / 2;

        // align to top
        canvas.save();
        canvas.translate(0, -halfCanvas + halfDrawable);
        mDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
