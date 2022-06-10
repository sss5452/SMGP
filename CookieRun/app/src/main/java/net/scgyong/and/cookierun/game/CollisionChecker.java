package net.scgyong.and.cookierun.game;

import android.graphics.Canvas;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;
import net.scgyong.and.cookierun.framework.interfaces.GameObject;
import net.scgyong.and.cookierun.framework.util.CollisionHelper;
import net.scgyong.and.cookierun.framework.res.Sound;
import java.util.ArrayList;

public class CollisionChecker implements GameObject {
    private final Player player;

    public CollisionChecker(Player player) {
        this.player = player;
    }

    @Override
    public void update(float frameTime) {
        MainGame game = MainGame.get();
//        Player player = (Player) game.objectsAt(MainGame.Layer.player.ordinal()).get(0);
        ArrayList<GameObject> obstacles = game.objectsAt(MainGame.Layer.Obstacle.ordinal());
        for (GameObject obstacle: obstacles){
            if (!(obstacle instanceof BoxCollidable)) {
                continue;
            }
            if (CollisionHelper.collides(player, (BoxCollidable) obstacle)) {
                    player.InitPlayer();
                    Sound.playEffect(R.raw.jelly);
                ArrayList<GameObject> Players = game.objectsAt(MainGame.Layer.player.ordinal());
                for( GameObject p : Players) {
                    Player array_player = (Player) p;
                    array_player.InitPlayer();
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
    }
}
