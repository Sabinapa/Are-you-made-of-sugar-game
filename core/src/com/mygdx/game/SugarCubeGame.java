package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.Iterator;

public class SugarCubeGame extends ApplicationAdapter {
	SpriteBatch batch;

	private Texture sugarImg;

	private Texture waterImg;

	private Rectangle sugar;

	private Array<Rectangle> waterDrops;

	private float waterSpawnTime; // in sec

	private int health;

	private static final float SUGAR_SPEED = 250f;

	private static final float WATER_SPEED = 150f;

	private static final float WATER_DAMAGE = 25f;

	private static final float WATER_SPAWN_TIME = 2f;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		sugarImg = new Texture("assets/SugarGame/images/sugar-cube.png");
		waterImg = new Texture("assets/SugarGame/images/waterDrop.png");

		sugar = new Rectangle();
		sugar.x = (int) (Gdx.graphics.getWidth() / 2f - sugarImg.getWidth() / 2f);
		sugar.y = (int) 20f;
		sugar.width = sugarImg.getWidth();
		sugar.height = sugarImg.getHeight();

		waterDrops = new Array<>();
		health = 100;
		spawnWater();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);

		if (health > 0)
		{
			handleInput();
			update(Gdx.graphics.getDeltaTime());
		}

		batch.begin();

		draw();

		batch.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());
	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		if (elapsedTime - waterSpawnTime > WATER_SPAWN_TIME) spawnWater();

		for (Iterator<Rectangle> it = waterDrops.iterator(); it.hasNext(); ) {
			Rectangle hammer = it.next();
			hammer.y -= WATER_SPEED * delta;
			if (hammer.y + waterImg.getHeight() < 0) {
				it.remove();
			}
			if (hammer.overlaps(sugar)) {
				health -= WATER_DAMAGE;
				//piggyVoice.play();
				it.remove();
			}
		}
	}

	private void draw()
	{
		for (Rectangle water : waterDrops) {
			batch.draw(waterImg, water.x, water.y);
		}

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

	private void spawnWater() {
		Rectangle water = new com.badlogic.gdx.math.Rectangle();
		water.x = MathUtils.random(0f, Gdx.graphics.getWidth() - waterImg.getWidth());
		water.y = Gdx.graphics.getHeight();
		water.width = waterImg.getWidth();
		water.height = waterImg.getHeight();
		waterDrops.add(water);
		waterSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sugarImg.dispose();
		waterImg.dispose();
	}
}
