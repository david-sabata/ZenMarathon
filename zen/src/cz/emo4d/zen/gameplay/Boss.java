package cz.emo4d.zen.gameplay;

import sun.security.pkcs11.Secmod.DbMode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Boss extends Enemy {

	private Player defaultPlayer;
	private BulletManager bulletManager;
	
	float shootFilter = -1;
	
	public Boss(Vector2 position, Player p, BulletManager bm) {
		super(position);
		defaultPlayer = p;
		bulletManager = bm;
		factor = 0.5f;
	}
	
	@Override
	public void update(float deltaTime) {

		Vector2 velocityVector = new Vector2();
		velocityVector.set(defaultPlayer.position);
		velocityVector.sub(position);
		float distance = velocityVector.len();
		//Gdx.app.log("VECTOR", Float.toString(distance));
		if (distance > 2) move(velocityVector); 
		//move(velocityVector.nor());
		
		if ((distance < 4) && (shootFilter < 0)) {
			bulletManager.shoot(position, currentDir, this);
			shootFilter = 0.4f;
		}
		
		if (shootFilter >= 0) shootFilter -= deltaTime;
		
		
				
		super.update(deltaTime);
	}
	
	public int getDamage() {
		return 10;
	}
	
	@Override
	public int getMaxHealth() {
		return 600;
	}

}
