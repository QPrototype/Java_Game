package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MyGdxGame implements ApplicationListener {
	private SpriteBatch batch;
	private TextureAtlas walkingAtlas;
	private TextureAtlas lookingAtlas;
	private Sprite sprite;
	private int currentFrame = 1;
	private String currentAtlasKey = new String("e0000");

	private float destinationX, destinationY;

	@Override
	public void create() {
		batch = new SpriteBatch();
		walkingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/walking.atlas"));
		lookingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/looking.atlas"));

		AtlasRegion region = walkingAtlas.findRegion("walking e0000");

		sprite = new Sprite(region);

		sprite.setPosition(120, 100);
		sprite.scale(2.5f);
		Timer.schedule(new Task(){
						   int x = 50;
						   int y = 100;

						   @Override
						   public void run() {


						   if (destinationX - x > 10) {
						   		if (destinationY > y) {
									x += 10;
									y += 10;
									sprite.setPosition(x, y);
									currentFrame++;
									if (currentFrame > 7) {
										currentFrame = 0;
									}
									currentAtlasKey = String.format("walking ne%04d", currentFrame);
									sprite.setRegion(walkingAtlas.findRegion(currentAtlasKey));
								} else if (y - destinationY > 10) {
									x += 10;
									y -= 10;
									sprite.setPosition(x, y);
									currentFrame++;
									if (currentFrame > 7) {
										currentFrame = 0;
									}
									currentAtlasKey = String.format("walking se%04d", currentFrame);
									sprite.setRegion(walkingAtlas.findRegion(currentAtlasKey));
								} else {
									x += 10;

									sprite.setPosition(x, y);
									currentFrame++;
									if (currentFrame > 7) {
										currentFrame = 0;
									}
									currentAtlasKey = String.format("walking e%04d", currentFrame);
									sprite.setRegion(walkingAtlas.findRegion(currentAtlasKey));
								}

						   }
						   if (x - destinationX > 10) {
						   		if (destinationY - y > 10) {
									x -= 10;
									y += 10;
									sprite.setPosition(x, y);
									currentFrame++;
									if (currentFrame > 7) {
										currentFrame = 0;
									}
									currentAtlasKey = String.format("walking nw%04d", currentFrame);
									sprite.setRegion(walkingAtlas.findRegion(currentAtlasKey));

								} else if (y - destinationY > 10) {
									x -= 10;
									y -= 10;
									sprite.setPosition(x, y);
									currentFrame++;
									if (currentFrame > 7) {
										currentFrame = 0;
									}
									currentAtlasKey = String.format("walking sw%04d", currentFrame);
									sprite.setRegion(walkingAtlas.findRegion(currentAtlasKey));

								} else {
									x -= 10;
									sprite.setPosition(x, y);
									currentFrame++;
									if (currentFrame > 7) {
										currentFrame = 0;
									}
									currentAtlasKey = String.format("walking w%04d", currentFrame);
									sprite.setRegion(walkingAtlas.findRegion(currentAtlasKey));

								}
						   }
						   if (Math.abs(destinationX - x) <= 10) {
							   if (destinationY - y > 10) {
								   y += 10;
								   sprite.setPosition(x, y);
								   currentFrame++;
								   if (currentFrame > 7) {
									   currentFrame = 0;
								   }
								   currentAtlasKey = String.format("walking n%04d", currentFrame);
								   sprite.setRegion(walkingAtlas.findRegion(currentAtlasKey));

							   } else if (y - destinationY > 10){
								   y -= 10;
								   sprite.setPosition(x, y);
								   currentFrame++;
								   if (currentFrame > 7) {
									   currentFrame = 0;
								   }
								   currentAtlasKey = String.format("walking s%04d", currentFrame);
								   sprite.setRegion(walkingAtlas.findRegion(currentAtlasKey));

							   } else {
								   currentAtlasKey = currentAtlasKey.replaceAll("walking", "looking");

								   sprite.setRegion(lookingAtlas.findRegion(currentAtlasKey));


							   }
						   }

						   }
					   }
				,0,1/30.0f);
	}

	@Override
	public void dispose() {
		batch.dispose();
		walkingAtlas.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			destinationX = Gdx.input.getX() - sprite.getWidth()/2;
			destinationY = Gdx.graphics.getHeight() - Gdx.input.getY() - sprite.getHeight()/2;
		}

		batch.begin();
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}