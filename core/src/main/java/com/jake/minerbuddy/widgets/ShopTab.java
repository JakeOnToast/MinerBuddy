package com.jake.minerbuddy.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.jake.minerbuddy.main.MinerBuddyGame;
import com.jake.minerbuddy.screens.ShopScreen;

public class ShopTab {

    // hold multiple cards
    protected String title;
    protected Array<ShopCard> cards;
    protected Stage stage;

    protected float scrollX;
    protected float dx;

    protected Texture cardTexture;

    public ShopTab(ShopScreen shopScreen){
        this.stage = new Stage(shopScreen.getGame().viewport, shopScreen.getGame().batch);
        cards = new Array<>();
        cardTexture = new Texture(Gdx.files.internal("raw_textures/cards/shop_card.png"));

    }

    public void draw(SpriteBatch batch){

        // check if scroll bar is moving
        if (Gdx.input.isTouched()){
            if (Gdx.graphics.getHeight() - Gdx.input.getY() < ShopCard.leftPadding + ShopCard.height){
                dx = Gdx.input.getDeltaX();
                translate(dx);
            }
        }else{

            // continue scroll
            if (Math.abs(dx) > 0.3){
                dx *= 0.875f;
                translate(dx);
            }


        }

        // draw card
        for (ShopCard card: cards) {
            card.draw(batch, scrollX);
        }

    }


    public void translate(float dx){

        // scrolls cards
        scrollX += dx;
        // adjust to bounds of scroll area
        if (scrollX > 0) scrollX = 0;
        
        for (ShopCard card: cards){
            card.move(scrollX);
        }


    }



    public void resize(){
        for (ShopCard card: cards){
            card.resize();
        }
    }

    public Stage getStage(){
        return this.stage;
    }

    public void dispose(){
        stage.dispose();
    }
}
