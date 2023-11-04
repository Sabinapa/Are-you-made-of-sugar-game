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
import com.badlogic.gdx.utils.TimeUtils;

public class SugarCubeGame1 extends ApplicationAdapter {
	SpriteBatch batch;

	private SugarCube sugar;
	private IceCream iceCream;

	private Array<Rectangle> iceCreams;

	private WaterDrop waterDrop;

	private Array<Rectangle> waterDrops;
	private Bullet bullet;

	private Array<Rectangle> bullets;

	private Bonus bonus;

	private Array<Rectangle> bonuses;

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
		iceCream = new IceCream(iceCreamImg, 0, 0, iceCreamImg.getWidth(), iceCreamImg.getHeight(), sugar, iceCreams);

		waterDrops = new Array<>();
		waterDrop = new WaterDrop(waterImg, 0, 0, waterImg.getWidth(), waterImg.getHeight(), sugar, waterDrops);

		bonuses = new Array<>();
		bonus = new Bonus(bonusImg, 0, 0, bonusImg.getWidth(), bonusImg.getHeight(), sugar, bonuses);



	}

	private void update(float delta) {
		//float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()));
		iceCream.update(delta);
		waterDrop.update(delta);
		bullet.update(delta, waterDrop);
		bonus.update(delta);

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
		// Po preteku časa izpisa "GAME OVER" nadaljujte z igro
		isGameOver = false;
		// Ponastavite vse spremenljivke in postavke igre
		sugar.initializeSugarPosition();
		sugar.setHealth(100);
		iceCream.setIceCreamsCollected(0);
		bonus.setBonusCollected(0);
		iceCreams.clear();
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
		iceCream.draw(batch);
		waterDrop.draw(batch);
		bullet.draw(batch);
		bonus.draw(batch);

		sugar.drawHealth(batch);
		iceCream.drawIceCreamsCollected(batch);
		bonus.drawBonusCollected(batch);

	}

	@Override
	public void dispose () {
		batch.dispose();
		Assets.dispose();

	}

}
