package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;

public class TurtleLevel extends BaseScreen{
    private BaseActor ocean;
    private ArrayList<BaseActor> rocklist;
    private ArrayList<BaseActor> starfishlist;
    private PhysicsActor turtle;
    private int mapWidth = 650;
    private int mapHeight = 500;
    private float audioVolume;
    private Sound waterDrop;
    private Music instrumental;
    private Music oceanSurf;

    public TurtleLevel(Game game) {
        super(game);
    }

    @Override
    public void create() {
        ocean = new BaseActor();
        ocean.setTexture(new Texture("water.jpg"));
        ocean.setPosition(0,0);
        mainStage.addActor(ocean);

        BaseActor overlay = ocean.clone();
        overlay.setPosition(-50,50);
        overlay.setColor(1,1,1,0.25f);
        uiStage.addActor(overlay);

        BaseActor rock = new BaseActor();
        rock.setTexture(new Texture("rock.png"));
        rock.setEllipseBoundary();

        rocklist = new ArrayList<BaseActor>();
        int[] rockCoords = {200,0, 200,100, 250,200, 360,200, 470,200};
        for (int i = 0; i < 5; i++) {
            BaseActor rockClone = rock.clone();
            // obtain coordinates from array, both x and y, at the same time
            rockClone.setPosition(rockCoords[2*i], rockCoords[2*i+1]);
            mainStage.addActor(rockClone);
            rocklist.add(rockClone);
        }

        BaseActor starfish = new BaseActor();
        starfish.setTexture(new Texture("starfish.png"));
        starfish.setEllipseBoundary();

        starfishlist = new ArrayList<BaseActor>();
        int[] starfishCoords = {300,100, 100,300, 350,400};
        for (int i = 0; i < 3; i++) {
            BaseActor starfishClone = starfish.clone();
            starfishClone.setPosition(starfishCoords[2*i], starfishCoords[2*i+1]);
            mainStage.addActor(starfishClone);
            starfishlist.add(starfishClone);
        }

        turtle = new PhysicsActor();
        TextureRegion[] frames = new TextureRegion[6];
        for (int i = 1; i <= 6; i++) {
            String fileName = "turtle-" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            frames[i-1] = new TextureRegion(texture);
        }
        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        Animation animation = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP);
        turtle.storeAnimation("swim", animation);

        Texture rest = new Texture(Gdx.files.internal("turtle-1.png"));
        turtle.storeAnimation("rest", rest);

        turtle.setOrigin(turtle.getWidth()/2, turtle.getHeight()/2);
        turtle.setPosition(20,20);
        turtle.setRotation(90);
        turtle.setEllipseBoundary();
        turtle.setMaxSpeed(100);
        turtle.setDeceleration(200);
        mainStage.addActor(turtle);

        waterDrop = Gdx.audio.newSound(Gdx.files.internal("Water_Drop.ogg"));
        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Master_of_the_Feast.ogg"));
        oceanSurf = Gdx.audio.newMusic(Gdx.files.internal("Ocean_Waves.ogg"));

        audioVolume = 0.80f;
        instrumental.setLooping(true);
        instrumental.setVolume(audioVolume);
        instrumental.play();
        oceanSurf.setLooping(true);
        oceanSurf.setVolume(audioVolume);
        oceanSurf.play();
    }

    @Override
    public void update(float dt) {
        //process input
        turtle.setAccelerationXY(0,0);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            turtle.rotateBy(90 * dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            turtle.rotateBy(-90 * dt);
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            turtle.accelerateForward(100);

        //set correct animation
        if ( turtle.getSpeed() > 1 && turtle.getAnimationName().equals("rest"))
            turtle.setActiveAnimation("swim");
        if ( turtle.getSpeed() < 1 && turtle.getAnimationName().equals("swim"))
            turtle.setActiveAnimation("rest");

        //bound turtle to the screen
        turtle.setX(MathUtils.clamp( turtle.getX(), 0, mapWidth - turtle.getWidth()));
        turtle.setY(MathUtils.clamp( turtle.getY(), 0, mapHeight - turtle.getHeight()));

        for (BaseActor rock: rocklist){
            turtle.overlaps(rock, true);
        }

        ArrayList<BaseActor> removeList = new ArrayList<BaseActor>();

        for (BaseActor starfish: starfishlist){
            if ( turtle.overlaps(starfish, false))
                removeList.add(starfish);
        }

        for (BaseActor baseActor : removeList){
            baseActor.remove();             //remove from stage
            starfishlist.remove(baseActor);//remove from list used by update
            waterDrop.play(audioVolume);
        }
    }

    @Override
    public void dispose() {
        waterDrop.dispose();
        instrumental.dispose();
        oceanSurf.dispose();
    }
}
