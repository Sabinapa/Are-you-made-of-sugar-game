package com.mygdx.game.Naloga22;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Wheels extends ApplicationAdapter
{
    private Texture wheelTexture;
    private SpriteBatch batch;
    private Sprite wheelSprite;
    private float x, y;
    private float angle;
    private float radius;

    @Override
    public void create() {
        batch = new SpriteBatch();
        wheelTexture = new Texture("assets/SugarGame/images/color.png");
        wheelSprite = new Sprite(wheelTexture);

        x = Gdx.graphics.getWidth() / 2 - wheelSprite.getWidth() / 2;
        y = Gdx.graphics.getHeight() / 2 - wheelSprite.getHeight() / 2;
        angle = 0.0f;
        radius = (float) (wheelSprite.getWidth() / (2 * Math.PI));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Simulirajte vrtenje kolesa za nek kot
        float alpha = 1.0f; // Kot, za katerega se kolo vrti (lahko prilagodite)
        angle += alpha;

        // Izračunajte novo pozicijo kolesa na podlagi formule
        float distance = (float) ((angle * 2 * Math.PI * radius) / 360);
        float newX = (float) (x + distance);

        // Nastavite novo pozicijo in rotacijo kolesa
        wheelSprite.setPosition(newX, y);
        wheelSprite.setRotation(-angle);

        batch.begin();
        wheelSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        wheelTexture.dispose();
    }
}
