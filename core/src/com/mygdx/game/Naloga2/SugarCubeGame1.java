package com.mygdx.game.Naloga2;

import static com.mygdx.game.Naloga2.Assets.bonusImg;
import static com.mygdx.game.Naloga2.Assets.bulletImg;
import static com.mygdx.game.Naloga2.Assets.font;
import static com.mygdx.game.Naloga2.Assets.iceCreamImg;
import static com.mygdx.game.Naloga2.Assets.sugarImg;
import static com.mygdx.game.Naloga2.Assets.waterImg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class SugarCubeGame1 extends ApplicationAdapter {
	SpriteBatch batch;

	private SugarCube sugar;

	private Array<IceCream> iceCreams;

	Pool<IceCream> iceCreamPool;

	private Array<WaterDrop> waterDrops;

	Pool<WaterDrop> waterDropPool;

	private Bullet bullet;

	private Array<Rectangle> bullets;

	private Array<Bonus> bonuses;

	Pool<Bonus> bonusPool;

	float width, height;

	private boolean isPaused = false; // Spremenljivka za sledenje stanja pavze
	private boolean isGameOver = false; // Spremenljivka za sledenje stanja konca igre

	private long gameOverStartTime = 0; // Časovnik za sledenje začetnega časa izpisa "GAME OVER"
	private long gameOverDuration = 5000; // Čas, koliko časa bo sporočilo "GAME OVER" prikazano (v milisekundah)


	@Override
	public void create () {
		Assets.load();

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		batch = new SpriteBatch();

		bullets = new Array<>();
		bullet = new Bullet(bulletImg, 0, 0, bulletImg.getWidth(), bulletImg.getHeight(), bullets);

		sugar = new SugarCube(sugarImg, 0, 0, sugarImg.getWidth(), sugarImg.getHeight(), bullet);
		sugar.initializeSugarPosition();

		iceCreams = new Array<>();
		iceCreamPool = new Pool<IceCream>() {
			@Override
			protected IceCream newObject() {
				return new IceCream(iceCreamImg);
			}
		};

		waterDrops = new Array<>();
		waterDropPool = new Pool<WaterDrop>() {
			@Override
			protected WaterDrop newObject() {
				return new WaterDrop(waterImg);
			}
		};



		bonuses = new Array<>();
		bonusPool = new Pool<Bonus>() {
			@Override
			protected Bonus newObject() {
				return new Bonus(bonusImg);
			}
		};




	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		sugar.update(delta);

		//ICECREAM UPDATE
		if (elapsedTime - IceCream.getIceCreamSpawnTime() > IceCream.getSPAWN_TIME()) IceCream.spawnIceCream(iceCreamPool, iceCreams);
		if (elapsedTime - WaterDrop.getWaterSpawnTime() > WaterDrop.getWATER_SPAWN_TIME()) WaterDrop.spawnWaterDrop(waterDropPool, waterDrops);
		if (elapsedTime - Bonus.getBonusSpawnTime() > Bonus.getICE_BONUS_TIME()) Bonus.spawnBonus(bonusPool, bonuses);

		for (Iterator<IceCream> it = iceCreams.iterator(); it.hasNext(); ) {
			IceCream iceCream = it.next();
			iceCream.update(delta);

			// Preveri, ali je ledena krema dosežena spodnji rob zaslona.
			if (iceCream.bounds.y + iceCreamImg.getHeight() < 0) {
				it.remove();
				// Pripeljite ledeno kremo nazaj v bazen.
				iceCreamPool.free(iceCream);
				iceCream.reset();
			}

			// Preveri, ali ledena krema prekriva SugarCube.
			if (iceCream.bounds.overlaps(sugar.getBounds())) {
				IceCream.setIceCreamsCollected(IceCream.getIceCreamsCollected() + 1);
				Assets.IceCreamCollect.play();
				System.out.println("Ice cream collected: " + IceCream.getIceCreamsCollected());
				it.remove();
				iceCream.reset();
				iceCreamPool.free(iceCream);
			}
		}
		//WATERDROP
		for (Iterator<WaterDrop> it = waterDrops.iterator(); it.hasNext(); ) {
			WaterDrop water = it.next();
			water.update(delta);

			if (water.bounds.y + waterImg.getHeight() < 0) {
				it.remove();
				water.reset();
				waterDropPool.free(water);
			}
			if (water.bounds.overlaps(sugar.getBounds())) {
				if (!sugar.isInvulnerable) {
					sugar.setHealth( (sugar.getHealth() - water.getDamage()));
					System.out.println("CurrentHealth: " + sugar.getHealth());
					Assets.waterDropVoice.play();
					it.remove();
					water.reset();
					waterDropPool.free(water);
				}

			}
		}

		//bullet.update(delta, waterDrop);
		//BONUS UPDATE
		for (Iterator<Bonus> it = bonuses.iterator(); it.hasNext(); ) {
			Bonus bonus = it.next();
			bonus.update(delta);

			if (bonus.bounds.y + bonusImg.getHeight() < 0) {
				it.remove();
				bonusPool.free(bonus);
				bonus.reset();
			}
			if (bonus.bounds.overlaps(sugar.getBounds())) {
				Bonus.setBonusCollected(Bonus.getBonusCollected() + 1);
				Assets.IceCreamCollect.play();
				System.out.println("Bonus collected: " + Bonus.getBonusCollected());
				sugar.isInvulnerable = true;
				sugar.invulnerabilityStartTime = TimeUtils.millis();
				it.remove();
				bonus.reset();
				bonusPool.free(bonus);
			}
		}

	}

	@Override
	public void render ()
	{
		if (!isPaused && sugar.getHealth() > 0)
		{
			sugar.handleInput();
			update(Gdx.graphics.getDeltaTime());
		}

		if (sugar.getHealth() <= 0 && !isGameOver) {
			isGameOver = true; // Nastavite, da je igra končana
			gameOverStartTime = TimeUtils.millis(); // Posodobite časovnik za začetni čas izpisa
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			isPaused = !isPaused; // Preklopi med pavzo in nadaljevanjem igre ob pritisku na tipko P
		}

		batch.begin();

		if (isPaused) //Izpis paused
		{
			font.setColor(Color.valueOf("#645b77"));
			GlyphLayout layout = new GlyphLayout(font, "PAUSED");
			float textWidth = layout.width;

			float x = (Gdx.graphics.getWidth() - textWidth) / 2;
			float y = (Gdx.graphics.getHeight() - layout.height) / 2;

			font.draw(batch,
					"PAUSED",
					x, y
			);
		}
		else
		{
			draw();
		}

		batch.end();
	}

	private void resetGame()
	{
		isGameOver = false;

		sugar.initializeSugarPosition();
		sugar.setHealth(100);
		IceCream.setIceCreamsCollected(0);
		Bonus.setBonusCollected(0);
		bonuses.clear();
		waterDrops.clear();
		bonuses.clear();
		bullets.clear();
	}

	private void draw()
	{
		batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (isGameOver)
		{
			long currentTime = TimeUtils.millis(); // Trenuten čas

			if (currentTime - gameOverStartTime <= gameOverDuration) {
				// Igra je končana in sporočilo "GAME OVER" se še vedno izpisuje
				font.setColor(Color.valueOf("#645b77"));
				GlyphLayout layout = new GlyphLayout(font, "GAME OVER");
				float textWidth = layout.width;

				float x = (Gdx.graphics.getWidth() - textWidth) / 2;
				float y = (Gdx.graphics.getHeight() - layout.height) / 2;

				font.draw(batch, "GAME OVER", x, y);
			} else {
				resetGame();
			}
		}

		sugar.draw(batch);

		for(IceCream iceCream: iceCreams)
		{
			iceCream.draw(batch);
		}

		for(WaterDrop waterdrop: waterDrops)
		{
			waterdrop.draw(batch);
		}

		bullet.draw(batch);

		for(Bonus bonus: bonuses)
		{
			bonus.draw(batch);
		}

		sugar.drawHealth(batch);
		font.setColor(Color.valueOf("#be605e"));
		font.draw(batch,
				"SCORE: " + IceCream.getIceCreamsCollected(),
				25f, Gdx.graphics.getHeight() - 60f
		);

		font.setColor(Color.valueOf("#facfa8"));
		font.draw(batch,
				"BONUS: " + Bonus.getBonusCollected(),
				25f, Gdx.graphics.getHeight() - 100f
		);

	}

	@Override
	public void dispose () {
		batch.dispose();
		Assets.dispose();

	}

}
