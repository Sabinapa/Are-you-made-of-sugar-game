package com.mygdx.game.Naloga2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Bullet extends DynamicGameObject {
    private int hitObjects = 0;
    private static final float SPEED = 100f;

    private Texture BulletTexture;
    private Array<Rectangle> bullets;
    private float widthT, heightT;

    public Bullet(Texture texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
        widthT = width;
        heightT = height;

        BulletTexture = texture;
        bullets = new Array<>();
    }

    public void update(float delta, WaterDrop waterDrop) {
        for (Iterator<Rectangle> bulletsit = bullets.iterator(); bulletsit.hasNext(); ) {
            Rectangle bullet = bulletsit.next();
            bullet.y += SPEED * delta;

            for (Iterator<Rectangle> it = waterDrop.getWaterDrops().iterator(); it.hasNext(); ) {
                Rectangle water = it.next();
                if (bullet.overlaps(water)) {
                    hitObjects++;
                    System.out.println("Hit waterDrops number: " + hitObjects);
                    it.remove();
                    bulletsit.remove();
                }
            }
        }
    }

    public void shoot(Rectangle bounds) {
        Rectangle bullet = new Rectangle();
        System.out.println("shot");
        bullet.x = bounds.x + 10 + bounds.width / 2 - widthT / 2;
        bullet.y = bounds.y + bounds.height ;
        bullet.width = widthT;
        bullet.height = heightT;
        bullets.add(bullet);

    }

    @Override
    public void draw(SpriteBatch batch)
    {
        for (Rectangle bullet : bullets) {
            batch.draw(BulletTexture, bullet.x, bullet.y);
        }

    }
}
