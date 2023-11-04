package com.mygdx.game.Naloga2;

import static com.mygdx.game.Naloga2.Assets.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Bonus extends DynamicGameObject
{
    private static final float SPEED = 200f;
    private static final float SPAWN_TIME = 5f;
    private float BonusSpawnTime;
    private int BonusCollected = 0;

    private Array<Rectangle> bonuses;
    private SugarCube sugarCube;

    private Texture bonusTexture;

    private Rectangle bounds;

    private float widthT, heightT;

    public Bonus(Texture texture, float x, float y, float width, float height, SugarCube sugarCube, Array<Rectangle> bonuses) {
        super(texture, x, y, width, height);
        this.sugarCube = sugarCube;
        this.bonuses = bonuses;
        widthT = width;
        heightT = height;
        bonusTexture = texture;
        bounds = new Rectangle(x, y, width, height);
    }



    public int getBonusCollected() {
        return BonusCollected;
    }

    public int setBonusCollected(int BonusCollected) {
        return this.BonusCollected = BonusCollected;
    }

    public void drawBonusCollected(SpriteBatch batch)
    {
        font.setColor(Color.valueOf("#facfa8"));
        font.draw(batch,
                "BONUS: " + getBonusCollected(),
                25f, Gdx.graphics.getHeight() - 100f
        );
    }

    public void update(float delta) {
        //if (elapsedTime - iceCreamSpawnTime > ICE_CREAM_SPAWN_TIME) spawnIceCream();
        BonusSpawnTime += delta; // Increment the spawn timer based on delta

        if (BonusSpawnTime > SPAWN_TIME) {
            spawnBonus();
            BonusSpawnTime = 0; // Reset the spawn timer
        }

        for (Iterator<Rectangle> it = bonuses.iterator(); it.hasNext(); ) {
            Rectangle iceCream = it.next();
            iceCream.y -= SPEED * delta;
            if (iceCream.y + heightT < 0) {
                it.remove();
            }
            if (iceCream.overlaps(sugarCube.getBounds())) {
                BonusCollected++;
                sugarCube.setHealth(sugarCube.getHealth() + 10);
                Assets.IceCreamCollect.play();
                System.out.println("Bonus collected: " + BonusCollected);
                it.remove();
            }
        }
    }

    private void spawnBonus() {
        Rectangle iceCream = new Rectangle();
        iceCream.x = MathUtils.random(0f, Gdx.graphics.getWidth() - widthT);
        iceCream.y = Gdx.graphics.getHeight();
        iceCream.width = widthT;
        iceCream.height = heightT;
        bonuses.add(iceCream);
        //System.out.println(iceCreams);
        BonusSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (Rectangle iceCream : bonuses) {
            batch.draw(bonusTexture, iceCream.x, iceCream.y);
        }
    }

}
