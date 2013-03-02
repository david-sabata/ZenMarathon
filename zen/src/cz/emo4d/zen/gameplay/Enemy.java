package cz.emo4d.zen.gameplay;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Mob {

	Random random = new Random();

	public Enemy(Vector2 position) {
		super();
		this.position = position;

		this.effect = new Effect(new Texture(Gdx.files.internal("data/effects/player_sheet.png")), 6, 8, 0.1f, 1);
		WIDTH = 1 / 32f * (effect.width - 3);
		HEIGHT = 1 / 32f * (effect.height - 15);

		effect.update(0, true); // 0 = Direction.S

		MAX_VELOCITY = 20f;
	}

	public void update(float deltaTime) {

		move((new Vector2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f)));

		super.update(deltaTime);
	}
}