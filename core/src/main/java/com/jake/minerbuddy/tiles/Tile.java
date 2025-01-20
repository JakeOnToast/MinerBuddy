package com.jake.minerbuddy.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
//import org.json.JSONException;
//import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

//TODO implement parcelable
public class Tile {

    public static final int DIRT = 0;
    public static final int SAND = 1;
    public static final int STONE = 2;
    public static final int TEMP = 2;

    private int id;
    private String name;
    private String description;
    private int powerRequired;
    private int maxPlantLevel;
    
    private Ore ore;

    private boolean miningLock;
    private boolean mined;
    private long minedTime;
    private long regenTime;

    private float maxHealth;
    private float health;

    // changes how fast the tile can regrow
    private int speedLevel;
    // changes how much of the ore is produced
    private int valueLevel;

    // money is dropped regardless of ore
    private int moneyGiven;
    private int xpGiven;

    // load fresh tile from json file
    public Tile(int id){

        this.id = id;
        //TODO remove this

        this.ore = new Ore();

        if (this.id == 1){
            this.ore.setId(0);
        }
        JsonReader json = new JsonReader();
        JsonValue data = json.parse(Gdx.files.internal("base_values/default_tiles.json")).get("tiles").get(id);
        this.name = data.getString("name");
        this.description = data.getString("description");
        this.powerRequired = data.getInt("powerRequired");
        this.maxHealth = this.health = data.getInt("maxHealth");
        this.regenTime = data.getInt("regenTime");
        this.xpGiven = data.getInt("xpGiven");
    }

    public Tile(JsonValue data){
        this.ore = new Ore();

        this.id = data.getInt("id");
        this.name = data.getString("name");
        this.description = data.getString("description");
        this.powerRequired = data.getInt("powerRequired");
        this.maxHealth = this.health = data.getInt("maxHealth");
        this.regenTime = data.getInt("regenTime");
        this.xpGiven = data.getInt("xpGiven");
    }

    public Tile(Tile other){
        this.ore = new Ore();

        this.id = other.id;
        this.name = other.name;
        this.description = other.description;
        this.powerRequired = other.powerRequired;
        this.maxHealth = this.health = other.maxHealth;
        this.regenTime = other.regenTime;
        this.xpGiven = other.xpGiven;
    }

    public void update(long currentTime){

        // only need to update ore if the block is full as it can only grow on full block
        if (!mined && ore.isMined()){
            ore.update(currentTime);
        }

        // regenerate ores after time
        if (mined){
            if (currentTime > minedTime + regenTime){
                health = maxHealth;
                mined = false;
                // check if ore can regen too on this frame
                if (ore.isMined()){
                    ore.update(currentTime);
                }
            }
        }

        // heal block when not being hit
        else if (!miningLock && health < maxHealth){

            health += maxHealth/300;
            if (health > maxHealth) health = maxHealth;

        }

    }


    // public void mine(Pickaxe pickaxe){
    //     if(mined){
    //         // make sound
    //         return;
    //     }
    //     if (pickaxe.getPower() < this.powerRequired){
    //         // play fail sound and give flag about power maybe
    //         return;
    //     }
    //     this.health -= pickaxe.getDamage();
    //     if (this.health <= 0){
    //         mined = true;
    //         minedTime = System.currentTimeMillis();
    //         pickaxe.addXp(this.xpGiven);
    //     }
    // }

    public void damage(){
        //TODO remove this shit
        this.health -= 6;
        if (health < 0){
            mined = true;
            minedTime = System.currentTimeMillis();
        }
    }

    // public int getOreId(){ return this.ore.getId(); }

    public boolean isOreGrown(){
        // if blank ore
        if (this.ore.getId() == -1){
            return false;
        }

        return !this.ore.isMined();
    }

    public int getId(){
        return this.id;
    }

    public String getName(int type){
        switch (type){
            case 0:
                return (this.name + "full").toLowerCase();
            case 1:
                return (this.name + "hole").toLowerCase();
            case 2:
                return (this.name + "right").toLowerCase();
            case 3:
                return (this.name + "left").toLowerCase();
        }
        return null;
    }

    public String getName(){
        return this.name;
    }

    public boolean isMined(){
        return mined;
    }

    public boolean isDamaged(){
        return (health < maxHealth);
    }

    public float getHealthRatio(){
        return health/maxHealth;
    }

    public void setMiningLock(boolean miningLock){
        this.miningLock = miningLock;
    }

    public boolean isMiningLocked(){return this.miningLock;}
}
