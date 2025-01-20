package com.jake.minerbuddy.map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.jake.minerbuddy.main.MinerBuddyGame;
import com.jake.minerbuddy.screens.GameScreen;
import com.jake.minerbuddy.tiles.Ore;
import com.jake.minerbuddy.tiles.Tile;

import java.util.HashMap;

public class MapHandler {

    // sizes
    private float width;
    private float height;
    private float tileSize;

    // scroll data
    private float xTrans;
    private float yTrans;
    private boolean scrolling;
    private final float[] scrollVector = new float[2];

    private boolean smoothScrolling;
    private Vector2 smoothScrollVector;

    // scaling data
    private boolean inScale;
    private float scale;
    private final float maxScale = 3.5f;

    private boolean focusScrolling;
    private final Vector2 cumulativeScroll = new Vector2();
    private FocusScrollDirection focusScrollDirection = null;
    private Vector2 focusedTileIndex = null;

    private Tile purchasingTile;
    private Ore purchasingOre;

    // rows by cols
    private final int ROWS = 7;
    private final int COLS = 7;
    private final Tile[][] map;

    private final Array<Vector2> damagedTileIndexes;

    private static final HashMap<String, Sprite> tileImages = new HashMap<>();
    private final HashMap<String, Sprite> oreImages = new HashMap<>();

    public MapHandler(MinerBuddyGame game){

        final TextureAtlas tileAtlas = game.getAssetManager().get("packed_textures/packed_tiles.atlas", TextureAtlas.class);

        // fill tileImages
        Array<TextureAtlas.AtlasRegion> regions = tileAtlas.getRegions();

        for (TextureAtlas.AtlasRegion region: regions){
            Sprite sprite = tileAtlas.createSprite(region.name);
            tileImages.put(region.name, sprite);
            //this increases the quality of the image but may decrease performance slightly
            sprite.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        map = new Tile[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                map[i][j] = new Tile(Tile.DIRT);
            }
        }

        map[6][6] = new Tile(Tile.STONE);
        map[6][0] = new Tile(Tile.STONE);
        damagedTileIndexes = new Array<>();

        this.scale = 2.5f;
    }

    public void draw(SpriteBatch batch, int mode){

        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = COLS - 1; j >= 0; j--) {

                Tile tile = map[i][j];

                // get position of tiles
                // this is the bottom center point of the tile
                Vector2 p = cartToIso(tileSize*j, tileSize*i);

                float yVal = p.y;
                float xVal = p.x - tileSize * scale;

                // only draw if onscreen
                if (!(yVal > this.height || yVal + tileSize * scale < 0 || xVal > this.width
                        || xVal + tileSize * scale * 2 < 0)) {

                    if (mode == GameScreen.PURCHASE && focusedTileIndex != null){
                        if (focusedTileIndex.x == i && focusedTileIndex.y == j){
                            if (this.purchasingTile != null) tile = purchasingTile;
                            // TODO need to add ore stuff
                        }
                    }

                    // draw mined state of tile
                    if (tile.isMined()) {
                        batch.draw(tileImages.get(tile.getName(1)), xVal, yVal, 2 * scale * tileSize, scale * tileSize);
                    } else {

                        batch.draw(tileImages.get(tile.getName(0)), xVal, yVal, 2 * scale * tileSize, scale * tileSize);

                        // if ore is grown on tile
                        if (tile.isOreGrown() || (j == 5 && i > 4)) {
                            // draw ore
                            batch.draw(tileImages.get("diamondtile"), xVal, yVal, 2 * scale * tileSize, scale * tileSize);
                        }

                        // add damaged tiles to array for alpha
                        if (tile.isMiningLocked() || tile.isDamaged()) {
                            // save position for shapeRenderer
                            damagedTileIndexes.add(new Vector2(i, j));

                        }
                    }
                }

                if(i == 0){
                    // draw right side of tile
                    float xVal2 = xVal + tileSize * scale;
                    float yVal2 = yVal - tileSize * scale / 2;

                    batch.draw(tileImages.get(tile.getName(2)), xVal2, yVal2, scale *  tileSize, scale * tileSize);

                }

                if(j == 0){
                    yVal -= tileSize * scale / 2;
                    batch.draw(tileImages.get(tile.getName(3)), xVal, yVal, scale *  tileSize, scale * tileSize);
                }
            }
        }
    }


    public void draw(ShapeRenderer renderer){

        // draw the alpha for breaking effects
        renderer.setColor(0.3f, 0f, 0f, 0.2f);

        for (Vector2 pos: damagedTileIndexes) {
            // float ratio = 1 - map[(int)pos.x][(int)pos.y].getHealthRatio();
            // renderer.setColor(0.2f, 0f, 0f, 0.7f * ratio);

            Vector2 p = cartToIso(tileSize*pos.y , tileSize*pos.x);
            // draw two triangles to make tile shape
            renderer.triangle(p.x, p.y, p.x + tileSize * scale, p.y + tileSize * scale / 2, p.x, p.y + tileSize * scale);
            renderer.triangle(p.x, p.y, p.x - tileSize * scale, p.y + tileSize * scale / 2, p.x, p.y + tileSize * scale);
        }
        damagedTileIndexes.clear();

        // if (focusedTileIndex != null){
        //     renderer.setColor(0.3f, 0.5f, 0.8f, 1f);
        //     Vector2 p = cartToIso(tileSize*focusedTileIndex.y , tileSize*focusedTileIndex.x);
        //     renderer.line(p.x, p.y, p.x + tileSize * scale, p.y + tileSize * scale / 2);
        //     renderer.line(p.x, p.y, p.x - tileSize * scale, p.y + tileSize * scale / 2);
        //     renderer.line(p.x, p.y + tileSize * scale, p.x + tileSize * scale, p.y + tileSize * scale / 2);
        //     renderer.line(p.x, p.y + tileSize * scale, p.x - tileSize * scale, p.y + tileSize * scale / 2);
        // }

    }


    public static void drawSingleTile(SpriteBatch batch, Tile tile, float x, float y, float width, float height){

        // draw top
        batch.draw(tileImages.get(tile.getName(0)), x, y, width, height);
        // draw sides
        batch.draw(tileImages.get(tile.getName(2)), x + width/2, y - height/2, width/2, height);
        batch.draw(tileImages.get(tile.getName(3)), x, y - height/2, width/2, height);
    }


    public void update(){

        long currentTime = System.currentTimeMillis();

        for (Tile[] tiles : map) {
            for (int j = 0; j < COLS; j++) tiles[j].update(currentTime);
        }

        // scroll momentum
        if(scrolling && !Gdx.input.isTouched()){

            float scrollSpeedMult = 0.8f;
            scrollVector[0] *= scrollSpeedMult;
            scrollVector[1] *= scrollSpeedMult;
            if (Math.hypot(scrollVector[0], scrollVector[1]) < 0.1 / (scale*scale)) scrolling = false;
            else{
                xTrans += scrollVector[0];

                if(checkTranslation()) scrollVector[0] = 0;

                yTrans += scrollVector[1];

                if (checkTranslation()) scrollVector[1] = 0;
            }
        }else if(smoothScrolling){
            // this sucks but i dont want to fix it yet
            Vector2 tempDist;

            if (smoothScrollVector.len() < 2){
                tempDist = smoothScrollVector.cpy();
            }else{
                tempDist = smoothScrollVector.cpy().scl(0.1f);
            }
            xTrans += tempDist.x;
            yTrans += tempDist.y;
            smoothScrollVector.sub(tempDist);
            if (smoothScrollVector.len2() < 4){
                smoothScrolling = false;
            }
        }
    }

    private Vector2 tileIndexAtPoint(float x, float y){
        // returns vector2(x = row, y = col)

        Vector2 p = isoToCart(x, y);

        int sCol = (int) (p.x / tileSize);
        int sRow = (int) (p.y / tileSize);

        if(sRow < 0 || sRow >= ROWS || sCol < 0 || sCol >= COLS){
            return null;
        }
        return new Vector2(sRow, sCol);
    }

    public void focusOnTileFromPoint(float x, float y){

        Vector2 index = tileIndexAtPoint(x, y);

        if(index == null) return;

        focusOnTile((int) index.x, (int) index.y);

    }

    public void focusOnTile(final int row, final int col){

        // zoom to tile on row and col
        // for now instant zoom and move but could add smooth transition
        scale = maxScale;

        // set translation to 0 to make calculation easier
        xTrans = 0;
        yTrans = 0;

        // get position of tile: center
        Vector2 p = cartToIso(tileSize*col + tileSize/2, tileSize*row + tileSize/2);

        // translate to match center of both
        xTrans = this.width/2 - p.x;
        yTrans = this.height/2 - p.y;

        checkTranslation();
    }

    private void smoothFocusOnTile(final int row, final int col){
        // zoom to tile on row and col
        // for now instant zoom and move but could add smooth transition
        scale = maxScale;

        float currentXTrans = xTrans;
        float currentYTrans = yTrans;
        // set translation to 0 to make calculation easier
        xTrans = 0;
        yTrans = 0;

        // get position of tile: center
        Vector2 p = cartToIso(tileSize*col + tileSize/2, tileSize*row + tileSize/2);

        xTrans = currentXTrans;
        yTrans = currentYTrans;
        // translate to match center of both
        smoothScrolling = true;
        smoothScrollVector = new Vector2(this.width/2 - p.x - currentXTrans, this.height/2 - p.y - currentYTrans);

    }

    public Tile selectTile(float x, float y){

        Vector2 index = tileIndexAtPoint(x, y);

        if(index == null) return null;

        return map[(int)index.x][(int)index.y];
    }


    public void onScrollFocus(float xDist, float yDist){

        cumulativeScroll.add(xDist, yDist);
        xDist = cumulativeScroll.x;
        yDist = cumulativeScroll.y;

        // use positive yDist to determine locked direction to be used
        if (!focusScrolling){
            focusScrolling = true;
            // this will decide the direction that the scroll will be locked into

            float absYDist = Math.abs(yDist);
            float flippedXDist = xDist;
            if (yDist < 0){
                flippedXDist *= -1;
            }

            if (cumulativeScroll.len2() < 100){
                focusScrolling = false;
                return;
            }

            // vertical
            if (absYDist > Math.abs(4*xDist)){
                focusScrollDirection = FocusScrollDirection.VERTICAL;
            }

            // horizontal
            else if (absYDist < Math.abs(xDist/8)){
                focusScrollDirection = FocusScrollDirection.HORIZONTAL;
            }

            // up right / down left
            else if(flippedXDist/4 < absYDist && absYDist < 2*flippedXDist){
                focusScrollDirection = FocusScrollDirection.UP_RIGHT;
            }

            // up left / down right
            else if(-flippedXDist/4 < absYDist && absYDist < 2*-flippedXDist){
                focusScrollDirection = FocusScrollDirection.UP_LEFT;
            }

            // distance is too small or doesn't fit close enough to any direction
            else {
                focusScrolling = false;
                return;
            }

        }

        // move in given direction
        float dist = Math.max(Math.abs(xDist), Math.abs(yDist));
        switch (focusScrollDirection){
            // TODO need to change the boundary mechanics
            case VERTICAL:
                onScroll(0, yDist);
                if (tileIndexAtPoint(width/2, height/2) == null){
                    onScroll(0, -yDist);
                }
                break;
            case HORIZONTAL:
                onScroll(xDist, 0);
                if (tileIndexAtPoint(width/2, height/2) == null){
                    onScroll(-xDist, 0);
                }
                break;
            case UP_RIGHT:
                if (xDist < 0 && yDist < 0) dist *= -1;
                onScroll(dist, dist/2);
                if (tileIndexAtPoint(width/2, height/2) == null){
                    onScroll(-dist, -dist/2);
                }
                break;
            case UP_LEFT:
                if (xDist < 0) dist *= -1;
                onScroll(dist, -dist/2);
                if (tileIndexAtPoint(width/2, height/2) == null){
                    onScroll(-dist, dist/2);
                }
                break;
            default:
                return;
        }

        focusedTileIndex = tileIndexAtPoint(width/2, height/2);

        cumulativeScroll.scl(0);

        // check if center of the screen has entered new tile

        // if close enough to center of new tile
        Vector2 cartPos = isoToCart(width/2, height/2);
        cartPos.x %= tileSize;
        cartPos.y %= tileSize;

        // 5% margin for center alignment
        if(cartPos.dst(tileSize/2, tileSize/2) < 0.025 * tileSize){
            // set scrolling to false to allow the user to chose new direction
            focusScrolling = false;
            focusOnTileFromPoint(width/2, height/2);
        }
    }

    public void endFocusScroll(){
        if (focusScrolling){
            // smooth scroll to closest tile TODO implement the smooth scroll
            scrollVector[0] = 0;
            scrollVector[1] = 0;
            if (focusedTileIndex != null){
                smoothFocusOnTile((int)focusedTileIndex.x, (int)focusedTileIndex.y);
//                gameScreen.setSelectedTile(map[(int)focusedTileIndex.x][(int)focusedTileIndex.y]);
            }
//            focusOnTileFromPoint(width/2, height/2);
            focusScrolling = false;
            cumulativeScroll.scl(0);
            focusScrollDirection = null;
//            focusedTileIndex = null;
        }
    }


    public void onScroll(float xDist, float yDist) {

        if(!inScale){
            scrolling = true;

            xTrans += xDist;
            yTrans += yDist;
            scrollVector[0] = xDist;
            scrollVector[1] = yDist;
        }

        checkTranslation();
    }

    public void onScale(float focusX, float focusY, float scaleFactor){
        inScale = true;

        // gets the location of the focus Vector2 on the 2d world
        Vector2 p = isoToCart(focusX, focusY);

        // zooms as required
        scale *= scaleFactor;
        scale = scale < 1 ? 1 : scale;
        scale = (float) (int) (scale * 100) / 100; // rounds to 2dp
        if (scale > maxScale) scale = maxScale;

        // get translated focus Vector2 position
        p = cartToIso(p.x, p.y);

        // moves translation to maintain focus Vector2
        xTrans += (focusX - p.x);
        yTrans += (focusY - p.y);

        // call this to adjust translation which ensures that the right amount of the screen is showing
        onScroll(0, 0);
    }

    public void onScaleEnd() {
        inScale = false;
        onScroll(0, 0);
    }

    private Vector2 isoToCart(float x, float y) {
        // takes screen position x and y
        // converts to flat 2d position
        Vector2 p2 = new Vector2();
        x -= xTrans + scale * tileSize * ROWS;
        y -= yTrans;
        x/= scale;
        y/= scale;

        p2.x = (int) (y + (x / 2));
        p2.y = (int) (y - (x / 2));

        return p2;
    }

    private Vector2 cartToIso(float x, float y) {
        // takes x and y position from flat 2d world view with no translations
        // converts to screen position in isometric view
        Vector2 p2 = new Vector2();

        p2.x = (int) ((x-y) * scale + xTrans + (scale * tileSize * ROWS));
        p2.y = (int) (((y + x) / 2) * scale + yTrans);

        return p2;
    }

    private boolean checkTranslation(){

        boolean adjusted = false;

        // max x value = width of every tile plus half tile padding on each side
        float maxX = getIsoWidth(true) + xTrans;

        if (maxX < this.width){
            xTrans = this.width - getIsoWidth(true);
            adjusted = true;
        }else if(xTrans > scale * tileSize * 2){
            xTrans = scale * tileSize * 2;
            adjusted = true;
        }

        float maxY = getIsoHeight(true) + yTrans;
        if (maxY < this.height){
            yTrans = this.height - getIsoHeight(true);
            adjusted = true;
        }else if(yTrans > scale * tileSize){
            yTrans = scale * tileSize;
            adjusted = true;
        }
        return adjusted;
    }

    public void setScreenDimensions(int width, int height){
        this.width = width;
        this.height = height;

        // resize tiles
        this.tileSize = this.width / (2 * COLS);

        // center camera
        xTrans = (width - getIsoWidth(false))/2;
        yTrans = (height - getIsoHeight(false))/2;


    }

    private float getIsoWidth(boolean padded){
        // ROWS == COLS so it doesn't matter which one is used
        if(padded) return scale * tileSize * (ROWS + 1) * 2;
        return scale * tileSize * ROWS * 2;
    }

    private float getIsoHeight(boolean padded){
        if(padded) return scale * tileSize * (COLS + 1);
        return scale * tileSize * COLS;
    }

    public void dispose(){

    }

    public Tile[][] getMap(){
        return this.map;
    }

    public float getHeight(){
        return height;
    }

    public void enterPurchaseTileMode(Tile tile){
        this.purchasingTile = tile;
    }

    public void enterPurchaseOreMode(Ore ore){
        this.purchasingOre = ore;
    }

    public void exitPurchasingMode(){
        this.purchasingTile = null;
        this.purchasingOre = null;

        this.focusedTileIndex = null;
        this.focusScrolling = false;
    }


    public boolean shouldShowBuyButton(){
        return focusedTileIndex != null && !focusScrolling;
    }

    public void buyTile(Tile purchasingTileCopy){
        map[(int)focusedTileIndex.x][(int)focusedTileIndex.y] = purchasingTileCopy;
    }

}
