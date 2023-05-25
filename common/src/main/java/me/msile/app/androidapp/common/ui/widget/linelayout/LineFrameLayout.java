package me.msile.app.androidapp.common.ui.widget.linelayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LineFrameLayout extends FrameLayout {

    private LineLayoutHelper lineLayoutHelper;

    public LineFrameLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public LineFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LineFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        lineLayoutHelper = new LineLayoutHelper();
        lineLayoutHelper.readLineAttr(context, attributeSet,this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        lineLayoutHelper.draw(canvas, this);
        super.onDraw(canvas);
    }

    public void setLineColor(int mLineColor) {
        lineLayoutHelper.setLineColor(mLineColor);
        postInvalidate();
    }

    public void setLineWidth(int mLineWidth) {
        lineLayoutHelper.setLineWidth(mLineWidth);
        postInvalidate();
    }

    public void setLineLeftMargin(int mLineLeftMargin) {
        lineLayoutHelper.setLineLeftMargin(mLineLeftMargin);
        postInvalidate();
    }

    public void setLineTopMargin(int mLineTopMargin) {
        lineLayoutHelper.setLineTopMargin(mLineTopMargin);
        postInvalidate();
    }

    public void setLineRightMargin(int mLineRightMargin) {
        lineLayoutHelper.setLineRightMargin(mLineRightMargin);
        postInvalidate();
    }

    public void setLineBottomMargin(int mLineBottomMargin) {
        lineLayoutHelper.setLineBottomMargin(mLineBottomMargin);
        postInvalidate();
    }

    public void setLineLeft(boolean mLineLeft) {
        lineLayoutHelper.setLineLeft(mLineLeft);
        postInvalidate();
    }

    public void setLineTop(boolean mLineTop) {
        lineLayoutHelper.setLineTop(mLineTop);
        postInvalidate();
    }

    public void setLineRight(boolean mLineRight) {
        lineLayoutHelper.setLineRight(mLineRight);
        postInvalidate();
    }

    public void setLineBottom(boolean mLineBottom) {
        lineLayoutHelper.setLineBottom(mLineBottom);
        postInvalidate();
    }

    public void setLineFixLength(int mLineFixLength) {
        lineLayoutHelper.setLineFixLength(mLineFixLength);
        postInvalidate();
    }

    public void setLineDrawable(Drawable mLineDrawable) {
        lineLayoutHelper.setLineDrawable(mLineDrawable);
        postInvalidate();
    }

}
