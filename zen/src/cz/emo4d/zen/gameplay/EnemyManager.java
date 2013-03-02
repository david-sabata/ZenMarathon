package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class EnemyManager {
	
	public EnemyManager() {
				
	}

	private Array<Enemy> activeEnemies = new Array<Enemy>();
	
	public Array<Enemy> getEnemies() {
		return activeEnemies;
	}
	
	
	public void addEnemy(Enemy enemy) {
		activeEnemies.add(enemy);		
		
	}
	
	public void update(float deltaTime) {
		for (Enemy e : activeEnemies) {
			e.update(deltaTime);			
		}
	}
	
	public void render(SpriteBatch spriteBatch) {		
		for (Enemy e : activeEnemies) {
			if (e.alive)
				e.render(spriteBatch);			
		}
		
	}
}
