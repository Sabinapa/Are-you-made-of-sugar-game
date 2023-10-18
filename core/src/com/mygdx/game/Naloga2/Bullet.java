package com.mygdx.game.Naloga2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Bullet extends DynamicGameObject {

    private SugarCube sugarCube;

    private WaterDrop waterDrop;

    private Texture BulletTexture;
    private Array<Rectangle> bullets;
    private Rectangle bounds;

    private int hitObjects = 0;

    private static final float BULLET_SPEED = 100f;

    public Bullet(Texture texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
        BulletTexture = texture;
        this.waterDrop = waterDrop;
        this.sugarCube = sugarCube;
        //this.waterDrop = waterDrop;
        bounds = new Rectangle(x, y, width, height);
        bullets = new Array<>();

    }

    public void update(float elapsedTime, float delta, WaterDrop waterDrop) {
        for (Iterator<Rectangle> bulletsit = bullets.iterator(); bulletsit.hasNext(); ) {
            Rectangle bullet = bulletsit.next();
            bullet.y += BULLET_SPEED * delta;

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

    public void shoot(float sugarX, float sugarY, float sugarWidth, float sugarHeight) {
        Rectangle bullet = new Rectangle();
        System.out.println("shot");
        bullet.x = sugarX + 10 + sugarWidth / 2 - BulletTexture.getWidth() / 2;
        bullet.y = sugarY + sugarHeight ;
        bullet.width = BulletTexture.getWidth();
        bullet.height = BulletTexture.getHeight();
        bullets.add(bullet);

    }

    public void draw(SpriteBatch batch)
    {
        for (Rectangle bullet : bullets) {
            batch.draw(BulletTexture, bullet.x, bullet.y);
        }

    }
}
