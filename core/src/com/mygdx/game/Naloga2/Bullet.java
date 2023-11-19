package com.mygdx.game.Naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Bullet extends DynamicGameObject  implements Pool.Poolable{
    private static int hitObjects;
    private static final float SPEED = 100f;

    private Texture BulletTexture;

    public Rectangle bounds;

    public Bullet(Texture texture) {
        super(texture, 0, 0, texture.getWidth(), texture.getHeight());

        BulletTexture = texture;

        bounds = new Rectangle(0, 0, BulletTexture.getWidth(), BulletTexture.getHeight());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public static void setHitObjects(int hitObjects) {
        Bullet.hitObjects = hitObjects;

    }

    public static int getHitObjects() {
        return hitObjects;
    }

    public static void shoot(Rectangle bounds, Array<Bullet> bullets, Pool<Bullet> bulletPool) {
        Bullet bullet = bulletPool.obtain();
        System.out.println("shot");
        bullet.bounds.setPosition(bounds.x + bounds.width / 2 - Assets.bulletImg.getWidth() / 2f, bounds.y + bounds.height);
        bullets.add(bullet);

    }

    @Override
    public void draw(SpriteBatch batch)
    {
            batch.draw(BulletTexture, bounds.x, bounds.y);
    }

    public void update(float delta) {
        bounds.y += SPEED * delta;
    }

    public void reset() {
        bounds.set(0, 0, BulletTexture.getWidth(), BulletTexture.getHeight());
    }
}
