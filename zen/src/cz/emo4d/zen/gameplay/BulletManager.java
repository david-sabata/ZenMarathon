package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import cz.emo4d.zen.gameplay.Entity.Direction;
import cz.emo4d.zen.screens.Map;

public class BulletManager {
	
	public BulletManager(Map map, Texture tex) {
		super();
		this.map = map;
		this.tex = tex;
		
		explode = new Effect(new Texture(Gdx.files.internal("data/effects/fx_bombsplosion_big_32.png")), 8, 1, 0.05f, 1);
	}

	private Array<Bullet> activeBullets = new Array<Bullet>();
	private Map map;
	private Texture tex;
	
	Effect explode;
	boolean exploding = false;
	Vector2 explodePos = new Vector2();
	
	public void shoot(Vector2 origin, Direction dir) {
		Bullet bullet = new Bullet(tex);
		bullet.setMap(map);
		bullet.shoot(origin, dir);
		activeBullets.add(bullet);
	}
	
	public void collision() {
		for (int i = 0; i < activeBullets.size; i++) {
			if (activeBullets.get(i).collision() != null) {
				explodePos.set(activeBullets.get(i).position);
				activeBullets.removeIndex(i);
				exploding = true;
				explode.reset(0);
			}				
		}	
	}
	
	public void update(float deltaTime) {
		for (Bullet p : activeBullets) {
			p.update(deltaTime);
		}
		
		if (exploding)
		{
			explode.update(0, false);
			if (explode.isAnimationFinished(0)) {
				exploding = false;
			}
		}
	}
	
	
	public void render(SpriteBatch spriteBatch) {
		for (Bullet p : activeBullets) {
			p.render(spriteBatch);
		}		
		
		if (exploding)
			explode.render(spriteBatch, explodePos.x, explodePos.y, 1/32f * explode.width, 1/32f * explode.height);		
	}
	
	

}
