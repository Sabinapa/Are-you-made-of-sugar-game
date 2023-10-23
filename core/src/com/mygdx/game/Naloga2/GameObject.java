package com.mygdx.game.Naloga2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.awt.Rectangle;

public abstract class GameObject {
    public final Vector2 position;
    public Rectangle bounds;

    public GameObject (float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle((int) (x - width / 2), (int) (y - height / 2), (int) width, (int) height);
    }

    public abstract void draw(SpriteBatch batch);
}
