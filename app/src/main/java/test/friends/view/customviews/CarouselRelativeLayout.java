package test.friends.view.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import android.widget.RelativeLayout;

import test.friends.adapters.FriendsPagerAdapter;

public class CarouselRelativeLayout extends RelativeLayout {
    private float scale = FriendsPagerAdapter.BIG_SCALE;

    public CarouselRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarouselRelativeLayout(Context context) {
        super(context);
    }

    public void setScaleBoth(float scale) {
        this.scale = scale;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = this.getWidth();
        int h = this.getHeight();
        canvas.scale(scale, scale, w/2, h/2);
    }
}
