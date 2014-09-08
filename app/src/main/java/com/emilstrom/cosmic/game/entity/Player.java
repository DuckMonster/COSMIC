package com.emilstrom.cosmic.game.entity;

import com.emilstrom.cosmic.game.Game;
import com.emilstrom.cosmic.game.PlayerController;
import com.emilstrom.cosmic.helper.Vertex2;
import com.emilstrom.math.ExtraMath;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class Player extends Actor {
	public static final float ACCELERATION = 28f;

	PlayerController controller;
	Vertex2 directionVertex = new Vertex2(0f, 1f);

	public Player(Vertex2 pos, Game g) {
		super(g);

		position = pos;
		controller = new PlayerController(this);
	}

	public void logic() {
		super.logic();

		controller.logic();
		Vertex2 rotationInput = controller.getRotationInput();
		if (rotationInput != null) {
			directionVertex = new Vertex2(rotationInput);
		}

		if (controller.getAccelerationInput()) {
			Vertex2 force = directionVertex.times(ACCELERATION * Game.updateTime);
			force.y += GRAVITY * Game.updateTime;
			applyForce(force);
		}

		if (rotation != directionVertex.getDirection()) {
			float targetDir = directionVertex.getDirection() + 180;
			float myDir = rotation + 180;

			double closestDif;
			closestDif = ExtraMath.minabs(targetDir - myDir, targetDir + 360 - myDir);
			closestDif = ExtraMath.minabs(closestDif, targetDir - 360 - myDir);

			myDir += closestDif * 10f * Game.updateTime;

			myDir = (float)ExtraMath.mod(myDir, 360);
			rotation = myDir - 180;
		}
	}

	public void draw() {
		super.draw();
		controller.draw();
	}
}