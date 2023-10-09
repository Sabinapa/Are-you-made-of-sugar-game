package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.Rectangle;

public class SugarCubeGame extends ApplicationAdapter {
	SpriteBatch batch;

	private Texture sugarImg;

	private Rectangle sugar;

	private static final float SUGAR_SPEED = 250f;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		sugarImg = new Texture("assets/SugarGame/images/sugar-cube.png");

		sugar = new Rectangle();
		sugar.x = (int) (Gdx.graphics.getWidth() / 2f - sugarImg.getWidth() / 2f);
		sugar.y = (int) 20f;
		sugar.width = sugarImg.getWidth();
		sugar.height = sugarImg.getHeight();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		handleInput();
		batch.begin();

		draw();

		batch.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());
	}

	private void draw()
	{

		batch.draw(sugarImg, sugar.x, sugar.y);

	}

	private void moveLeft(float delta) {
		sugar.x -= SUGAR_SPEED * delta;
		if (sugar.x < 0)
			sugar.x = (int) 0f;
	}

	private void moveRight(float delta) {
		sugar.x += SUGAR_SPEED * delta;
		if (sugar.x > Gdx.graphics.getWidth() - sugarImg.getWidth())
			sugar.x = Gdx.graphics.getWidth() - sugarImg.getWidth();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sugarImg.dispose();
	}
}
