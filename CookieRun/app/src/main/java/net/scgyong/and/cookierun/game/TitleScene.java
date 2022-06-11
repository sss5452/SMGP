package net.scgyong.and.cookierun.game;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.game.BaseGame;
import net.scgyong.and.cookierun.framework.objects.Button;
import net.scgyong.and.cookierun.framework.objects.Sprite;
import net.scgyong.and.cookierun.framework.res.Metrics;
import net.scgyong.and.cookierun.framework.res.Sound;

public class TitleScene extends BaseGame {
    private static TitleScene singleton;
    public static TitleScene get() {
        if (singleton == null) {
            singleton = new TitleScene();
            singleton.init();
        }
        return singleton;
    }

    public enum Layer {
        ui, touchUi, COUNT;
    }

    @Override
    public void init() {
        super.init();
        initLayers(Layer.COUNT.ordinal());


        add(Layer.ui.ordinal(), new Sprite(
                Metrics.width/2, Metrics.height/2,
                Metrics.width, Metrics.height,
                R.mipmap.logobg2));



        float btn_width = Metrics.width /9 ;
        float btn_height =  Metrics.height/7 ;
        float btn_x = Metrics.width / 2;
        float btn_y = Metrics.height / 2 - btn_height/3;

        add(Layer.touchUi.ordinal(), new Button(
                btn_x, btn_y, btn_width, btn_height, R.mipmap.strat_p, R.mipmap.strat_n,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action,boolean pressed) {
                        Sound.stopMusic();
                        BaseGame.popScene();
                        Sound.playMusic(R.raw.map);
                        return true;
                    }
                }));

    }
    @Override
    public void start() {
        Sound.playMusic(R.raw.titlemusic);

    }


    @Override
    protected int getTouchLayerIndex() {
        return Layer.touchUi.ordinal();
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean handleBackKey() {
        BaseGame.popScene();
        return true;
    }
}
