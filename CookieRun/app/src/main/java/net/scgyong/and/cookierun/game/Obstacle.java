package net.scgyong.and.cookierun.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.game.BaseGame;
import net.scgyong.and.cookierun.framework.game.RecycleBin;
import net.scgyong.and.cookierun.framework.res.BitmapPool;
import net.scgyong.and.cookierun.framework.res.Metrics;

public class Obstacle extends MapSprite {
    public static final int JELLY_COUNT = 60;
    private static final int SIZE = 66;
    private static final int BORDER = 2;
    private static final int ITEMS_IN_A_ROW = 30;
    private static final String TAG = JellyItem.class.getSimpleName();
    private final float inset;
    protected Rect srcRect = new Rect();
    protected RectF collisionBox = new RectF();
    private int count = 0;
    private float left;
    private float top;
    private float unit;
    private  int dir = 1;
    public static Obstacle get(int index, float unitLeft, float unitTop) {
        Obstacle obs = (Obstacle) RecycleBin.get(Obstacle.class);
        if (obs == null) {
            obs = new Obstacle(index);
        }
        obs.init(index, unitLeft, unitTop);
        return obs;
    }

    private void init(int index, float unitLeft, float unitTop) {
        MainGame game = MainGame.get();
        srcRect.set(0, 0, 64, 64);
         left = game.size(unitLeft);
         top = game.size(unitTop);
         unit = game.size(1);
        dstRect.set(left, top, left + unit, top + unit);
    }

    @Override
    public void update(float frameTime) {

        float speed = MapLoader.get().speed;
        float dx = speed * frameTime  * dir;
        dstRect.offset(dx, 0);
        srcRect.set(count * 64 , 0 ,(count+1)*64 , 64);
        count++;
        if(count == 4) count = 0;
        if (dstRect.right < 0  || dstRect.left > 3000) {
            MainGame game = MainGame.get();
            dstRect.set(left, top, left + unit, top + unit);
        }
        collisionBox.set(dstRect);
        collisionBox.inset(inset, inset);
    }

    @Override
    public RectF getBoundingRect() {
        return collisionBox;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }

    private Obstacle(int id) {
        if(id ==0 || id == 1){
            bitmap = BitmapPool.get(R.mipmap.cutting_blade);
            if(id ==1) dir = -1;
        }
        inset = MainGame.get().size(0.15f);
    }
}
