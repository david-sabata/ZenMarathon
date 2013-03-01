package cz.emo4d.zen.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import cz.emo4d.zen.screens.BaseScreen;

public class MainMenuStage extends BaseStage {


	private final Table mainMenuRoot;




	public MainMenuStage(BaseScreen screen) {
		super(screen);

		Table bgRoot = new Table();
		bgRoot.setFillParent(true);
		addActor(bgRoot);

		mainMenuRoot = prepareMainMenu();
		addActor(mainMenuRoot);
	}





	private Table prepareMainMenu() {
		Table root = new Table();
		//		root.debug();
		root.setFillParent(true);

		TextButton btnPlay = new TextButton("PLAY", skin);
		btnPlay.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				screen.getGame().showGameScreen();
			}
		});

		TextButton btnTop = new TextButton("HIGH SCORES", skin);

		TextButton btnCredits = new TextButton("CREDITS", skin);

		TextButton btnExit = new TextButton("EXIT", skin);
		btnExit.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				Gdx.app.exit();
			}
		});


		root.add().colspan(3).expandY();
		root.row();

		root.add();
		root.add(btnPlay).center().width(150).height(50).fill().spaceBottom(50);
		root.add();
		root.row();

		root.add();
		root.add(btnTop).center().width(150).height(50).fill().spaceBottom(50);
		root.add();
		root.row();

		root.add();
		root.add(btnCredits).center().width(150).height(50).fill().spaceBottom(50);
		root.add();
		root.row();

		root.add();
		root.add(btnExit).center().height(50).fill();
		root.add();
		root.row();

		root.add().colspan(3).expandY();

		return root;
	}







	@Override
	public void draw() {
		super.draw();

		Table.drawDebug(this);
	}


}
