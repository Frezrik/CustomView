package com.ming.customview.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CircleIndicator extends Drawable implements Animatable {

    private int bgColor = Color.parseColor("#00574B");
    private int startColor = Color.parseColor("#008577");
    private int endColor = Color.parseColor("#99FFD9");
    private int paintWidth = 30;

    private int alpha = 255;
    private Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint rotatePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private ValueAnimator rotateAnim;
    private float degrees;
    private Rect drawBounds = new Rect();

    /**
     * CircleIndicator构造
     */
    public CircleIndicator(){

        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(paintWidth);

        rotatePaint.setStyle(Paint.Style.STROKE);
        rotatePaint.setStrokeCap(Paint.Cap.ROUND);
        rotatePaint.setStrokeWidth(paintWidth);

        rotateAnim = ValueAnimator.ofFloat(0,180,360);
        rotateAnim.setDuration(1600);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setRepeatMode(ValueAnimator.RESTART);
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees = (float) animation.getAnimatedValue();
                //Log.d("zmzm", "onAnimationUpdate:" + degrees);

                invalidateSelf();
            }
        });
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        Log.d("zmzm", "onBoundsChange");
        drawBounds = new Rect(bounds.left , bounds.top, bounds.right, bounds.bottom);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        RectF rectF = new RectF(drawBounds.left + paintWidth / 2,drawBounds.top + paintWidth / 2, drawBounds.right - paintWidth / 2, drawBounds.bottom - paintWidth / 2);
        drawBg(canvas, rectF);
        drawRotate(canvas, rectF);
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    @Override
    public int getAlpha() {
        return alpha;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void start() {
        if (rotateAnim == null) return;
        if (rotateAnim.isRunning()) return;
        rotateAnim.start();

        Log.d("zmzm", "start anim");

        rotateAnim.start();
    }

    @Override
    public void stop() {
        if (rotateAnim == null) return;
        rotateAnim.cancel();
        rotateAnim.removeAllUpdateListeners();
    }

    @Override
    public boolean isRunning() {
        return rotateAnim != null && rotateAnim.isRunning();
    }



    private void drawBg(Canvas canvas, RectF rectF) {
        bgPaint.setColor(bgColor);
        //canvas.drawRect(rectF, bgPaint);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, bgPaint);
    }

    private void drawRotate(Canvas canvas, RectF rectF) {
        canvas.rotate(degrees, rectF.centerX(), rectF.centerY());
        canvas.save();
        for (int i = 0, end = 75; i < end; i++) {
            rotatePaint.setColor(getGradient(i / (float) end, startColor, endColor));
            canvas.drawArc(rectF,
                    0 + i,
                    1,
                    false,
                    rotatePaint);
            canvas.drawArc(rectF,
                    180 + i,
                    1,
                    false,
                    rotatePaint);
        }
        Log.d("zmzm", "onDraw rotate:" + degrees);
        canvas.restore();
    }

    private int getGradient(float fraction, int startColor, int endColor) {
        if (fraction > 1) fraction = 1;
        int alphaStart = Color.alpha(startColor);
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaEnd = Color.alpha(endColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaDifference = alphaEnd - alphaStart;
        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaCurrent = (int) (alphaStart + fraction * alphaDifference);
        int redCurrent = (int) (redStart + fraction * redDifference);
        int blueCurrent = (int) (blueStart + fraction * blueDifference);
        int greenCurrent = (int) (greenStart + fraction * greenDifference);
        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }
}
