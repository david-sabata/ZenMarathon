package cz.emo4d.zen;

import com.badlogic.gdx.Game;

import cz.emo4d.zen.screens.GameScreen;
import cz.emo4d.zen.screens.MainMenuScreen;

public class Zen extends Game {

	private MainMenuScreen menu;
	private GameScreen game;

	@Override
	public void create() {
		game = new GameScreen(this);
		menu = new MainMenuScreen(this);

		if (true) {
			showMenuScreen();
		} else {
			showGameScreen();
		}
	}






	public void showGameScreen() {
		setScreen(game);
	}



	public void showMenuScreen() {
		setScreen(menu);
	}


}
