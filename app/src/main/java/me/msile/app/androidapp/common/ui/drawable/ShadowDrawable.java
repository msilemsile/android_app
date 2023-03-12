package me.msile.app.androidapp.common.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * 阴影drawable(copy {RoundRectDrawableWithShadow.java})
 */
public class ShadowDrawable extends Drawable {

    private int mSoldColor;

    private Paint mPaint;

    private Paint mCornerShadowPaint;

    private Paint mEdgeShadowPaint;

    private RectF mCardBounds;

    private int mCornerRadius;

    private Path mCornerShadowPath;

    private int mShadowElevation;

    private int mShadowStartColor;

    private int mShadowEndColor;

    private boolean mDirty = true;

    public ShadowDrawable(int radius, int shadowColor, int shadowElevation) {
        this(radius, shadowColor, 0, shadowElevation, 0);
    }

    public ShadowDrawable(int radius, int shadowStartColor, int shadowEndColor, int shadowElevation, int soldColor) {
        mCornerRadius = radius;
        mShadowStartColor = shadowStartColor;
        if (shadowEndColor == 0) {
            int red = Color.red(shadowStartColor);
            int green = Color.green(shadowStartColor);
            int blue = Color.blue(shadowStartColor);
            mShadowEndColor = Color.argb(0, red, green, blue);
        } else {
            mShadowEndColor = shadowEndColor;
        }
        mShadowElevation = shadowElevation;
        mCornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mCornerShadowPaint.setStyle(Paint.Style.FILL);
        mCardBounds = new RectF();
        mEdgeShadowPaint = new Paint(mCornerShadowPaint);
        mEdgeShadowPaint.setAntiAlias(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(soldColor);
        mSoldColor = soldColor;
    }

    private void drawShadow(Canvas canvas) {
        if (mCornerShadowPath == null) {
            mDirty = true;
            return;
        }
        final float edgeShadowTop = -mCornerRadius - mShadowElevation;
        final float inset = mCornerRadius;
        // LT
        int saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        canvas.drawRect(0, edgeShadowTop, mCardBounds.width() - 2 * inset, -mCornerRadius,
                mEdgeShadowPaint);
        canvas.restoreToCount(saved);
        // RB
        saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.bottom - inset);
        canvas.rotate(180f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        canvas.drawRect(0, edgeShadowTop, mCardBounds.width() - 2 * inset, -mCornerRadius,
                mEdgeShadowPaint);
        canvas.restoreToCount(saved);
        // LB
        saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - inset);
        canvas.rotate(270f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        canvas.drawRect(0, edgeShadowTop, mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
        canvas.restoreToCount(saved);
        // RT
        saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.top + inset);
        canvas.rotate(90f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        canvas.drawRect(0, edgeShadowTop, mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
        canvas.restoreToCount(saved);
    }

    private void buildShadowCorners() {
        RectF innerBounds = new RectF(-mCornerRadius, -mCornerRadius, mCornerRadius, mCornerRadius);
        RectF outerBounds = new RectF(innerBounds);
        outerBounds.inset(-mShadowElevation, -mShadowElevation);

        if (mCornerShadowPath == null) {
            mCornerShadowPath = new Path();
        } else {
            mCornerShadowPath.reset();
        }
        mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        mCornerShadowPath.moveTo(-mCornerRadius, 0);
        mCornerShadowPath.rLineTo(-mShadowElevation, 0);
        // outer arc
        mCornerShadowPath.arcTo(outerBounds, 180f, 90f, false);
        // inner arc
        mCornerShadowPath.arcTo(innerBounds, 270f, -90f, false);
        mCornerShadowPath.close();
        float startRatio = mCornerRadius * 1.0f / (mCornerRadius + mShadowElevation);
        mCornerShadowPaint.setShader(new RadialGradient(0, 0, mCornerRadius + mShadowElevation,
                new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
                new float[]{0f, startRatio, 1f},
                android.graphics.Shader.TileMode.CLAMP));

        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.setShader(new LinearGradient(0, -mCornerRadius + mShadowElevation, 0,
                -mCornerRadius - mShadowElevation,
                new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
                new float[]{0f, .5f, 1f}, android.graphics.Shader.TileMode.CLAMP));
        mEdgeShadowPaint.setAntiAlias(false);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDirty) {
            buildShadowCorners();
            mDirty = false;
        }
        drawShadow(canvas);
        if (mSoldColor != 0) {
            canvas.drawRoundRect(mCardBounds, mCornerRadius, mCornerRadius, mPaint);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mCardBounds.set(mShadowElevation, mShadowElevation, bounds.right - mShadowElevation, bounds.bottom - mShadowElevation);
        super.onBoundsChange(bounds);
        mDirty = true;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mCornerShadowPaint.setAlpha(alpha);
        mEdgeShadowPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        mCornerShadowPaint.setColorFilter(colorFilter);
        mEdgeShadowPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

