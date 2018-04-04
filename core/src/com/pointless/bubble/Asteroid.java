package com.pointless.bubble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

/**
 * Created by vaibh on 03-04-2018.
 */

public class Asteroid {                  // the bubbles are named as asteroids to avoid ambiguity with main bubble class
    public int SPEED = 250;
    public int WIDTH = 64;
    public int HEIGHT = 64;
    public static final int radius = 15;
    public  int TILE_WIDTH=64;
    public  int TILE_HEIGHT=64 ;
    private static Texture texture;
    private Animation animation;
    private float animationTimer = 0;
    public static final float FRAME_DURATION = .50F;
    public final Circle AsteroidCircle;
    public int flag = 0;
    public boolean pass = false ;
    int max = 4 ;
    int min = 0 ;
    int type = 0 ;

    float x, y;

    public boolean remove = false;

    public Asteroid (float y ) {

        type = min + (int)(Math.random() * max);
        if(type == 1)
            texture = new Texture("asteroid.png");

        else if(type == 2) {
            texture = new Texture("asteroid2.png");
        }

        else if(type == 3) {
            texture = new Texture("asteroid3.png");
        }

        else  {
            texture = new Texture("asteroid.png");
        }

        TextureRegion[][] textures = new TextureRegion(texture).split(TILE_WIDTH, TILE_HEIGHT);

        animation = new Animation(FRAME_DURATION, textures[0][0],textures[0][1]);

        animation.setPlayMode(Animation.PlayMode.LOOP);
        this.x = GameScreen.WORLD_WIDTH;
        this.y = y;

        AsteroidCircle = new Circle(x , y , radius);
    }

    private void check() {
        if (y >= GameScreen.WORLD_HEIGHT-HEIGHT) {
            y = GameScreen.WORLD_HEIGHT-HEIGHT ;
            flag = 0;
        }
        else if ( y <= 0 + HEIGHT/4 ) {
            y = 0+HEIGHT/4;
            flag = 1;
        }
    }

    public void update (float deltaTime) {
        x -= SPEED * deltaTime;
        animationTimer += deltaTime;

        if(type == 2) {
            if( GameScreen.WORLD_WIDTH >= x && x >= 3*(GameScreen.WORLD_WIDTH)/4) {
                y -= SPEED * deltaTime;
            }
            else if( (3*GameScreen.WORLD_WIDTH)/4 >= x && x >= GameScreen.WORLD_WIDTH/2){
                y += SPEED * deltaTime;
            }
            else if( GameScreen.WORLD_WIDTH/2 >= x && x >= GameScreen.WORLD_WIDTH/4) {
                y -= SPEED * deltaTime;
            }
            else if( (GameScreen.WORLD_WIDTH)/4 >= x && x >= -GameScreen.WORLD_WIDTH){
                y += SPEED * deltaTime;
            }
        }

        if(type == 3) {
            check();
            if (flag == 0) {
                y -= SPEED * deltaTime;
            }
            else if (flag == 1) {
                y += SPEED * deltaTime;
            }
        }


        if (x < - GameScreen.WORLD_WIDTH){
            remove = true;
            pass = true;}

        updateCollisionCircle();

    }

    private void updateCollisionCircle() {
        AsteroidCircle.setX(x);
        AsteroidCircle.setY(y);
    }

    public boolean ispopperColliding(popper pop) {
        Circle flappyCollisionCircle = pop.getCollisionCircle();
        return
                Intersector.overlaps(flappyCollisionCircle, AsteroidCircle) ;

    }


    public void render (SpriteBatch sb) {
        TextureRegion asteroidSlice = (TextureRegion) animation.getKeyFrame(animationTimer);
        float textureX = AsteroidCircle.x - TILE_WIDTH / 2;
        float textureY = AsteroidCircle.y - TILE_HEIGHT / 2;
        sb.draw(asteroidSlice, textureX, textureY);
    }


    public void drawDebug(ShapeRenderer shapeRenderer) {

        shapeRenderer.circle(AsteroidCircle.x, AsteroidCircle.y, radius);
    }

    public float getX () {
        return x;
    }

    public float getY () {
        return y;
    }
}
