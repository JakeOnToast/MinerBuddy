package com.jake.minerbuddy.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.jake.minerbuddy.main.MinerBuddyGame;
import com.jake.minerbuddy.screens.ShopScreen;

public class TileTab extends ShopTab{

    private TextureAtlas tileAtlas;

    public TileTab(ShopScreen shopScreen) {
        super(shopScreen);

        this.title = "Blocks";

        tileAtlas = shopScreen.getGame().getAssetManager().get("packed_textures/packed_tiles.atlas", TextureAtlas.class);
        JsonReader jsonReader = new JsonReader();
        JsonValue tiles = jsonReader.parse(Gdx.files.internal("base_values/default_tiles.json")).get("tiles");

        for (JsonValue data:tiles) {
            cards.add(new TileCard(this, shopScreen, this.stage, data));
        }




    }



    public TextureAtlas getTileAtlas() {
        return tileAtlas;
    }
}
