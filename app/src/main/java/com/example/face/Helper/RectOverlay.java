package com.example.face.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic {
    private int RColor = Color.BLUE;
    private float mStrockeWidth=4.0f;
    private Paint RPaint;
    private GraphicOverlay graphicOverlay;
    private Rect rect;
    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);
        RPaint=new Paint();
        RPaint.setColor(RColor);
        RPaint.setStyle(Paint.Style.STROKE);
        RPaint.setStrokeWidth(mStrockeWidth);
        this.graphicOverlay=graphicOverlay;
        this.rect=rect;
        postInvalidate();

    }
//this is all for drawing a rectangular around the face
    @Override
    public void draw(Canvas canvas) {
        RectF RF=new RectF(rect);
        RF.left=translateX(RF.left);
        RF.right=translateX(RF.right);
        RF.top=translateX(RF.top);
        RF.bottom=translateX(RF.bottom);
        canvas.drawRect(RF,RPaint);


    }
}
