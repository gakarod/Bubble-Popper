package com.pointless.bubble;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by vaibh on 02-04-2018.
 */

public class Explosion {
    public static final float FRAME_LENGTH = 0.10f;            // when bubbles explode , explosion is activated
    public static final int OFFSET = 8;
    public static final int SIZE = 50;
    public static final int IMAGE_SIZE = 50;
    private static Animation anim = null;
    float x, y;
    float statetime;

    public boolean remove = false;

    public Explosion (float x, float y) {
        this.x = x - OFFSET;
        this.y = y - OFFSET;
        statetime = 0;
        if (anim == null)
            anim = new Animation(FRAME_LENGTH, TextureRegion.split(new Texture("explosion.png"), IMAGE_SIZE, IMAGE_SIZE)[0]);
    }

    public void update (float deltatime) {
        statetime += deltatime;
        if (anim.isAnimationFinished(statetime))
            remove = true;
    }

    public void render (SpriteBatch sb) {
        sb.draw((TextureRegion) anim.getKeyFrame(statetime), x, y, SIZE, SIZE);
    }
}
