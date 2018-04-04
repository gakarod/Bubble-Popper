package com.pointless.bubble;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

/**
 * Created by vaibh on 04-04-2018.
 */

public class Meteor {                     //rocks that hurl accordingly
    public int SPEED = 250;
    public static final int radius = 15;
    public  int TILE_WIDTH=64;
    public  int TILE_HEIGHT=64 ;
    private static Texture texture;
    private Animation animation;
    private float animationTimer = 0;
    public static final float FRAME_DURATION = .50F;
    public final Circle MeteorCircle;

    float x, y;

    public boolean remove = false;

    public Meteor(float y ) {

        texture = new Texture("meteor.png");
        TextureRegion[][] textures = new TextureRegion(texture).split(TILE_WIDTH, TILE_HEIGHT);

        animation = new Animation(FRAME_DURATION, textures[0][0],textures[0][1]);

        animation.setPlayMode(Animation.PlayMode.LOOP);
        this.x = GameScreen.WORLD_WIDTH;
        this.y = y;

        MeteorCircle = new Circle(x , y , radius);
    }


    public void update (float deltaTime) {
        x -= SPEED * deltaTime;
        animationTimer += deltaTime;


        if (x < - GameScreen.WORLD_WIDTH)
            remove = true;


        updateCollisionCircle();

    }

    private void updateCollisionCircle() {
        MeteorCircle.setX(x);
        MeteorCircle.setY(y);
    }

    public boolean ispopperColliding(popper pop) {
        Circle popperCollisionCircle = pop.getCollisionCircle();
        return
                Intersector.overlaps(popperCollisionCircle, MeteorCircle) ;

    }


    public void render (SpriteBatch sb) {
        TextureRegion asteroidSlice = (TextureRegion) animation.getKeyFrame(animationTimer);
        float textureX = MeteorCircle.x - TILE_WIDTH / 2;
        float textureY = MeteorCircle.y - TILE_HEIGHT / 2;
        sb.draw(asteroidSlice, textureX, textureY);
    }


    public float getX () {
        return x;
    }

    public float getY () {
        return y;
    }
}
