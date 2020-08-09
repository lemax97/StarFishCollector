package com.mygdx.game;

import com.badlogic.gdx.Game;

public class StarFishCollectorGame extends Game {
    @Override
    public void create() {
        TurtleLevel turtleLevel = new TurtleLevel(this);
        setScreen(turtleLevel);
    }
}
