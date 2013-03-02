package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.math.Vector2;

public class Entity {
	
	public enum Direction {
		S, SW, W, NW, N, NE, E, SE
	}
	
	public Vector2 position;
	public Vector2 velocity;
	
		
	public Entity() {
		position = new Vector2();
		velocity = new Vector2();	
		
		
	}	

}
