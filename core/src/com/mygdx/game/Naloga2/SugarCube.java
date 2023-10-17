package com.mygdx.game.Naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class SugarCube extends DynamicGameObject
{
    private static final float SUGAR_SPEED = 250f;
    private int health;
    private Texture sugarTexture;

    private Rectangle bounds;

    private float width, height;

    public SugarCube(Texture texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
        sugarTexture = texture;
        this.width = width;
        this.height = height;
        health = 100;

        bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }


    public void initializeSugarPosition(Texture sugarImg) {
        position.x = (int) (Gdx.graphics.getWidth() / 2f - sugarImg.getWidth() / 2f);
        position.y = (int) 20f;
    }

    public int getHealth() {
        return health;
    }

    public void draw(SpriteBatch batch) {

        batch.draw(sugarTexture, position.x, position.y);

    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());

    }

    private void moveLeft(float delta) {
        position.x -= SUGAR_SPEED * delta;
        if (position.x < 0)
            position.x = (int) 0f;
        updateBounds();
    }

    private void moveRight(float delta) {
        position.x += SUGAR_SPEED * delta;
        if (position.x > Gdx.graphics.getWidth() - sugarTexture.getWidth())
            position.x = Gdx.graphics.getWidth() - sugarTexture.getWidth();
        updateBounds();
    }


    private void updateBounds() {
        bounds.set(position.x, position.y, width, height);
    }


}
