package com.emilstrom.cosmic.game;

import com.emilstrom.cosmic.helper.Vertex2;
import com.emilstrom.math.ExtraMath;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class Screen {
	Game game;
	public Vertex2 position = new Vertex2();

	public Screen(Game g) {
		game = g;
	}

	public Vertex2 getPosition() {
		return new Vertex2(position);
	}

	public void logic() {
		if (game.player != null) {
			float heightRatio = game.canvasHeight / game.canvasWidth;

			Vertex2 targetPosition = new Vertex2(game.player.position),
					movementFactor = new Vertex2(game.player.velocity.times(0.8f));

			targetPosition.x += movementFactor.x;
			targetPosition.y += movementFactor.y * heightRatio;

			Vertex2 dif = targetPosition.minus(position);

			position.x += ExtraMath.minabs(dif.x * 2f * Game.updateTime, dif.x);
			position.y += ExtraMath.minabs(dif.y * 2f * Game.updateTime / heightRatio, dif.y);
		}
	}
}
