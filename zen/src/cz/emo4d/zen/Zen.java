package cz.emo4d.zen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Zen implements ApplicationListener {

	private static class Character {
		private static float WIDTH;
		private static float HEIGHT;
		private static float MAX_VELOCITY = 8f;
		private static float DAMPING = 0.87f;

		enum State {
			Standing, Walking, Shooting
		}

		final Vector2 position = new Vector2();
		final Vector2 velocity = new Vector2();
		State state = State.Walking;
		float stateTime = 0;
		boolean facesRight = true;
	}

	private Animation stand;
	private Animation walk;
	private Animation jump;

	private Character koala;
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	private Array<Rectangle> tiles = new Array<Rectangle>();

	private Texture koalaTexture;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	@Override
	public void create() {
		// load the koala frames, split them, and assign them to Animations
		koalaTexture = new Texture("data/maps/koalio.png");
		TextureRegion[] regions = TextureRegion.split(koalaTexture, 18, 26)[0];
		stand = new Animation(0, regions[0]);
		jump = new Animation(0, regions[1]);
		walk = new Animation(0.15f, regions[2], regions[3], regions[4]);
		walk.setPlayMode(Animation.LOOP_PINGPONG);

		// figure out the width and height of the koala for collision
		// detection and rendering by converting a koala frames pixel
		// size into world units (1 unit == 32 pixels)
		Character.WIDTH = 1 / 32f * regions[0].getRegionWidth();
		Character.HEIGHT = 1 / 32f * regions[0].getRegionHeight();

		// load the map, set the unit scale to 1/32 (1 unit == 32 pixels)
		map = new TmxMapLoader().load("data/maps/test.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
		map.getTileSets().getTile(1).getTextureRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		// create an orthographic camera, shows us 30x20 units of the world
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 30, 20);
		camera.update();

		// create the Koala we want to move around the world
		int height = (Integer) map.getProperties().get("height");
		koala = new Character();
		koala.position.set(13, height - 3);
	}

	@Override
	public void render() {
		// clear the screen
		Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// get the delta time
		float deltaTime = Gdx.graphics.getDeltaTime();

		// update the koala (process input, collision detection, position
		// update)
		updateKoala(deltaTime);

		// Gdx.app.log("POS", koala.position.toString());

		// let the camera follow the koala, x-axis only
		camera.position.x = koala.position.x;
		camera.position.y = koala.position.y;
		camera.update();

		// set the tile map rendere view based on what the
		// camera sees and render the map
		renderer.setView(camera);
		renderer.render();

		// render the koala
		renderKoala(deltaTime);
	}

	private void renderKoala(float deltaTime) {
		// based on the koala state, get the animation frame
		TextureRegion frame = null;
		switch (koala.state) {
			case Standing:
				frame = stand.getKeyFrame(koala.stateTime);
				break;
			case Walking:
				frame = walk.getKeyFrame(koala.stateTime);
				break;
			case Shooting:
				frame = jump.getKeyFrame(koala.stateTime);
				break;
		}

		// draw the koala, depending on the current velocity
		// on the x-axis, draw the koala facing either right
		// or left
		SpriteBatch batch = renderer.getSpriteBatch();
		batch.begin();
		if (koala.facesRight) {
			batch.draw(frame, koala.position.x, koala.position.y, Character.WIDTH, Character.HEIGHT);
		} else {
			batch.draw(frame, koala.position.x + Character.WIDTH, koala.position.y, -Character.WIDTH, Character.HEIGHT);
		}
		batch.end();
	}

	private void updateKoala(float deltaTime) {
		koala.stateTime += deltaTime;

		// check input and apply to velocity & state
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			koala.velocity.y += Character.MAX_VELOCITY;
			koala.state = Character.State.Walking;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			koala.velocity.y -= Character.MAX_VELOCITY;
			koala.state = Character.State.Walking;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			koala.velocity.x = -Character.MAX_VELOCITY;
			koala.state = Character.State.Walking;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			koala.velocity.x = Character.MAX_VELOCITY;
			koala.state = Character.State.Walking;
		}

		// clamp the velocity to the maximum
		if (Math.abs(koala.velocity.x) > Character.MAX_VELOCITY) {
			koala.velocity.x = Math.signum(koala.velocity.x) * Character.MAX_VELOCITY;
		}
		if (Math.abs(koala.velocity.y) > Character.MAX_VELOCITY) {
			koala.velocity.y = Math.signum(koala.velocity.y) * Character.MAX_VELOCITY;
		}

		// clamp the velocity to 0 if it's < 1, and set the state to standign
		if (Math.abs(koala.velocity.x) < 1 && Math.abs(koala.velocity.y) < 1) {
			koala.velocity.set(0, 0);
			koala.state = Character.State.Standing;
		}

		// multiply by delta time so we know how far we go
		// in this frame
		koala.velocity.mul(deltaTime);

		// perform collision detection & response, on each axis, separately
		// if the koala is moving right, check the tiles to the right of it's
		// right bounding box edge, otherwise check the ones to the left
		Rectangle koalaRect = rectPool.obtain();
		koalaRect.set(koala.position.x, koala.position.y, Character.WIDTH, Character.HEIGHT);
		int startX, startY, endX, endY;
		if (koala.velocity.x > 0) {
			startX = endX = (int) (koala.position.x + Character.WIDTH + koala.velocity.x);
		} else {
			startX = endX = (int) (koala.position.x + koala.velocity.x);
		}
		startY = (int) (koala.position.y);
		endY = (int) (koala.position.y + Character.HEIGHT);
		getTiles(startX, startY, endX, endY, tiles);
		koalaRect.x += koala.velocity.x;
		for (Rectangle tile : tiles) {
			if (koalaRect.overlaps(tile)) {
				koala.velocity.x = 0;
				break;
			}
		}
		koalaRect.x = koala.position.x;

		// if the koala is moving upwards, check the tiles to the top of it's
		// top bounding box edge, otherwise check the ones to the bottom
		if (koala.velocity.y > 0) {
			startY = endY = (int) (koala.position.y + Character.HEIGHT + koala.velocity.y);
		} else {
			startY = endY = (int) (koala.position.y + koala.velocity.y);
		}
		startX = (int) (koala.position.x);
		endX = (int) (koala.position.x + Character.WIDTH);
		getTiles(startX, startY, endX, endY, tiles);
		koalaRect.y += koala.velocity.y;
		for (Rectangle tile : tiles) {
			if (koalaRect.overlaps(tile)) {
				koala.velocity.y = 0;
				break;
			}
		}
		rectPool.free(koalaRect);

		// unscale the velocity by the inverse delta time and set
		// the latest position
		koala.position.add(koala.velocity);
		koala.velocity.mul(1 / deltaTime);

		// Apply damping to the velocity so we don't
		// walk infinitely once a key was pressed
		koala.velocity.mul(Character.DAMPING);
	}

	private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
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

	@Override
	public void dispose() {
		if (batch != null)
			batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
