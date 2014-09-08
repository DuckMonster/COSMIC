package com.emilstrom.cosmic.game.entity;

import com.emilstrom.cosmic.game.Game;
import com.emilstrom.cosmic.helper.Vertex2;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class Actor extends Entity {
	public static final float GRAVITY = 20f, FRICTION = 1.4f;

	public Vertex2 velocity = new Vertex2();

	public Actor(Game g) {
		super(g);
	}

	public void applyForce(Vertex2 force) {
		velocity.add(force);
	}

	public void logic() {
		super.logic();

		velocity.x -= velocity.x * FRICTION * Game.updateTime;
		velocity.y -= velocity.y * FRICTION * Game.updateTime;

		velocity.y -= (GRAVITY * Game.updateTime);

		if (game.getCollision(position.plus(new Vertex2(velocity.x, 0f).times(Game.updateTime)), size)) {
			if (Math.abs(velocity.x) > 10f)
				velocity.x *= -0.3f;
			else
				velocity.x = 0f;
		}

		if (game.getCollision(position.plus(new Vertex2(0f, velocity.y).times(Game.updateTime)), size)) {
			if (Math.abs(velocity.y) > 10f)
				velocity.y *= -0.3f;
			else
				velocity.y = 0f;
		}

		position.add(velocity.times(Game.updateTime));
	}
}
