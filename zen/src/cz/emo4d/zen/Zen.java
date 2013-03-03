package cz.emo4d.zen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ObjectMap;

import cz.emo4d.zen.screens.GameScreen;
import cz.emo4d.zen.screens.MainMenuScreen;

public class Zen extends Game {

	private MainMenuScreen menu;
	private GameScreen game;

	public enum BossPerson {
		PP, KRENA, HRUSKA, KOLAR
	}

	public final static ObjectMap<String, BossPerson> bossTypes = new ObjectMap<String, BossPerson>();

	public static BossPerson currentBoss;


	@Override
	public void create() {
		initBosses();

		game = new GameScreen(this);
		menu = new MainMenuScreen(this);

		if (true) {
			showMenuScreen();
		} else {
			showGameScreen();
		}
	}



	private void initBosses() {
		bossTypes.put("IJC", BossPerson.PP);
		bossTypes.put("IMS", BossPerson.PP);
		bossTypes.put("ICP", BossPerson.PP);
		bossTypes.put("SNT", BossPerson.PP);
		bossTypes.put("IPP", BossPerson.KOLAR);
		bossTypes.put("FLP", BossPerson.KOLAR);
		bossTypes.put("PDB", BossPerson.KOLAR);
		bossTypes.put("ITY", BossPerson.KRENA);
		bossTypes.put("IUS", BossPerson.KRENA);
		bossTypes.put("IIS", BossPerson.HRUSKA);
		bossTypes.put("PIS", BossPerson.HRUSKA);
		bossTypes.put("WAP", BossPerson.HRUSKA);
	}



	public void showGameScreen() {
		setScreen(game);
	}



	public void showMenuScreen() {
		setScreen(menu);
	}


}
