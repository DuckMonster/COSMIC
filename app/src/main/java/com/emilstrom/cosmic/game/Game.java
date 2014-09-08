package com.emilstrom.cosmic.game;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.emilstrom.cosmic.GLSurface;
import com.emilstrom.cosmic.R;
import com.emilstrom.cosmic.game.entity.Entity;
import com.emilstrom.cosmic.game.entity.Key;
import com.emilstrom.cosmic.game.entity.Player;
import com.emilstrom.cosmic.game.entity.Portal;
import com.emilstrom.cosmic.game.environment.Environment;
import com.emilstrom.cosmic.helper.Camera;
import com.emilstrom.cosmic.helper.Shader;
import com.emilstrom.cosmic.helper.Vertex2;
import com.emilstrom.cosmic.helper.Vertex3;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class Game implements GLSurface.Renderer {
	public static Game currentGame;
	public static float updateTime = -1;

	long lastTime;

	public Player player;
	Key key;
	Portal portal;

	public Environment environment;
	public Screen screen;

	public Game() {
		currentGame = this;

		startGame();
	}

	public void startGame() {
		screen = new Screen(this);
		environment = new Environment(this, R.drawable.map_1);
	}

	public void spawnPlayer(Vertex2 pos) {
		player = new Player(pos, this);
		screen.position = new Vertex2(player.position);
	}

	public void spawnKey(Vertex2 pos) {
		key = new Key(pos, this);
	}

	public void spawnPortal(Vertex2 pos) {
		portal = new Portal(pos, this);
	}

	public boolean getCollision(Entity e) {
		return getCollision(e.position, e.size);
	}
	public boolean getCollision(Vertex2 pos, Vertex2 size) {
		return environment.getCollision(pos, size);
	}

	public void logic() {
		calculateUpdateTime();

		screen.logic();
		environment.logic();
		player.logic();
		key.logic();
		portal.logic();
	}

	public void draw() {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		environment.draw();
		player.draw();
		key.draw();
		portal.draw();
	}

	private void calculateUpdateTime() {
		if (updateTime == -1) {
			lastTime = System.currentTimeMillis();
		}

		long newTime = System.currentTimeMillis();
		if ((newTime - lastTime) * 0.001f > 0.5f)  //Heavy lag
			updateTime = 0f;
		else
			updateTime = (newTime - lastTime) * 0.001f;

		lastTime = newTime;
	}


	////GL STUFF
	public Camera camera;

	float[] projectionMatrix = new float[16];
	public float canvasWidth = 20, canvasHeight;

	public float[] getVPMatrix() {
		float vpMatrix[] = new float[16];
		Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, camera.getViewMatrix(), 0);

		return vpMatrix;
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(1f, 1f, 1f, 1f);

		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);

		camera = new Camera(this);

		Shader.generateShaders();
	}

	public void onDrawFrame(GL10 unused) {
		logic();
		draw();
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float)width / height;
		canvasHeight = canvasWidth/ratio;

		Matrix.orthoM(projectionMatrix, 0, -canvasWidth / 2, canvasWidth / 2, -canvasHeight / 2, canvasHeight / 2, 1f, 50f);
	}
}
