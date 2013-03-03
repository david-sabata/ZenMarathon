package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class EffectManager {

	public enum AvailableEffects {
		BULLET_EXPLOSION, POWERUP_TAKE,
		HIT_BLOOD_E,
		HIT_BLOOD_N,
		HIT_BLOOD_NE,
		HIT_BLOOD_NW,
		HIT_BLOOD_S,
		HIT_BLOOD_SE,
		HIT_BLOOD_SW,
		HIT_BLOOD_W,
		DEATH_BLOOD_N,
		DEATH_BLOOD_S,
		DEATH_BLOOD_W,
		DEATH_BLOOD_E,
	}

	private Array<Effect> activeEffects = new Array<Effect>();
	private Texture explosionTexture, powerupTexture;
	private Texture bloodTexE, bloodTexN, bloodTexNE, bloodTexNW, bloodTexS, bloodTexSE, bloodTexSW, bloodTexW;
	private Texture deathTexN, deathTexS, deathTexW, deathTexE;

	public EffectManager() {
		explosionTexture = new Texture(Gdx.files.internal("data/effects/fx_bombsplosion_big_32.png"));
		powerupTexture = new Texture(Gdx.files.internal("data/effects/effect_shine_big_13.png"));
		
		deathTexN = new Texture(Gdx.files.internal("data/effects/blood_death_n.png"));
		deathTexS = new Texture(Gdx.files.internal("data/effects/blood_death_s.png"));
		deathTexW = new Texture(Gdx.files.internal("data/effects/blood_death_w.png"));
		deathTexE = new Texture(Gdx.files.internal("data/effects/blood_death_e.png"));
		
		bloodTexE  = new Texture(Gdx.files.internal("data/effects/blood_hit_e.png"));
		bloodTexN  = new Texture(Gdx.files.internal("data/effects/blood_hit_n.png"));
		bloodTexNE = new Texture(Gdx.files.internal("data/effects/blood_hit_ne.png"));
		bloodTexNW = new Texture(Gdx.files.internal("data/effects/blood_hit_nw.png"));
		bloodTexS  = new Texture(Gdx.files.internal("data/effects/blood_hit_s.png"));
		bloodTexSE = new Texture(Gdx.files.internal("data/effects/blood_hit_se.png"));
		bloodTexSW = new Texture(Gdx.files.internal("data/effects/blood_hit_sw.png"));
		bloodTexW  = new Texture(Gdx.files.internal("data/effects/blood_hit_w.png"));
	}

	public void addEffect(AvailableEffects effect, float posX, float posY) {

		switch (effect) {
			case BULLET_EXPLOSION:
				activeEffects.add(new Effect(explosionTexture, 8, 1, 0.05f, 0, posX, posY));
				break;
			case POWERUP_TAKE:
				activeEffects.add(new Effect(powerupTexture, 7, 1, 0.1f, 0, posX, posY));
				break;
			
			case DEATH_BLOOD_N:
				activeEffects.add(new Effect(deathTexN, 8, 1, 0.075f, 0, posX - (1 / 32f * 15), posY - (1 / 32f * 12)));
				break;
			case DEATH_BLOOD_S:
				activeEffects.add(new Effect(deathTexS, 8, 1, 0.075f, 0, posX - (1 / 32f * 15), posY - (1 / 32f * 12)));
				break;
			case DEATH_BLOOD_W:
				activeEffects.add(new Effect(deathTexW, 8, 1, 0.075f, 0, posX - (1 / 32f * 15), posY - (1 / 32f * 12)));
				break;
			case DEATH_BLOOD_E:
				activeEffects.add(new Effect(deathTexE, 8, 1, 0.075f, 0, posX - (1 / 32f * 15), posY - (1 / 32f * 12)));
				break;
			
			case HIT_BLOOD_E:
				activeEffects.add(new Effect(bloodTexE, 10, 1, 0.075f, 0, posX, posY));
				break;
			case HIT_BLOOD_N:
				activeEffects.add(new Effect(bloodTexN, 10, 1, 0.075f, 0, posX, posY));
				break;
			case HIT_BLOOD_NE:
				activeEffects.add(new Effect(bloodTexNE, 10, 1, 0.075f, 0, posX, posY));
				break;
			case HIT_BLOOD_NW:
				activeEffects.add(new Effect(bloodTexNW, 10, 1, 0.075f, 0, posX, posY));
				break;
			case HIT_BLOOD_S:
				activeEffects.add(new Effect(bloodTexS, 10, 1, 0.075f, 0, posX, posY));
				break;
			case HIT_BLOOD_SE:
				activeEffects.add(new Effect(bloodTexSE, 10, 1, 0.075f, 0, posX, posY));
				break;
			case HIT_BLOOD_SW:
				activeEffects.add(new Effect(bloodTexSW, 10, 1, 0.075f, 0, posX, posY));
				break;
			case HIT_BLOOD_W:
				activeEffects.add(new Effect(bloodTexW, 10, 1, 0.075f, 0, posX, posY));
				break;
		}
	}

	public void update(float deltaTime) {
		for (int i = 0; i < activeEffects.size; i++) {
			activeEffects.get(i).update(0, false);
			if (activeEffects.get(i).isAnimationFinished(0)) {
				activeEffects.removeIndex(i);
			}
		}
		/*for (Effect e : activeEffects) {	
			e.update(0, false);
		}*/
	}

	public void render(SpriteBatch spriteBatch) {
		for (Effect e : activeEffects) {
			e.render(spriteBatch, e.posX, e.posY, 1 / 32f * e.width, 1 / 32f * e.height);
		}
	}
}
