package cz.emo4d.zen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import cz.emo4d.zen.Zen;
import cz.emo4d.zen.gameplay.Bullet;
import cz.emo4d.zen.gameplay.Enemy;
import cz.emo4d.zen.gameplay.PlayerManager;
import cz.emo4d.zen.remote.ClientMove;
import cz.emo4d.zen.remote.DeviceEvent;
import cz.emo4d.zen.remote.DeviceEventHandler;
import cz.emo4d.zen.remote.RemoteControl;
import cz.emo4d.zen.ui.GameGuiStage;


public class GameScreen extends BaseScreen implements DeviceEventHandler {

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	private GameGuiStage gui;

	private PlayerManager playerManager;
	private OrthographicCamera camera;
	Vector2 moveVec = new Vector2();
	
	private Bullet bullet;
	private Enemy enemy;

	private GameInputAdapter gameInputAdapter = new GameInputAdapter(this);
	private InputMultiplexer inputMpx = new InputMultiplexer();

	private RemoteControl rc = new RemoteControl();


	public GameScreen(Zen game) {
		super(game);

		// load the map, set the unit scale to 1/32 (1 unit == 32 pixels)
		map = new TmxMapLoader().load("data/maps/fit.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
		map.getTileSets().getTile(1).getTextureRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		// create an orthographic camera, shows us 30x20 units of the world
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 30, 20);
		camera.update();

		// create player we want to move around the world
		int height = (Integer) map.getProperties().get("height");
		playerManager = new PlayerManager(new Vector2(7, height - 6));
		playerManager.addPlayer(new Vector2(7, height - 4));

		bullet = new Bullet(new Texture(Gdx.files.internal("data/bullet.png")));
		enemy = new Enemy(new Vector2(7, height - 8));

		rc.RegisterEventHandler(this);

		gui = new GameGuiStage(this);

		inputMpx.addProcessor(gui);
		inputMpx.addProcessor(gameInputAdapter);
	}

	public void onKeyPress(int keycode) {
		if (keycode == Keys.CONTROL_LEFT) {
			bullet.shoot(playerManager.getMainPlayer().position, playerManager.getMainPlayer().currentDir);
		}
	}



	@Override
	public void render(float deltaTime) {
		// clear the screen
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// -- process input -- 
		moveVec.set(0, 0);

		// controller input
		ClientMove cm = rc.getClientMove(1);
		if (cm != null) {
			moveVec.set(cm.X, -cm.Y); // * player.MAX_VELOCITY
			playerManager.controllerInput(0, moveVec);			
		}		
		// keyboard input
		playerManager.keyboardInput();
		

		// -- update --
		playerManager.update(deltaTime, map);
		
		bullet.update(deltaTime);
		if (bullet.collision(map) != null) {
			bullet.alive = false;
		}
		enemy.update(deltaTime, map);

		// let the camera follow the player
		camera.position.x = playerManager.getMainPlayer().position.x;
		camera.position.y = playerManager.getMainPlayer().position.y;
		camera.update();

		// set the tile map renderer view based on what the
		// camera sees and render the map
		renderer.setView(camera);
		renderer.render();

		// -- render --
		SpriteBatch batch = renderer.getSpriteBatch();
		batch.begin();
		playerManager.render(batch);
		bullet.render(batch);
		enemy.render(batch);
		batch.end();

		// gui
		gui.act(deltaTime);
		gui.draw();
	}


	@Override
	public void acceptEvent(int type, int device, float X, float Y) {
		if (type == DeviceEvent.MOVE) {
			//player.move(new Vector2(X * player.MAX_VELOCITY,  -Y * player.MAX_VELOCITY));
		}
		else if (type == DeviceEvent.CONNECT) {
			
		}
		else if (type == DeviceEvent.DISCONNECT) {
			
		}
	}


	@Override
	public void show() {
		super.show();

		Gdx.input.setInputProcessor(inputMpx);
	}

	@Override
	public void resize(int width, int height) {
		gui.setViewport(width, height, true);
	}

}
