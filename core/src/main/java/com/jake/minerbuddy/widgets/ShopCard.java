package com.jake.minerbuddy.widgets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.jake.minerbuddy.screens.ShopScreen;

public abstract class ShopCard {

    // this class will create the display cards for items that
    // can be purchased in the shop
    public static float width;
    public static float height;
    public static float leftPadding;

    // image is square
    public static float imageSize;
    public static float imagePadding;

    protected ShopTab tab;

    protected int cardIndex;
    protected float x;
    protected float y;

    protected Label nameLabel;
    protected Array<Button> buttons;

    public abstract void draw(SpriteBatch batch, float scrollX);
    public abstract void move(float scrollX);
    public abstract void resize();




}
