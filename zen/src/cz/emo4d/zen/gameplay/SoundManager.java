package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public final class SoundManager {

	private static String soundsDir = "data/sounds/";
	private static Array<String> loadedFiles = new Array<String>();
	private static  Array<Sound> loadedSounds = new Array<Sound>();
	
	public static Sound getSound(String filename) {
		int pos = loadedFiles.indexOf(filename, false);
		
		if (pos == -1) {
			Sound sound = Gdx.audio.newSound(Gdx.files.internal(soundsDir + filename));
			loadedFiles.add(filename);
			loadedSounds.add(sound);
			
			pos = loadedSounds.size - 1; 
		}
		
		return loadedSounds.get(pos);
	}
}
