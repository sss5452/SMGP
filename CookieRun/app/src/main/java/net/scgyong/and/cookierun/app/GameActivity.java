package net.scgyong.and.cookierun.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import net.scgyong.and.cookierun.framework.game.BaseGame;
import net.scgyong.and.cookierun.framework.view.GameView;
import net.scgyong.and.cookierun.game.MainGame;
import net.scgyong.and.cookierun.game.TitleScene;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int stageIndex = intent.getExtras().getInt(MainGame.PARAM_STAGE_INDEX);
        setContentView(new GameView(this, null));
        MainGame game = MainGame.get();
        game.setMapIndex(stageIndex);
        BaseGame.push(game);

    //    TitleScene titleScene = TitleScene.get();
    //    BaseGame.push(titleScene);
//      Scene.push(PausedScene.get());
    }

    @Override
    protected void onPause() {
        GameView.view.pauseGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameView.view.resumeGame();
    }

    @Override
    protected void onDestroy() {
        GameView.view = null;
        BaseGame.clear();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (GameView.view.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}