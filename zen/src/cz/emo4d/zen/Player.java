package cz.emo4d.zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {
	
	enum Direction {
		S, SW, W, NW, N, NE, E, SE
	}
	
	private Vector2 position;
	private Direction currentDir = Direction.S;
	private Effect effect;
	
	public Player() {		
		position = new Vector2(50, 50);
		this.effect = new Effect(new Texture(Gdx.files.internal("data/effects/player_sheet.png")), 6, 8, 0.1f, 1);
	}

	public void move(Vector2 dir) {
		position.add(dir);
		
		update();
		
	}
	
	public void update() {
		
		/*switch (currentDir) {
		
		case S:
		
		effect.update() {
		
		
		
		}*/
		
		
		effect.update(0, true);		
	}	
	
	public void render(SpriteBatch spriteBatch) {
		effect.render(spriteBatch, position.x, position.y);		
	}
	
	

}
