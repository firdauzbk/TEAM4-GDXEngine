package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.engine.GameObject;

public class Bullet extends GameObject {
    private static final int BULLET_SPEED = 10;
    private float width;
    private float height;

    public Bullet(float startX, float startY, float width, float height) {
        super(startX, startY, new Texture("bullet.png"));
        this.width = width;
        this.height = height;
    }

    @Override
    public void update() {
        setY(getY() + BULLET_SPEED);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
