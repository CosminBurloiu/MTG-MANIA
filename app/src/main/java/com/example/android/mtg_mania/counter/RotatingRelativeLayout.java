package com.example.android.mtg_mania.counter;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class RotatingRelativeLayout extends RelativeLayout {
    public RotatingRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RotatingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotatingRelativeLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        setStaticTransformationsEnabled(true);
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        t.setTransformationType(Transformation.TYPE_MATRIX);
        Matrix m = t.getMatrix();
        m.reset();
        m.postRotate(270, child.getWidth() / 2.0f, child.getHeight() / 2.0f);
        return true;
    }
}