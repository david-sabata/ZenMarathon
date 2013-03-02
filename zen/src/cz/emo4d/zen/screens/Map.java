package cz.emo4d.zen.screens;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Map {

	public static class Position {
		public String mapName;
		public String identifier;
		public Vector2 coordinates;

		// smer kterym se nakopne hrac po pruchodu
		public Vector2 direction;

		@Override
		public String toString() {
			return "Position [mapName=" + mapName + ", identifier=" + identifier + ", coordinates=" + coordinates + ", direction=" + direction + "]";
		}
	}



	public final OrthogonalTiledMapRenderer renderer;

	public final TiledMap map;

	// vstupni body na mapu
	public final HashMap<String, Position> inPoints = new HashMap<String, Position>();
	public final HashMap<String, Position> outPoints = new HashMap<String, Position>();


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

		MapProperties mapProps = map.getProperties();
		parseProperties(mapProps);
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



	private void parseProperties(MapProperties props) {
		Iterator<String> it = props.getKeys();
		while (it.hasNext()) {
			String key = it.next();

			// vstupni body
			if (key.indexOf("in-") == 0) {
				Position pos = new Position();
				pos.identifier = key.substring(3);
				pos.coordinates = parseCoords(props.get(key, String.class));

				inPoints.put(pos.identifier, pos);

				//				Gdx.app.log("IN PT", pos.toString());
			}

			// vystupni body
			if (key.indexOf("out-") == 0) {
				Position pos = new Position();

				String tmp = key.substring(4);
				int sep = tmp.indexOf('-');

				pos.mapName = tmp.substring(0, sep);
				tmp = tmp.substring(pos.mapName.length() + 1);

				pos.identifier = tmp;
				pos.coordinates = parseCoords(props.get(key, String.class));

				outPoints.put(pos.identifier, pos);

				//				Gdx.app.log("OUT PT", pos.toString());
			}
		}

		// doplnit smery pro IN body
		it = props.getKeys();
		while (it.hasNext()) {
			String key = it.next();

			// vstupni smery
			if (key.indexOf("indir-") == 0) {
				String id = key.substring(6);

				inPoints.get(id).direction = parseVector(props.get(key, String.class));

				//				Gdx.app.log("IN DIR", inPoints.get(id).toString());
			}
		}
	}

	private Vector2 parseCoords(String s) {
		Vector2 vec = new Vector2();
		int sep = s.indexOf('x');

		vec.x = Integer.valueOf(s.substring(0, sep));
		vec.y = Integer.valueOf(s.substring(sep + 1));

		// prevest do souradneho systemu mapy - Y0 dole
		vec.y = height - vec.y;

		return vec;
	}


	private Vector2 parseVector(String s) {
		Vector2 vec = new Vector2();
		int sep = s.indexOf('x');

		vec.x = Integer.valueOf(s.substring(0, sep));
		vec.y = Integer.valueOf(s.substring(sep + 1));

		return vec;
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
