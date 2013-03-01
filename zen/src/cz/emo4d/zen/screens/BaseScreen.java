package cz.emo4d.zen.screens;

import com.badlogic.gdx.Screen;

import cz.emo4d.zen.Zen;

public class BaseScreen implements Screen {

	protected final Zen game;

	public BaseScreen(Zen game) {
		this.game = game;
	}

	public final Zen getGame() {
		return game;
	}


	@Override
	public void render(float delta) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
