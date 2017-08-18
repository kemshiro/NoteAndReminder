package com.k3mshiro.knotes.customview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by k3mshiro on 8/18/17.
 */

public class SquareButton extends android.support.v7.widget.AppCompatButton {

    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
