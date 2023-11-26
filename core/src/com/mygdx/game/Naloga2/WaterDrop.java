package com.mygdx.game.Naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

public class WaterDrop extends DynamicGameObject  implements Pool.Poolable{

    private static final float SPEED = 170f;
    private static final float DAMAGE = 25f;
    private static final float SPAWN_TIME = 1f;

    private static float waterSpawnTime;

    private TextureAtlas.AtlasRegion waterDropTexture;
    public Rectangle bounds;


    public WaterDrop(TextureAtlas.AtlasRegion texture) {
        super(texture.getTexture(), 0, 0, texture.getRegionWidth(), texture.getRegionHeight());
        waterDropTexture = texture;

        bounds = new Rectangle(0, 0, waterDropTexture.getRegionWidth(), waterDropTexture.getRegionHeight());
    }

    public static float getWaterSpawnTime() {
        return waterSpawnTime;
    }

    public static float getWATER_SPAWN_TIME()
    {
        return SPAWN_TIME;
    }

    public static void spawnWaterDrop(Pool<WaterDrop> waterDropPool, Array<WaterDrop> waterDrops) {
        WaterDrop waterdrop = waterDropPool.obtain();
        float randomX = MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.iceCreamImg.getWidth());
        float randomY = Gdx.graphics.getHeight();
        waterdrop.bounds.x = (int) randomX;
        waterdrop.bounds.y = (int) randomY;
        waterDrops.add(waterdrop);
        waterSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    @Override
    public void draw(SpriteBatch batch) {
            batch.draw(waterDropTexture, bounds.x, bounds.y);
    }

    public void update(float delta) {
        bounds.y -= SPEED * delta;
    }

    public int getDamage() {
        return (int) DAMAGE;
    }

    public void reset() {
        bounds.y = 0;
        bounds.x = 0;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
