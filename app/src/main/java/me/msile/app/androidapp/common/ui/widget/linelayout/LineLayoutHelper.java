package me.msile.app.androidapp.common.ui.widget.linelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import me.msile.app.androidapp.R;

/**
 * 布局划线帮助类
 */
public class LineLayoutHelper {

    private Drawable mLineDrawable;
    private Rect targetRect = new Rect();
    private Paint mPaint;
    private int mLineFixLength;
    private int mLineColor;
    private int mLineWidth;
    private int mLineLeftMargin;
    private int mLineTopMargin;
    private int mLineRightMargin;
    private int mLineBottomMargin;
    private boolean mLineLeft;
    private boolean mLineTop;
    private boolean mLineRight;
    private boolean mLineBottom;

    public LineLayoutHelper() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void readLineAttr(Context context, AttributeSet attributeSet, View view) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.LineLayoutHelper);
        mLineDrawable = a.getDrawable(R.styleable.LineLayoutHelper_line_drawable);
        mLineFixLength = a.getDimensionPixelSize(R.styleable.LineLayoutHelper_line_fix_length, 0);
        mLineColor = a.getColor(R.styleable.LineLayoutHelper_line_color, 0);
        mLineWidth = a.getDimensionPixelSize(R.styleable.LineLayoutHelper_line_width, 0);
        mLineLeftMargin = a.getDimensionPixelSize(R.styleable.LineLayoutHelper_line_left_margin, 0);
        mLineTopMargin = a.getDimensionPixelSize(R.styleable.LineLayoutHelper_line_top_margin, 0);
        mLineRightMargin = a.getDimensionPixelSize(R.styleable.LineLayoutHelper_line_right_margin, 0);
        mLineBottomMargin = a.getDimensionPixelSize(R.styleable.LineLayoutHelper_line_bottom_margin, 0);
        mLineLeft = a.getBoolean(R.styleable.LineLayoutHelper_line_left, false);
        mLineTop = a.getBoolean(R.styleable.LineLayoutHelper_line_top, false);
        mLineRight = a.getBoolean(R.styleable.LineLayoutHelper_line_right, false);
        mLineBottom = a.getBoolean(R.styleable.LineLayoutHelper_line_bottom, false);
        a.recycle();
        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.FILL);
        view.setWillNotDraw(false);
    }

    public void draw(Canvas canvas, View view) {
        if (mLineWidth <= 0 || (mLineColor == 0 && mLineDrawable == null)) {
            return;
        }
        if (canvas == null || view == null) {
            return;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        boolean needDrawLine = false;
        if (mLineLeft) {
            needDrawLine = true;
            if (mLineLeftMargin > 0) {
                targetRect.set(0, mLineLeftMargin, mLineWidth, height - mLineLeftMargin);
            } else {
                if (mLineFixLength > 0) {
                    int startTop = (height - mLineFixLength) / 2;
                    targetRect.set(0, startTop, mLineWidth, startTop + mLineFixLength);
                } else {
                    targetRect.set(0, 0, mLineWidth, height);
                }
            }
        }
        if (mLineTop) {
            needDrawLine = true;
            if (mLineTopMargin > 0) {
                targetRect.set(mLineTopMargin, 0, width - mLineTopMargin, mLineWidth);
            } else {
                if (mLineFixLength > 0) {
                    int startLeft = (width - mLineFixLength) / 2;
                    targetRect.set(startLeft, 0, startLeft + mLineFixLength, mLineWidth);
                } else {
                    targetRect.set(0, 0, width, mLineWidth);
                }
            }
        }
        if (mLineRight) {
            needDrawLine = true;
            if (mLineRightMargin > 0) {
                targetRect.set(width - mLineWidth, mLineRightMargin, width, height - mLineRightMargin);
            } else {
                if (mLineFixLength > 0) {
                    int startTop = (height - mLineFixLength) / 2;
                    targetRect.set(width - mLineWidth, startTop, width, startTop + mLineFixLength);
                } else {
                    targetRect.set(width - mLineWidth, 0, width, height);
                }
            }
        }
        if (mLineBottom) {
            needDrawLine = true;
            if (mLineBottomMargin > 0) {
                targetRect.set(mLineBottomMargin, height - mLineWidth, width - mLineBottomMargin, height);
            } else {
                if (mLineFixLength > 0) {
                    int startLeft = (width - mLineFixLength) / 2;
                    targetRect.set(startLeft, height - mLineWidth, startLeft + mLineFixLength, height);
                } else {
                    targetRect.set(0, height - mLineWidth, width, height);
                }
            }
        }
        if (needDrawLine) {
            if (mLineDrawable != null) {
                mLineDrawable.setBounds(targetRect);
                mLineDrawable.draw(canvas);
            } else {
                canvas.drawRect(targetRect, mPaint);
            }
        }
    }

    public void setLineColor(int mLineColor) {
        this.mLineColor = mLineColor;
        mPaint.setColor(mLineColor);
    }

    public void setLineWidth(int mLineWidth) {
        this.mLineWidth = mLineWidth;
    }

    public void setLineLeftMargin(int mLineLeftMargin) {
        this.mLineLeftMargin = mLineLeftMargin;
    }

    public void setLineTopMargin(int mLineTopMargin) {
        this.mLineTopMargin = mLineTopMargin;
    }

    public void setLineRightMargin(int mLineRightMargin) {
        this.mLineRightMargin = mLineRightMargin;
    }

    public void setLineBottomMargin(int mLineBottomMargin) {
        this.mLineBottomMargin = mLineBottomMargin;
    }

    public void setLineLeft(boolean mLineLeft) {
        this.mLineLeft = mLineLeft;
    }

    public void setLineTop(boolean mLineTop) {
        this.mLineTop = mLineTop;
    }

    public void setLineRight(boolean mLineRight) {
        this.mLineRight = mLineRight;
    }

    public void setLineBottom(boolean mLineBottom) {
        this.mLineBottom = mLineBottom;
    }

    public void setLineFixLength(int mLineFixLength) {
        this.mLineFixLength = mLineFixLength;
    }

    public void setLineDrawable(Drawable mLineDrawable) {
        this.mLineDrawable = mLineDrawable;
    }
}
