package com.example.administrator.circlearoundpro;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CustomView extends View {
    private Paint circlePaint;

    //小圆半径
    private float smartRadius;
    //大圆半径
    private float bigRadius;
    //笔刷宽度
    private float strokeWidth;

    private float startAngle = 0;
    private int viewHeight;
    private int viewWidth;
    private ValueAnimator animator;


    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);

        smartRadius = typedArray.getFloat(R.styleable.CustomView_Smart_Radius, 10);

        strokeWidth = typedArray.getFloat(R.styleable.CustomView_Stroke_Width, 10);

        if (viewWidth > viewHeight) {
            bigRadius = typedArray.getFloat(R.styleable.CustomView_Big_Radius, viewHeight / 2 - getPaddingLeft() - strokeWidth / 2);

        } else {
            bigRadius = typedArray.getFloat(R.styleable.CustomView_Big_Radius, viewWidth / 2 - getPaddingLeft() - strokeWidth / 2);

        }


        typedArray.recycle();

    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBigRadius(float bigRadius) {
        this.bigRadius = bigRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        circlePaint = new Paint();

        //大圆颜色
        circlePaint.setColor(Color.parseColor("#FFDAB9"));
        //空心圆
        circlePaint.setStyle(Paint.Style.STROKE);
        //笔刷宽度
        circlePaint.setStrokeWidth(strokeWidth);
        //抗锯齿
        circlePaint.setAntiAlias(true);
        //防抖动
        circlePaint.setDither(true);

        canvas.drawCircle(viewWidth / 2, viewHeight / 2, bigRadius, circlePaint);

        //空心圆
        circlePaint.setStyle(Paint.Style.FILL);
        //小圆颜色
        circlePaint.setColor(Color.parseColor("#2E8B57"));

        MathDisc(canvas);

    }

    //计算圆上每一个点的坐标
    private void MathDisc(Canvas canvas) { //angle 角度
        float hudu = (float) ((2 * Math.PI / 360) * (startAngle));//  360/8=45,即45度(这个随个人设置)

        float X = (float) (viewWidth / 2 + Math.sin(hudu) * (bigRadius));    //  mCenterX 是圆形中心的坐标X   即定位left 的值
        float Y = (float) (viewHeight / 2 - Math.cos(hudu) * (bigRadius));    //  mCenterY 是圆形中心的坐标Y   即定位top 的值

        canvas.drawCircle(X, Y, smartRadius, circlePaint);
    }

    //属性动画
    public void startAnimation() {
        if (animator != null) {
            return;
        }
        animator = ValueAnimator.ofFloat(0, 1.0f);
        animator.setDuration(2000); //动画时长
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//表示不循环，-1表示无限循环

        //添加监听器,监听动画过程中值的实时变化(animation.getAnimatedValue()得到的值就是0-1.0)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                /**
                 * 这里我们已经知道ValueAnimator只是对值做动画运算，而不是针对控件的，因为我们设置的区间值为0-1.0f
                 * 所以animation.getAnimatedValue()得到的值也是在[0.0-1.0]区间，而我们在画进度条弧度时，设置的当前角度为360*currentAngle，
                 * 因此，当我们的区间值变为1.0的时候弧度刚好转了360度
                 */

                startAngle = 360 * (float) animation.getAnimatedValue();

                //实时刷新view，这样我们的进度条弧度就动起来了
                postInvalidateDelayed(10);
            }
        });
        //开启动画
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

}
