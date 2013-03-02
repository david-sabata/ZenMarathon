package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Entity {
	
	public enum Direction {
		S, SW, W, NW, N, NE, E, SE
	}
	
	protected static Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	protected static Array<Rectangle> tiles = new Array<Rectangle>();
	
	
	
	public Vector2 position;
	public Vector2 velocity;
	public float WIDTH;
	public float HEIGHT;
	
		
	public Entity() {
		position = new Vector2();
		velocity = new Vector2();		
	}
	
	public Entity collision(TiledMap map) {
		// perform collision detection & response, on each axis, separately
		// if the koala is moving right, check the tiles to the right of it's
		// right bounding box edge, otherwise check the ones to the left
		
		Rectangle playerRect = rectPool.obtain();
		playerRect.set(this.position.x, this.position.y, this.WIDTH, this.HEIGHT);	

		/*int startX, startY, endX, endY;
		if (this.velocity.x > 0) {
			startX = endX = (int) (this.position.x + this.WIDTH + this.velocity.x);
		} else {
			startX = endX = (int) (this.position.x + this.velocity.x);
		}
		startY = (int) (this.position.y);
		endY = (int) (this.position.y + this.HEIGHT);*/
		getTiles((int)this.position.x, (int)this.position.y, (int)(this.position.x + this.WIDTH), (int)(this.position.y + this.HEIGHT), tiles, map);
		//playerRect.x += this.velocity.x;
		
		for (Rectangle tile : tiles) {
			if (playerRect.overlaps(tile)) {
				//this.velocity.x = 0;
				rectPool.free(playerRect);
				return new Entity();
			}
		}
		
	/*	playerRect.x = this.position.x;

		// if the koala is moving upwards, check the tiles to the top of it's
		// top bounding box edge, otherwise check the ones to the bottom
		if (this.velocity.y > 0) {
			startY = endY = (int) (this.position.y + this.HEIGHT + this.velocity.y);
		} else {
			startY = endY = (int) (this.position.y + this.velocity.y);
		}
		startX = (int) (this.position.x);
		endX = (int) (this.position.x + this.WIDTH);
		getTiles(startX, startY, endX, endY, tiles, map);
		playerRect.y += this.velocity.y;
		for (Rectangle tile : tiles) {
			if (playerRect.overlaps(tile)) {
				this.velocity.y = 0;
				rectPool.free(playerRect);
				return new Entity();
			}
		}		*/
		rectPool.free(playerRect);

		return null;
	}
	
	protected static void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles, TiledMap map) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().getLayer(1);
		rectPool.freeAll(tiles);
		tiles.clear();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					Rectangle rect = rectPool.obtain();
					rect.set(x, y, 1, 1);
					tiles.add(rect);
				}
			}
		}
	}

}
