package net.scgyong.and.cookierun.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.game.RecycleBin;
import net.scgyong.and.cookierun.framework.interfaces.GameObject;
import net.scgyong.and.cookierun.framework.res.BitmapPool;
import net.scgyong.and.cookierun.framework.res.Metrics;

import java.util.ArrayList;
import java.util.Random;

public class Platform extends MapSprite {
    private Type type;
    protected Rect srcRect = new Rect();
    private  float  hh =1;
    private  float left;
    private  float top;
    public   boolean isPress = false;
    private  float downper = 0;
    protected RectF collisionBox = new RectF();

    public boolean canPass() {
        return type != Type.Ground;
    }

    public enum Type {
        Ground, Button, Plat,DownButton, COUNT;
        float width() {
            int w = 1;
            switch (this) {
                case Ground: w = 20; break;
                case Button: w = 2; break;
                case Plat: w = 3; break;
                case DownButton: w= 2; break;
            }
            return MainGame.get().size(w);
        }
        float height() {
            int h = 1;
            switch (this) {
                case Ground: case Button: h = 2; break;
                case Plat: h = 1; break; case DownButton: h =2; break;
            }
            return MainGame.get().size(h);
        }
        int bitmapId() {
            return BITMAP_IDS[this.ordinal()];
        }
        public static Type random(Random random) {
            int index = random.nextInt(COUNT.ordinal());
            return values()[index];
        }
    }
    protected static int[] BITMAP_IDS = {
            R.mipmap.block_grass,
            R.mipmap.button,
            R.mipmap.block_grass,
            R.mipmap.button,
    };
    public static Platform get(Type type, float unitLeft, float unitTop) {
        Platform platform = (Platform) RecycleBin.get(Platform.class);
        if (platform == null) {
            platform = new Platform();
        }
        platform.init(type, unitLeft, unitTop);
        return platform;
    }

    private Platform() {
    }
    public Type getType()
    {
        return type;
    }

    public void pushButton(boolean push){
        if(push) {
            dstRect.set(left, top+downper + 10, left + type.width() / 1.5f, top +downper+ type.height() / 3.f);
            srcRect.set(138, 0, 300, 65);
            setBoundingRect(dstRect);
            isPress = true;

            int pushCount = 0;
            MainGame game = MainGame.get();
            ArrayList<GameObject> platforms = game.objectsAt(MainGame.Layer.platform.ordinal());
            for (GameObject obj: platforms) {
                Platform platform = (Platform) obj;
                if(platform.type == Type.Button){
                    if(platform.isPress) pushCount++;
                }
            }
            if(pushCount==2)
            {
                game.nextStage();
            }
        }
        else{
            srcRect.set(0,0,bitmap.getWidth()/2,bitmap.getHeight());
            dstRect.set(left, top+downper, left + type.width()/1.5f, top+downper + type.height()/3.f);
            setBoundingRect(dstRect);
            isPress = false;
        }

    }

    public boolean getIsPress()
    {
        return isPress;
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }

    private void init(Type type, float unitLeft, float unitTop) {
        this.type = type;
        bitmap = BitmapPool.get(type.bitmapId());
        if(this.type == Type.DownButton) {
            this.type = Type.Button;
            downper = type.height()/4;
        }
        MainGame game = MainGame.get();
         left = game.size(unitLeft);
         if(this.type == Type.Button)
            top = game.size(unitTop/0.7f);
        else top = game.size(unitTop);

        if(this.type == Type.Button){
            srcRect.set(0,0,bitmap.getWidth()/2,bitmap.getHeight());
        }
        else {
            srcRect.set(0,0,bitmap.getWidth(),bitmap.getHeight());
        }
        dstRect.set(left, top+downper, left + type.width()/1.5f, top+downper + type.height()/3f);
    }
}
