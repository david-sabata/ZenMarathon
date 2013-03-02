package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import cz.emo4d.zen.gameplay.Entity.Direction;
import cz.emo4d.zen.screens.Map;

public class BulletManager {
	
	public BulletManager(Map map, Texture tex, EffectManager em) {
		super();
		this.map = map;
		this.tex = tex;
		this.em = em;
	}

	private Array<Bullet> activeBullets = new Array<Bullet>();
	private Map map;
	private Texture tex;
	private EffectManager em;
	
	public void shoot(Vector2 origin, Direction dir) {
		Bullet bullet = new Bullet(tex);
		bullet.setMap(map);
		bullet.shoot(origin, dir);
		activeBullets.add(bullet);
	}
	
	public int collision(Entity enemy) {
		int hits = 0;
		for (int i = 0; i < activeBullets.size; i++) {					
			if (activeBullets.get(i).collision(enemy)) {
				hits++;
				em.addEffect(EffectManager.AvailableEffects.BULLET_EXPLOSION,
						activeBullets.get(i).position.x, activeBullets.get(i).position.y);
				activeBullets.removeIndex(i);
			}			
		}	
		return hits;
	}
	
	public void collisionWithMap() {
		for (int i = 0; i < activeBullets.size; i++) {
			if (activeBullets.get(i).collisionWithMap()) {
				em.addEffect(EffectManager.AvailableEffects.BULLET_EXPLOSION,
						activeBullets.get(i).position.x, activeBullets.get(i).position.y);
				
				activeBullets.removeIndex(i);
			}			
		}
	}
	
	public void update(float deltaTime) {
		for (Bullet p : activeBullets) {
			p.update(deltaTime);
		}
	}
	
	
	public void render(SpriteBatch spriteBatch) {
		for (Bullet p : activeBullets) {
			p.render(spriteBatch);
		}		
	}
}
