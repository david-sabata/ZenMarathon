package cz.emo4d.zen.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import cz.emo4d.zen.Zen;
import cz.emo4d.zen.screens.GameScreen;

public class GameGuiStage extends BaseStage {

	/**
	 * cilene zatemnuje BaseStage.screen, abychom nemuseli
	 * porad pretypovavat BaseScreen na GameScreen
	 */
	private final GameScreen screen;

	private Table root;


	public GameGuiStage(GameScreen screen) {
		super(screen);
		this.screen = screen;

		root = new Table(skin);
		root.debug();

		root.add();
		root.add().expandX();
		root.row();
		root.add(new Label("test", skin));
		root.add();

		addActor(root);
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
		final Dialog dlg = new Dialog("Uz mas dost?", skin);

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

		dlg.button(btn1);
		dlg.button(btn2);

		dlg.show(this);

	}


	@Override
	public void draw() {
		super.draw();

		Table.drawDebug(this);
	}


}