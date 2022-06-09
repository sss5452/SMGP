package net.scgyong.and.cookierun.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.game.RecycleBin;
import net.scgyong.and.cookierun.framework.res.BitmapPool;
import net.scgyong.and.cookierun.framework.res.Metrics;

public class PressButton extends MapSprite {
    public static final int JELLY_COUNT = 60;
    private static final int SIZE = 66;
    private static final int BORDER = 2;
    private static final int ITEMS_IN_A_ROW = 30;
    private static final String TAG = JellyItem.class.getSimpleName();
    private final float inset;
    protected Rect srcRect = new Rect();
    protected RectF collisionBox = new RectF();
    public static PressButton get(int index, float unitLeft, float unitTop) {
        PressButton item = (PressButton) RecycleBin.get(PressButton.class);
        if (item == null) {
            item = new PressButton();
        }
        item.init(index, unitLeft, unitTop);
        return item;
    }

    private void init(int index, float unitLeft, float unitTop) {
        MainGame game = MainGame.get();
        srcRect.set(0, 0, 150, 65);
//        Log.d(TAG, "index=" + index + " rect=" + srcRect);
        float left = game.size(unitLeft);
        float top = game.size(unitTop);
        float unit = game.size(1);
        dstRect.set(left, top, left + unit*2, top + unit);
    }

//    @Override
//    public void update(float frameTime) {
//        super.update(frameTime);
//        collisionBox.set(dstRect);
//        collisionBox.inset(inset, inset);
//    }

    @Override
    public RectF getBoundingRect() {
        return collisionBox;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }

    private PressButton() {
        bitmap = BitmapPool.get(R.mipmap.button);
        inset = MainGame.get().size(0.15f);
    }
}
