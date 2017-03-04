package com.mygdx.game;

import character_movement.CharacterMovement;
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
	private CharacterMovement sprite;


	private float destinationX, destinationY;

	@Override
	public void create() {
		batch = new SpriteBatch();
		walkingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/walking.atlas"));
		lookingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/looking.atlas"));

		AtlasRegion region = walkingAtlas.findRegion("walking e0000");

		sprite = new CharacterMovement(walkingAtlas, lookingAtlas, region);

		sprite.setPosition(120, 100);
		sprite.scale(0.1f);
		Timer.schedule(new Task(){

						   @Override
						   public void run() {
							   sprite.walk(destinationX, destinationY);

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