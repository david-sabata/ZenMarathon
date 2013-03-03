package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PowerupManager {
	
	private Array<Powerup> powerups = new Array<Powerup>();
	private Texture tex;
	private EffectManager em;
	
		
	public PowerupManager(EffectManager em) {
		tex = new Texture(Gdx.files.internal("data/effects/pickup_gem_diamond_24.png"));
		this.em = em;
		
	}
	
	public void addPowerup(Vector2 pos) {
		Powerup powerup = new Powerup(tex, pos);
		powerups.add(powerup);		
	}
	
	public void collision(Array<Player> players) {
		for (int i = 0; i < powerups.size; i++) {
			Powerup pu = powerups.get(i);
			for (int j = 0; j < players.size; j++) {
				Player p = players.get(j);

				if (pu.collision(p)) {
					if (p.takePowerup(pu)) {
						em.addEffect(EffectManager.AvailableEffects.POWERUP_TAKE, pu.position.x, pu.position.y);
						SoundManager.getSound("powerup.wav").play();
						powerups.removeIndex(i);
						break;
					}
				}
			}
		}
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
