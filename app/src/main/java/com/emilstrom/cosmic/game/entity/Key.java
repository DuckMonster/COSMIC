package com.emilstrom.cosmic.game.entity;

import com.emilstrom.cosmic.R;
import com.emilstrom.cosmic.game.Game;
import com.emilstrom.cosmic.helper.Color;
import com.emilstrom.cosmic.helper.Texture;
import com.emilstrom.cosmic.helper.Vertex2;
import com.emilstrom.math.ExtraMath;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class Key extends Entity {
	float floatValue;
	boolean pickedUp = false;

	public Key(Vertex2 pos, Game g) {
		super(g);

		position = pos;

		texture = new Texture(R.drawable.key);
		size = new Vertex2(1.4f, 1.4f);
	}

	public Vertex2 getScreenPosition() {
		Vertex2 pos = super.getScreenPosition();
		pos.y += Math.sin(floatValue) * 0.2f;

		return pos;
	}

	public void pickUp() {
		game.environment.unlockDoors();
		pickedUp = true;
	}

	public void logic() {
		if (pickedUp) return;

		floatValue += Game.updateTime * 2f;
		rotation += 120 * Game.updateTime;

		if (collidesWith(game.player)) pickUp();
	}

	public void draw() {
		if (pickedUp) return;

		mesh.reset();

		mesh.translate(getScreenPosition());
		mesh.scale(size);
		mesh.rotate(rotation, 0f, 0f, 1f);

		mesh.setColor(Color.RED);
		mesh.draw(texture);
	}
}
