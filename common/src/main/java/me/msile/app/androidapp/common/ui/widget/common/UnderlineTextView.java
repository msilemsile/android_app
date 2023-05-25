package me.msile.app.androidapp.common.ui.widget.common;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class UnderlineTextView extends AppCompatTextView {

    public UnderlineTextView(Context context) {
        super(context);
        init(context);
    }

    public UnderlineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UnderlineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        TextPaint paint = getPaint();
        if (paint != null) {
            paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }

}
