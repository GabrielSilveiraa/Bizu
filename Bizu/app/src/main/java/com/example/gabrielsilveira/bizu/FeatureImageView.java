package com.example.gabrielsilveira.bizu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by gabrielsilveira on 19/04/16.
 */
public class FeatureImageView extends ImageView {

    private String feature;
    private Context context;

    public FeatureImageView(Context context, String feature) {
        super(context);
        this.context = context;
        this.feature = feature;
    }

    public FeatureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeatureImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.AppBackgroundEndColor));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        float startX = 0;
        float topY = 0;
        float endX = 100;
        float bottomY = 100;
        canvas.drawRect(startX, topY, endX, bottomY, paint);
    }
}
