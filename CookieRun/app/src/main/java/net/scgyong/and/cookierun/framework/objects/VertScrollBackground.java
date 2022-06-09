package net.scgyong.and.cookierun.framework.objects;

import android.graphics.Canvas;

import net.scgyong.and.cookierun.framework.game.BaseGame;
import net.scgyong.and.cookierun.framework.res.Metrics;

public class VertScrollBackground extends Sprite {
    private final float speed;
    private final int height;

    public VertScrollBackground(int bitmapResId, float speed) {
        super(0, 0,
                Metrics.width, Metrics.height, bitmapResId);
        this.height = bitmap.getHeight() * Metrics.width / bitmap.getWidth();
        setDstRect(Metrics.width, height);
        this.speed = speed;
    }


    @Override
    public void draw(Canvas canvas) {
        dstRect.set(0, 0, Metrics.width, Metrics.height);
    }
}