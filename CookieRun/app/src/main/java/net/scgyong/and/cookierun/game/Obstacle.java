package net.scgyong.and.cookierun.game;

import android.graphics.Rect;
import android.graphics.RectF;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;
import net.scgyong.and.cookierun.framework.interfaces.GameObject;
import net.scgyong.and.cookierun.framework.objects.SheetSprite;
import net.scgyong.and.cookierun.framework.res.Metrics;

import java.util.ArrayList;

public class Obstacle extends SheetSprite implements BoxCollidable {

    private static final float FRAMES_PER_SECOND = 8f;
    private static final String TAG = Enemy.class.getSimpleName();

    static {
        State.initRects();
    }

    private enum State {
        run, jump, doubleJump, falling, slide, COUNT;
        Rect[] srcRects() {
            return rectsArray[this.ordinal()];
        }
        void applyInsets(RectF dstRect) {
            float[] inset = insets[this.ordinal()];
            float w = dstRect.width();
            float h = dstRect.height();
            dstRect.left += w * inset[0];
            dstRect.top += h * inset[1];
            dstRect.right -= w * inset[2];
            dstRect.bottom -= h * inset[3];
        }
        static Rect[][] rectsArray;
        static void initRects() {
            int[][] indices = {
                    new int[] { 0,1,2,3,4,5,6,7,8,9,10}, // run
                    new int[] { 0,}, // jump
                    new int[] { 1, 2, 3, 4 }, // doubleJump
                    new int[] { 0 }, // falling
                    new int[] { 9, 10 },    //slide
            };
            rectsArray = new Rect[indices.length][];
            for (int r = 0; r < indices.length; r++) {
                int[] ints = indices[r];
                Rect[] rects = new Rect[ints.length];

                for (int i = 0; i < ints.length; i++) {
                    int idx = ints[i];
                    int l = (idx % 100) * 32;
                    Rect rect = new Rect(l, 0, l+32, 32);
                    rects[i] = rect;
                }

                rectsArray[r] = rects;
            }
        }
        float[][] insets = {
                new float[] { 0.10f, 0.05f, 0.10f, 0.00f }, // run
                new float[] { 0.10f, 0.20f, 0.10f, 0.00f }, // jump
                new float[] { 0.10f, 0.15f, 0.10f, 0.00f }, // doubleJump
                new float[] { 0.10f, 0.05f, 0.10f, 0.00f }, // falling
                new float[] { 0.00f, 0.50f, 0.00f, 0.00f }, // slide
        };
    }
    private State state = State.run;
    private final float jumpPower;
    private final float gravity;
    private float jumpSpeed;
    protected RectF collisionBox = new RectF();
    private float moveSpeed;
    private float fliction;


    public Obstacle(float x, float y, float w, float h, int type) {
        super(R.mipmap.frog_idle, FRAMES_PER_SECOND * 2);
        this.x = x;
        this.y = y;
        jumpPower = Metrics.size(R.dimen.player_jump_power);
        gravity = Metrics.size(R.dimen.player_gravity);
        setDstRect(w, h);
        setState(State.run);
    }

    @Override
    public RectF getBoundingRect() {
        return collisionBox;
    }

    @Override
    public void update(float frameTime) {

    }
    public void findPlayer()
    {

    }

    public void move(boolean isPushMove)
    {

        if ( isPushMove) {
            moveSpeed = 10;
            this.x += 0.1;
            dstRect.offset(this.x, 0);
            collisionBox.offset(this.x, 0);
            return;
        }
        else{
            return;
        }

    }

    private void setState(State state) {
        this.state = state;
        if (state== State.jump)
        {
            ChangeBitmap(R.mipmap.frog_jump);
        }
        if (state== State.run)
        {
            ChangeBitmap(R.mipmap.frog_move);
        }
        if (state== State.jump)
        {
            ChangeBitmap(R.mipmap.frog_jump);
        }
        srcRects = state.srcRects();
        collisionBox.set(dstRect);
        state.applyInsets(collisionBox);
    }
}
