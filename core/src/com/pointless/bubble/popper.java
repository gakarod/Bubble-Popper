package com.pointless.bubble;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 * Created by vaibh on 03-04-2018.
 */

public class popper {                                          // main character class
    public static final float COLLISION_RADIUS = 24f;
    private static final float DIVE_ACCEL = 0.30F;
    private static final float FLY_ACCEL = 3F;
    public static final int TILE_WIDTH=64;
    public static final int TILE_HEIGHT=43;
    Texture ship ;

    private static Circle collisionCircle;

    public float x = 0;
    public float y = 0;
    private float ySpeed = 0;


    public popper(Texture texture){

        ship = texture ;
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
    }

    public static Circle getCollisionCircle() {return collisionCircle;}
    public float getY() { return y;}
    public float getX() {return x;}

    public void update(float delta) {

        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void flyUp() {                                 // fly mechanism
        ySpeed = FLY_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void draw(SpriteBatch sb) {

        float textureX = collisionCircle.x - TILE_WIDTH / 2;
        float textureY = collisionCircle.y - TILE_HEIGHT / 2;
        sb.draw(ship, textureX, textureY);
    }


}
