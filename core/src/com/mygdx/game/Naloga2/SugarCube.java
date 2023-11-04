package com.mygdx.game.Naloga2;

import static com.mygdx.game.Naloga2.Assets.bulletImg;
import static com.mygdx.game.Naloga2.Assets.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class SugarCube extends DynamicGameObject
{
    private static final float SPEED = 250f;

    public boolean isInvulnerable = false;
    public long invulnerabilityDuration = 5000; // 5 sekund v milisekundah
    public long invulnerabilityStartTime = 0; // Čas začetka neobčutljivosti

    private int health;
    private Texture sugarTexture;

    private Rectangle bounds;

    private Bullet bullet;
    private float widthT, heightT;


    public SugarCube(Texture texture, float x, float y, float width, float height, Bullet bullet) {
        super(texture, x, y, width, height);
        sugarTexture = texture;
        widthT = width;
        heightT = height;
        this.bullet = bullet;
        health = 100;

        bounds = new Rectangle(x, y, width, height);

    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getWidth() {
        return widthT;
    }

    public float getHeight() {
        return heightT;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    private void updateBounds() {
        bounds.set(position.x, position.y, widthT, heightT);
    }

    public void initializeSugarPosition() {
        position.x = (int) (Gdx.graphics.getWidth() / 2f - widthT / 2f);
        position.y = (int) 20f;
    }

    public void drawHealth(SpriteBatch batch) {
        font.setColor(Color.valueOf("#9cecfc"));
        font.draw(batch,
                "HEALTH: " + getHealth(),
                25f, Gdx.graphics.getHeight() - 20f
        );
    }

    @Override
    public void draw(SpriteBatch batch) {

        batch.draw(sugarTexture, position.x, position.y);

    }

    public void update(float delta) {
        if (isInvulnerable) {
            long currentTime = TimeUtils.millis();
            if (currentTime - invulnerabilityStartTime >= invulnerabilityDuration) {
                isInvulnerable = false;
            }
        }

    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            //bullet.shoot(position.x, position.y, widthT, heightT);
            bullet.shoot(bounds);
            Assets.LaserGun.play();
        }

    }

    private void moveLeft(float delta) {
        position.x -= SPEED * delta;
        if (position.x < 0)
            position.x = (int) 0f;
        updateBounds();
    }

    private void moveRight(float delta) {
        position.x += SPEED * delta;
        if (position.x > Gdx.graphics.getWidth() - widthT)
            position.x = Gdx.graphics.getWidth() - widthT;
        updateBounds();
    }
}
