package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class BaseScreen implements Screen, InputProcessor {
    protected Game game;
    protected Stage mainStage;
    protected Stage uiStage;

    public final int viewWidth = 640;
    public final int viewHeight = 480;

    private boolean paused;

    public BaseScreen(Game game) {
        this.game = game;
        this.mainStage = new Stage(new FitViewport(viewWidth, viewHeight));
        this.uiStage = new Stage(new FitViewport(viewWidth, viewHeight));

        this.paused = false;

        InputMultiplexer inputMultiplexer = new InputMultiplexer(this, uiStage, mainStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        
        create();
    }

    public abstract void create();

    public abstract void update(float dt);

    //gameloop code; update, then render.
    public void render(float dt){
        uiStage.act(dt);

        //only pause gameplay events, not UI events
        if (!isPaused()){
            mainStage.act(dt);
            update(dt);
        }

        // render
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.draw();
        uiStage.draw();
    }

    //pause methods
    public boolean isPaused(){
        return paused;
    }

    public void setPaused(boolean b){
        paused = b;
    }

    public void togglePaused(){
        paused =! paused;
    }

    public void resize(int width, int height){
        mainStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width,height,true);
    }

    //methods required by Screen interface
    public void pause(){}
    public void resume(){}
    public void dispose(){}
    public void show(){}
    public void hide(){}

    //methods required by InputProcessor interface
    public boolean keyDown(int keycode)
    {return false;}
    public boolean keyUp(int keycode)
    {return false;}
    public boolean keyTyped(char c)
    {return false;}
    public boolean mouseMoved(int screenX, int screenY)
    {return false;}
    public boolean scrolled(int amount)
    {return false;}

    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {return false;}
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {return false;}
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {return false;}

}


