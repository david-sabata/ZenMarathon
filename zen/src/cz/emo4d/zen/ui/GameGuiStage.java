package cz.emo4d.zen.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import cz.emo4d.zen.Zen;
import cz.emo4d.zen.gameplay.Player;
import cz.emo4d.zen.gameplay.PlayerManager;
import cz.emo4d.zen.screens.GameScreen;

public class GameGuiStage extends BaseStage {

	/**
	 * cilene zatemnuje BaseStage.screen, abychom nemuseli
	 * porad pretypovavat BaseScreen na GameScreen
	 */
	private final GameScreen screen;

	private final PlayerManager playerManager;

	private Table rootPlayer;
	private Table subplayers;


	public GameGuiStage(GameScreen screen, PlayerManager manager) {
		super(screen);
		this.screen = screen;
		this.playerManager = manager;

		rootPlayer = new Table(skin);
		rootPlayer.setFillParent(true);
		//		rootPlayer.debug();

		Table playerGui = createMainPlayerGui(playerManager.getMainPlayer());
		rootPlayer.add(playerGui).left().spaceBottom(50);

		subplayers = new Table(skin);
		//		subplayers.debug();
		rootPlayer.row();
		rootPlayer.add(subplayers).left();

		rootPlayer.row();
		rootPlayer.add().expand();

		addActor(rootPlayer);

		regenerateSubplayersGui();
	}


	public void regenerateSubplayersGui() {
		subplayers.clear();

		boolean first = true;
		for (Player p : playerManager.getPlayers()) {
			if (first) {
				first = false;
				continue;
			}

			Table sub = createSubPlayerGui(p);
			subplayers.add(sub).left().spaceBottom(10);
			subplayers.row();
		}
	}




	private Table createMainPlayerGui(Player p) {
		Table tbl = new Table(skin);
		//		tbl.debug();
		tbl.setBackground(skin.getDrawable("gray-transparent"));

		Texture animSheet = new Texture(Gdx.files.internal("data/effects/avatar.png"));
		TextureRegion[][] tmp = TextureRegion.split(animSheet, animSheet.getWidth() / 3, animSheet.getHeight());
		Animation animation = new Animation(0.7f, tmp[0]);
		AnimatedImage avatar = new AnimatedImage(animation);
		avatar.setPlayMode(Animation.LOOP_PINGPONG);

		tbl.add(avatar).width(60).height(60).pad(10);

		Table stats = new Table(skin);
		tbl.add(stats).pad(0, 0, 0, 10);

		stats.add(new Label("LIVES", skin)).left().spaceRight(20);
		Drawable heart = skin.getDrawable("heart-0");
		for (int i = 0; i < 5; i++) {
			Image im = new Image(heart);
			stats.add(im).spaceRight(5);
			p.hearts.add(im);
		}

		p.updateHearts();

		stats.row().spaceTop(5);

		stats.add(new Label("ZEN", skin)).left().spaceRight(20);
		Drawable leaf = skin.getDrawable("leaf");
		for (int i = 0; i < 5; i++) {
			Image im = new Image(leaf);
			stats.add(im).spaceRight(5);
			p.leaves.add(im);
		}

		p.updateLeaves();

		return tbl;
	}


	private Table createSubPlayerGui(Player player) {
		Table tbl = new Table(skin);
		//		tbl.debug();
		tbl.setBackground(skin.getDrawable("gray-transparent"));

		Texture animSheet = new Texture(Gdx.files.internal("data/effects/avatar.png"));
		TextureRegion[][] tmp = TextureRegion.split(animSheet, animSheet.getWidth() / 3, animSheet.getHeight());
		Animation animation = new Animation(10, tmp[0]);

		Texture deadFace = new Texture(Gdx.files.internal("data/effects/avatar-dead.png"));
		TextureRegion[][] tmp2 = TextureRegion.split(deadFace, animSheet.getWidth() / 3, animSheet.getHeight());
		Animation koAnimation = new Animation(10, tmp2[0]);

		AnimatedImage avatar = new AnimatedImage(animation);
		avatar.setPlayMode(Animation.LOOP_PINGPONG);

		player.setFaceAnimations(avatar, animation, koAnimation);

		Table line = new Table(skin);
		line.setColor(player.shadowColor);
		line.setBackground("white");
		tbl.add(line).width(10).height(52);

		tbl.add(avatar).width(30).height(32).pad(10);

		Table stats = new Table(skin);
		tbl.add(stats).pad(0, 0, 0, 10);

		Drawable heart = skin.getDrawable("heart-0");
		for (int i = 1; i <= 5; i++) {
			Image im = new Image(heart);
			stats.add(im).width(10).height(10).spaceRight(5);
			player.hearts.add(im);
		}

		player.updateHearts();

		stats.row().spaceTop(5);
		Drawable leaf = skin.getDrawable("leaf");
		for (int i = 0; i < 5; i++) {
			Image im = new Image(leaf);
			stats.add(im).width(10).height(10).spaceRight(5);
			player.leaves.add(im);
		}

		player.updateLeaves();

		return tbl;
	}

	public Table createBackground() {
		Table background = new Table();
		background.setFillParent(true);
		background.setBackground(skin.getDrawable("gray"));
		Color c = background.getColor();
		background.setColor(c.r, c.g, c.b, 0);
		addActor(background);

		return background;
	}





	@Override
	public boolean keyDown(int keyCode) {

		if (keyCode == Keys.ESCAPE) {

			// zavrit jakykoliv otevreny dialog
			for (Actor a : this.getActors()) {
				if (a instanceof Dialog) {
					Dialog dlg = (Dialog) a;
					dlg.remove();
					return true;
				}
			}

			showExitDialog();
			return true;
		}

		return super.keyDown(keyCode);
	}




	private void showExitDialog() {
		final Zen game = screen.getGame();
		final Dialog dlg = new Dialog("", skin);

		TextButton btn1 = new TextButton("NE", skin);
		btn1.pad(10, 30, 10, 30);
		btn1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dlg.remove();
			}
		});

		TextButton btn2 = new TextButton("ano", skin);
		btn2.pad(10, 30, 10, 30);
		btn2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dlg.remove();
				game.showMenuScreen();
			}
		});

		dlg.text("uz mas dost?");

		dlg.button(btn1);
		dlg.button(btn2);

		dlg.pad(30, 10, 10, 10);

		dlg.show(this);
	}

	@Override
	public void draw() {
		super.draw();

		Table.drawDebug(this);
	}


}
