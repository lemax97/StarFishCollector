package com.mygdx.game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;


public class BaseActor extends Actor {
    public TextureRegion region;
    public Polygon boundingPolygon;

    public BaseActor(){
        super();
        region = new TextureRegion();
        boundingPolygon = null;
    }

    public void setTexture(Texture texture){
        int width = texture.getWidth();
        int height = texture.getHeight();
        setWidth(width);
        setHeight(height);
        region.setRegion(texture);
    }

    public void act(float dt){
        super.act(dt);
    }

    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        if (isVisible())
            batch.draw(region,
                    getX(),
                    getY(),
                    getOriginX(),
                    getOriginY(),
                    getWidth(),
                    getHeight(),
                    getScaleX(),
                    getScaleY(),
                    getRotation());
    }

    public void setRectangleBoundary()
    {
        float width = getWidth();
        float height = getHeight();
        float[] vertices = {0,0, width,0, width,height, 0,height};
        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(getOriginX(), getOriginY());
    }

    public void setEllipseBoundary()
    {
        int number = 8; //number of vertices
        float width = getWidth();
        float height = getHeight();
        float[] vertices = new float[2*number];
        for (int i = 0; i < number; i++)
        {
            float trajectory = i * 6.28f / number;
            // x - coordinates
            vertices[2*i] = width / 2 * MathUtils.cos(trajectory) + width / 2;
            // y - coordinates
            vertices[2*i + 1] = height / 2 * MathUtils.cos(trajectory) + height / 2;
        }
        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(getOriginX(), getOriginY());
    }

    public Polygon getBoundingPolygon(){
        boundingPolygon.setPosition(getX(), getY());
        boundingPolygon.setRotation(getRotation());
        return boundingPolygon;
    }

    /**
     * Determine if the collision polygons of two BaseActor objects overlap.
     * if (resolve == true), then when there is overlap, move this BaseActor
     * along minimum translation vector until there is no overlap.
     */
    public boolean overlaps(BaseActor other, boolean resolve)
    {
        Polygon polygon1 = this.getBoundingPolygon();
        Polygon polygon2 = other.getBoundingPolygon();

        if (!polygon1.getBoundingRectangle().overlaps(polygon2.getBoundingRectangle()))
            return false;
        MinimumTranslationVector minimumTranslationVector = new MinimumTranslationVector();
        boolean polyOverlap = Intersector.overlapConvexPolygons(polygon1, polygon2, minimumTranslationVector);
        if (polyOverlap && resolve)
            this.moveBy(minimumTranslationVector.normal.x * minimumTranslationVector.depth, minimumTranslationVector.normal.y * minimumTranslationVector.depth);
        float significant = 0.5f;
        return (polyOverlap && (minimumTranslationVector.depth > significant));
    }

    public void copy(BaseActor original)
    {
        this.region = new TextureRegion(original.region);
        if (original.boundingPolygon != null)
        {
            this.boundingPolygon = new Polygon(original.boundingPolygon.getVertices());
            this.boundingPolygon.setOrigin(original.getOriginX(), original.getOriginY());
            this.setPosition(original.getX(), original.getY());
            this.setOriginX(original.getOriginX());
            this.setOriginY(original.getOriginY());
            this.setWidth(original.getWidth());
            this.setHeight(original.getHeight());
            this.setColor(original.getColor());
            this.setVisible(original.isVisible());
        }
    }

    public BaseActor clone()
    {
        BaseActor newbie = new BaseActor();
        newbie.copy(this);
        return newbie;
    }
}
