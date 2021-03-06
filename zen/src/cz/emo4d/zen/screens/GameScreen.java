package cz.emo4d.zen.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import cz.emo4d.zen.Zen;
import cz.emo4d.zen.Zen.BossPerson;
import cz.emo4d.zen.Zen.Modes;
import cz.emo4d.zen.gameplay.Boss;
import cz.emo4d.zen.gameplay.BulletManager;
import cz.emo4d.zen.gameplay.EffectManager;
import cz.emo4d.zen.gameplay.Enemy;
import cz.emo4d.zen.gameplay.EnemyManager;
import cz.emo4d.zen.gameplay.PlayerManager;
import cz.emo4d.zen.gameplay.PowerupManager;
import cz.emo4d.zen.gameplay.RemotePlayer;
import cz.emo4d.zen.gameplay.SoundManager;
import cz.emo4d.zen.remote.ClientMove;
import cz.emo4d.zen.remote.DeviceEvent;
import cz.emo4d.zen.remote.DeviceEventHandler;
import cz.emo4d.zen.remote.RemoteControl;
import cz.emo4d.zen.ui.GameGuiStage;


public class GameScreen extends BaseScreen implements DeviceEventHandler {

	private static final float SHOOT_INTERVAL = 0.14f;

	// celkovy cas od spusteni hry
	private float elapsedTime = 0;

	private Map map;

	private GameGuiStage gui;

	private final PlayerManager playerManager;
	private OrthographicCamera camera;
	Vector2 moveVec = new Vector2();

	private BulletManager bulletManager;
	private EffectManager effectManager;
	private EnemyManager enemyManager;
	private PowerupManager powerupManager;

	private GameInputAdapter gameInputAdapter = new GameInputAdapter(this);
	private InputMultiplexer inputMpx = new InputMultiplexer();

	private RemoteControl rc = new RemoteControl();

	// Master player remote connection
	private int remoteMaster = -1;

	// Slave players array
	private ArrayList<RemotePlayer> remoteSlaves;
	private ArrayList<RemotePlayer> pendingSlaves;

	private Vector2 kickvector = new Vector2();

	private boolean bossSpawned = false;
	private Boss boss;


	public GameScreen(Zen game) {
		super(game);

		elapsedTime = 0;

		map = new Map("floor1");

		SoundManager.getSound("background.wav").loop();

		// create an orthographic camera, shows us 30x20 units of the world
		camera = new OrthographicCamera();

		if (game.getMode() == Modes.SENIOR) {
			camera.setToOrtho(false, 8, 5);
		} else {
			camera.setToOrtho(false, 30, 20);
		}

		camera.update();


		// invertovat Y souradnici pro indexovani s nulou v levem HORNIM rohu
		playerManager = new PlayerManager(map, map.getCoord(34, 66));
		//		playerManager.addPlayer(map.getCoord(54, 31));

		Texture bulletTex = (game.getMode() == Modes.KID) ? new Texture(Gdx.files.internal("data/ball.png")) : new Texture(
				Gdx.files.internal("data/bullet.png"));
		effectManager = new EffectManager();
		bulletManager = new BulletManager(map, bulletTex, effectManager, this);
		enemyManager = new EnemyManager();
		powerupManager = new PowerupManager(effectManager, this);

		powerupManager.addPowerup(map.getCoord(57, 15));
		powerupManager.addPowerup(map.getCoord(58, 16));
		powerupManager.addPowerup(map.getCoord(59, 15));

		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(map.getCoord(56, 39));
			enemy.setMap(map);
			enemyManager.addEnemy(enemy);
		}

		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(map.getCoord(37, 28));
			enemy.setMap(map);
			enemyManager.addEnemy(enemy);
		}

		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(map.getCoord(36, 65));
			enemy.setMap(map);
			enemyManager.addEnemy(enemy);
		}

		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(map.getCoord(54, 66));
			enemy.setMap(map);
			enemyManager.addEnemy(enemy);
		}

		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(map.getCoord(62, 31));
			enemy.setMap(map);
			enemyManager.addEnemy(enemy);
		}

		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(map.getCoord(62, 19));
			enemy.setMap(map);
			enemyManager.addEnemy(enemy);
		}

		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(map.getCoord(54, 28));
			enemy.setMap(map);
			enemyManager.addEnemy(enemy);
		}

		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(map.getCoord(57, 22));
			enemy.setMap(map);
			enemyManager.addEnemy(enemy);
		}


		remoteSlaves = new ArrayList<RemotePlayer>();
		pendingSlaves = new ArrayList<RemotePlayer>();
		rc.RegisterEventHandler(this);

		gui = new GameGuiStage(this, playerManager);

		inputMpx.addProcessor(gui);
		inputMpx.addProcessor(gameInputAdapter);
	}

	@Override
	public void dispose() {
		rc.killThemAll();
		super.dispose();
	}

	public void onKeyPress(int keycode) {
		//if (keycode == Keys.CONTROL_LEFT) {
		//bulletManager.shoot(playerManager.getMainPlayer().position,
		//		playerManager.getMainPlayer().currentDir, PLAYER_DAMAGE, playerManager.getMainPlayer());
		//}
		//if (keycode == Keys.TAB) {
		//			doTeleport(map.getCoord(14, 36));
		//}
	}

	float lastTime = 0;





	@Override
	public void render(float deltaTime) {
		elapsedTime += deltaTime;

		// clear the screen
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// -- process input -- 
		moveVec.set(0, 0);

		// controller input

		ClientMove cm;
		if (remoteMaster != -1) {
			if ((cm = rc.getClientMove(remoteMaster)) != null) {
				moveVec.set(cm.X, -cm.Y);
				playerManager.controllerInput(0, moveVec);
			}
		}
		for (int i = 0; i < pendingSlaves.size(); i++) {
			RemotePlayer rp = pendingSlaves.get(i);
			rp.localId = playerManager.addPlayer(playerManager.getMainPlayer().position);
			Gdx.app.log("SLAVE CONN", Integer.toString(rp.localId));
			playerManager.controllerInput(rp.localId, new Vector2(1.0f, 1.0f));

			remoteSlaves.add(rp);
			pendingSlaves.remove(i);
			i--;

			gui.regenerateSubplayersGui();
		}

		for (int i = 0; i < remoteSlaves.size(); i++) {
			if ((cm = rc.getClientMove(remoteSlaves.get(i).remoteId)) != null) {

				//Gdx.app.log("MOVE","SLAVE " + Integer.toString(remoteSlaves.get(i).localId));

				moveVec.set(cm.X, -cm.Y);
				playerManager.controllerInput(remoteSlaves.get(i).localId, moveVec);
			}
		}

		// keyboard input
		playerManager.keyboardInput();


		lastTime += deltaTime;
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			if (lastTime > SHOOT_INTERVAL) {

				bulletManager.shoot(playerManager.getMainPlayer().position, playerManager.getMainPlayer().currentDir, playerManager.getMainPlayer());
				lastTime = 0;
			}
		}


		// -- update --
		playerManager.update(deltaTime);

		// teleport ven
		if (playerManager.getMainPlayer().getCollidingOutPoint() != null) {
			Map.Position outPt = playerManager.getMainPlayer().getCollidingOutPoint();

			playerManager.setSwitchingRooms(true);
			doTeleport(outPt);

			Gdx.app.log("OUT", "--> " + outPt.identifier + " (" + outPt.mapName + ") @ " + outPt.coordinates.toString());
		}

		// teleport dovnitr
		if (playerManager.getMainPlayer().getCollidingInPoint() != null) {
			Gdx.app.log("IN", "<-- " + playerManager.getMainPlayer().getCollidingInPoint().identifier);
		}




		bulletManager.update(deltaTime);
		bulletManager.collisionWithMap(rc);
		bulletManager.collision(playerManager.getPlayers(), enemyManager.getEnemies(), rc, remoteSlaves, remoteMaster);

		enemyManager.update(deltaTime);
		/*if (enemy.health > 0) {

			int hits = bulletManager.collision(enemy);
			if (hits > 0) {
				enemy.takeHit(20 * hits);
				enemy.update(deltaTime);
				if (enemy.health <= 0) {
					effectManager.addEffect(EffectManager.AvailableEffects.DIE_EXPLOSION, enemy.position.x, enemy.position.y);
				}
			}
		}*/
		powerupManager.update(deltaTime);
		powerupManager.collision(playerManager.getPlayers());

		effectManager.update(deltaTime);

		// let the camera follow the player
		camera.position.x = playerManager.getMainPlayer().position.x;
		camera.position.y = playerManager.getMainPlayer().position.y;
		camera.update();

		// render 'underlay'
		map.renderUnderlay(camera);

		// render players & dynamic entities 
		SpriteBatch batch = map.renderer.getSpriteBatch();
		batch.begin();
		powerupManager.render(batch);
		playerManager.render(batch);
		enemyManager.render(batch);
		bulletManager.render(batch);
		effectManager.render(batch);

		batch.end();

		// render overlay
		map.renderOverlay(camera);

		// gui
		gui.act(deltaTime);
		gui.draw();
	}




	public void doTeleport(final Map.Position newPos) {
		final Table background = gui.createBackground();

		SequenceAction seq = new SequenceAction();

		SoundManager.getSound("teleport.wav").play();

		seq.addAction(Actions.fadeIn(0.2f, Interpolation.fade));

		seq.addAction(new RunnableAction() {
			@Override
			public void run() {
				map = new Map(newPos.mapName);
				Map.Position targetPos = map.inPoints.get(newPos.identifier);
				playerManager.teleportAllPlayers(map, targetPos.coordinates);

				kickvector.set(targetPos.direction);
				kickvector.y *= -1;

				if (bossSpawned) {
					boss.setMap(map);
					boss.position.set(targetPos.coordinates);
					boss.timedMove(kickvector, 1, 0);
				}

				if (!bossSpawned && newPos.mapName.equals("arena1")) {
					boss = new Boss(map.getCoord(16, 6), playerManager.getMainPlayer(), bulletManager, Zen.currentBoss);
					boss.setMap(map);
					enemyManager.addEnemy(boss);
					bossSpawned = true;
				}

				bulletManager.setMap(map);

				playerManager.applyKick(kickvector);
			}
		});

		seq.addAction(Actions.fadeOut(0.2f, Interpolation.fade));

		seq.addAction(new RunnableAction() {
			@Override
			public void run() {
				Map.Position targetPos = map.inPoints.get(newPos.identifier);

				kickvector.set(targetPos.direction);
				kickvector.y *= -1;
				playerManager.applyKick(kickvector);
			}
		});

		seq.addAction(Actions.delay(0.3f));

		seq.addAction(new RunnableAction() {
			@Override
			public void run() {
				playerManager.setSwitchingRooms(false);
			}
		});

		seq.addAction(Actions.removeActor());

		background.addAction(seq);
	}




	public void showDialog(BossPerson person, String msg, float time) {
		gui.showGameDialog(person, msg, time);
	}




	@Override
	public void acceptEvent(int type, int device, float X, float Y) {
		if (type == DeviceEvent.MOVE) {

		} else if (type == DeviceEvent.CONNECT) {
			Gdx.app.log("Player Connection", Float.toString(X));
			if (X == 0.0) {
				// Master connected
				Gdx.app.log("Conn", "Master at slot " + Integer.toString(device));
				remoteMaster = device;
			} else {
				Gdx.app.log("Conn", "Slave");
				RemotePlayer rp = new RemotePlayer();
				rp.remoteId = device;


				pendingSlaves.add(rp);

			}

		} else if (type == DeviceEvent.DISCONNECT) {

		} else if (type == DeviceEvent.PRESS_A) {
			// Master is shooting
			if (device == remoteMaster)
				bulletManager.shoot(playerManager.getMainPlayer().position, playerManager.getMainPlayer().currentDir, playerManager.getMainPlayer());

			// Slave is shooting
			for (int i = 0; i < remoteSlaves.size(); i++) {
				if (remoteSlaves.get(i).remoteId == device) {
					bulletManager.shoot(playerManager.getPlayer(remoteSlaves.get(i).localId).position,
							playerManager.getPlayer(remoteSlaves.get(i).localId).currentDir, playerManager.getPlayer(remoteSlaves.get(i).localId));
				}
			}


		}
	}


	@Override
	public void show() {
		super.show();

		if (boss != null) {
			enemyManager.getEnemies().removeValue(boss, true);
			boss = null;
		}

		bossSpawned = false;

		//		showDialog(Zen.currentBoss, "YOU SHALL\nNOT PASS !!!", 10);

		Gdx.input.setInputProcessor(inputMpx);
	}

	@Override
	public void resize(int width, int height) {
		gui.setViewport(width, height, true);
	}

}
