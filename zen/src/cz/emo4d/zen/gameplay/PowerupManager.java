package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PowerupManager {
	
	private Array<Powerup> powerups = new Array<Powerup>();
	private Texture tex;
	
		
	public PowerupManager() {
		tex = new Texture(Gdx.files.internal("data/effects/avatar.png"));
		
	}
	
	public void addPowerup(Vector2 pos) {
		Powerup powerup = new Powerup(tex, pos);
		powerups.add(powerup);		
	}
	
	public void collision(Array<Player> players) {
		
		
	}

	public void update(float deltaTime) {
		for (Powerup p : powerups) {
			p.update(deltaTime);			
		}		
	}
	
	
	public void render(SpriteBatch spriteBatch) {
		for (Powerup p : powerups) {
			p.render(spriteBatch);			
		}
	}

}
