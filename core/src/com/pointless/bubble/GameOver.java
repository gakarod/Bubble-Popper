package com.pointless.bubble;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by vaibh on 02-04-2018.
 */

public class GameOver extends ScreenAdapter {
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 320;
    private Stage stage;
    private SpriteBatch sb;
    private Texture bgTexture;
    private Texture bUpTexture;
    private Texture bDnTexture;
    private Texture titleTexture;
    private GlyphLayout glyphLayout;
    BitmapFont scoreFont;
    int score = 0 , highscore = 0 ;
    private final Game game;
    Label text;
    Label name ;
    Label.LabelStyle label1Style = new Label.LabelStyle();


    public GameOver(Game game, int score) {
        this.game = game;
        this.score = score ;
        //Get highscore from save file
        Preferences prefs = Gdx.app.getPreferences("spacegame");
        this.highscore = prefs.getInteger("highscore", 0);

        //Check if score beats highscore and save it in file
        if (score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }

        scoreFont = new BitmapFont(Gdx.files.internal("score.fnt"));
    }


    public void show(){
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        sb = new SpriteBatch();
        sb.begin();
        glyphLayout = new GlyphLayout();
        bgTexture = new Texture(Gdx.files.internal("bg.png"));
        Image bgImage = new Image(bgTexture);
        stage.addActor(bgImage);
        bUpTexture = new Texture(Gdx.files.internal("retry.png"));
        bDnTexture = new Texture(Gdx.files.internal("retrypress.png"));



        String scoreAsString = Integer.toString(score);
        String HighscoreAsString = Integer.toString(highscore);       //  score conversion ot string for display
        label1Style.font = scoreFont;

        text = new Label("Score" + scoreAsString,label1Style);      // score displayed at glyph
        name = new Label("HighScore" + HighscoreAsString,label1Style) ;
        name.setPosition(WORLD_WIDTH/2,WORLD_HEIGHT/2,Align.center);
        text.setPosition(WORLD_WIDTH/3,(2*WORLD_HEIGHT)/5,Align.center);


        text.setFontScale(0.5f,0.5f);

        name.setFontScale(0.5f,0.5f);


        ImageButton retry = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(bUpTexture)),
                new TextureRegionDrawable(new TextureRegion(bDnTexture))
        );

        // Add event listener to this button
        retry.addListener(new ActorGestureListener(){
            public void tap(InputEvent event, float x, float y, int count, int button){
                super.tap(event, x, y, count, button);
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });


        retry.setPosition(WORLD_WIDTH/2,WORLD_HEIGHT/4, Align.center);
        stage.addActor(retry);
        stage.addActor(text);
        stage.addActor(name);
        titleTexture = new Texture(Gdx.files.internal("title.png"));
        Image titleImage = new Image(titleTexture);
        titleImage.setPosition(WORLD_WIDTH/2, 3*WORLD_HEIGHT/4, Align.center);
        stage.addActor(titleImage);


    }



    public void resize(int w, int h){
        stage.getViewport().update(w,h,true);
    }

    public void render(float delta){
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        bDnTexture.dispose();
        bDnTexture.dispose();
        bUpTexture.dispose();
        titleTexture.dispose();
    }
}
