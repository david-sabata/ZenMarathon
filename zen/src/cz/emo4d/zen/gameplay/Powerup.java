package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Powerup extends Entity {
	
	Effect effect;
	
	public Powerup(Texture tex, Vector2 pos) {
		effect = new Effect(tex, 3, 1, 0.1f, 0, 0, 0);
		position.set(pos);		
	}
	
	public void update(float deltaTime) {
		effect.update(0, true);	
	}
	
	public void render(SpriteBatch spriteBatch) {
		effect.render(spriteBatch, position.x, position.y, 1 / 32f * (effect.width), 1 / 32f * (effect.height));
	}
}
