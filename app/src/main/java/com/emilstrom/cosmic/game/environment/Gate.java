package com.emilstrom.cosmic.game.environment;

import com.emilstrom.cosmic.helper.Color;
import com.emilstrom.cosmic.helper.Vertex2;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class Gate extends Tile {
	boolean locked = true;

	public Gate(int x, int y, Environment e) {
		super(x, y, e);
	}

	public boolean getCollision(Vertex2 pos, Vertex2 size) {
		if (locked) return super.getCollision(pos, size);
		else return false;
	}

	public void draw() {
		if (!locked) return;

		mesh.reset();

		mesh.translate(getScreenPosition());
		mesh.scale(SIZE);

		mesh.setColor(new Color(0.7f, 0f, 0f, 1f));
		mesh.draw(tileTexture);
	}
}
