package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.util.HashMap;

public class AnimatedActor extends BaseActor {
    private float elapsedTime;
    private Animation activeAnimation;
    private String activeName;
    private HashMap<String, Animation> animationStorage;

    public AnimatedActor(){
        super();
        elapsedTime = 0;
        activeAnimation = null;
        activeName = null;
        animationStorage = new HashMap<String, Animation>();
    }

    public void storeAnimation(String name, Animation animation){
        animationStorage.put(name, animation);
        if (activeName == null)
            setActiveAnimation(name);
    }

    public void storeAnimation(String name, Texture texture){
        TextureRegion textureRegion = new TextureRegion(texture);
        TextureRegion[] frames = { textureRegion };
        Animation animation = new Animation(1.0f, frames);
        storeAnimation(name, animation);
    }

    public void setActiveAnimation(String name) {
        if (!animationStorage.containsKey(name))
        {
            System.out.println("No animation: " + name);
            return;
        }

        activeName = name;
        activeAnimation = animationStorage.get(name);
        elapsedTime = 0;

        Texture texture = ((TextureRegion)activeAnimation.getKeyFrame(0)).getTexture();
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
    }

    public String getAnimationName()
    {
        return activeName;
    }

    public void act(float dt){
        super.act(dt);
        elapsedTime += dt;

    }

    public void draw(Batch batch, float parentAlpha){
        region.setRegion((TextureRegion) activeAnimation.getKeyFrame(elapsedTime));
        super.draw(batch, parentAlpha);
    }
}
