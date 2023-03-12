package me.msile.app.androidapp.common.ui.widget.shadowlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 带阴影frameLayout
 */
public class ShadowTextView extends AppCompatTextView {

    private ShadowLayoutHelper layoutHelper;

    public ShadowTextView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ShadowTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShadowTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        layoutHelper = new ShadowLayoutHelper();
        layoutHelper.init(context, attributeSet, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        layoutHelper.draw(canvas, this);
        super.onDraw(canvas);
    }

}
