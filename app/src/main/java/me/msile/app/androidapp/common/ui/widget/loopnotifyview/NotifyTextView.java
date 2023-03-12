package me.msile.app.androidapp.common.ui.widget.loopnotifyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class NotifyTextView extends AppCompatTextView {

    private NotifyTextInfo textInfo;
    private NotifyTextConfig textConfig;
    private int titleTextWidth;

    public NotifyTextView(Context context) {
        super(context);
        init();
    }

    public NotifyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NotifyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setNotifyInfo(NotifyTextInfo notifyInfo, NotifyTextConfig textConfig) {
        if (notifyInfo == null || textConfig == null) {
            return;
        }
        this.textInfo = notifyInfo;
        this.textConfig = textConfig;
        //content
        int maxLines = textConfig.getMaxLines();
        if (maxLines > 1) {
            setMaxLines(maxLines);
            setLineSpacing(textConfig.getLineSpace(), 1.0f);
        } else {
            setSingleLine(true);
            setEllipsize(TextUtils.TruncateAt.END);
            setLineSpacing(0, 1.0f);
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textConfig.getContentTextSize());
        setTextColor(textConfig.getContentTextColor());
        setText(notifyInfo.getContent());
        //title
        TextPaint titleTextPaint = textConfig.getTitleTextPaint();
        String infoTitle = notifyInfo.getTitle();
        if (!TextUtils.isEmpty(infoTitle) && titleTextPaint != null) {
            titleTextWidth = (int) titleTextPaint.measureText(infoTitle);
            int paddingLeft = titleTextWidth + textConfig.getTitleBgPaddingLR() * 2 + textConfig.getTitleContentDistance();
            setPadding(paddingLeft, 0, 0, 0);
        } else {
            setPadding(0, 0, 0, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        if (width == 0 || height == 0) {
            return;
        }
        if (textInfo == null || textConfig == null) {
            return;
        }
        //draw title
        Paint titleBgPaint = textConfig.getTitleBgPaint();
        TextPaint titleTextPaint = textConfig.getTitleTextPaint();
        if (titleBgPaint == null || titleTextPaint == null) {
            return;
        }
        String title = textInfo.getTitle();
        if (!TextUtils.isEmpty(title)) {
            float leftOffset = titleBgPaint.getStrokeWidth();
            Paint.FontMetricsInt titleFontMetrics = titleTextPaint.getFontMetricsInt();
            int baseline = (height - titleFontMetrics.bottom - titleFontMetrics.top) / 2;
            //text
            canvas.drawText(title, leftOffset + textConfig.getTitleBgPaddingLR(), baseline, titleTextPaint);
            int bgWidth = titleTextWidth + textConfig.getTitleBgPaddingLR() * 2;
            int bgHeight = titleFontMetrics.bottom - titleFontMetrics.top + textConfig.getTitleBgPaddingTB() * 2;
            int top = (height - bgHeight) / 2;
            //bg
            canvas.drawRoundRect(leftOffset, top, bgWidth + leftOffset, top + bgHeight, textConfig.getTitleBgCornerSize(), textConfig.getTitleBgCornerSize(), titleBgPaint);
        }
    }
}
