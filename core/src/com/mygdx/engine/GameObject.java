package com.mygdx.engine;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullet;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected Texture texture;
    
    public float getWidth() {
        return getWidth(); // width = texture width size
    }

    public float getHeight() {
        return getHeight(); // height = texture height size
    }

    public GameObject(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    public abstract void update();

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Texture getTexture() {
        return texture;
    }
}
