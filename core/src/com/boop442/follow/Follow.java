package com.boop442.follow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.Rectangle;

public class Follow extends ApplicationAdapter {
	SpriteBatch batch;
    OrthographicCamera camera;

    private Rectangle follower;
    private Rectangle dot;
    Vector3 touchPos = new Vector3();

    private Texture dotImage;
    private Texture followerImage;

	
	@Override
	public void create () {
	    //setup:----------------------------------

        Gdx.app.log("Follow.java", "-------------CREATE----------------");

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);

        // load the images for the droplet and the bucket, 64x64 pixels each
        dotImage = new Texture(Gdx.files.internal("test.jpg"));
        followerImage = new Texture(Gdx.files.internal("badlogic.jpg"));



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
        dot.x = 800 / 2 - 64 / 2;
        dot.y = 220;
        dot.width = 64;
        dot.height = 64;
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);//openGL
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//openGL
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(followerImage, follower.x, follower.y);
        batch.draw(dotImage, dot.x, dot.y);
        batch.end();

        if(Gdx.input.isTouched()) {
            Gdx.app.log("Follow.java", "-------------input.isTouched----------------");
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            dot.x = touchPos.x - 64 / 2;
            dot.y = touchPos.y - 64/2;
        }

        if (follower.x < dot.x) {
            follower.x += 1;
        }
        else if (follower.x > dot.x) {
            follower.x -= 1;
        }
	}

	
	@Override
	public void dispose () {
		batch.dispose();
        dotImage.dispose();
        followerImage.dispose();
	}
}
