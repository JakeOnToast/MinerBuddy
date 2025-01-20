package com.jake.minerbuddy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.jake.minerbuddy.main.MinerBuddyGame;
import com.jake.minerbuddy.tiles.Ore;
import com.jake.minerbuddy.widgets.ShopCard;
import com.jake.minerbuddy.widgets.ShopTab;
import com.jake.minerbuddy.widgets.TileTab;

public class ShopScreen implements Screen {


    private MinerBuddyGame game;

    private final Stage stage;

    private final InputMultiplexer inputMultiplexer;

    private final ShopTab tab;

    //button assets
    private final TextureAtlas buttonAtlas;
    private final Skin buttonSkin;


    //tabs - buttons that don't move
    // tile tab, ore tab etc





    public ShopScreen(MinerBuddyGame game){

        this.game = game;

        stage = new Stage(game.viewport, game.batch);


        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);


        buttonAtlas = new TextureAtlas("packed_textures/buttons.atlas");
        buttonSkin = new Skin(buttonAtlas);
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.up = buttonSkin.getDrawable("red_button_up");
        tbs.down = buttonSkin.getDrawable("red_button_down");
        tbs.font = game.font;

        TextButton returnButton = new TextButton("X", tbs);
//        returnButton.setPosition(1800, 800);
        returnButton.setPosition(0, 0);
        returnButton.setSize(128,128);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.gameScreen);
            }
        });
        stage.addActor(returnButton);


        tab = new TileTab(this);
        //TODO move this
        inputMultiplexer.addProcessor(tab.getStage());


    }


    private void createTabs(){




    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        tab.draw(game.batch);

        game.batch.end();

        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

        game.viewport.update(width, height, true);
        game.batch.setProjectionMatrix(game.camera.combined);


        // set card sizes
        ShopCard.height = (float)height*0.6f;
        ShopCard.width = (int)(ShopCard.height*2/3);
        ShopCard.leftPadding = (int)(ShopCard.width/5);

        ShopCard.imageSize = (int)(ShopCard.width*0.71875f);
        ShopCard.imagePadding = (int)(ShopCard.width*9/64);

        // 2.25/8

        // resize all tabs
        tab.resize();


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        buttonAtlas.dispose();
        buttonSkin.dispose();
        tab.dispose();
    }


    // getters
    public Stage getStage(){
        return this.stage;
    }

    public MinerBuddyGame getGame() {
        return game;
    }

    public Skin getButtonSkin(){
        return this.buttonSkin;
    }


}
