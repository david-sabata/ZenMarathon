package cz.emo4d.zen.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import cz.emo4d.zen.Zen;
import cz.emo4d.zen.gameplay.PlayerManager;
import cz.emo4d.zen.screens.GameScreen;

public class GameGuiStage extends BaseStage {

	/**
	 * cilene zatemnuje BaseStage.screen, abychom nemuseli
	 * porad pretypovavat BaseScreen na GameScreen
	 */
	private final GameScreen screen;

	private Table rootPlayer;


	public GameGuiStage(GameScreen screen) {
		super(screen);
		this.screen = screen;

		rootPlayer = new Table(skin);
		rootPlayer.setFillParent(true);
		//		rootPlayer.debug();

		Table playerGui = createPlayerGui();
		rootPlayer.add(playerGui).left();

		rootPlayer.row();
		rootPlayer.add().expand();

		addActor(rootPlayer);
	}





	private Table createPlayerGui() {
		Table tbl = new Table(skin);
		//		tbl.debug();
		tbl.setBackground(skin.getDrawable("gray-transparent"));


		tbl.add(new Image(skin.getDrawable("avatar-main"))).width(60).height(60).pad(10);

		Table stats = new Table(skin);
		tbl.add(stats).pad(0, 0, 0, 10);

		stats.add(new Label("LIVES", skin)).left().spaceRight(20);
		Drawable heart = skin.getDrawable("heart-0");
		stats.add(new Image(heart)).spaceRight(5);
		stats.add(new Image(heart)).spaceRight(5);
		stats.add(new Image(heart)).spaceRight(5);
		stats.add(new Image(heart)).spaceRight(5);
		stats.add(new Image(heart)).spaceRight(5);

		stats.row().spaceTop(5);
		stats.add(new Label("RAGE", skin)).left().spaceRight(20);
		Drawable rageOn = skin.getDrawable("rage-on");
		Drawable rageOff = skin.getDrawable("rage");
		stats.add(new Image(rageOn)).spaceRight(5);
		stats.add(new Image(rageOn)).spaceRight(5);
		stats.add(new Image(rageOff)).spaceRight(5);
		stats.add(new Image(rageOff)).spaceRight(5);
		stats.add(new Image(rageOff)).spaceRight(5);

		return tbl;
	}




	public void doTeleportAnimation(final PlayerManager manager, final Vector2 newPos) {
		Table background = new Table();
		background.setFillParent(true);
		background.setBackground(skin.getDrawable("gray"));
		Color c = background.getColor();
		background.setColor(c.r, c.g, c.b, 0);
		addActor(background);

		SequenceAction seq = new SequenceAction();

		seq.addAction(Actions.fadeIn(0.2f, Interpolation.fade));

		seq.addAction(new RunnableAction() {
			@Override
			public void run() {
				manager.teleportAllPlayers(newPos);
			}
		});

		seq.addAction(Actions.fadeOut(0.2f, Interpolation.fade));
		seq.addAction(Actions.removeActor());

		background.addAction(seq);
	}



	private static class MovePlayersAction extends Action {

		private PlayerManager manager;
		private Vector2 pos;

		public MovePlayersAction(PlayerManager manager, Vector2 newPos) {
			this.manager = manager;
			this.pos = newPos;
		}

		@Override
		public boolean act(float delta) {
			manager.teleportAllPlayers(pos);
			return true;
		}

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
