package com.mygdx.game.Naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class WaterDrop extends DynamicGameObject {

    private static final float SPEED = 170f;
    private static final float DAMAGE = 25f;
    private static final float SPAWN_TIME = 1f;

    private float waterSpawnTime;
    private SugarCube sugarCube;

    private Texture waterDropTexture;
    public Array<Rectangle> waterDrops;
    private Rectangle bounds;

    private float widthT, heightT;

    public WaterDrop(Texture texture, float x, float y, float width, float height, SugarCube sugarCube) {
        super(texture, x, y, width, height);
        this.sugarCube = sugarCube;
        widthT = width;
        heightT = height;

        waterDrops = new Array<>();
        waterDropTexture = texture;

        bounds = new Rectangle(x, y, width, height);
    }

    public Array getWaterDrops() {
        return waterDrops;
    }

    public void update(float delta) {
        //if (elapsedTime - waterSpawnTime > WATER_SPAWN_TIME) spawnWater();
        waterSpawnTime += delta; // Increment the spawn timer based on delta

        if (waterSpawnTime > SPAWN_TIME) {
            spawnWaterDrop();
            waterSpawnTime = 0; // Reset the spawn timer
        }

        for (Iterator<Rectangle> it = waterDrops.iterator(); it.hasNext(); ) {
            Rectangle water = it.next();

            water.y -= SPEED * delta;
            if (water.y + heightT < 0) {
                it.remove();
            }
            if (water.overlaps(sugarCube.getBounds())) {
                sugarCube.setHealth((int) (sugarCube.getHealth() - DAMAGE));
                System.out.println("CurrentHealth: " + sugarCube.getHealth());
                Assets.waterDropVoice.play();
                it.remove();
            }
        }
    }

    private void spawnWaterDrop() {
        Rectangle water = new com.badlogic.gdx.math.Rectangle();
        water.x = MathUtils.random(0f, Gdx.graphics.getWidth() - widthT);
        water.y = Gdx.graphics.getHeight();
        water.width = widthT;
        water.height = heightT;
        waterDrops.add(water);
        waterSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (Rectangle waterDrop : waterDrops) {
            batch.draw(waterDropTexture, waterDrop.x, waterDrop.y);
        }
    }

}
