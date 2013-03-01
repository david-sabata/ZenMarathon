package cz.emo4d.zen.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import cz.emo4d.zen.screens.BaseScreen;

public abstract class BaseStage extends Stage {

	protected Skin skin;

	protected final BaseScreen screen;


	public BaseStage(BaseScreen screen) {
		// use current screen dims; do not enforce keeping ratio
		super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		this.screen = screen;

		// activate input processing
		Gdx.input.setInputProcessor(this);

		skin = new Skin(Gdx.files.internal("data/ui.json"), new TextureAtlas("data/ui.atlas"));
	}



	@Override
	public void dispose() {
		super.dispose();

		skin.dispose();
	}


}