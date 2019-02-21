package flappy.bird;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.os.Bundle;

public class StartGame extends Activity {

    GameView gameView;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }

}

