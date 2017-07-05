package com.example.ync.mytimer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by YNC on 2017/6/19.
 */

public class ClockDraw extends View {

    Paint paintCircle;
    Paint paintFont;
    String countMinuteShow = "00";

    public ClockDraw(Context context) {
        super(context);
        init();
    }

    public ClockDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {

        //Font
        paintFont = new Paint();
        paintFont.setStyle(Paint.Style.FILL);
        paintFont.setAntiAlias(true); //線條無鋸齒狀
        paintFont.setColor(Color.parseColor("#ffffff"));
        paintFont.setTextSize(180);

        //畫圓
        paintCircle = new Paint();
        paintCircle.setAntiAlias(true); //線條無鋸齒狀
        paintCircle.setColor(Color.parseColor("#ffffff"));
        paintCircle.setStrokeWidth(10); //線寬
        paintCircle.setStyle(Paint.Style.STROKE); //空心效果

    }

    public void setCountMinuteShow(String countMinuteShow) {
        this.countMinuteShow = countMinuteShow;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(155, 155, 150, paintCircle);
        canvas.drawText(countMinuteShow,55, 215, paintFont );
    }


}
