package com.emilstrom.cosmic.helper;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.emilstrom.cosmic.game.Game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Emil on 2014-07-30.
 */
public class Mesh {
	static Texture blankTexture = Texture.blankTexture;

	public Color color;

	//HANDLES

	private int a_position,
		a_texPosition,
		u_mvp,
		u_texture,
		u_color;

	//

	Game game;

	int nmbrOfVertices = 0;//, textureDataSize = 0;
	float modelMatrix[] = new float[16];
	private FloatBuffer vertexBuffer,
		textureBuffer;

	public Mesh(Game g) {
		game = g;
		setColor(Color.WHITE);
		generate();
	}

	public void generate() {
		setVertices(
				new Vertex3(-0.5f, 0.5f, 0f),
				new Vertex3(0.5f, 0.5f, 0f),
				new Vertex3(-0.5f, -0.5f, 0f),
				new Vertex3(0.5f, -0.5f, 0f)
		);

		setTextureData(
				new Vertex2(0f, 0f),
				new Vertex2(1f, 0f),
				new Vertex2(0f, 1f),
				new Vertex2(1f, 1f)
		);
	}

	public void setVertices(float[] v) {
		if (v.length / 3 != nmbrOfVertices) {
			nmbrOfVertices = v.length / 3;

			vertexBuffer = ByteBuffer.allocateDirect(v.length * 4)
					.order(ByteOrder.nativeOrder())
					.asFloatBuffer();
		}

		vertexBuffer.put(v);
		vertexBuffer.position(0);
	}

	public void setVertices(Vertex3... v) {
		float coordList[] = new float[v.length * 3];
		for(int i=0; i<v.length; i++) {
			coordList[i*3] = v[i].x;
			coordList[i*3+1] = v[i].y;
			coordList[i*3+2] = v[i].z;
		}

		setVertices(coordList);
	}

	public void setVertices(Vertex2... v) {
		float coordList[] = new float[v.length * 3];
		for(int i=0; i<v.length; i++) {
			coordList[i*3] = v[i].x;
			coordList[i*3+1] = v[i].y;
			coordList[i*3+2] = 0;
		}

		setVertices(coordList);
	}

	public void setTextureData(float[] v) {
		textureBuffer = ByteBuffer.allocateDirect(v.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();

		textureBuffer.put(v);
		textureBuffer.position(0);
	}

	public void setTextureData(Vertex2... v) {
		float coordList[] = new float[v.length * 2];
		for(int i=0; i<v.length; i++) {
			coordList[i*2] = v[i].x;
			coordList[i*2+1] = v[i].y;
		}

		setTextureData(coordList);
	}

	public void prepareHandles() {
		int prog = Shader.program;

		a_position = GLES20.glGetAttribLocation(prog, "a_position");
		a_texPosition = GLES20.glGetAttribLocation(prog, "a_texPosition");
		u_mvp = GLES20.glGetUniformLocation(prog, "u_mvp");
		u_texture = GLES20.glGetUniformLocation(prog, "u_texture");
		u_color = GLES20.glGetUniformLocation(prog, "u_color");

		GLES20.glEnableVertexAttribArray(a_position);
		GLES20.glEnableVertexAttribArray(a_texPosition);
		GLES20.glVertexAttribPointer(a_position, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glVertexAttribPointer(a_texPosition, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);

		GLES20.glUniformMatrix4fv(u_mvp, 1, false, getMVPMatrix(), 0);
		GLES20.glUniform1i(u_texture, 0);
		GLES20.glUniform4f(u_color, color.r, color.g, color.b, color.a);
	}

	public void unloadHandles() {
		GLES20.glDisableVertexAttribArray(a_position);
	}

	public void draw() { draw(Texture.blankTexture); }
	public void draw(Texture t) {
		GLES20.glUseProgram(Shader.program);

		prepareHandles();

		if (t != null) {
			t.uploadToGL();
		}

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, nmbrOfVertices);

		unloadHandles();
	}

	public void drawLines(float lineWidth) {
		GLES20.glUseProgram(Shader.program);

		prepareHandles();

		Texture.blankTexture.uploadToGL();

		GLES20.glLineWidth(lineWidth);
		GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, nmbrOfVertices);

		unloadHandles();
	}

	//Mesh manipulation
	public float[] getMVPMatrix() {
		float mvp[] = new float[16];
		Matrix.multiplyMM(mvp, 0, game.getVPMatrix(), 0, modelMatrix, 0);
		return mvp;
	}

	public void reset() {
		Matrix.setIdentityM(modelMatrix, 0);
	}

	public void translate(Vertex2 v) { translate(v.x, v.y, 0f); }
	public void translate(Vertex3 v) { translate(v.x, v.y, v.z); }
	public void translate(float x, float y, float z) {
		Matrix.translateM(modelMatrix, 0, x, y, z);
	}

	public void rotate(float a, float x, float y, float z) {
		Matrix.rotateM(modelMatrix, 0, a, x, y, z);
	}

	public void scale(float s) { scale(s, s, s); }
	public void scale(Vertex2 s) { scale(s.x, s.y, 1f); }
	public void scale(Vertex3 s) { scale(s.x, s.y, s.z); }
	public void scale(float x, float y, float z) {
		Matrix.scaleM(modelMatrix, 0, x, y, z);
	}

	public void setColor(float r, float g, float b, float a) { setColor(new Color(r, g, b, a)); }
	public void setColor(Color c) {
		color = new Color(c);
	}
}