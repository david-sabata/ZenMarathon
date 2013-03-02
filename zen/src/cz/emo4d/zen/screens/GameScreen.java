package cz.emo4d.zen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

	//	private TiledMap map;
	//	private OrthogonalTiledMapRenderer renderer;

	private Map map;

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

		map = new Map("fit");

		// create an orthographic camera, shows us 30x20 units of the world
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 30, 20);
		camera.update();


		// invertovat Y souradnici pro indexovani s nulou v levem HORNIM rohu
		playerManager = new PlayerManager(map, new Vector2(7, map.height - 6));
		playerManager.addPlayer(new Vector2(7, map.height - 4));

		bullet = new Bullet(new Texture(Gdx.files.internal("data/bullet.png")));
		bullet.setMap(map);

		enemy = new Enemy(new Vector2(7, map.height - 8));
		enemy.setMap(map);

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
		playerManager.update(deltaTime);


		bullet.update(deltaTime);
		if (bullet.collision() != null) {
			bullet.alive = false;
		}
		enemy.update(deltaTime);

		// let the camera follow the player
		camera.position.x = playerManager.getMainPlayer().position.x;
		camera.position.y = playerManager.getMainPlayer().position.y;
		camera.update();

		// render map
		map.render(camera);


		// render
		SpriteBatch batch = map.renderer.getSpriteBatch();
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
		} else if (type == DeviceEvent.CONNECT) {

		} else if (type == DeviceEvent.DISCONNECT) {

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
