package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.Iterator;

public class SugarCubeGame extends ApplicationAdapter {
	SpriteBatch batch;

	private BitmapFont font;

	private Sound waterDropVoice;

	private Sound IceCreamCollect;

	private Texture background;

	private Texture sugarImg;

	private Texture waterImg;

	private Texture iceCreamImg;

	private Texture bulletImg;

	private Rectangle sugar;

	private Array<Rectangle> waterDrops;

	private Array<Rectangle> bullets;

	private Array<Rectangle> iceCreams;

	private float iceCreamSpawnTime;  // in sec
	private int iceCreamsCollected;

	private float waterSpawnTime; // in sec

	private int health;

	private int hitObjects;

	private static final float SUGAR_SPEED = 250f;

	private static final float WATER_SPEED = 170f;

	private static final float WATER_DAMAGE = 25f;

	private static final float WATER_SPAWN_TIME = 1f;

	private static final float ICE_CREAM_SPEED = 100f;
	private static final float ICE_CREAM_SPAWN_TIME = 1f;    // in sec
	private static final float BULLET_SPEED = 100f;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		sugarImg = new Texture("assets/SugarGame/images/sugar-cube.png");
		waterImg = new Texture("assets/SugarGame/images/waterDrop1.png");
		background = new Texture("assets/SugarGame/images/backgroundClouds.png");
		iceCreamImg = new Texture("assets/SugarGame/images/iceCream.png");
		bulletImg = new Texture("assets/SugarGame/images/bullet.png");

		waterDropVoice = Gdx.audio.newSound(Gdx.files.internal("assets/SugarGame/sounds/water_drop_1.wav"));
		IceCreamCollect = Gdx.audio.newSound(Gdx.files.internal("assets/SugarGame/sounds/plop-effect.wav"));

		font = new BitmapFont(Gdx.files.internal("assets/SugarGame/fonts/arial-32.fnt"));

		sugar = new Rectangle();
		sugar.x = (int) (Gdx.graphics.getWidth() / 2f - sugarImg.getWidth() / 2f);
		sugar.y = (int) 20f;
		sugar.width = sugarImg.getWidth();
		sugar.height = sugarImg.getHeight();

		waterDrops = new Array<>();
		health = 100;
		spawnWater();

		iceCreams = new Array<>();
		iceCreamsCollected = 0;
		spawnIceCream();

		bullets = new Array<>();

		hitObjects = 0;
	}

	@Override
	public void render () {

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

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			shoot();
		}
	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		if (elapsedTime - iceCreamSpawnTime > ICE_CREAM_SPAWN_TIME) spawnIceCream();
		if (elapsedTime - waterSpawnTime > WATER_SPAWN_TIME) spawnWater();

		for (Iterator<Rectangle> it = iceCreams.iterator(); it.hasNext(); ) {
			Rectangle coin = it.next();
			coin.y -= ICE_CREAM_SPEED * delta;
			if (coin.y + iceCreamImg.getHeight() < 0) {
				it.remove();
			}
			if (coin.overlaps(sugar)) {
				iceCreamsCollected++;
				IceCreamCollect.play();
				it.remove();
			}
		}

		for (Iterator<Rectangle> bulletsit = bullets.iterator(); bulletsit.hasNext(); ) {
			Rectangle bullet = bulletsit.next();
			bullet.y += BULLET_SPEED * delta;

			for (Iterator<Rectangle> it = waterDrops.iterator(); it.hasNext(); ) {
				Rectangle water = it.next();
				if (bullet.overlaps(water)) {
					hitObjects++;
					System.out.println("Hit waterDrops number: " + hitObjects);
					it.remove();
					bulletsit.remove();
				}
			}
		}

			for (Iterator<Rectangle> it = waterDrops.iterator(); it.hasNext(); ) {
				Rectangle water = it.next();

				water.y -= WATER_SPEED * delta;
				if (water.y + waterImg.getHeight() < 0) {
					it.remove();
				}
				if (water.overlaps(sugar)) {
					health -= WATER_DAMAGE;
					waterDropVoice.play();
					it.remove();
				}
			}
	}

	private void draw()
	{
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (health <= 0) {
			font.setColor(Color.valueOf("#645b77"));
			GlyphLayout layout = new GlyphLayout(font, "GAME OVER");
			float textWidth = layout.width;

			float x = (Gdx.graphics.getWidth() - textWidth) / 2;
			float y = (Gdx.graphics.getHeight() - layout.height) / 2;

			font.draw(batch,
					"GAME OVER",
					x, y
			);
			return;
		}

		for (Rectangle bullet : bullets) {
			batch.draw(bulletImg, bullet.x, bullet.y);
		}

		for (Rectangle iceCream : iceCreams) {
			batch.draw(iceCreamImg, iceCream.x, iceCream.y);
		}

		for (Rectangle water : waterDrops) {
			batch.draw(waterImg, water.x, water.y);
		}

		batch.draw(sugarImg, sugar.x, sugar.y);

		font.setColor(Color.valueOf("#9cecfc"));
		font.draw(batch,
				"HEALTH: " + health,
				25f, Gdx.graphics.getHeight() - 20f
		);

		font.setColor(Color.valueOf("#be605e"));
		font.draw(batch,
				"SCORE: " + iceCreamsCollected,
				25f, Gdx.graphics.getHeight() - 60f
		);

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

	private void spawnIceCream() {
		Rectangle coin = new Rectangle();
		coin.x = MathUtils.random(0f, Gdx.graphics.getWidth() - iceCreamImg.getWidth());
		coin.y = Gdx.graphics.getHeight();
		coin.width = iceCreamImg.getWidth();
		coin.height = iceCreamImg.getHeight();
		iceCreams.add(coin);
		iceCreamSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;    // 1 second = 1000 miliseconds
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

	private void shoot() {
		Rectangle bullet = new Rectangle();
		bullet.x = sugar.x + sugar.getWidth() / 2 - bulletImg.getWidth() / 2;
		bullet.y = sugar.y + sugar.getHeight();
		bullet.width = bulletImg.getWidth();
		bullet.height = bulletImg.getHeight();
		bullets.add(bullet);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sugarImg.dispose();
		waterImg.dispose();
		iceCreamImg.dispose();
		font.dispose();
		bulletImg.dispose();
		IceCreamCollect.dispose();

	}
}
