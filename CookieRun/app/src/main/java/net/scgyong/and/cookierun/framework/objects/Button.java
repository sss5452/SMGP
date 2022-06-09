package net.scgyong.and.cookierun.framework.objects;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import net.scgyong.and.cookierun.framework.interfaces.Touchable;
import net.scgyong.and.cookierun.framework.res.BitmapPool;

public class Button extends Sprite implements Touchable {
    protected final Callback callback;
    private final Bitmap normalBitmap;
    private Bitmap pressedBitmap;
    private boolean pressed;
    public float press_x;
    public float press_y;

    public enum Action {
        pressed, released, moveLeft, moveRight
    }
    public interface Callback {
        public boolean onTouch(Action action, boolean pressed);
    }
    public Button(float x, float y, float w, float h, int bitmapResId, int pressedResId, Callback callback) {
        super(x, y, w, h, bitmapResId);
        normalBitmap = bitmap;
        if (pressedResId != 0) {
            pressedBitmap = BitmapPool.get(pressedResId);
        }
        this.callback = callback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        press_x = e.getX();
        press_y = e.getY();
        int Pointer_Count = e.getPointerCount();
        if(Pointer_Count >2){
            Pointer_Count = 2;
        }
        if (!pressed && !dstRect.contains(press_x, press_y)) {
            return false;
        }
        int action = e.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                pressed = true;
                bitmap = pressedBitmap;
                if(press_x > dstRect.centerX() && press_x < dstRect.right) {
                    return callback.onTouch(Action.moveRight,pressed);
                }
                if(press_x < dstRect.centerX() && press_x > dstRect.left) {
                    return callback.onTouch(Action.moveLeft,pressed);
                }
                break;

            case MotionEvent.ACTION_UP:
                pressed = false;
                bitmap = normalBitmap;
                return callback.onTouch(Action.released,pressed);

            case MotionEvent.ACTION_MOVE:
                pressed = true;
                if(press_x > dstRect.centerX() && press_x < dstRect.right) {
                    return callback.onTouch(Action.moveRight,pressed);
                }
                if(press_x < dstRect.centerX() && press_x > dstRect.left) {
                    return callback.onTouch(Action.moveLeft,pressed);
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                pressed = true;
                bitmap = pressedBitmap;
                if(press_x > dstRect.centerX() && press_x < dstRect.right) {
                    return callback.onTouch(Action.moveRight,pressed);
                }
                if(press_x < dstRect.centerX() && press_x > dstRect.left) {
                    return callback.onTouch(Action.moveLeft,pressed);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                pressed = false;
                bitmap = normalBitmap;
                return callback.onTouch(Action.released,pressed);
        }
        return false;
    }
}
