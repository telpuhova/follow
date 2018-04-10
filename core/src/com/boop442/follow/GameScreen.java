package com.boop442.follow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    //animation:

    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;

    // Objects used
    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;

    // A variable for tracking elapsed time for the animation
    float stateTime;



    public GameScreen(final Follow game) {

        this.game = game;

        Gdx.app.log("Follow.java", "-------------CREATE----------------");

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);


        // CREATING TEXTURES

        // load the images for the droplet and the bucket, 64x64 pixels each
        dotImage = new Texture(Gdx.files.internal("red_dot_64px.png"));
        exitImage = new Texture(Gdx.files.internal("shining_128px.png"));

        // Load the sprite sheet as a Texture
        walkSheet = new Texture(Gdx.files.internal("sprite-animation4.png"));




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
        exit.x = -20;
        exit.y = -10;
        exit.width = 128;
        exit.height = 128;


        // create a 2D array of TextureRegions
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        // place the regions into a 1D array. (from the top left, going across first)
        // (The Animation constructor requires a 1D array.)
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);

        // time to 0
        stateTime = 0f;




        //MUSIC AND SOUNDS:-----------------------------

        // load the drop sound effect and the rain background "music"
        //dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        // start the playback of the background music immediately
        //rainMusic.setLooping(true);
        //rainMusic.play();

    }

    @Override
    public void render(float delta) {

        run();
    }

    public void run() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);//openGL//dark blue screen
//        Gdx.gl.glClearColor(1f, 1f, 1f, 1);//openGL//white screen

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//openGL
        camera.update();


        //animation:
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion followerFrame = walkAnimation.getKeyFrame(stateTime, true);


        if (dot.x - follower.x >= 1) {
            follower.x += 1;
        }
        else if (follower.x - dot.x >= 1) {
            followerFrame.flip(true, false);
            follower.x -= 1;
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
//        batch.draw(followerImage, follower.x, follower.y);
        batch.draw(followerFrame, follower.x, follower.y); // Draw current frame at (50, 50)
        batch.draw(dotImage, dot.x, dot.y);
        batch.draw(exitImage, exit.x, exit.y);
        batch.end();


        // flipping back
        if (follower.x - dot.x >= 1) {
            followerFrame.flip(true, false);
        }


        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            dot.x = touchPos.x - 64 / 2;
            dot.y = touchPos.y - 64/2;
        }



        if(follower.overlaps(exit)) {
            Gdx.app.log("Follow.java", "-------------OVERLAPS----------------");
//            dropSound.play();
//            iter.remove();

            //level complete
            game.setScreen(new LevelCompleteMenuScreen(game));
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
        exitImage.dispose();
        walkSheet.dispose();
    }
}