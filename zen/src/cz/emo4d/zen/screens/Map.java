package cz.emo4d.zen.screens;

import java.util.HashMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class Map {

	public final OrthogonalTiledMapRenderer renderer;

	public final TiledMap map;

	private HashMap<String, Vector2> entryPoints = new HashMap<String, Vector2>();


	public final int width;
	public final int height;



	public Map(String mapName) {
		// load the map, set the unit scale to 1/32 (1 unit == 32 pixels)
		map = new TmxMapLoader().load("data/maps/" + mapName + ".tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);

		width = (Integer) map.getProperties().get("width");
		height = (Integer) map.getProperties().get("height");
	}




	public void render(OrthographicCamera camera) {
		renderer.setView(camera);
		renderer.render();
	}


}
