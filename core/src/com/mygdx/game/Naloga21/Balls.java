package com.mygdx.game.Naloga21;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Balls extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private Array<Ball> balls;

    public class Ball {
        Vector2 position;
        Vector2 velocity;
        float radius;
        Color color;

        public Ball(Vector2 initialPosition) {
            position = initialPosition;
            velocity = new Vector2(0, -20); // Začetna hitrost nič
            radius = MathUtils.random(10, 40); // Naključna velikost žogice
            color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1); // Naključna barva
        }

        public void update() {
            float dt = Gdx.graphics.getDeltaTime();
            velocity.y -= 19.81f * dt; // Gravitacija

            position.add(velocity.cpy().scl(dt));

            if (position.y - radius < 0) {
                position.y = radius;
                velocity.y = -velocity.y * 0.9f; // Odboj od tal z izgubo del hitrosti
            }
        }
    }

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        balls = new Array<>();
        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 clickPosition = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
                balls.add(new Ball(clickPosition));
                return true;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (Ball ball : balls) {
            ball.update();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Ball ball : balls) {
            shapeRenderer.setColor(ball.color);
            shapeRenderer.circle(ball.position.x, ball.position.y, ball.radius);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

}
