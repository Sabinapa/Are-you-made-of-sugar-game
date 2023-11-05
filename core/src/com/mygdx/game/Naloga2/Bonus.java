package com.mygdx.game.Naloga2;

import static com.mygdx.game.Naloga2.Assets.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Bonus extends DynamicGameObject  implements Pool.Poolable
{
    private static final float SPEED = 200f;
    private static final float SPAWN_TIME = 5f;
    private static float BonusSpawnTime;

    public static int BonusCollected;

    private Texture bonusTexture;

    public Rectangle bounds;


    public Bonus(Texture texture) {
        super(texture, 0, 0, texture.getWidth(), texture.getHeight());
        bonusTexture = texture;

        bounds = new Rectangle(0, 0, bonusTexture.getWidth(), bonusTexture.getHeight());
    }

    public static float getBonusSpawnTime() {
        return BonusSpawnTime;
    }

    public static float getICE_BONUS_TIME()
    {
        return SPAWN_TIME;
    }


    public static int getBonusCollected() {
        return BonusCollected;
    }

    public static int setBonusCollected(int BonusCollected) {
        return Bonus.BonusCollected = BonusCollected;
    }

    public static void spawnBonus(Pool<Bonus> bonusPool, Array<Bonus> bonuses) {
        Bonus bonus = bonusPool.obtain();
        float randomX = MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.iceCreamImg.getWidth());
        float randomY = Gdx.graphics.getHeight();
        bonus.bounds.x = (int) randomX;
        bonus.bounds.y = (int) randomY;
        bonuses.add(bonus);
        //System.out.println(iceCreams);
        BonusSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    @Override
    public void draw(SpriteBatch batch) {
            batch.draw(bonusTexture, bounds.x, bounds.y);
    }

    public void update(float delta)
    {
        bounds.y -= SPEED * delta;
    }

    public void reset() {
        bounds.y = 0;
        bounds.x = 0;
    }
}
