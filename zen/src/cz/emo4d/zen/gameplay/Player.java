package cz.emo4d.zen.gameplay;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import cz.emo4d.zen.screens.Map;


public class Player extends Mob {

	private Map.Position collidingInPoint;
	private Map.Position collidingOutPoint;

	// prave prochazime dverma
	public boolean isSwitchingRooms = false;

	private Rectangle tmpRect = new Rectangle();
	private Rectangle tmpPlayerRect = new Rectangle();

	public static int MAX_ZEN = 100;
	private int zen = MAX_ZEN;

	// gui sem nastavuje obrazky s listkama, ktere si pak hrac updatuje
	public final Array<Image> leaves = new Array<Image>();

	public Player(Vector2 pos, float width, float height) {
		super();
		position.set(pos);

		this.effect = new Effect(new Texture(Gdx.files.internal("data/effects/player_sheet.png")), 6, 8, 0.1f, 1, 0, 0);
		WIDTH = 1 / 32f * (effect.width - 3);
		HEIGHT = 1 / 32f * (effect.height - 15);

		effect.update(0, true); // 0 = Direction.S 

		Random r = new Random();
		zen = Math.round(r.nextFloat() * 100);
	}



	public void addZen(int diff) {
		zen += diff;

		if (leaves.size > 0) {
			updateLeaves();
		}
	}

	public void updateLeaves() {
		float zenPerHeart = Player.MAX_ZEN / (float) leaves.size;

		for (int i = 1; i <= 5; i++) {
			Image im = leaves.get(i - 1);
			Color c = im.getColor();

			if (zen < i * zenPerHeart) {
				float part = (zen - ((i - 1) * zenPerHeart)) / (float) zenPerHeart;

				if (part < 0) {
					part = 0;
				}

				im.setColor(c.r, c.g, c.b, part);
			}
		}
	}



	@Override
	public boolean collisionWithMap() {
		tmpPlayerRect.set(this.position.x, this.position.y, this.WIDTH, this.HEIGHT);

		// resetovat IN/OUT flagy
		collidingOutPoint = null;
		collidingInPoint = null;

		if (isSwitchingRooms) {
			return false;
		}

		// detekce dveri ven
		for (Map.Position pos : currentMap.outPoints.values()) {
			tmpRect.set(pos.coordinates.x, pos.coordinates.y - 1, 1, 1);
			if (tmpPlayerRect.overlaps(tmpRect) || tmpRect.overlaps(tmpPlayerRect)) {
				collidingOutPoint = pos;
				return false;
			}
		}

		// detekce dveri dovnitr
		for (Map.Position pos : currentMap.inPoints.values()) {
			tmpRect.set(pos.coordinates.x, pos.coordinates.y - 1, 1, 1);
			if (tmpPlayerRect.overlaps(tmpRect) || tmpRect.overlaps(tmpPlayerRect)) {
				collidingInPoint = pos;
				return false;
			}
		}

		// detekce kolize se zdi (pouze pokud nejsme na in/out pozici)
		return super.collisionWithMap();
	}



	public Map.Position getCollidingInPoint() {
		return collidingInPoint;
	}

	public Map.Position getCollidingOutPoint() {
		return collidingOutPoint;
	}

}
