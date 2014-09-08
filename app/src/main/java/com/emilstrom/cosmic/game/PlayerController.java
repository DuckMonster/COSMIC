package com.emilstrom.cosmic.game;

import com.emilstrom.cosmic.game.entity.Player;
import com.emilstrom.cosmic.helper.Color;
import com.emilstrom.cosmic.helper.Input;
import com.emilstrom.cosmic.helper.InputHelper;
import com.emilstrom.cosmic.helper.Mesh;
import com.emilstrom.cosmic.helper.Vertex2;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class PlayerController {
	Player player;
	int rotateFinger = -1, accelerateFinger = -1;
	Vertex2 rotateOrigin, currentRotation;

	Mesh mesh;

	Input oldInput;

	public PlayerController(Player p) {
		player = p;
		mesh = new Mesh(p.game);
	}

	public Vertex2 getRotationInput() {
		return currentRotation;
	}

	public boolean getAccelerationInput() {
		return accelerateFinger != -1;
	}

	public void logic() {
		Input in = InputHelper.getInput();
		if (oldInput == null) oldInput = in;

		if (rotateFinger == -1 || accelerateFinger == -1) {
			for(int i=0; i<Input.NMBR_OF_FINGERS; i++) {
				if (in.isPressed(i)) {
					if (in.getPosition(i).x > 0f && rotateFinger == -1) {
						rotateFinger = i;
						rotateOrigin = in.getPosition(i);
					}

					else if (in.getPosition(i).x < 0f && accelerateFinger == -1) {
						accelerateFinger = i;
					}
				}
			}
		}

		if (rotateFinger != -1) {
			if (!in.isPressed(rotateFinger)) {
				rotateFinger = -1;
				currentRotation = null;
			} else {
				currentRotation = Vertex2.getDirectionVertex(rotateOrigin, in.getPosition(rotateFinger));
			}
		}

		if (accelerateFinger != -1) {
			if (!in.isPressed(accelerateFinger)) {
				accelerateFinger = -1;
			}
		}

		oldInput = in;
	}

	public void draw() {
		if (rotateFinger != -1) {
			mesh.setVertices(
					rotateOrigin,
					oldInput.getPosition(rotateFinger)
			);

			mesh.reset();
			mesh.setColor(new Color(0.8f, 0.8f, 0f, 1f));
			mesh.drawLines(10f);
		}
	}
}
