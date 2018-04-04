package com.pointless.bubble;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vaibh on 02-04-2018.
 */

public class GameScreen  extends ScreenAdapter {
    public  static float WORLD_WIDTH = 480;
    public  static float WORLD_HEIGHT = 320;
    public static final float MIN_ASTEROID_SPAWN_TIME = 0.4f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.8f;

    Random random;
    Random rand = new Random();

    private ShapeRenderer shapeRenderer;
    private FitViewport viewPort;
    public Camera camera;
    private SpriteBatch sb;
    private popper pop;


    float asteroidSpawnTimer;
    float asteroidlocation;
    float ylocation;
    float y2location;

    private int score = 0;


    float health = 1;//0 = dead, 1 = full health
    float life = 3;
    public boolean flag = false;


    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;
    long startTime = System.currentTimeMillis();

    private Texture bg;
    private Texture popperTexture;
    private Texture blank;
    private Texture heart;
    Game gamenow;


    public ArrayList<Asteroid> asteroids;
    public ArrayList<Meteor> meteors;
    public ArrayList<Explosion> explosions;

    ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
    ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
    ArrayList<Meteor> meteorsToRemove = new ArrayList<Meteor>();


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewPort.update(width, height);
    }

    public GameScreen(Game game ) {
        this.gamenow = game;
    }

    @Override
    public void show() {                      // initial deployment
        super.show();
        bitmapFont = new BitmapFont();
        glyphLayout = new GlyphLayout();
        asteroids = new ArrayList<Asteroid>();
        explosions = new ArrayList<Explosion>();
        meteors = new ArrayList<Meteor>();
        random = new Random();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        sb = new SpriteBatch();


        bg = new Texture(Gdx.files.internal("bg.png"));

        popperTexture = new Texture(Gdx.files.internal("ship.png"));
        heart = new Texture(Gdx.files.internal("heart.png"));
        blank = new Texture("blank.png");
        pop = new popper(popperTexture);
        pop.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);


    }



    @Override
    public void dispose() {
        super.dispose();
    }


    @Override
    public void render(float delta) {


                super.render(delta);
                update(delta);
                clearScreen();
                draw(startTime);
                //   drawDebug();

    }

    private void drawDebug() {                                       // for checking the positions of entities
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Asteroid asteroid : asteroids) {
            asteroid.drawDebug(shapeRenderer);
        }
        shapeRenderer.end();
    }

    private void update(float delta) {

        updatePopper(delta);
        build(delta);
        updateAsteroids(delta);
        updateMeteors(delta);
        updateExplosions(delta);
        flag = false;

        checkForCollision();

    }

    private void draw(long time) {
        long elapsedtime = System.currentTimeMillis() - time;
        sb.setProjectionMatrix(camera.projection);
        sb.setTransformMatrix(camera.view);
        sb.begin();
        sb.draw(bg, 0, 0);
        drawMeteors();
        drawAsteroids();
        drawExplosions();

        Health();

        sb.setColor(Color.WHITE);
        pop.draw(sb);
        if (elapsedtime / 1000 >= 30) {
            drawScore();
        }
        sb.end();
    }


    private void updatePopper(float delta) {
        pop.update(delta);

        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;
            pop.flyUp();

        }

        blockpopperLeavingTheWorld();

    }

    private void updateScore(int type) {      // score according to the type of bubbles
        if (type == 1)
            score += 5;
        if (type == 2)
            score += 10;
        if (type == 3)
            score += 15;
    }


    private void drawScore() {                                  // Score displayed on a glyph as bitmap font
        String scoreAsString = Integer.toString(score);
        glyphLayout.setText(bitmapFont, scoreAsString);
        bitmapFont.draw(sb, scoreAsString, (viewPort.getWorldWidth() - glyphLayout.width) / 2, (4 * viewPort.getWorldHeight() / 5) - glyphLayout.height / 2);
    }


    private void drawExplosions() {

        for (Explosion explosion : explosions) {
            explosion.render(sb);
        }
    }

    private void build(float delta) {               // asteroid and meteors are initialized
        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0) {
            ylocation = pop.y + 50;
            y2location = pop.y - 50;
            asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            asteroidlocation = rand.nextFloat() * (ylocation - y2location) + y2location;
            asteroids.add(new Asteroid(random.nextInt((int) (GameScreen.WORLD_HEIGHT - 32))));
            asteroids.add(new Asteroid(asteroidlocation));
            meteors.add(new Meteor(pop.y));
        }
    }

    private void drawAsteroids() {
        for (Asteroid asteroid : asteroids) {
            asteroid.render(sb);
        }
    }

    private void drawMeteors() {
        for (Meteor meteor : meteors) {
            meteor.render(sb);
        }
    }

    private void updateAsteroids(float delta) {

        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
            if (asteroid.remove)
                asteroidsToRemove.add(asteroid);
        }
        asteroids.removeAll(asteroidsToRemove);
    }

    private void updateMeteors(float delta) {

        for (Meteor meteor : meteors) {
            meteor.update(delta);
            if (meteor.remove)
                meteorsToRemove.add(meteor);
        }
        meteors.removeAll(meteorsToRemove);
    }

    private void updateExplosions(float delta) {

        for (Explosion explosion : explosions) {
            explosion.update(delta);
            if (explosion.remove)
                explosionsToRemove.add(explosion);
        }
        explosions.removeAll(explosionsToRemove);
    }



    private void checkForCollision() {    // check for collision


        for (Asteroid asteroid : asteroids) {
            if (asteroid.ispopperColliding(pop)) {
                updateScore(asteroid.type);
                explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
                asteroidsToRemove.add(asteroid);
            }

            for (Meteor meteor : meteors) {
                if (meteor.ispopperColliding(pop)) {
                    meteorsToRemove.add(meteor);
                    health -= 0.05;
                }
            }
        }
    }



    private void Health() {                 // health bar updation and drawing it of different colours
        if (life == 0)                      // according to the health level
            gamenow.setScreen(new GameOver(gamenow,score));
        else if (life == 3) {
            sb.draw(heart, 20, WORLD_HEIGHT - 60);
            sb.draw(heart, 60, WORLD_HEIGHT - 60);
            sb.draw(heart, 100, WORLD_HEIGHT - 60);
        } else if (life == 2) {
            sb.draw(heart, 20, WORLD_HEIGHT - 60);
            sb.draw(heart, 60, WORLD_HEIGHT - 60);
        } else if (life == 1) {
            sb.draw(heart, 20, WORLD_HEIGHT - 60);
        }

        if (health > 0.7f)
            sb.setColor(Color.GREEN);
        else if (health > 0.4f)
            sb.setColor(Color.ORANGE);
        else if (health > 0)
            sb.setColor(Color.RED);
        else {
            life--;
            health = 1;
        }

        sb.draw(blank, 0, 0, GameScreen.WORLD_WIDTH * health, 5);

    }

    private void blockpopperLeavingTheWorld() {           // block outside gamescreen movement and stay at the ground
        pop.setPosition(pop.getX(), MathUtils.clamp(pop.getY(), 20, WORLD_HEIGHT));
    }


    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
