package com.emilstrom.cosmic.helper;

import android.opengl.Matrix;

import com.emilstrom.cosmic.game.Game;

/**
 * Created by Emil on 2014-07-31.
 */
public class Camera {
	Game game;
	public Vertex3 position;

	public Camera(Game g) {
		game = g;
		position = new Vertex3(0f, 0f, 3f);
	}

	public float[] getViewMatrix() {
		float matrix[] = new float[16];
		Matrix.setLookAtM(matrix, 0, position.x, position.y, position.z, position.x, position.y, 0f, 0f, 1f, 0f);

		return matrix;
	}

	public void logic() {
	}

	public void draw() {
	}
}
