package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Player extends Entity {

	public float MAX_VELOCITY = 8f;
	public float DAMPING = 0.87f;
	
	enum State {
		Standing, Walking, Shooting
	}	
	
	//public Vector2 position = new Vector2();
	//public Vector2 velocity = new Vector2();
	public State state = State.Walking;	
	public Direction currentDir = Direction.S;
	
	public Effect effect;
	
	public Player(Vector2 pos, float width, float height) {		
		super();
		position = pos;

		this.effect = new Effect(new Texture(Gdx.files.internal("data/effects/player_sheet.png")), 6, 8, 0.1f, 1);
		WIDTH = 1 / 32f * (effect.width - 3);
		HEIGHT = 1 / 32f * (effect.height - 15);

		effect.update(0, true); // 0 = Direction.S 
	}

	public void move(Vector2 dir) {
		float dirAngle = dir.angle();

		if (dirAngle > 337.5f) {
			currentDir = Direction.E;
		} else if (dirAngle > 292.5f) {
			currentDir = Direction.SE;
		} else if (dirAngle > 247.5f) {
			currentDir = Direction.S;
		} else if (dirAngle > 202.5f) {
			currentDir = Direction.SW;
		} else if (dirAngle > 157.5f) {
			currentDir = Direction.W;
		} else if (dirAngle > 112.5f) {
			currentDir = Direction.NW;
		} else if (dirAngle > 67.5f) {
			currentDir = Direction.N;
		} else if (dirAngle > 22.5f) {
			currentDir = Direction.NE;
		} else
			currentDir = Direction.E;

		velocity.set(dir.nor().mul(MAX_VELOCITY));
		state = Player.State.Walking;
	}

	public void update(float deltaTime, TiledMap map) {

		// clamp the velocity to the maximum
		if (Math.abs(velocity.x) > MAX_VELOCITY) {
			velocity.x = Math.signum(velocity.x) * MAX_VELOCITY;
		}
		if (Math.abs(velocity.y) > MAX_VELOCITY) {
			velocity.y = Math.signum(velocity.y) * MAX_VELOCITY;
		}

		// clamp the velocity to 0 if it's < 1, and set the state to standing
		if (Math.abs(velocity.x) < 1 && Math.abs(velocity.y) < 1) {
			velocity.set(0, 0);
			state = State.Standing;
		}

		// multiply by delta time so we know how far we go
		// in this frame
		velocity.mul(deltaTime);

		switch (state) {
			case Standing:
				break;
			case Walking: {
				switch (currentDir) {
					case S:
						effect.update(0, true);
						break;
					case SW:
						effect.update(1, true);
						break;
					case W:
						effect.update(2, true);
						break;
					case NW:
						effect.update(3, true);
						break;
					case N:
						effect.update(4, true);
						break;
					case NE:
						effect.update(5, true);
						break;
					case E:
						effect.update(6, true);
						break;
					case SE:
						effect.update(7, true);
						break;
				}
				break;
			}
			case Shooting:
				//frame = jump.getKeyFrame(koala.stateTime);
				//effect.update(2, true);
				break;
		}

		// perform collision detection & response, on each axis, separately
		// if the koala is moving right, check the tiles to the right of it's
		// right bounding box edge, otherwise check the ones to the left
		Rectangle playerRect = rectPool.obtain();
		playerRect.set(this.position.x, this.position.y, this.WIDTH, this.HEIGHT);	

		int startX, startY, endX, endY;
		if (this.velocity.x > 0) {
			startX = endX = (int) (this.position.x + this.WIDTH + this.velocity.x);
		} else {
			startX = endX = (int) (this.position.x + this.velocity.x);
		}
		startY = (int) (this.position.y);
		endY = (int) (this.position.y + this.HEIGHT);
		getTiles(startX, startY, endX, endY, tiles, map);
		playerRect.x += this.velocity.x;
		for (Rectangle tile : tiles) {
			if (playerRect.overlaps(tile)) {
				this.velocity.x = 0;
				break;
			}
		}
		playerRect.x = this.position.x;

		// if the koala is moving upwards, check the tiles to the top of it's
		// top bounding box edge, otherwise check the ones to the bottom
		if (this.velocity.y > 0) {
			startY = endY = (int) (this.position.y + this.HEIGHT + this.velocity.y);
		} else {
			startY = endY = (int) (this.position.y + this.velocity.y);
		}
		startX = (int) (this.position.x);
		endX = (int) (this.position.x + this.WIDTH);
		getTiles(startX, startY, endX, endY, tiles, map);
		playerRect.y += this.velocity.y;
		for (Rectangle tile : tiles) {
			if (playerRect.overlaps(tile)) {
				this.velocity.y = 0;
				break;
			}
		}
		rectPool.free(playerRect);

		// unscale the velocity by the inverse delta time and set
		// the latest position
		this.position.add(this.velocity);
		this.velocity.mul(1 / deltaTime);

		// Apply damping to the velocity so we don't
		// walk infinitely once a key was pressed
		this.velocity.mul(this.DAMPING);
	}

	public void render(SpriteBatch spriteBatch) {
		effect.render(spriteBatch, position.x, position.y, 1 / 32f * (effect.width), 1 / 32f * (effect.height));
	}
}
