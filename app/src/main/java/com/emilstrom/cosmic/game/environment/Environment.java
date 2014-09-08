package com.emilstrom.cosmic.game.environment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.emilstrom.cosmic.MainActivity;
import com.emilstrom.cosmic.game.Game;
import com.emilstrom.cosmic.helper.Vertex2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class Environment {
	Game game;
	Tile[] tileList;
	List<Gate> gateList = new ArrayList<Gate>();

	int mapWidth, mapHeight;

	public Environment(Game g, int resource) {
		game = g;
		loadEnvironment(resource);
	}

	public Vertex2 getWorldPosition(int x, int y) {
		return new Vertex2(x * Tile.SIZE, -y * Tile.SIZE);
	}

	public int getTileIndex(int x, int y) {
		int i = x + (y * mapWidth);
		if (i < 0 || i >= tileList.length) return -1;
		else return i;
	}

	public Tile getTile(int x, int y) {
		int i = getTileIndex(x, y);
		if (i == -1) return null;

		return tileList[getTileIndex(x, y)];
	}

	public void unlockDoors() {
		for(Gate g : gateList) g.locked = false;
	}

	public void loadEnvironment(int resource) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;

			Bitmap b = BitmapFactory.decodeResource(MainActivity.context.getResources(), resource, options);

			mapWidth = b.getWidth();
			mapHeight = b.getHeight();

			tileList = new Tile[mapWidth * mapHeight];

			for (int x = 0; x < mapWidth; x++)
				for (int y = 0; y < mapHeight; y++) {
					int pixel = b.getPixel(x, y);
					pixel = pixel & 0xFFFFFF;
					if (pixel == 0x000000) tileList[getTileIndex(x, y)] = new Tile(x, y, this);
					if (pixel == 0xFF0000) {
						Gate g = new Gate(x, y, this);
						tileList[getTileIndex(x, y)] = g;
						gateList.add(g);
					}
					if (pixel == 0x00FF00) game.spawnPlayer(getWorldPosition(x, y));
					if (pixel == 0xFF00FF) game.spawnKey(getWorldPosition(x, y));
					if (pixel == 0xFFFF00) game.spawnPortal(getWorldPosition(x, y));
				}
		} catch(Exception e) {
			Log.v(MainActivity.TAG, "Couldn't load map");
			Log.v(MainActivity.TAG, e.toString());
		}
	}

	public boolean getCollision(Vertex2 pos, Vertex2 size) {
		int tilex = (int)(pos.x / Tile.SIZE),
				tiley = (int)(-pos.y / Tile.SIZE);

		for(int x=tilex - 2; x<=tilex + 2; x++)
			for(int y=tiley - 2; y<=tiley + 2; y++) {
				Tile t = getTile(x, y);

				if (t != null && t.getCollision(pos, size)) return true;
			}

		return false;
	}

	public void logic() {
		//for(Tile t : tileList) if (t != null) t.logic();
	}

	public void draw() {
		float screenw = game.canvasWidth, screenh = game.canvasHeight;
		int tileFitWidth = (int)Math.ceil(screenw / Tile.SIZE) + 1,
				tileFitHeight = (int)Math.ceil(screenh / Tile.SIZE) + 1;

		Vertex2 screenPos = game.screen.getPosition();

		int tilex = (int)(screenPos.x / Tile.SIZE),
				tiley = (int)(-screenPos.y / Tile.SIZE);

		for(int x=tilex - tileFitWidth/2; x<=tilex + tileFitWidth/2 + 1; x++)
			for(int y=tiley - tileFitHeight/2; y<=tiley + tileFitHeight/2 + 1; y++) {
				Tile t = getTile(x, y);

				if (t != null) t.draw();
			}
	}
}
