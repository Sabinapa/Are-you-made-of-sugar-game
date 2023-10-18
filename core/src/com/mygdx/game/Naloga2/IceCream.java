package com.mygdx.game.Naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class IceCream extends DynamicGameObject {
    private static final float ICE_CREAM_SPEED = 100f;
    private static final float ICE_CREAM_SPAWN_TIME = 1f;
    private float iceCreamSpawnTime;
    private int iceCreamsCollected = 0;

    private SugarCube sugarCube;

    private Texture iceCreamTexture;
    private Array<Rectangle> iceCreams;
    private Texture IceCreamTexture;
    private Rectangle bounds;

    public IceCream(Texture texture, float x, float y, float width, float height, SugarCube sugarCube) {
        super(texture, x, y, width, height);
        this.iceCreamTexture = texture;
        this.sugarCube = sugarCube;
        //IceCreamTexture = texture;
        iceCreams = new Array<>();
        bounds = new Rectangle(x, y, width, height);
    }

    public void update(float elapsedTime, float delta) {
        //if (elapsedTime - iceCreamSpawnTime > ICE_CREAM_SPAWN_TIME) spawnIceCream();
        iceCreamSpawnTime += delta; // Increment the spawn timer based on delta

        if (iceCreamSpawnTime > ICE_CREAM_SPAWN_TIME) {
            spawnIceCream();
            iceCreamSpawnTime = 0; // Reset the spawn timer
        }

        for (Iterator<Rectangle> it = iceCreams.iterator(); it.hasNext(); ) {
            Rectangle iceCream = it.next();
            iceCream.y -= ICE_CREAM_SPEED * delta;
            if (iceCream.y + iceCreamTexture.getHeight() < 0) {
                it.remove();
            }
            if (iceCream.overlaps(sugarCube.getBounds())) {
                iceCreamsCollected++;
                System.out.println("Ice cream collected: " + iceCreamsCollected);
                it.remove();
            }
        }
    }

    private void spawnIceCream() {
        Rectangle iceCream = new Rectangle();
        iceCream.x = MathUtils.random(0f, Gdx.graphics.getWidth() - iceCreamTexture.getWidth());
        iceCream.y = Gdx.graphics.getHeight();
        iceCream.width = iceCreamTexture.getWidth();
        iceCream.height = iceCreamTexture.getHeight();
        iceCreams.add(iceCream);
        //System.out.println(iceCreams);
        iceCreamSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
    }

    public void draw(SpriteBatch batch) {
        for (Rectangle iceCream : iceCreams) {
            batch.draw(iceCreamTexture, iceCream.x, iceCream.y);
        }
    }

}
