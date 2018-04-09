package com.boop442.follow.adapters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.boop442.follow.Follow;

import sun.rmi.runtime.Log;

public class CustomGestureListener implements GestureDetector.GestureListener{

    Follow mGame;

    public CustomGestureListener(Follow game) {
        Gdx.app.log("CustomGestureListener", "-------------CONSTRUCTOR----------------");
        mGame = game;
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        mGame.renderOnSpot(500, 500);
        Gdx.app.log("CustomGestureListener", "-------------TAP-GESTURE----------------");
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        Gdx.app.log("CustomGestureListener", "-------------LONG-PRESS----------------");
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){
        Gdx.app.log("CustomGestureListener", "-------------ZOOM----------------");

        return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){

        return false;
    }
    @Override
    public void pinchStop () {
    }
}
