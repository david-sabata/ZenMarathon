package cz.emo4d.zen.gameplay;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import cz.emo4d.zen.gameplay.Entity.Direction;
import cz.emo4d.zen.remote.RemoteControl;
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


	public void setMap(Map map) {
		this.map = map;
	}

	public void shoot(Vector2 origin, Direction dir, Player player) {
		Bullet bullet = new Bullet(tex);
		bullet.setMap(map);
		bullet.shoot(origin, dir, player.getDamage(), player);
		activeBullets.add(bullet);
	}

	private void addHitEffect(Direction dir, float posX, float posY) {
		switch (dir) {
			case N:
				em.addEffect(EffectManager.AvailableEffects.HIT_BLOOD_N, posX, posY);
				break;
			case E:
				em.addEffect(EffectManager.AvailableEffects.HIT_BLOOD_E, posX, posY);
				break;
			case NE:
				em.addEffect(EffectManager.AvailableEffects.HIT_BLOOD_NE, posX, posY);
				break;
			case NW:
				em.addEffect(EffectManager.AvailableEffects.HIT_BLOOD_NW, posX, posY);
				break;
			case S:
				em.addEffect(EffectManager.AvailableEffects.HIT_BLOOD_S, posX, posY);
				break;
			case SE:
				em.addEffect(EffectManager.AvailableEffects.HIT_BLOOD_SE, posX, posY);
				break;
			case SW:
				em.addEffect(EffectManager.AvailableEffects.HIT_BLOOD_SW, posX, posY);
				break;
			case W:
				em.addEffect(EffectManager.AvailableEffects.HIT_BLOOD_W, posX, posY);
				break;
		}
	}

	private void addDeathEffect(Direction dir, float posX, float posY) {
		switch (dir) {
			case N:
			case NW:
			case NE:
				em.addEffect(EffectManager.AvailableEffects.DEATH_BLOOD_N, posX, posY);
				break;

			case S:
			case SW:
			case SE:
				em.addEffect(EffectManager.AvailableEffects.DEATH_BLOOD_S, posX, posY);
				break;

			case W:
				em.addEffect(EffectManager.AvailableEffects.DEATH_BLOOD_W, posX, posY);
				break;
			case E:
				em.addEffect(EffectManager.AvailableEffects.DEATH_BLOOD_E, posX, posY);
				break;
		}
		
		Random rnd = new Random();
		if (rnd.nextBoolean())		
			SoundManager.getSound("squish1.waw").play();
		else
			SoundManager.getSound("squish2.waw").play();
	}

	public void collision(Array<Player> players, Array<Enemy> enemies) {
		// players
		for (int i = 0; i < activeBullets.size; i++) {
			Bullet b = activeBullets.get(i);

			for (int j = 0; j < players.size; j++) {
				Mob p = players.get(j);

				if (p.alive && p != b.shooter && b.collision(p)) {
					p.takeHit(b.strength);
					em.addEffect(EffectManager.AvailableEffects.BULLET_EXPLOSION, b.position.x, b.position.y);

					addHitEffect(b.dir, p.position.x, p.position.y);

					if (p.health <= 0) {
						if (b.shooter instanceof Player) {
							Player pShooter = (Player) b.shooter;
							pShooter.killedPlayer();
						}

						addDeathEffect(b.dir, p.position.x, p.position.y);
						p.alive = false;
					}

					activeBullets.removeIndex(i);
					break;
				}
			}
		}

		// enemies
		for (int i = 0; i < activeBullets.size; i++) {
			Bullet b = activeBullets.get(i);

			for (int j = 0; j < enemies.size; j++) {
				Mob p = enemies.get(j);

				if (p.alive && p != b.shooter && b.collision(p)) {
					p.takeHit(b.strength);
					em.addEffect(EffectManager.AvailableEffects.BULLET_EXPLOSION, b.position.x, b.position.y);

					addHitEffect(b.dir, p.position.x, p.position.y);

					if (p.health <= 0) {
						if (b.shooter instanceof Player) {
							Player pShooter = (Player) b.shooter;
							pShooter.killedEnemy();
						}

						addDeathEffect(b.dir, p.position.x, p.position.y);
						p.alive = false;
					}

					activeBullets.removeIndex(i);
					break;
				}
			}
		}


		/*if (activeBullets.get(i).collision(enemy)) {
			enemy.takeHit(activeBullets.get(i).strength);				
			em.addEffect(EffectManager.AvailableEffects.BULLET_EXPLOSION,
					activeBullets.get(i).position.x, activeBullets.get(i).position.y);
			activeBullets.removeIndex(i);
			collision = true;
		}			
		}
		return collision;*/
	}

	public void collisionWithMap(RemoteControl rc) {
		for (int i = 0; i < activeBullets.size; i++) {
			if (activeBullets.get(i).collisionWithMap()) {
				em.addEffect(EffectManager.AvailableEffects.BULLET_EXPLOSION, activeBullets.get(i).position.x, activeBullets.get(i).position.y);

				activeBullets.removeIndex(i);

				SoundManager.getSound("wall.wav").play();
				//rc.emitEvent(0, DeviceEvent.VIBRATE);
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
