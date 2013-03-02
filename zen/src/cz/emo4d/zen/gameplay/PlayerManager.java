package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import cz.emo4d.zen.screens.Map;


public class PlayerManager {

	private Array<Player> players = new Array<Player>();
	private Vector2 moveVec = new Vector2();

	public PlayerManager(Map map, Vector2 mainPlayerPos) {
		super();
		Player p = new Player(mainPlayerPos, 0, 0);
		p.setMap(map);
		players.add(p);
	}

	public Player getMainPlayer() {
		return players.get(0);
	}

	public int addPlayer(Vector2 playerPos) {
		Player p = new Player(playerPos, 0, 0);
		p.setMap(getMainPlayer().getMap());

		players.add(p);

		return 0;
	}

	public void keyboardInput() {
		// process input 
		moveVec.set(0, 0);

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
			players.get(0).move(moveVec);
		}
	}

	public void controllerInput(int playerID, Vector2 moveVec) {
		if (moveVec.x != 0 || moveVec.y != 0) {
			players.get(playerID).move(moveVec);
		}
	}


	public void update(float deltaTime) {
		for (Player p : players) {
			p.update(deltaTime);
		}
	}

	public void render(SpriteBatch spriteBatch) {
		for (Player p : players) {
			p.render(spriteBatch);
		}

	}



}
