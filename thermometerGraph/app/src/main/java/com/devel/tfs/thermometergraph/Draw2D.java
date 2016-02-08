package com.devel.tfs.thermometergraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by tfs on 07.02.2016.
 */
public class Draw2D extends View {
    public Draw2D(Context context){
        super(context);
    }

    private Paint mPaint = new Paint();
    private float center_x, center_y, radius;
    private Path mPath = new Path();
    private Matrix matrix = new Matrix();

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawPaint(mPaint);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        center_x = 400;
        center_y = 600;
        radius = 200;
        final RectF oval = new RectF();
        oval.set(center_x - radius, center_y - radius, center_x + radius, center_y + radius);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        canvas.drawArc(oval, 180, 180, false, mPaint);
        canvas.drawLine(center_x - radius, center_y, center_x - radius-20, center_y, mPaint);
        canvas.drawLine(center_x + radius, center_y, center_x + radius+20, center_y, mPaint);
        canvas.drawText("-40",center_x-radius,center_y,mPaint);
        mPath.moveTo(center_x,center_y);
        mPath.lineTo(center_x - radius + 10, center_y);
        //matrix.reset();
        matrix.setRotate(45,center_x,center_y);
        mPath.transform(matrix);
        canvas.drawPath(mPath, mPaint);
    }
}
