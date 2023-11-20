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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.util.ViewportUtils;
import com.mygdx.game.util.debug.DebugCameraController;
import com.mygdx.game.util.debug.MemoryInfo;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;

public class SugarCubeGame1 extends ApplicationAdapter {
	SpriteBatch batch;

	private SugarCube sugar;

	private Array<IceCream> iceCreams;
	Pool<IceCream> iceCreamPool;

	private Array<WaterDrop> waterDrops;
	Pool<WaterDrop> waterDropPool;

	private Array<Bullet> bullets;
	Pool<Bullet> bulletPool;

	private Array<Bonus> bonuses;
	Pool<Bonus> bonusPool;

	private boolean isPaused = false;
	private boolean isGameOver = false;

	private long gameOverStartTime = 0; // Timer
	private long gameOverDuration = 5000; // milisenkunde

	private boolean debugMode = false;

	private ShapeRenderer shapeRenderer;

	private OrthographicCamera camera;

	private Viewport viewport;

	private DebugCameraController debugCameraController;
	private MemoryInfo memoryInfo;

	private ParticleEffect starEffect;

	private ParticleEffect fireWorkEffect;




	@Override
	public void create () {
		batch = new SpriteBatch();
		Assets.load();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// debug
		debugCameraController = new DebugCameraController();
		debugCameraController.setStartPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		memoryInfo = new MemoryInfo(500);

		shapeRenderer = new ShapeRenderer();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

		bullets = new Array<>();
		bulletPool = new Pool<Bullet>() {
			@Override
			protected Bullet newObject() {
				return new Bullet(bulletImg);
			}
		};

		sugar = new SugarCube(sugarImg, 0, 0, sugarImg.getWidth(), sugarImg.getHeight());
		sugar.initializeSugarPosition();

		iceCreams = new Array<>();
		iceCreamPool = new Pool<IceCream>() {
			@Override
			protected IceCream newObject() {
				IceCream iceCream = new IceCream(iceCreamImg);
				Gdx.app.log("IceCreamPool", "Created new ice cream: " + iceCream);
				return iceCream;
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

		// Load particle effect
		starEffect = new ParticleEffect();
		starEffect.load(Gdx.files.internal("assets/SugarGame/particles/star.pe"), Gdx.files.internal("assets/SugarGame/particles"));

		fireWorkEffect = new ParticleEffect();
		fireWorkEffect.load(Gdx.files.internal("assets/SugarGame/particles/firework.pe"), Gdx.files.internal("assets/SugarGame/particles"));

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		sugar.update();

		if (elapsedTime - IceCream.getIceCreamSpawnTime() > IceCream.getSPAWN_TIME()) IceCream.spawnIceCream(iceCreamPool, iceCreams);
		if (elapsedTime - WaterDrop.getWaterSpawnTime() > WaterDrop.getWATER_SPAWN_TIME()) WaterDrop.spawnWaterDrop(waterDropPool, waterDrops);
		if (elapsedTime - Bonus.getBonusSpawnTime() > Bonus.getICE_BONUS_TIME()) Bonus.spawnBonus(bonusPool, bonuses);

		//ICECREAM UPDATE
		for (Iterator<IceCream> it = iceCreams.iterator(); it.hasNext(); ) {
			IceCream iceCream = it.next();
			iceCream.update(delta);

			if (iceCream.bounds.y + iceCreamImg.getHeight() < 0)
			{
				System.out.println("IceCream array size before removal: " + iceCreams.size);

				it.remove();
				iceCreamPool.free(iceCream);

				System.out.println("IceCream array size after removal: " + iceCreams.size);
				//iceCream.reset();
			}

			if (iceCream.bounds.overlaps(sugar.getBounds())) {
				IceCream.setIceCreamsCollected(IceCream.getIceCreamsCollected() + 1);
				Assets.IceCreamCollect.play();
				System.out.println("Ice cream collected: " + IceCream.getIceCreamsCollected());

				if (IceCream.getIceCreamsCollected() % 5 == 0) {
					// Trigger firework effect every 5 ice creams
					fireWorkEffect.setPosition(sugar.getBounds().x + sugar.getBounds().width / 2, sugar.getBounds().y + sugar.getBounds().height / 2);
					fireWorkEffect.start();
				}

				it.remove();
				//iceCream.reset();
				iceCreamPool.free(iceCream);
			}
		}

		//WATERDROP
		for (Iterator<WaterDrop> it = waterDrops.iterator(); it.hasNext(); ) {
			WaterDrop water = it.next();
			water.update(delta);

			if (water.bounds.y + waterImg.getHeight() < 0) {
				it.remove();
				//water.reset();
				waterDropPool.free(water);
			}
			if (water.bounds.overlaps(sugar.getBounds())) {
				if (!sugar.isInvulnerable) {
					sugar.setHealth( (sugar.getHealth() - water.getDamage()));
					System.out.println("CurrentHealth: " + sugar.getHealth());
					Assets.waterDropVoice.play();
					it.remove();
					//water.reset();
					waterDropPool.free(water);
				}

			}
		}

		//BULLETS UPDATE
		for (Iterator<Bullet> bulletsit = bullets.iterator(); bulletsit.hasNext(); ) {
				Bullet bullet = bulletsit.next();
				bullet.update(delta);

				for (Iterator<WaterDrop> it = waterDrops.iterator(); it.hasNext(); ) {
					WaterDrop water = it.next();
					if (bullet.bounds.overlaps(water.bounds)) {
						Bullet.setHitObjects(Bullet.getHitObjects() + 1);
						System.out.println("Hit waterDrops number: " + Bullet.getHitObjects());
						it.remove();
						bulletsit.remove();
					}
				}

			if (bullet.bounds.y + bulletImg.getHeight() > Gdx.graphics.getHeight()) {
				bulletsit.remove();
				bulletPool.free(bullet);
				//bullet.reset();
			}
		}

		//BONUS UPDATE
		for (Iterator<Bonus> it = bonuses.iterator(); it.hasNext(); ) {
			Bonus bonus = it.next();
			bonus.update(delta);

			bonus.updateStarEffectPosition(bonus.bounds.x, bonus.bounds.y);

			if (bonus.bounds.y + bonusImg.getHeight() < 0) {
				it.remove();
				bonus.reset();
				bonusPool.free(bonus);
			}
			if (bonus.bounds.overlaps(sugar.getBounds())) {
				Bonus.setBonusCollected(Bonus.getBonusCollected() + 1);
				Assets.IceCreamCollect.play();
				System.out.println("Bonus collected: " + Bonus.getBonusCollected());
				sugar.isInvulnerable = true;
				sugar.invulnerabilityStartTime = TimeUtils.millis();
				it.remove();
				//bonus.reset();
				bonusPool.free(bonus);

			}
		}

	}

	private void toggleDebugMode() {
		debugMode = !debugMode;
	}

	@Override
	public void render ()
	{
		ScreenUtils.clear(0, 0.5f, 0.5f, 1);

		if (!isPaused && sugar.getHealth() > 0)
		{
			sugar.handleInput(bulletPool, bullets);
			update(Gdx.graphics.getDeltaTime());
		}

		if (sugar.getHealth() <= 0 && !isGameOver) {
			isGameOver = true; // Nastavite, da je igra končana
			gameOverStartTime = TimeUtils.millis(); // Posodobite časovnik za začetni čas izpisa

		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			isPaused = !isPaused; // Preklopi med pavzo in nadaljevanjem igre ob pritisku na tipko P
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			toggleDebugMode();
		}

		if (debugMode)
		{
			debugCameraController.handleDebugInput(Gdx.graphics.getDeltaTime());
			memoryInfo.update();
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined);

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

		if(debugMode) {
			renderDebug();
		}
	}

	private void resetGame()
	{
		isGameOver = false;
		sugar.initializeSugarPosition();
		sugar.setHealth(100);
		IceCream.setIceCreamsCollected(0);
		Bonus.setBonusCollected(0);

		Gdx.app.log("IceCreamPool", "Freed ice cream: " + iceCreams.size);
		iceCreamPool.freeAll(iceCreams);
		waterDropPool.freeAll(waterDrops);
		bonusPool.freeAll(bonuses);
		bulletPool.freeAll(bullets);
		iceCreams.clear();
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
				font.setColor(Color.valueOf("#645b77"));
				GlyphLayout layout = new GlyphLayout(font, "GAME OVER");
				float textWidth = layout.width;

				float x = (Gdx.graphics.getWidth() - textWidth) / 2;
				float y = (Gdx.graphics.getHeight() - layout.height) / 2;

				font.draw(batch, "GAME OVER", x, y);
			} else
			{
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

		for(Bullet bullet: bullets)
		{
			bullet.draw(batch);
		}

		for(Bonus bonus: bonuses)
		{
			bonus.draw(batch);

			// Draw star effect on top of the bonus
			starEffect.setPosition(bonus.starEffectX, bonus.starEffectY);
			starEffect.draw(batch, Gdx.graphics.getDeltaTime());
		}

		fireWorkEffect.draw(batch, Gdx.graphics.getDeltaTime());

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

	private void renderDebug() {
		debugCameraController.applyTo(camera);
		batch.begin();
		{
			GlyphLayout layout = new GlyphLayout(Assets.font, "FPS:" + Gdx.graphics.getFramesPerSecond());
			Assets.font.setColor(Color.YELLOW);
			Assets.font.draw(batch, layout, Gdx.graphics.getWidth() - layout.width, Gdx.graphics.getHeight() - 50);

			Assets.font.setColor(Color.YELLOW);
			Assets.font.draw(batch, "RC:" + batch.totalRenderCalls, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 20);

			memoryInfo.render(batch, Assets.font);
		}
		batch.end();

		batch.totalRenderCalls = 0;
		ViewportUtils.drawGrid(viewport, shapeRenderer, 50);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		{
			shapeRenderer.setColor(1, 0, 0, 1);
			for (IceCream icecream : iceCreams) {
				shapeRenderer.rect(icecream.bounds.x, icecream.bounds.y, iceCreamImg.getWidth(), iceCreamImg.getHeight());
			}
			for (WaterDrop waterDrop : waterDrops) {
				shapeRenderer.rect(waterDrop.bounds.x, waterDrop.bounds.y, waterImg.getWidth(), waterImg.getHeight());
			}
			for (Bullet bullet : bullets) {
				shapeRenderer.rect(bullet.bounds.x, bullet.bounds.y, Assets.bulletImg.getWidth(), Assets.bulletImg.getHeight());
			}
			for (Bonus bonus : bonuses) {
				shapeRenderer.rect(bonus.bounds.x, bonus.bounds.y, bonusImg.getWidth(), bonusImg.getHeight());
			}
			shapeRenderer.rect(sugar.getBounds().x, sugar.getBounds().y, sugarImg.getWidth(), sugarImg.getHeight());
		}
		shapeRenderer.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		Assets.dispose();

	}

}
