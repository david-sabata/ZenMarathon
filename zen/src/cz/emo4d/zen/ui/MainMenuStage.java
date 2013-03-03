package cz.emo4d.zen.ui;

import java.util.Random;

import javax.naming.AuthenticationException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import cz.emo4d.zen.WisImport;
import cz.emo4d.zen.Zen;
import cz.emo4d.zen.gameplay.SoundManager;
import cz.emo4d.zen.screens.BaseScreen;

public class MainMenuStage extends BaseStage {




	public MainMenuStage(BaseScreen screen) {
		super(screen);

		Table bgRoot = new Table();
		bgRoot.setFillParent(true);
		addActor(bgRoot);

		Table root = new Table();
		//		root.debug();
		root.setFillParent(true);

		root.add(new Image(skin.getDrawable("fit"))).center().spaceRight(80);

		Table menu = prepareMainMenu();
		root.add(menu);


		addActor(root);
	}



	private Table prepareMainMenu() {
		Table root = new Table();
		//		root.debug();

		TextButton btnPlay = new TextButton("PLAY", skin);
		btnPlay.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				showNewGameDialog();
			}
		});

		//		TextButton btnTop = new TextButton(" HIGH SCORES ", skin);

		TextButton btnCredits = new TextButton("CREDITS", skin);
		btnCredits.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showCredits();
			}
		});

		TextButton btnExit = new TextButton("EXIT", skin);
		btnExit.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				Gdx.app.exit();
			}
		});


		root.add().colspan(3).expandY();
		root.row();

		root.add();
		root.add(btnPlay).center().height(70).fill().spaceBottom(50);
		root.add();
		root.row();

		//		root.add();
		//		root.add(btnTop).center().height(70).fill().spaceBottom(50);
		//		root.add();
		//		root.row();

		root.add();
		root.add(btnCredits).center().height(70).fill().spaceBottom(50);
		root.add();
		root.row();

		root.add();
		root.add(btnExit).center().height(70).fill();
		root.add();
		root.row();

		root.add().colspan(3).expandY();

		return root;
	}



	private void showAlertDialog() {
		final Dialog dlg = new Dialog("", skin);
		dlg.pad(20);

		dlg.row();
		dlg.add(new Label("INVALID LOGIN/PASS", skin)).center();
		dlg.row();

		ImageButton btn = new ImageButton(skin.getDrawable("okay"));
		dlg.add(btn).center();
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dlg.hide();
			}
		});

		dlg.show(this);
	}



	private void showNewGameDialog() {
		final Dialog dlg = new Dialog("", skin);

		dlg.pad(30, 10, 10, 10);

		dlg.row();
		dlg.add(new Label("FOR BEST GAME EXPERIENCE\nENTER WIS CREDENTIALS", skin)).colspan(2).spaceBottom(40);

		dlg.row();

		dlg.add(new Label("LOGIN", skin)).right();
		final TextField login = new TextField("", skin);
		dlg.add(login).padLeft(10).width(300).left();

		dlg.row();

		dlg.add(new Label("PASS", skin)).right();
		final TextField pwd = new TextField("", skin);
		pwd.setPasswordMode(true);
		pwd.setPasswordCharacter('*');
		dlg.add(pwd).padLeft(10).width(300).left();

		dlg.row();
		final TextButton okBtn = new TextButton("OK, LET'S GO", skin);
		dlg.add(okBtn).colspan(2).center().spaceTop(20);
		okBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				WisImport wis = new WisImport();

				String nameTxt = login.getText();
				String passTxt = pwd.getText();

				try {
					wis.login(nameTxt, passTxt);
					Zen.currentBoss = wis.getMostHatedBoss();

					String key = Zen.bossTypes.findKey(Zen.currentBoss, false);
					Gdx.app.log("BOSS", "worst subject: " + key);

					dlg.hide();
				} catch (AuthenticationException e) {
					dlg.hide();
					showAlertDialog();
					return;
				}

				SoundManager.getSound("intro.wav").play();
				screen.getGame().showGameScreen();
			}
		});

		dlg.row();
		dlg.add(new Label("OR", skin)).colspan(2).center().spaceTop(20).spaceBottom(20);

		dlg.row();
		TextButton randBtn = new TextButton("PICK RANDOM BOSS", skin);
		dlg.add(randBtn).colspan(2).center();
		randBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dlg.hide();
				Random rand = new Random();
				int idx = rand.nextInt(Zen.bossTypes.size);
				String key = Zen.bossTypes.keys().toArray().get(idx);
				Zen.currentBoss = Zen.bossTypes.get(key);

				Gdx.app.log("BOSS", "random subject: " + key);

				SoundManager.getSound("intro.wav").play();
				screen.getGame().showGameScreen();
			}
		});


		dlg.show(this);
	}




	public void showCredits() {
		final Dialog dlg = new Dialog("", skin);
		dlg.pad(50);
		dlg.row();

		dlg.add(new Label("ZEN @ FIT", skin)).center().spaceBottom(40);

		dlg.row();
		dlg.add(new Label("TOMAS HOLOMEK", skin)).center();
		dlg.row();
		dlg.add(new Label("TOMAS KIMER", skin)).center();
		dlg.row();
		dlg.add(new Label("VOJTECH SMEJKAL", skin)).center();
		dlg.row();
		dlg.add(new Label("DAVID SABATA", skin)).center();

		dlg.row();
		dlg.add(new Label("March 2013", skin)).spaceTop(30).center();

		dlg.row();
		TextButton btn = new TextButton("OK", skin);
		dlg.add(btn).spaceTop(30).center();
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dlg.hide();
			}
		});

		dlg.show(this);
	}



	@Override
	public void draw() {
		super.draw();

		Table.drawDebug(this);
	}


}
