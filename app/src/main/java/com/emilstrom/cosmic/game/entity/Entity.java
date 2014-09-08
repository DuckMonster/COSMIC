package com.emilstrom.cosmic.game.entity;

import com.emilstrom.cosmic.R;
import com.emilstrom.cosmic.game.Game;
import com.emilstrom.cosmic.helper.Color;
import com.emilstrom.cosmic.helper.Mesh;
import com.emilstrom.cosmic.helper.Texture;
import com.emilstrom.cosmic.helper.Vertex2;

/**
 * Created by DuckMonster on 2014-09-05.
 */
public class Entity {
	public Game game;

	public Vertex2 position,
		size = new Vertex2(1f, 1f);
	public float rotation;

	public Mesh mesh;
	public Texture texture;

	public Entity(Game g) {
		game = g;

		position = new Vertex2();

		mesh = new Mesh(g);
		texture = new Texture(R.drawable.ship);

		rotation = 90;
	}

	public boolean collidesWith(Entity e) {
		return collidesWith(e.position, e.size);
	}
	public boolean collidesWith(Vertex2 pos, Vertex2 size) {
		return
				pos.x + size.x/2 >= position.x - size.x/2 &&
						pos.x - size.x/2 < position.x + size.x/2 &&
						pos.y + size.y/2 >= position.y - size.y/2 &&
						pos.y - size.y/2 < position.y + size.y/2;
	}

	public Vertex2 getScreenPosition() {
		return new Vertex2(position.minus(game.screen.getPosition()));
	}

	public void logic() {
	}

	public void draw() {
		mesh.reset();

		mesh.translate(getScreenPosition());
		mesh.scale(size);
		mesh.rotate(rotation, 0f, 0f, 1f);

		mesh.setColor(Color.BLACK);
		mesh.draw(texture);
	}
}
