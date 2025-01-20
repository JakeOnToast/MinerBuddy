package com.jake.minerbuddy.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jake.minerbuddy.screens.GameScreen;
import com.jake.minerbuddy.screens.ShopScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MinerBuddyGame extends Game {

	public GameScreen gameScreen;
	public ShopScreen shopScreen;

	//screen
	public Camera camera;
	public Viewport viewport;


	//graphics
	private AssetManager assetManager;
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;

	public BitmapFont font;


	@Override
	public void create() {
		//TODO use AssetManager when all more assets have been designed and added
		assetManager = new AssetManager();
		assetManager.load("packed_textures/packed_tiles.atlas", TextureAtlas.class);
		while (!assetManager.update()){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("loading");
		}
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();

		font.getData().setScale(5);
		font.setColor(Color.BLACK);
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);

		gameScreen = new GameScreen(this);
		shopScreen = new ShopScreen(this);
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		gameScreen.dispose();
		shopScreen.dispose();
		assetManager.dispose();

		font.dispose();
		batch.dispose();
		shapeRenderer.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}