package com.jake.minerbuddy.tiles;


import com.badlogic.gdx.utils.JsonValue;

public class Ore {

    public static final int WOOD = 0;
    public static final int ROCK = 0;
    public static final int COAL = 0;
    public static final int IRON = 1;
    public static final int GOLD = 2;

    private int id;
    private String name;
    private String description;

    private int maxHealth;
    private int health;
    private boolean mined;


    private float buyPrice;
    // sell price will increase as ore levels up
    private float baseValue;

    private float minedTime;
    private float growthTime;

    private int xpGiven;

    public Ore(){
        // this will be used for blank ores
        this.mined = false;
        this.id = -1;
        this.health = 0;

    }

    public Ore(JsonValue data){

        this.id = data.getInt("id");
        this.name = data.getString("name");
        this.description = data.getString("description");

    }

    public void update(long currentTime){

        // regen ore
        if (mined){
            if (currentTime > minedTime + growthTime){
                health = maxHealth;
                mined = false;
            }
        }

    }

    //TODO remove this
    public void setId(int id){
        this.id = id;
    }


    public boolean isMined(){return this.mined;}

    public int getId(){ return this.id; }

    public String getName(){
        return this.name;
    }


}
