package com.boop442.follow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {

    SpriteBatch batch;
    OrthographicCamera camera;

    private Rectangle follower;
    private Rectangle dot;
    private Rectangle exit;

    Vector3 touchPos = new Vector3();

    private Texture dotImage;
    private Texture followerImage;
    private Texture exitImage;

    Follow game;


    public GameScreen(final Follow game) {

        this.game = game;
        //setup:----------------------------------

        Gdx.app.log("Follow.java", "-------------CREATE----------------");

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);

        // load the images for the droplet and the bucket, 64x64 pixels each
        dotImage = new Texture(Gdx.files.internal("test.jpg"));
        followerImage = new Texture(Gdx.files.internal("badlogic.jpg"));
        exitImage = new Texture(Gdx.files.internal("badlogic.jpg"));



        //MUSIC AND SOUNDS:

        // load the drop sound effect and the rain background "music"
        //dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        // start the playback of the background music immediately
        //rainMusic.setLooping(true);
        //rainMusic.play();



        batch = new SpriteBatch();


        follower = new Rectangle();
        follower.x = 800 / 2 - 64 / 2;
        follower.y = 20;
        follower.width = 64;
        follower.height = 64;


        dot = new Rectangle();
        dot.x = follower.x;
        dot.y = 10000;
        dot.width = 64;
        dot.height = 64;

        exit = new Rectangle();
        exit.x = 0;
        exit.y = 20;
        exit.width = 64;
        exit.height = 64;
    }

    @Override
    public void render(float delta) {

        run();
    }

    public void run() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);//openGL
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//openGL
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(followerImage, follower.x, follower.y);
        batch.draw(dotImage, dot.x, dot.y);
        batch.draw(exitImage, exit.x, exit.y);
        batch.end();


        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            dot.x = touchPos.x - 64 / 2;
            dot.y = touchPos.y - 64/2;
        }


        if (dot.x - follower.x >= 1) {
            follower.x += 1;
        }
        else if (follower.x - dot.x >= 1) {
            follower.x -= 1;
        }

        if(follower.overlaps(exit)) {
            Gdx.app.log("Follow.java", "-------------OVERLAPS----------------");
//            dropSound.play();
//            iter.remove();

            //level complete
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
//        rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    @Override
    public void dispose() {
        //batch, images, sounds, music
        batch.dispose();
        dotImage.dispose();
        followerImage.dispose();
        exitImage.dispose();
    }
}