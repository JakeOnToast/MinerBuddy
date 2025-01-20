package com.jake.minerbuddy.player;

import com.jake.minerbuddy.tiles.Ore;
import com.jake.minerbuddy.tiles.Tile;

import java.util.ArrayList;

public class Inventory {


    // needs to store bought ores, blocks, and workers etc

    private ArrayList<Tile> tiles;
    private ArrayList<Ore> ores;

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<Ore> getOres() {
        return ores;
    }
}
