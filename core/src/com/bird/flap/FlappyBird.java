package com.bird.flap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    SpriteBatch batch;
    Texture gamebg;
    Texture[] birdimg;
    Texture gover;
    //   ShapeRenderer shapeRenderer;
    BitmapFont scoringFont;
    int wingstate = 0;
    float birdY = 0;
    float speed = 0;
    int gstate = 0;
    float gravity = 2;
    Texture toppipe;
    Texture bottompipe;
    float space = 400;
    float randompipeoffset;
    Random randomnumgen;
    Circle mbird;
    float pipespeed = 4;
    int score = 0;
    int interceptTube = 0;

    int numofpipes = 4;
    float[] pipeX = new float[numofpipes];
    float[] offset = new float[numofpipes];
    float pipedistance;
    Rectangle[] mtoppipe;
    Rectangle[] mbottompipe;


    @Override
    public void create() {
        scoringFont = new BitmapFont();
        scoringFont.setColor(Color.DARK_GRAY);
        scoringFont.getData().setScale(10);
        batch = new SpriteBatch();
        gamebg = new Texture("background.png");
        birdimg = new Texture[2];
        birdimg[0] = new Texture("bird1.png");
        birdimg[1] = new Texture("bird2.png");


        toppipe = new Texture("toppipe.png");
        bottompipe = new Texture("bottompipe.png");
        gover = new Texture("game_over_img.png");
        //       shapeRenderer = new ShapeRenderer();
        mbird = new Circle();
        randompipeoffset = Gdx.graphics.getHeight() / 2 - space / 2 - 100;
        randomnumgen = new Random();
        pipedistance = Gdx.graphics.getWidth() * 3 / 4;

        mtoppipe = new Rectangle[numofpipes];
        mbottompipe = new Rectangle[numofpipes];
        gamestart();
    }


    public void gamestart() {
        birdY = Gdx.graphics.getHeight() / 2 - birdimg[0].getHeight() / 2;
        for (int count = 0; count < numofpipes; count++) {
            offset[count] = (randomnumgen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - space - 200);
            pipeX[count] = Gdx.graphics.getWidth() / 2 - toppipe.getWidth() / 2 + Gdx.graphics.getWidth() + count * pipedistance;

            mtoppipe[count] = new Rectangle();
            mbottompipe[count] = new Rectangle();
        }
    }

    @Override
    public void render() {

        batch.begin();

        // Drew the Game BackGround
        batch.draw(gamebg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gstate == 1) {

            if (pipeX[interceptTube] < Gdx.graphics.getWidth() / 2) {
                score++;
                Gdx.app.log("Current Score:", String.valueOf(score));
                if (interceptTube < numofpipes - 1) {
                    interceptTube++;
                } else {
                    interceptTube = 0;
                }
            }


            if (Gdx.input.justTouched()) {
                speed = -30;

            }
            for (int i = 0; i < numofpipes; i++) {

                if (pipeX[i] < -toppipe.getWidth()) {
                    pipeX[i] = numofpipes * pipedistance;
                    offset[i] = (randomnumgen.nextFloat() - 0.5f) * Gdx.graphics.getHeight() - space - 200;
                } else {
                    pipeX[i] = pipeX[i] - pipespeed;

                }
                // Drawing the pipes
                batch.draw(toppipe, pipeX[i], Gdx.graphics.getHeight() / 2 + space / 2 + offset[i]);
                batch.draw(bottompipe, pipeX[i], Gdx.graphics.getHeight() / 2 - space / 2 - toppipe.getHeight() + offset[i]);
                mtoppipe[i] = new Rectangle(pipeX[i], Gdx.graphics.getHeight() / 2 + space / 2 + offset[i], toppipe.getWidth(), toppipe.getHeight());
                mbottompipe[i] = new Rectangle(pipeX[i], Gdx.graphics.getHeight() / 2 - space / 2 - toppipe.getHeight() + offset[i], bottompipe.getWidth(), bottompipe.getHeight());
            }

            if (birdY > 0) {

                speed = speed + gravity;
                birdY -= speed;
            } else {
                gstate = 2;
            }
            if (wingstate == 0) {
                wingstate = 1;
            } else {
                wingstate = 0;
            }

        } else if (gstate == 0) {
            if (Gdx.input.justTouched()) {

                gstate = 1;
            }
        } else if (gstate == 2) {
            // Drawing Game over Screen
            batch.draw(gover, Gdx.graphics.getWidth() / 2 - gover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gover.getHeight() / 2);
            if (Gdx.input.justTouched()) {

                gstate = 1;
                gamestart();
                score = 0;
                interceptTube = 0;
                speed = 0;
            }
        }
        // Drawing the Bird
        batch.draw(birdimg[wingstate], Gdx.graphics.getWidth() / 2 - birdimg[wingstate].getWidth() / 2, birdY);
        scoringFont.draw(batch, String.valueOf(score), 100, 200);


        // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // shapeRenderer.setColor(Color.GREEN);
        mbird.set(Gdx.graphics.getWidth() / 2, birdY + birdimg[wingstate].getHeight() / 2, birdimg[wingstate].getWidth() / 2);
        //   shapeRenderer.circle(mbird.x, mbird.y, mbird.radius);
        for (int i = 0; i < numofpipes; i++) {
            //   shapeRenderer.setColor(Color.RED);
            //   shapeRenderer.rect(pipeX[i], Gdx.graphics.getHeight() / 2 + space / 2 + offset[i], toppipe.getWidth(), toppipe.getHeight());
            //   shapeRenderer.rect(pipeX[i], Gdx.graphics.getHeight() / 2 - space / 2 - toppipe.getHeight() + offset[i], bottompipe.getWidth(), bottompipe.getHeight());

            if (Intersector.overlaps(mbird, mtoppipe[i]) || Intersector.overlaps(mbird, mbottompipe[i])) {
                gstate = 2;

            }

        }
        batch.end();
        //  shapeRenderer.end();

    }

/*    @Override
    public void dispose() {
        batch.dispose();
        gamebg.dispose();
    }
    */
}
