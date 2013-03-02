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
import cz.emo4d.zen.gameplay.Player;
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

	private Player player;
	private OrthographicCamera camera;
	Vector2 moveVec = new Vector2();
	private Bullet bullet;

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
		player = new Player(new Vector2(7, map.height - 4), 0, 0);
		player.setMap(map);

		bullet = new Bullet(new Texture(Gdx.files.internal("data/bullet.png")));
		bullet.setMap(map);

		rc.RegisterEventHandler(this);

		gui = new GameGuiStage(this);

		inputMpx.addProcessor(gui);
		inputMpx.addProcessor(gameInputAdapter);
	}

	public void onKeyPress(int keycode) {
		if (keycode == Keys.CONTROL_LEFT) {
			bullet.shoot(player.position, player.currentDir);
		}
	}



	@Override
	public void render(float deltaTime) {
		// clear the screen
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// process input 
		moveVec.set(0, 0);

		ClientMove cm = rc.getClientMove(1);
		if (cm != null) {
			moveVec.set(cm.X, -cm.Y); // * player.MAX_VELOCITY
		}

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			moveVec.y = 1f; //player.MAX_VELOCITY
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			moveVec.y = -1f;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			moveVec.x = -1f;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			moveVec.x = 1f;
		}

		if (moveVec.x != 0 || moveVec.y != 0) {
			player.move(moveVec);
		}

		// update
		player.update(deltaTime);
		bullet.update(deltaTime);
		if (bullet.collision() != null) {
			bullet.alive = false;
		}

		// let the camera follow the player
		camera.position.x = player.position.x;
		camera.position.y = player.position.y;
		camera.update();

		// render map
		map.render(camera);

		// render
		SpriteBatch batch = map.renderer.getSpriteBatch();
		batch.begin();
		player.render(batch);
		bullet.render(batch);
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
