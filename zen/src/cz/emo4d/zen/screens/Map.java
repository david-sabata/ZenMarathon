package cz.emo4d.zen.screens;

import java.util.HashMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Map {

	public final OrthogonalTiledMapRenderer renderer;

	public final TiledMap map;

	private HashMap<String, Vector2> entryPoints = new HashMap<String, Vector2>();

	// rozdeleni vrstev pro ucely kresleni
	private int[] tilesBelowPlayer;
	private int[] tilesAbovePlayer;


	public final int width;
	public final int height;



	public Map(String mapName) {
		// load the map, set the unit scale to 1/32 (1 unit == 32 pixels)
		map = new TmxMapLoader().load("data/maps/" + mapName + ".tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);

		width = (Integer) map.getProperties().get("width");
		height = (Integer) map.getProperties().get("height");


		prepareTiles();
	}


	/**
	 * Roztridi vrstvy mapy tak, abychom mohli renderovat vse
	 * POD hracem a oddelene vse NAD hracem
	 * 
	 * Navic resi opruz s Array<Integer> vs int[]
	 */
	private void prepareTiles() {
		Array<Integer> tilesAbove = new Array<Integer>();
		Array<Integer> tilesBelow = new Array<Integer>();

		int i = 0;
		for (MapLayer l : map.getLayers()) {
			if (l.getName().contains("overlay")) {
				tilesAbove.add(i);
			} else {
				tilesBelow.add(i);
			}

			i++;
		}

		tilesBelowPlayer = new int[tilesBelow.size];
		for (int k = 0; k < tilesBelow.size; k++) {
			tilesBelowPlayer[k] = tilesBelow.get(k);
		}

		tilesAbovePlayer = new int[tilesAbove.size];
		for (int k = 0; k < tilesAbove.size; k++) {
			tilesAbovePlayer[k] = tilesAbove.get(k);
		}
	}



	/**
	 * Vraci pozicovy vektor v mape na policku X|Y
	 * kde 0|0 je v level hornim rohu
	 * 
	 * (metoda zajistuje predevsim prevraceni Y smeru)
	 */
	public Vector2 getCoord(int x, int y) {
		return new Vector2(x, height - y);
	}


	public void renderOverlay(OrthographicCamera camera) {
		renderer.setView(camera);

		renderer.render(tilesAbovePlayer);
	}

	public void renderUnderlay(OrthographicCamera camera) {
		renderer.setView(camera);

		renderer.render(tilesBelowPlayer);
	}


}
