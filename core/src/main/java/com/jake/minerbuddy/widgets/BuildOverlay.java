package com.jake.minerbuddy.widgets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.jake.minerbuddy.screens.GameScreen;

public class BuildOverlay {
    // this holds the buttons that show the items to place

    public static final int TILE = 0;
    public static final int ORE = 1;
    public static final int WORKER = 2;

    private final GameScreen gameScreen;
    private final Stage stage;

    private final TextButton buyButton;

    public BuildOverlay(GameScreen gameScreen, Skin buttonSkin){
        this.gameScreen = gameScreen;
        stage = new Stage(gameScreen.getGame().viewport, gameScreen.getGame().batch);

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.font = gameScreen.getGame().font;

        tbs.up = buttonSkin.getDrawable("green_button_up");
        tbs.down = buttonSkin.getDrawable("green_button_down");


        buyButton = new TextButton("enoughText", tbs);
        buyButton.setPosition(250, 250);

        stage.addActor(buyButton);
    }

    public void draw(Batch batch){
        buyButton.draw(batch, 1);
    }

    public TextButton getBuyButton(){
        return this.buyButton;
    }

    public void activate(){
        gameScreen.getInputMultiplexer().addProcessor(stage);
    }

    public void deactivate(){
        gameScreen.getInputMultiplexer().removeProcessor(stage);
    }
}
