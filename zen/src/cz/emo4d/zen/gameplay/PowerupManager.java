package cz.emo4d.zen.gameplay;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PowerupManager {
	
	public static float NEXT_POWERUP_INTERVAL_MIN = 20f;
	public static float NEXT_POWERUP_INTERVAL_MAX = 40f;
	
	private Array<Powerup> powerups = new Array<Powerup>();
	private Texture tex;
	private EffectManager em;
	private Random random = new Random();
	private float nextPowerupTime = generateNextInterval();	
		
	public PowerupManager(EffectManager em) {
		tex = new Texture(Gdx.files.internal("data/effects/heart.png"));
		this.em = em;		
	}
	
	private float generateNextInterval() {
		return NEXT_POWERUP_INTERVAL_MIN + random.nextFloat() *
				(NEXT_POWERUP_INTERVAL_MAX - NEXT_POWERUP_INTERVAL_MIN);
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

				if (pu.alive && pu.collision(p)) {
					if (p.takePowerup(pu)) {
						em.addEffect(EffectManager.AvailableEffects.POWERUP_TAKE, pu.position.x, pu.position.y);
						SoundManager.getSound("powerup.wav").play();
						pu.alive = false;
						break;
					}
				}
			}
		}
	}

	public void update(float deltaTime) {
		for (Powerup p : powerups) {
			if (p.alive) {			
				p.update(deltaTime);
			} else {
				if (nextPowerupTime <= 0.f) {
					p.alive = true;
					nextPowerupTime = generateNextInterval();					
				} else {
					nextPowerupTime -= deltaTime;					
				}				
			}			
		}
	}	
	
	public void render(SpriteBatch spriteBatch) {
		for (Powerup p : powerups) {
			if (p.alive)
				p.render(spriteBatch);			
		}
	}
}
