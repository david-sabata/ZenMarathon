package cz.emo4d.zen;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "zen";
		cfg.useGL20 = true;
		cfg.width = 32 * 30;
		cfg.height = 32 * 20;
		new LwjglApplication(new Zen(), cfg);
	}
}
