package com.boop442.follow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.boop442.follow.adapters.CustomGestureListener;

public class Follow extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
//    OrthographicCamera camera;
	
	@Override
	public void create () {
        Gdx.app.log("Follow.java", "-------------CREATE----------------");

//        camera = new OrthographicCamera();
//        camera.setToOrtho(false,800,400);

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        Gdx.input.setInputProcessor(new GestureDetector(new CustomGestureListener(this)));
	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
	}

	public void renderOnSpot(float x, float y) {
        Gdx.app.log("Follow.java", "-------------renderOnSpot----------------");
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, x, y);
        batch.end();
        Gdx.app.log("Follow.java", "-------------renderOnSpot-END---------------");
    }
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
