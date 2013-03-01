package cz.emo4d.zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Effect {
	
	private Animation[] animation;
	private int frameCols = 8; 
    private int frameRows = 1;
	
	private Texture animSheet;	
	
	private TextureRegion  currentAnimFrame;    
    private float animStateTime;
	
    
    public Effect(Texture animSheet, int frameCols, int frameRows, float speed, int initialFrame) {  // 0.025
		this.frameCols = frameCols;
		this.frameRows = frameRows;
		this.animSheet = animSheet;

		//walkSheet.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion[][] tmp = TextureRegion.split(this.animSheet,
				this.animSheet.getWidth() / this.frameCols, this.animSheet.getHeight() / this.frameRows);
		
		animation = new Animation[this.frameRows];		
		
		for (int i = 0; i < this.frameRows; i++) {
			animation[i] = new Animation(speed, tmp[i]);
		}

		animStateTime = 0f;	
		currentAnimFrame = animation[initialFrame % this.frameRows].getKeyFrame(animStateTime);
	}
    
    public void update(int animRow, boolean looping) {
    	animStateTime += Gdx.graphics.getDeltaTime();              
		currentAnimFrame = animation[animRow % this.frameRows].getKeyFrame(animStateTime, looping);    	
    }    
    
    public void render(SpriteBatch spriteBatch, float x, float y, float width, float height) {
    	spriteBatch.draw(currentAnimFrame, x, y, width, height);    	
    }
    
    public void render(SpriteBatch spriteBatch, float x, float y) {
    	spriteBatch.draw(currentAnimFrame, x, y);    	
    }
}