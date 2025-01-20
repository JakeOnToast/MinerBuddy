package com.jake.minerbuddy.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.jake.minerbuddy.screens.ShopScreen;
import com.jake.minerbuddy.tiles.Tile;

import static com.jake.minerbuddy.widgets.WidgetFunctions.cleanPrice;

public class TileCard extends ShopCard{

    private String tileName;

    // full, left, right
    private Array<Sprite> sprites;

    private Texture card;

    public TileCard(TileTab tab, ShopScreen shopScreen, Stage stage, JsonValue data) {

        this.tab = tab;

        card = tab.cardTexture;
        this.cardIndex = data.getInt("id");
        // calculate position
        this.x = 0;
        this.y = 0;

        this.tileName = data.getString("name");
        //TODO sync label style with game
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = shopScreen.getGame().font;
        labelStyle.fontColor = Color.GOLDENROD;
        nameLabel = new Label(this.tileName, labelStyle);
        nameLabel.setAlignment(Align.center);

        sprites = new Array<>();
        sprites.add(tab.getTileAtlas().createSprite(tileName.toLowerCase()+"full"));
        sprites.add(tab.getTileAtlas().createSprite(tileName.toLowerCase()+"left"));
        sprites.add(tab.getTileAtlas().createSprite(tileName.toLowerCase()+"right"));

        buttons = new Array<>();

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.font = shopScreen.getGame().font;
        Skin skin = shopScreen.getButtonSkin();
        tbs.up = skin.getDrawable("green_button_up");
        tbs.down = skin.getDrawable("green_button_down");

        TextButton button = new TextButton(cleanPrice(data.getInt("price")), tbs);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO ADD REAL STUFF
                shopScreen.getGame().gameScreen.enterPurchaseMode(data);
                shopScreen.getGame().setScreen(shopScreen.getGame().gameScreen);
            }
        });

        buttons.add(button);
        stage.addActor(button);

        move(0f);
        resizeButtons();
    }

    @Override
    public void draw(SpriteBatch batch, float scrollX) {

        // draw card background
        batch.draw(card, x + scrollX, y, width, height);

        // draw tile on card
        float boxHeight = imageSize / 2;
        batch.draw(sprites.get(0), x + imagePadding + scrollX, y + height*5/16 + boxHeight/2 , imageSize, boxHeight);
        // draw sides
        batch.draw(sprites.get(1), x + imagePadding + scrollX, y + height*5/16 , boxHeight, boxHeight);
        batch.draw(sprites.get(2), x + imagePadding + scrollX + imageSize/2, y + height*5/16, boxHeight, boxHeight);

        buttons.get(0).draw(batch, 1);
        nameLabel.draw(batch, 1);
    }

    @Override
    public void move(float scrollX) {
        for (Button button: buttons) {
            button.setPosition(this.x + imagePadding + scrollX, this.y + height * 0.052f);
        }
        // 0.8020833 is closer but not needed
        nameLabel.setPosition(this.x + imagePadding + scrollX, this.y + height * 0.8f);
    }

    @Override
    public void resize() {
        this.x = leftPadding*(this.cardIndex + 1) + width*this.cardIndex;
        this.y = leftPadding;
        nameLabel.setSize(width - 2*imagePadding, height/6);
        resizeButtons();
        move(tab.scrollX);
    }

    public void resizeButtons(){
        for (Button button: buttons){
            button.setSize(width - 2*imagePadding, height/6);
        }
    }
}
