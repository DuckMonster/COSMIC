package com.emilstrom.cosmic.game.environment;

import com.emilstrom.cosmic.R;
import com.emilstrom.cosmic.game.Game;
import com.emilstrom.cosmic.game.entity.Entity;
import com.emilstrom.cosmic.helper.Color;
import com.emilstrom.cosmic.helper.Mesh;
import com.emilstrom.cosmic.helper.Texture;
import com.emilstrom.cosmic.helper.Vertex2;

/**
 * Created by DuckMonster on 2014-09-05.
 */
class Tile {
	public static final float SIZE = 2f;
	static Texture tileTexture = new Texture(R.drawable.wall);

	Environment environment;
	public int x, y;
	Mesh mesh = new Mesh(Game.currentGame);

	public Tile(int x, int y, Environment e) {
		environment = e;
		this.x = x;
		this.y = y;
	}

	public Vertex2 getPosition() {
		return new Vertex2(x, -y).times(SIZE);
	}

	public Vertex2 getScreenPosition() {
		return getPosition().minus(environment.game.screen.getPosition());
	}

	public boolean isOnScreen() {
		Vertex2 pos = getScreenPosition();
		return pos.x > -environment.game.canvasWidth/2 &&
				pos.x < environment.game.canvasWidth/2 &&
				pos.y > -environment.game.canvasHeight/2 &&
				pos.y < environment.game.canvasHeight/2;
	}

	public boolean getCollision(Entity e) {
		return getCollision(e.position, e.size);
	}
	public boolean getCollision(Vertex2 pos, Vertex2 size) {
		Vertex2 p = getPosition();

		return
				pos.x + size.x/2 >= p.x - SIZE/2 &&
				pos.x - size.x/2 < p.x + SIZE/2 &&
				pos.y + size.y/2 >= p.y - SIZE/2 &&
				pos.y - size.y/2 < p.y + SIZE/2;
	}

	public void logic() {
	}

	public void draw() {
		//if (!isOnScreen()) return;

		mesh.reset();

		mesh.translate(getScreenPosition());
		mesh.scale(SIZE);

		mesh.setColor(new Color(0.1f, 0.1f, 0.1f, 1f));
		mesh.draw(tileTexture);
	}
}