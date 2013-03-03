package cz.emo4d.zen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import cz.emo4d.zen.screens.GameScreen;
import cz.emo4d.zen.screens.MainMenuScreen;

public class Zen extends Game {

	private MainMenuScreen menu;
	private GameScreen game;

	public enum BossPerson {
		PP, KRENA, HRUSKA, KOLAR, ZDENEK
	}

	public final static ObjectMap<String, BossPerson> bossTypes = new ObjectMap<String, BossPerson>();
	public final static ObjectMap<BossPerson, Array<String>> bossQuotes = new ObjectMap<BossPerson, Array<String>>();

	public static BossPerson currentBoss;


	@Override
	public void create() {
		initBosses();

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

		Array<String> q1 = new Array<String>();
		q1.add("tak vy rikate\nze znate normu?");
		q1.add("nestrelte se\ndo nohy!");
		q1.add("error\nbad pointer");

		Array<String> q2 = new Array<String>();
		q2.add("tak vy rikate\nze znate normu?");
		q2.add("nestrelte se\ndo nohy!");
		q2.add("error\nbad pointer");

		Array<String> q3 = new Array<String>();
		q3.add("tak vy rikate\nze znate normu?");
		q3.add("nestrelte se\ndo nohy!");
		q3.add("error\nbad pointer");

		Array<String> q4 = new Array<String>();
		q4.add("tak vy rikate\nze znate normu?");
		q4.add("nestrelte se\ndo nohy!");
		q4.add("error\nbad pointer");

		bossQuotes.put(BossPerson.PP, q1);
		bossQuotes.put(BossPerson.KOLAR, q2);
		bossQuotes.put(BossPerson.KRENA, q3);
		bossQuotes.put(BossPerson.HRUSKA, q4);
	}



	public void showGameScreen() {
		if (game == null)
			game = new GameScreen(this);

		setScreen(game);
	}



	public void showMenuScreen() {
		if (menu == null)
			menu = new MainMenuScreen(this);

		setScreen(menu);
	}


}
