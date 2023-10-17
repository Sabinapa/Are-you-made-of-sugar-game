package com.mygdx.game.Naloga2;

import static com.mygdx.game.Naloga2.Assets.iceCreamImg;
import static com.mygdx.game.Naloga2.Assets.sugarImg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class SugarCubeGame1 extends ApplicationAdapter {
	SpriteBatch batch;

	private SugarCube sugar;
	private IceCream IceCream;

	float width, height;


	@Override
	public void create () {
		Assets.load();

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();


		batch = new SpriteBatch();

		sugar = new SugarCube(sugarImg, 0, 0, sugarImg.getWidth(), sugarImg.getHeight());
		sugar.initializeSugarPosition(sugarImg);

		IceCream = new IceCream(iceCreamImg, 0, 0, iceCreamImg.getWidth(), iceCreamImg.getHeight(), sugar);


	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()));
		IceCream.update(elapsedTime, delta);

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

		sugar.draw(batch);
		IceCream.draw(batch);



	}


}
