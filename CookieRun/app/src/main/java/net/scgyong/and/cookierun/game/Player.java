package net.scgyong.and.cookierun.game;

import android.graphics.Rect;
import android.graphics.RectF;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;
import net.scgyong.and.cookierun.framework.interfaces.GameObject;
import net.scgyong.and.cookierun.framework.objects.SheetSprite;
import net.scgyong.and.cookierun.framework.objects.Sprite;
import net.scgyong.and.cookierun.framework.res.Metrics;

import java.util.ArrayList;

public class Player extends SheetSprite implements BoxCollidable {

    private static final float FRAMES_PER_SECOND = 8f;
    private static final String TAG = Player.class.getSimpleName();
    private  CollisionChecker collisionChecker = new CollisionChecker(this);
    private Platform buttonPlatform;
    private float save_pos_x;
    private  float save_pos_y;

    static {
        State.initRects();
    }
    public enum  movestate{
        left,right,stop ,COUNT;
    }
    public enum PlayerType{
        Red,Blue,COUNT;
        public int playerBitmap(PlayerType type){
            if (type == PlayerType.Red){
                return R.mipmap.frog_idle;
            }
            else if(type == PlayerType.Blue){
                return R.mipmap.frog_idle2;
            }
            return  0;
        }
    }

    private enum State {
        run, jump,idle, falling, slide, COUNT;
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
                    new int[] { 0,1,2,3,4,5,6}, // jump
                    new int[] { 0,}, // doubleJump
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
                    Rect rect = new Rect(l, 0, l+32,   32);
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
    public movestate movedir;
    private  float m_w;
    private  float m_h;
    private PlayerType p_type;

    public Player(float x, float y, float w, float h, PlayerType type ) {

        super(type.playerBitmap(type), FRAMES_PER_SECOND * 2);
        this.x = x;
        this.y = y;
        m_w =w;
        m_h = h;
        save_pos_x = x;
        save_pos_y = y;
        jumpPower = Metrics.size(R.dimen.player_jump_power);
        gravity = Metrics.size(R.dimen.player_gravity);
        setDstRect(w, h);
        setState(State.run);
        SetBitmapflipSize(32);
        p_type = type;

    }
    public void InitPlayer()
    {
        this.x = save_pos_x;
        this.y = save_pos_y;
        setDstRect(m_w, m_h);
        collisionBox.set(dstRect);
        setState(State.falling);
        jumpSpeed = 0;
    }

    @Override
    public RectF getBoundingRect() {
        return collisionBox;
    }

    @Override
    public void update(float frameTime) {
        collisionChecker.update(frameTime);
        float foot = collisionBox.bottom;
        switch (state) {
            case jump:
            case falling:
                float dy = jumpSpeed * frameTime;
                jumpSpeed += gravity * frameTime;
//            Log.d(TAG, "y=" + y + " dy=" + dy + " js=" + jumpSpeed);
                if (jumpSpeed >= 0) {
                    if(state!=State.falling) setState(State.falling);
                    float platformTop = findNearestPlatformTop(foot);
//                    Log.i(TAG, "foot="+foot + " ptop=" + platformTop);
                    if (foot + dy >= platformTop) {
                        dy = platformTop - foot;
                        if(findNearestPlatform(foot).getType() == Platform.Type.Button) {
                            findNearestPlatform(foot).pushButton(true);
                            platformTop = findNearestPlatformTop(foot);
                            dy = platformTop - foot;
                        }
                        setState(State.run);

                        // if(buttonPlatform != null) buttonPlatform.srcRect.set(100,100,200,200);
                    }
                }
                y += dy;

                float dx = 0;

                if( movedir == movestate.stop)
                {
                    dx = 0;
                }
                if( movedir == movestate.left)
                {
                    dx = -10;
                    this.x -= 10;

                }
                if( movedir == movestate.right)
                {

                     dx = 10;
                    this.x += 10;
//                    moveSpeed = 2;
//                    this.x += 000000.1 * frameTime;
//                    dstRect.offset(this.x, 0);
//                    collisionBox.offset(this.x, 0);
                }

                dstRect.offset(dx, dy);
                collisionBox.offset(dx, dy);
                break;

            case run:
            case idle:
                if( movedir == movestate.stop)
                {
                    break;
                }
                if( movedir == movestate.left)
                {
                    dstRect.offset( -10, 0);
                    collisionBox.offset(-10, 0);
                    this.x -= 10;

                }
                if( movedir == movestate.right)
                {

                    dstRect.offset( 10, 0);
                    collisionBox.offset(10, 0);
                    this.x += 10;
//                    moveSpeed = 2;
//                    this.x += 000000.1 * frameTime;
//                    dstRect.offset(this.x, 0);
//                    collisionBox.offset(this.x, 0);
                }
                 float platformTop = findNearestPlatformTop(foot);
                if (foot < platformTop) {
                    setState(State.falling);
                    jumpSpeed = 0;
                }
                break;
        }
    }

    private boolean CollisionByObastacle()
    {
        MainGame game = MainGame.get();
        ArrayList<GameObject> obs = game.objectsAt(MainGame.Layer.Obstacle.ordinal());
        for (GameObject obj: obs) {
            Obstacle obstacle = (Obstacle) obj;
            RectF rect = obstacle.getBoundingRect();
            if (rect.left > x || x > rect.right) {
                return true;
            }
            if (rect.top > y || y > rect.bottom) {
                return true;
            }
        }
        return  false;
    }

    private float findNearestPlatformTop(float foot) {
        Platform platform = findNearestPlatform(foot);
//        if (platform.getType() == Platform.Type.Button)
//        {
//            buttonPlatform = platform;
//        }
//        else{
//            buttonPlatform = null;
//        }
        if (platform == null) return Metrics.height;
        if (platform.isPress) {
            return platform.getBoundingRect().centerY();
        }
        else
            return platform.getBoundingRect().top;
    }

    private Platform findNearestPlatform(float foot) {
        Platform nearest = null;
        MainGame game = MainGame.get();
        ArrayList<GameObject> platforms = game.objectsAt(MainGame.Layer.platform.ordinal());
        float top = Metrics.height;
        for (GameObject obj: platforms) {
            Platform platform = (Platform) obj;
            RectF rect = platform.getBoundingRect();
            if (rect.left > x || x > rect.right) {
                continue;
            }
//            Log.d(TAG, "foot:" + foot + " platform: " + rect);
            if(platform.isPress) {
                if (rect.centerY() < foot) {
                    continue;
                }
                if (top > rect.centerY()) {
                    top = rect.centerY();
                    nearest = platform;
                }
            }
            else {
                if (rect.top < foot) {
                    continue;
                }
                if (top > rect.top) {
                    top = rect.top;
                    nearest = platform;
                }

            }//            Log.d(TAG, "top=" + top + " gotcha:" + platform);
        }
        return nearest;
    }
    public void setmovedir(int dir)
    {

        if(dir == 0){
            movedir = movestate.stop;
        }
       else if(dir == 1){
            movedir = movestate.left;
            SetBitmapflip(true);
           // setState(State.run);
        }
        else if(dir == 2){
            SetBitmapflip(false);
         //   setState(State.run);

            movedir = movestate.right;
        }


    }

    public void jump() {
        float foot = collisionBox.bottom;
        if(findNearestPlatform(foot).getType() == Platform.Type.Button) {
            findNearestPlatform(foot).pushButton(false);
        }
//        Log.d(TAG, "Jump");
        if (state == State.run) {
            setState(State.jump);
            jumpSpeed = -jumpPower;
        }
    }



    public void slide(boolean startsSlide) {
        if (state == State.run && startsSlide) {
            setState(State.slide);
            return;
        }
        if (state == State.slide && !startsSlide) {
            setState(State.run);
            return;
        }
    }

    public void fall() {
        if (state != State.run) return;
        float foot = collisionBox.bottom;
        Platform platform = findNearestPlatform(foot);
        if (platform == null) return;
        if (!platform.canPass()) return;
        setState(State.falling);
        dstRect.offset(0, 0.001f);
        collisionBox.offset(0, 0.001f);
        jumpSpeed = 0;
    }

    public void setState(State state) {
        this.state = state;
        if (state== State.jump)
        {
            if(p_type == PlayerType.Red)
                ChangeBitmap(R.mipmap.frog_jump);
            else if (p_type == PlayerType.Blue){
                ChangeBitmap(R.mipmap.frog_jump2);
            }
        }
        if (state== State.run)
        {
            if(p_type == PlayerType.Red)
                ChangeBitmap(R.mipmap.frog_move);
            else if (p_type == PlayerType.Blue){
                ChangeBitmap(R.mipmap.frog_move2);
            }
        }
        if (state== State.idle)
        {
            if(p_type == PlayerType.Red)
                ChangeBitmap(R.mipmap.frog_idle);
            else if (p_type == PlayerType.Blue){
                ChangeBitmap(R.mipmap.frog_idle2);
            }
        }
        if (state== State.falling)
        {
            if(p_type == PlayerType.Red)
                ChangeBitmap(R.mipmap.frog_fall);
            else if (p_type == PlayerType.Blue){
                ChangeBitmap(R.mipmap.frog_fall2);
            }
        }

        srcRects = state.srcRects();
        collisionBox.set(dstRect);
        state.applyInsets(collisionBox);
    }
}
