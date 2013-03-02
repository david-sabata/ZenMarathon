package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity {
	
	
	boolean alive = false;
	TextureRegion activeRegion;
	public static final float MAX_VELOCITY = 10.f;
	public static final int COLS = 8;
	public static final int ROWS = 1;
	
	private TextureRegion tr[][];
	private Vector2 tmp = new Vector2();
		
	public Bullet(Texture tex) {
		super();
		
		tr = TextureRegion.split(tex, tex.getWidth() / COLS, tex.getHeight() / ROWS);
	}

	public void update(float deltaTime) {
		if (alive) {
			tmp.set(velocity);
			tmp.mul(deltaTime);			
			position.add(tmp);			
		}		
	}	
	
	public void shoot(Vector2 origin, Direction dir) {
		alive = true;
		this.position.set(origin);		
				
		switch (dir) {
		case S:
			this.velocity.set(0.f, -MAX_VELOCITY);
			activeRegion = tr[0][0];			
			break;
		case SW:
			this.velocity.set(-MAX_VELOCITY / 2f, -MAX_VELOCITY / 2f);
			activeRegion = tr[0][1];
			break;				
		case W:
			this.velocity.set(-MAX_VELOCITY, 0.f);
			activeRegion = tr[0][2];
			break;
		case NW:
			this.velocity.set(-MAX_VELOCITY / 2f, MAX_VELOCITY / 2f);
			activeRegion = tr[0][3];
			break;
		case N:
			this.velocity.set(0.f, MAX_VELOCITY);
			activeRegion = tr[0][4];
			break;
		case NE:
			this.velocity.set(MAX_VELOCITY / 2f, MAX_VELOCITY / 2f);
			activeRegion = tr[0][5];
			break;
		case E:
			this.velocity.set(MAX_VELOCITY, 0.f);
			activeRegion = tr[0][6];
			break;
		case SE:
			this.velocity.set(MAX_VELOCITY / 2f, -MAX_VELOCITY / 2f);
			activeRegion = tr[0][7];
			break;		
		}		
	}
	
	public void render(SpriteBatch spriteBatch) {
		if (alive) {
			spriteBatch.draw(activeRegion, position.x, position.y,
					(1 / 32.f) * activeRegion.getRegionWidth(), (1 / 32.f) * activeRegion.getRegionHeight());			
		}		
	}
	

}
