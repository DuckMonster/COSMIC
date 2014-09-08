package com.emilstrom.cosmic.game.entity;

import com.emilstrom.cosmic.R;
import com.emilstrom.cosmic.game.Game;
import com.emilstrom.cosmic.helper.Color;
import com.emilstrom.cosmic.helper.Texture;
import com.emilstrom.cosmic.helper.Vertex2;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class Portal extends Entity {
	public Portal(Vertex2 pos, Game g) {
		super(g);
		position = pos;

		texture = new Texture(R.drawable.portal);
		size = new Vertex2(2f, 2f);
	}

	public void logic() {
		rotation += 80 * Game.updateTime;
		if (collidesWith(game.player)) game.startGame();
	}

	public void draw() {
		mesh.reset();

		mesh.translate(getScreenPosition());
		mesh.scale(size);
		mesh.rotate(rotation, 0f, 0f, 1f);

		mesh.setColor(new Color(73, 23, 92));
		mesh.draw(texture);
	}
}
