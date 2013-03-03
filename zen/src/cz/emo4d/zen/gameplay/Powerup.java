package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Powerup extends Entity {
	
	Effect effect;
	
	public Powerup(Texture tex, Vector2 pos) {
		effect = new Effect(tex, 14, 1, 0.1f, 0, 0, 0);
		effect.setAnimPlayMode(0, Animation.LOOP_PINGPONG);
		position.set(pos);
		
		WIDTH = 1 / 32f * (effect.width - 3);
		HEIGHT = 1 / 32f * (effect.height - 15);
	}
	
	public void update(float deltaTime) {
		effect.update(0, true);	
	}
	
	public void render(SpriteBatch spriteBatch) {
		effect.render(spriteBatch, position.x, position.y, 1 / 32f * (effect.width), 1 / 32f * (effect.height));
	}
}
