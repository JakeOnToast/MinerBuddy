package com.jake.minerbuddy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.jake.minerbuddy.main.MinerBuddyGame;
import com.jake.minerbuddy.map.MapHandler;
import com.jake.minerbuddy.tiles.Ore;
import com.jake.minerbuddy.tiles.Tile;
import com.jake.minerbuddy.widgets.BuildOverlay;

import static com.jake.minerbuddy.widgets.WidgetFunctions.cleanPrice;


/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen, GestureDetector.GestureListener {

	
	//MODES
	// used to mine the blocks
	public static final int MINE = 0;
	// used to place new blocks and change ores/workers
	public static final int PURCHASE = 1;
	// used to select tiles then upgrade ores ect
	public static final int SELECT = 2;

	private int mode;

	// setting to prevent zooming and scrolling on screen
	private boolean scrollLock = false;


	// build mode variables
	private Tile selectedTile;


	private final MapHandler mapHandler;

	private final BuildOverlay buildOverlay;

	private final MinerBuddyGame game;

	private final Sound mineSound;

	private final InputMultiplexer inputMultiplexer;




	// UI buttons and info
	private final Stage uiStage;
	private final TextButton shopButton;
	private final TextButton.TextButtonStyle shopButtonStyle;
	private final Skin buttonSkin;
	private final TextureAtlas buttonAtlas;


	public GameScreen(MinerBuddyGame game){

		this.game = game;

		mapHandler = new MapHandler(game);



		mineSound = Gdx.audio.newSound(Gdx.files.internal("sounds/dirt_mine.mp3"));

		GestureDetector gd = new GestureDetector(this);


		uiStage = new Stage(game.viewport, game.batch);

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(gd);
		inputMultiplexer.addProcessor(uiStage);


		buttonAtlas = new TextureAtlas("packed_textures/buttons.atlas");
		buttonSkin = new Skin(buttonAtlas);
		shopButtonStyle = new TextButton.TextButtonStyle();
		shopButtonStyle.font = game.font;
		shopButtonStyle.up = buttonSkin.getDrawable("green_button_up");
		shopButtonStyle.down = buttonSkin.getDrawable("green_button_down");
		// up down checked
		shopButton = new TextButton("Shop", shopButtonStyle);
		shopButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(game.shopScreen);
//				mineSound.play();
//				mapHandler.focusOnTileFromPoint(Gdx.input.getX(), Gdx.input.getY());
			}
		});


		shopButton.setPosition(100, 100);
		uiStage.addActor(shopButton);

		TextButton selectModeButton = new TextButton("Select", shopButtonStyle);
		selectModeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				if (mode == MINE) {
					mode = SELECT;
					mapHandler.focusOnTileFromPoint(Gdx.input.getX(), -Gdx.input.getY());
				}
				else {
					mapHandler.endFocusScroll();
					mode = MINE;
				}
			}
		});

		selectModeButton.setPosition(400, 100);
		uiStage.addActor(selectModeButton);

		buildOverlay = new BuildOverlay(this, buttonSkin);

	}

	@Override
	public void render(float delta) {
		// Draw your screen here. "delta" is the time since last render in seconds.
//		Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



		game.camera.update();

		mapHandler.update();

		game.batch.begin();

		mapHandler.draw(game.batch, mode);

		game.font.draw(game.batch, "Jake did this", 50, game.viewport.getScreenHeight());
		// debug data
		game.font.draw(game.batch, Float.toString(Gdx.graphics.getFramesPerSecond()), 100, 500);

		// draw mode dependent stuff here
		switch (mode){
			case MINE:
				if (Gdx.input.isTouched()){
					mapHandler.onScroll(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
					Tile tile = mapHandler.selectTile(Gdx.input.getX(), flipY(Gdx.input.getY()));

					//TODO remove this and replace with real code
					if (tile != null) {
						if (!tile.isMined()){
							tile.damage();
							if (tile.isMined()){
								mineSound.play();
							}
						}
					}
				}
				//draw pickaxe
				break;
			case PURCHASE:
				// draw build ui
				if (Gdx.input.isTouched()){
					mapHandler.onScrollFocus(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
				}else mapHandler.endFocusScroll();

				if (mapHandler.shouldShowBuyButton()) buildOverlay.draw(game.batch);

				break;
			case SELECT:
				if (Gdx.input.isTouched()){
					mapHandler.onScrollFocus(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
				}else mapHandler.endFocusScroll();
				break;
		}



		game.batch.end();

		// draw shapes
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		game.shapeRenderer.setProjectionMatrix(game.camera.combined);
		game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		mapHandler.draw(game.shapeRenderer);

		// can be used to mark center of screen
//		game.shapeRenderer.circle(game.viewport.getScreenWidth()/2, game.viewport.getScreenHeight()/2, 20);

		game.shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);


		uiStage.draw();


	}

	private float flipY(float y){
		return mapHandler.getHeight() - y;
	}

	private void setMode(int mode){
		this.mode = mode;
		mapHandler.exitPurchasingMode();
		try{
			buildOverlay.deactivate();
		}catch (Exception e){
			e.printStackTrace();
		}

		switch (mode){
			case MINE:
				mapHandler.endFocusScroll();
				break;
			case PURCHASE:
				buildOverlay.activate();
				break;
			case SELECT:
				break;
		}
	}

	@Override
	public void resize(int width, int height) {
		// Resize your screen here. The parameters represent the new window size.
		game.viewport.update(width, height, true);
		game.batch.setProjectionMatrix(game.camera.combined);
		game.shapeRenderer.setProjectionMatrix(game.camera.combined);
		mapHandler.setScreenDimensions(width, height);
	}

	@Override
	public void pause() {
		// Invoked when your application is paused.
	}

	@Override
	public void resume() {
		// Invoked when your application is resumed after pause.
	}

	@Override
	public void show() {
		// Prepare your screen here.
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void hide() {
		// This method is called when another screen replaces this one.
	}


	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return true;
	}

	// pinch data
	float lastScaleFactor = 1;

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {


		// find midpoint to zoom into
		float focusX = (initialPointer1.x + initialPointer2.x)/2;
		float focusY = (initialPointer1.y + initialPointer2.y)/2;
		float scaleFactor = pointer1.dst(pointer2)/initialPointer1.dst(initialPointer2);

		// if distance has changed enough scale screen
		if (Math.abs(1 - scaleFactor / lastScaleFactor) > 0.01){
			mapHandler.onScale(focusX, flipY(focusY), scaleFactor / lastScaleFactor);
			lastScaleFactor = scaleFactor;
		}


		return true;
	}

	@Override
	public void pinchStop() {
		mapHandler.onScaleEnd();
		lastScaleFactor = 1;
	}


	@Override
	public void dispose() {
		// Destroy screen's assets here.
		System.out.println("disposing game screen");
		mapHandler.dispose();
		mineSound.dispose();
		buttonAtlas.dispose();
		uiStage.dispose();

	}

	public MinerBuddyGame getGame() {
		return game;
	}

	public Tile getSelectedTile(){
		return this.selectedTile;
	}

	public void setSelectedTile(Tile tile){
		this.selectedTile = tile;
	}

	public MapHandler getMapHandler(){
		return this.mapHandler;
	}

	public InputMultiplexer getInputMultiplexer(){
		return this.inputMultiplexer;
	}

	public void enterPurchaseMode(JsonValue data){
		Tile tile = new Tile(data);
		setMode(PURCHASE);
		mapHandler.enterPurchaseTileMode(tile);
		TextButton buyButton = buildOverlay.getBuyButton();
		buyButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// purchase tile here
				buyTile(new Tile(tile));
			}
		});
		buyButton.setText(tile.getName() + " " + cleanPrice(data.getInt("price")));
		buyButton.setPosition(game.viewport.getScreenWidth()/2f - buyButton.getWidth()/2, game.viewport.getScreenHeight()/5f);

	}

	public void enterPurchaseMode(Ore ore){
		setMode(PURCHASE);
		mapHandler.enterPurchaseOreMode(ore);
	}

	public void buyTile(Tile tile){
		mapHandler.buyTile(tile);
		// TODO take money away from player
	}
}