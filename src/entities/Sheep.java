package entities;

import graphics.Rectangle;
import graphics.Textures;

import java.util.Random;

public class Sheep extends Entity {
    private float x, y, w, h;
    private float[] enemy = {0f, 0f};
    private Rectangle sheep;
    private Random rand = new Random();
    private int runAttempts = 0;
    private boolean canRun = true;
    private long runDelay = System.currentTimeMillis();

    public Sheep(float x, float y, Textures tex)
    {
        setTextures(tex);
        this.x = x;
        this.y = y;
        this.w = getWidth();
        this.h = getHeight();
        this.sheep = createEntity(x, y, "sheep");
    }

    public void draw()
    {
        this.sheep.drawTex();
    }

    public void update()
    {
        this.sheep = AI(this.sheep);
        this.x = this.sheep.getX();
        this.y = this.sheep.getY();

        if(isAttacked() && canRun)
        {
            this.runAttempts++;

            int[] dir = new int[] {rand.nextInt(3) - 1, rand.nextInt(3) - 1};

            this.sheep = this.setDirection(dir, this.x, this.y);

            moveX(dir[0] * this.w);
            moveY(dir[1] * this.h);

            canRun = false;

        }

        if(runDelay + 200L < System.currentTimeMillis())
        {
            canRun = true;
            runDelay = System.currentTimeMillis();
        }

        if(this.runAttempts > rand.nextInt(6) + 10)
        {
            this.setAttackedStatus(false);
            this.runAttempts = 0;
        }

        if(getHealthPoints() < 1)
        {

            this.moveX(-100f);
        }
    }

    public void moveX(float mx)
    {
        this.sheep.moveX(mx);
        this.x += mx;
    }

    public void moveY(float my)
    {
        this.sheep.moveY(my);
        this.y += my;
    }

    public Rectangle getSheep()
    {
        return this.sheep;
    }

    public float getX()
    {
        return this.x;
    }

    public float getY()
    {
        return this.y;
    }
}
