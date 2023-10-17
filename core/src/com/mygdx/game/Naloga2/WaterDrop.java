package com.mygdx.game.Naloga2;

import static com.mygdx.game.Naloga2.Assets.waterImg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class WaterDrop extends DynamicGameObject {

    private static final float WATER_SPEED = 170f;
    private static final float WATER_DAMAGE = 25f;
    private static final float WATER_SPAWN_TIME = 1f;
    private SugarCube sugarCube;

    private float waterSpawnTime;
    private Texture waterDropTexture;
    private Array<Rectangle> waterDrops;
    private Rectangle bounds;

    public WaterDrop(Texture texture, float x, float y, float width, float height, SugarCube sugarCube) {
        super(texture, x, y, width, height);
        this.waterDropTexture = texture;
        this.sugarCube = sugarCube;
        //waterDropTexture = texture;
        this.waterDrops = new Array<>();
        bounds = new Rectangle(x, y, width, height);
    }

    public void update(float elapsedTime, float delta) {
        //if (elapsedTime - waterSpawnTime > WATER_SPAWN_TIME) spawnWater();
        waterSpawnTime += delta; // Increment the spawn timer based on delta

        if (waterSpawnTime > WATER_SPAWN_TIME) {
            spawnWaterDrop();
            waterSpawnTime = 0; // Reset the spawn timer
        }

        for (Iterator<Rectangle> it = waterDrops.iterator(); it.hasNext(); ) {
            Rectangle water = it.next();

            water.y -= WATER_SPEED * delta;
            if (water.y + waterImg.getHeight() < 0) {
                it.remove();
            }
            if (water.overlaps(sugarCube.getBounds())) {
                sugarCube.setHealth((int) (sugarCube.getHealth() - WATER_DAMAGE));
                System.out.println("CurrentHealth: " + sugarCube.getHealth());
                //waterDropVoice.play();
                it.remove();
            }
        }
    }

    private void spawnWaterDrop() {
        Rectangle water = new com.badlogic.gdx.math.Rectangle();
        water.x = MathUtils.random(0f, Gdx.graphics.getWidth() - waterImg.getWidth());
        water.y = Gdx.graphics.getHeight();
        water.width = waterImg.getWidth();
        water.height = waterImg.getHeight();
        waterDrops.add(water);
        waterSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    public void draw(SpriteBatch batch) {
        for (Rectangle iceCream : waterDrops) {
            batch.draw(waterDropTexture, iceCream.x, iceCream.y);
        }
    }

}
