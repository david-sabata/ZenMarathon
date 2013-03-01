package cz.emo4d.zen;

import com.badlogic.gdx.Game;

import cz.emo4d.zen.screens.GameScreen;
import cz.emo4d.zen.screens.MainMenuScreen;

public class Zen extends Game {

	private MainMenuScreen menu;
	private GameScreen game;

	@Override
	public void create() {
		menu = new MainMenuScreen(this);
		setScreen(menu);
	}







	public void showGameScreen() {
		if (game != null) {
			game.dispose();
		}

		game = new GameScreen(this);
		setScreen(game);
	}




}
