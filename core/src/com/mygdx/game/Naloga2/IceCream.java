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

public class IceCream extends DynamicGameObject {
    private static final float SPEED = 100f;
    private static final float SPAWN_TIME = 1f;
    private float iceCreamSpawnTime;
    private int iceCreamsCollected = 0;

    private Array<Rectangle> iceCreams;
    private SugarCube sugarCube;

    private Texture iceCreamTexture;

    private Rectangle bounds;

    private float widthT, heightT;

    public IceCream(Texture texture, float x, float y, float width, float height, SugarCube sugarCube, Array<Rectangle> iceCreams) {
        super(texture, x, y, width, height);
        this.sugarCube = sugarCube;
        this.iceCreams = iceCreams;
        widthT = width;
        heightT = height;
        iceCreamTexture = texture;
        bounds = new Rectangle(x, y, width, height);
    }



    public int getIceCreamsCollected() {
        return iceCreamsCollected;
    }

    public int setIceCreamsCollected(int iceCreamsCollected) {
        return this.iceCreamsCollected = iceCreamsCollected;
    }

    public void drawIceCreamsCollected(SpriteBatch batch)
    {
        font.setColor(Color.valueOf("#be605e"));
        font.draw(batch,
                "SCORE: " + getIceCreamsCollected(),
                25f, Gdx.graphics.getHeight() - 60f
        );
    }

    public void update(float delta) {
        //if (elapsedTime - iceCreamSpawnTime > ICE_CREAM_SPAWN_TIME) spawnIceCream();
        iceCreamSpawnTime += delta; // Increment the spawn timer based on delta

        if (iceCreamSpawnTime > SPAWN_TIME) {
            spawnIceCream();
            iceCreamSpawnTime = 0; // Reset the spawn timer
        }

        for (Iterator<Rectangle> it = iceCreams.iterator(); it.hasNext(); ) {
            Rectangle iceCream = it.next();
            iceCream.y -= SPEED * delta;
            if (iceCream.y + heightT < 0) {
                it.remove();
            }
            if (iceCream.overlaps(sugarCube.getBounds())) {
                iceCreamsCollected++;
                Assets.IceCreamCollect.play();
                System.out.println("Ice cream collected: " + iceCreamsCollected);
                it.remove();
            }
        }
    }

    private void spawnIceCream() {
        Rectangle iceCream = new Rectangle();
        iceCream.x = MathUtils.random(0f, Gdx.graphics.getWidth() - widthT);
        iceCream.y = Gdx.graphics.getHeight();
        iceCream.width = widthT;
        iceCream.height = heightT;
        iceCreams.add(iceCream);
        //System.out.println(iceCreams);
        iceCreamSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (Rectangle iceCream : iceCreams) {
            batch.draw(iceCreamTexture, iceCream.x, iceCream.y);
        }
    }

}
