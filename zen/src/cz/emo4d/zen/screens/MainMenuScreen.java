package cz.emo4d.zen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import cz.emo4d.zen.Zen;
import cz.emo4d.zen.gameplay.SoundManager;
import cz.emo4d.zen.ui.MainMenuStage;

public class MainMenuScreen extends BaseScreen {

	private MainMenuStage menu;


	public MainMenuScreen(Zen game) {
		super(game);

		menu = new MainMenuStage(this);

		Gdx.input.setInputProcessor(menu);
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		menu.act(delta);
		menu.draw();
	}


	@Override
	public void resize(int width, int height) {
		menu.setViewport(width, height, true);
	}



	@Override
	public void show() {
		super.show();

		Gdx.input.setInputProcessor(menu);
	}
}
