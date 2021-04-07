package entities;

import graphics.Rectangle;
import graphics.Texture;
import graphics.Textures;

import java.util.Random;

public class Zombie{
    private float x, y, w, h;
    private float[] enemy = {0f, 0f};
    private Rectangle zombie;
    private Random rand = new Random();
    private int runAttempts = 0;
    private boolean canRun = true;
    private long runDelay = System.currentTimeMillis();
    private Textures textures;
    private boolean isAttacked = false;
    private int[] dir = {0, 0};
    private int healthPoints = 10;
    private boolean attackedStatus = false;
    private boolean canMove = true;
    private long moveDelay = System.currentTimeMillis();
    public int pow = 3;
    private boolean canAttack = true;
    private long attackDelay = System.currentTimeMillis();

    public Zombie(float x, float y, Textures tex)
    {
        this.textures = tex;
        this.x = x;
        this.y = y;
        this.w = this.textures.size;
        this.h = this.textures.size;
        this.zombie = new Rectangle(x, y, this.w, this.h, new int[] {0, 0, 0, 255}, textures.get(textures.ZOMBIE_BACK), "zombie");
    }

    public void draw()
    {
        this.zombie.drawTex();
    }

    public double distance()
    {
        float x = this.x;
        float y = this.y;

        System.out.println(x + " " + y + " " + x*x + " " + y*y + " " + ((double)(x*x) + (double)(y*y)) +" " + Double.parseDouble((String) (Math.sqrt(x * x + y * y) + "").subSequence(0, 4)));

        if((Math.sqrt(x * x + y * y) + "").length() > 6) {
            return Double.parseDouble((String) (String.valueOf(Math.sqrt(x * x + y * y))).subSequence(0, 4));
        }
        else
        {
            return Math.sqrt(x * x + y * y);
        }
    }

    public void setCombatStatus()
    {
        this.canAttack = false;
    }

    private void AI()
    {
        if(this.x > -1f && this.x < 1f && this.y > -1f && this.y < 1f)
        {
            boolean moved = false;

            if(this.x != 0f) {
                if (this.x <= 0f) {
                    this.dir = new int[]{1, 0};
                    moved = true;
                } else if (this.x >= 0f) {
                    this.dir = new int[]{-1, 0};
                    moved = true;
                }
            }

            if(this.y != 0f) {
                if (this.y <= 0f && !moved) {
                    this.dir = new int[]{0, 1};
                } else if (this.y >= 0f && !moved) {
                    this.dir = new int[]{0, -1};
                }
            }

            this.zombie = setDirection();

            if(canMove) {

                moveX(this.dir[0] * this.textures.size);
                moveY(this.dir[1] * this.textures.size);

                canMove = false;
            }

        }

        if(moveDelay + 1000l < System.currentTimeMillis())
        {
            canMove = true;
            moveDelay = System.currentTimeMillis();
        }

    }

    private boolean isAttacked()
    {
        return this.isAttacked;
    }

    public void update()
    {
        AI();
        this.x = this.zombie.getX();
        this.y = this.zombie.getY();

        if(isAttacked() && canRun)
        {
            this.runAttempts++;

            this.dir = new int[] {rand.nextInt(3) - 1, rand.nextInt(3) - 1};

            this.zombie = this.setDirection();

            moveX(dir[0] * this.w);
            moveY(dir[1] * this.h);

            canRun = false;

        }

        if(runDelay + 200L < System.currentTimeMillis())
        {
            canRun = true;
            runDelay = System.currentTimeMillis();
        }

        if(attackDelay + 1000L < System.currentTimeMillis())
        {
            canAttack = true;
            attackDelay = System.currentTimeMillis();
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

    private void setAttackedStatus(boolean x)
    {
        this.attackedStatus = x;
    }

    public void damage(int damage)
    {
        this.healthPoints -= damage;
    }

    public int getHealthPoints()
    {
        return this.healthPoints;
    }

    public Rectangle setDirection()
    {

        Texture tex;

        if(this.dir[0] < 0) {
            tex = this.textures.get(this.textures.ZOMBIE_RIGHT);
        }
        else if(this.dir[0] > 0)
        {
            tex = this.textures.get(this.textures.ZOMBIE_LEFT);

        }
        else if(this.dir[1] < 0) {
            tex = this.textures.get(this.textures.ZOMBIE_BACK);
        }
        else
        {
            tex = this.textures.get(this.textures.ZOMBIE_UP);
        }

        return new Rectangle(x, y, this.w, this.h, new int[] {0, 0, 0, 255}, tex, "sheep");

    }

    public void moveX(float mx)
    {
        this.zombie.moveX(mx);
        this.x += mx;
    }

    public void moveY(float my)
    {
        this.zombie.moveY(my);
        this.y += my;
    }

    public Rectangle getzombie()
    {
        return this.zombie;
    }

    public float getX()
    {
        return this.x;
    }

    public float getY()
    {
        return this.y;
    }

    public boolean canAttack()
    {
        return this.canAttack;
    }
}
