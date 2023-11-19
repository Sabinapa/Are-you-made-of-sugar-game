package com.mygdx.game.Naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;


public class IceCream extends DynamicGameObject implements Pool.Poolable{
    private static final float SPEED = 100;
    private static final float SPAWN_TIME = 1.0f;
    private static float iceCreamSpawnTime;
    private static int iceCreamsCollected;

    private Texture iceCreamTexture;

    public Rectangle bounds;

    public IceCream(Texture texture) {
        super(texture, 0, 0, texture.getWidth(), texture.getHeight());
        iceCreamTexture = texture;

        bounds = new Rectangle(0, 0, iceCreamTexture.getWidth(), iceCreamTexture.getHeight());

    }
    public static float getIceCreamSpawnTime() {
        return iceCreamSpawnTime;
    }

    public static float getSPAWN_TIME() {
        return SPAWN_TIME;
    }

    public static int getIceCreamsCollected() {
        return iceCreamsCollected;
    }

    public static int setIceCreamsCollected(int iceCreamsCollected) {
        return IceCream.iceCreamsCollected = iceCreamsCollected;
    }

    public static void spawnIceCream(Pool<IceCream> iceCreamPool, Array<IceCream> iceCreams) {
        IceCream iceCream = iceCreamPool.obtain();
        float randomX = MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.iceCreamImg.getWidth());
        float randomY = Gdx.graphics.getHeight();
        iceCream.bounds.x = (int) randomX;
        iceCream.bounds.y = (int) randomY;
        iceCreams.add(iceCream);
        //System.out.println(iceCreams);
        iceCreamSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    @Override
    public void draw(SpriteBatch batch) {
            batch.draw(iceCreamTexture, bounds.x, bounds.y);

    }
    @Override
    public void reset() {
        bounds.y = 0;
        bounds.x = 0;
    }

    public void update(float delta)
    {
        bounds.y -= SPEED * delta;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
