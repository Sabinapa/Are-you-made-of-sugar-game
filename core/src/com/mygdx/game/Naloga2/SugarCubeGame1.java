package com.mygdx.game.Naloga2;

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
	private Bullet bullet;

	float width, height;

	@Override
	public void create () {
		Assets.load();

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		batch = new SpriteBatch();

		bullet = new Bullet(bulletImg, 0, 0, bulletImg.getWidth(), bulletImg.getHeight());

		sugar = new SugarCube(sugarImg, 0, 0, sugarImg.getWidth(), sugarImg.getHeight(), bullet);
		sugar.initializeSugarPosition();

		iceCreams = new Array<>();
		iceCream = new IceCream(iceCreamImg, 0, 0, iceCreamImg.getWidth(), iceCreamImg.getHeight(), sugar, iceCreams);

		waterDrop = new WaterDrop(waterImg, 0, 0, waterImg.getWidth(), waterImg.getHeight(), sugar);

	}

	private void update(float delta) {
		//float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()));
		iceCream.update(delta);
		waterDrop.update(delta);
		bullet.update(delta, waterDrop);

	}

	@Override
	public void render ()
	{
		if (sugar.getHealth() > 0)
		{
			sugar.handleInput();
			update(Gdx.graphics.getDeltaTime());
		}

		batch.begin();

		draw();

		batch.end();
	}

	private void draw()
	{
		batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (sugar.getHealth() <= 0) {
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

		sugar.draw(batch);
		iceCream.draw(batch);
		waterDrop.draw(batch);
		bullet.draw(batch);

		sugar.drawHealth(batch);
		iceCream.drawIceCreamsCollected(batch);

	}

	@Override
	public void dispose () {
		batch.dispose();
		Assets.dispose();

	}

}
