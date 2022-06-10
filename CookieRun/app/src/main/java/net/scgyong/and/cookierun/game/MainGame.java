package net.scgyong.and.cookierun.game;

import android.view.MotionEvent;

import androidx.constraintlayout.widget.ConstraintSet;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.game.BaseGame;
import net.scgyong.and.cookierun.framework.objects.Button;
import net.scgyong.and.cookierun.framework.objects.HorzScrollBackground;
import net.scgyong.and.cookierun.framework.res.Metrics;
import net.scgyong.and.cookierun.framework.res.Sound;

public class MainGame extends BaseGame {
    public static final String PARAM_STAGE_INDEX = "stage_index";
    private static final String TAG = MainGame.class.getSimpleName();

    private Player playerRed;
    private Player playerBlue;
    private static MainGame singleton;
    public static MainGame get() {
        if (singleton == null) {
            singleton = new MainGame();
        }
        return singleton;
    }
    public enum Layer {
        bg, platform, Obstacle, player, ui, touchUi, controller, COUNT
    }

    public float size(float unit) {
        return Metrics.height / 9.5f * unit;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }

    protected int mapIndex;

    public void setStage(int index)
    {
        MapLoader mapLoader = MapLoader.get();
        mapLoader.deleteObject();
        mapLoader.init(index);
        playerRed.InitPlayer();
        playerBlue.InitPlayer();

    }

    public void init() {
        super.init();
        initLayers(Layer.COUNT.ordinal());
        playerRed = new Player(
                size(1), size(6),
                size(1*0.9f), size(1 * 0.9f), Player.PlayerType.Red
        );
        playerBlue = new Player(
                size(3), size(6),
                size(1*0.9f), size(1*0.9f),Player.PlayerType.Blue
        );
        add(Layer.player.ordinal(), playerRed);
        add(Layer.player.ordinal(), playerBlue);

        //배경
        add(Layer.bg.ordinal(), new HorzScrollBackground(R.mipmap.background_grass, Metrics.size(R.dimen.bg_scroll_1)));
        //맵 오브젝트
        MapLoader mapLoader = MapLoader.get();
        mapLoader.init(mapIndex);
        add(Layer.controller.ordinal(), mapLoader);
        //add(Layer.controller.ordinal(), new CollisionChecker(player));

        float btn_x = size(2.5f);
        float btn_y = size(8.75f);
        float btn_w = size(8.0f / 2.0f);
        float btn_h = size(1.5f);


        add(Layer.touchUi.ordinal(), new Button(
                btn_x, btn_y, btn_w, btn_h, R.mipmap.bt_nomal, R.mipmap.bt_press,
                new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action,boolean pressed) {
                if(action == Button.Action.moveLeft)
                {
                    playerBlue.setmovedir(1);
                }
                else if(action == Button.Action.moveRight)
                {
                    playerBlue.setmovedir(2);
                }
                else if(action == Button.Action.released)
                {
                    playerBlue.setmovedir(0);
                    playerBlue.jump();
                }
                return true;
            }
        }));

        add(Layer.touchUi.ordinal(), new Button(
                Metrics.width - btn_x, btn_y, btn_w, btn_h, R.mipmap.bt_nomal, R.mipmap.bt_press,
                new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action,boolean pressed) {
                if(action == Button.Action.moveLeft)
                {
                    playerRed.setmovedir(1);
                }
                else if(action == Button.Action.moveRight)
                {
                    playerRed.setmovedir(2);
                }
                else if(action == Button.Action.released)
                {
                    playerRed.setmovedir(0);
                    playerRed.jump();
                }
                return true;
            }
        }));
        Sound.playMusic(R.raw.jelly);
        push(TitleScene.get());
    }
    @Override
    public boolean handleBackKey() {
        return true;
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touchUi.ordinal();
    }

    @Override
    public void start() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void end() {
    }
}
