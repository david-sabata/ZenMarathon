package cz.emo4d.zen.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import cz.emo4d.zen.screens.Map;
import cz.emo4d.zen.ui.AnimatedImage;


public class Player extends Mob {

	private static Color[] shadowColors = { Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.GRAY, Color.WHITE, Color.PINK, Color.ORANGE, Color.YELLOW,
			Color.MAGENTA, Color.CYAN };

	private final static Texture shadow = new Texture(Gdx.files.internal("data/shadow.png"));
	public final Color shadowColor;
	private static int nextShadowColor = 0;

	private static int MIN_DAMAGE = 5;
	private static int MAX_DAMAGE = 20;

	private Map.Position collidingInPoint;
	private Map.Position collidingOutPoint;

	// prave prochazime dverma
	public boolean isSwitchingRooms = false;

	private Rectangle tmpRect = new Rectangle();
	private Rectangle tmpPlayerRect = new Rectangle();

	public static int MAX_ZEN = 100;
	private int zen = MAX_ZEN / 10;

	// gui sem nastavuje obrazky s listkama, ktere si pak hrac updatuje
	public final Array<Image> leaves = new Array<Image>();

	// cas ktery stravi hrac jako mrtvy; postupne se odecita, az je nula tak se spawne
	private float respawnTimer;

	// gui hracovy tabulky
	private AnimatedImage faceAnimation;
	private Animation okFaceAnimation;
	private Animation koFaceAnimation;


	public Player(Vector2 pos, float width, float height) {
		super();
		position.set(pos);

		this.effect = new Effect(new Texture(Gdx.files.internal("data/effects/player_sheet_1.png")), 3, 8, 0.1f, 1, 0, 0);
		WIDTH = 1 / 32f * (effect.width - 3);
		HEIGHT = 1 / 32f * (effect.height - 15);

		effect.update(0, true); // 0 = Direction.S

		shadowColor = shadowColors[nextShadowColor % shadowColors.length];
		nextShadowColor++;
	}




	@Override
	public int getMaxHealth() {
		return 200;
	}


	/**
	 * Vola se ve chvili kdy hrac umre
	 */
	public void setDead() {
		alive = false;
		zen = 0;
		health = 0;

		updateHearts();
		updateLeaves();

		float timeDead = 5;
		respawnTimer = timeDead;

		Gdx.app.log("", "Player respawn in " + respawnTimer + " seconds");
	}





	@Override
	public void update(float deltaTime) {
		if (respawnTimer > 0) {
			respawnTimer -= deltaTime;

			if (respawnTimer <= 0) {
				respawnTimer = 0;
				alive = true;
				health = getMaxHealth();

				updateHearts();
				updateLeaves();

				Gdx.app.log("", "Player respawning");
			}

			return;
		}

		super.update(deltaTime);
	}




	/**
	 * utok je zavisly na zenu
	 */
	public int getDamage() {
		float percent = zen / (float) MAX_ZEN;
		int dmg = (int) (MAX_DAMAGE * percent);
		return Math.max(MIN_DAMAGE, dmg);
	}


	public void killedEnemy() {
		float part = MAX_ZEN * 0.05f; // 5%
		zen += part;
		zen = Math.min(zen, MAX_ZEN);

		updateLeaves();
	}

	public void killedPlayer() {

	}

	public boolean takePowerup(Powerup powerup) {
		
		return true;
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
	public void updateHearts() {
		super.updateHearts();

		if (faceAnimation != null) {
			faceAnimation.setAnimation(health == 0 ? koFaceAnimation : okFaceAnimation);
		}
	}



	public void setFaceAnimations(AnimatedImage image, Animation ok, Animation ko) {
		faceAnimation = image;
		okFaceAnimation = ok;
		koFaceAnimation = ko;
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



	@Override
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.setColor(shadowColor);
		spriteBatch.draw(shadow, position.x + 0.05f, position.y - 0.2f, WIDTH, HEIGHT);
		spriteBatch.setColor(Color.WHITE);
		super.render(spriteBatch);
	}




}
