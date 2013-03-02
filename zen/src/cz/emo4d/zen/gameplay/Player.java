package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class Player extends Mob {

	public Player(Vector2 pos, float width, float height) {
		super();
		position = pos;

		this.effect = new Effect(new Texture(Gdx.files.internal("data/effects/player_sheet.png")), 6, 8, 0.1f, 1, 0, 0);
		WIDTH = 1 / 32f * (effect.width - 3);
		HEIGHT = 1 / 32f * (effect.height - 15);

		effect.update(0, true); // 0 = Direction.S 
	}

}
