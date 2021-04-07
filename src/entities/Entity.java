package entities;

import graphics.Rectangle;
import graphics.Texture;
import graphics.Textures;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Entity {
    public float w = 0.3f, h = 0.3f;
    public Textures textures;
    private Random rand = new Random();
    private boolean canMove;
    private int moveChance = 900;
    private long moveDelay = System.currentTimeMillis();
    private int dir[] = {0, 0};
    private int healthPoints = 10;
    private float[] enemy = {0f, 0f};
    private boolean isAttacked = false;

    public void setTextures(Textures tex)
    {
        this.textures = tex;
    }

    public float getWidth()
    {
        return this.w;
    }

    public float getHeight()
    {
        return this.h;
    }

    public Rectangle createEntity(float x, float y, String id)
    {
        return new Rectangle(x, y, getWidth(), getHeight(), new int[] {0, 0, 0, 255}, textures.get(textures.SHEEP_RIGHT), id);
    }

    public Rectangle AI(Rectangle en)
    {
        float x = en.x;
        float y = en.y;

        if(moveDelay + 1000l < System.currentTimeMillis())
        {
            moveDelay = System.currentTimeMillis();
            this.canMove = true;
        }
        if(this.canMove) {
            if (rand.nextInt(1000) > moveChance) {
                float mx = (rand.nextInt(3) - 1) * getWidth();
                x += mx;
                if(mx > 0) {
                    this.dir = new int[]{1, 0};
                }
                else
                {
                    this.dir = new int[] {-1, 0};
                }
                //System.out.println(mx);

            } else if (rand.nextInt(1000) > moveChance) {
                float my = (rand.nextInt(3) - 1) * getHeight();
                y += my;
                if(my > 0) {
                    this.dir = new int[]{0, 1};
                }
                else
                {
                    this.dir = new int[] {0, -1};
                }
                //System.out.println(my);
            }

            canMove = false;
        }

        Texture tex = en.getTexture();

        if(en.getId().equalsIgnoreCase("sheep")) {


            if(this.dir[0] < 0) {
                tex = this.textures.get(this.textures.SHEEP_RIGHT);
            }
            else if(this.dir[0] > 0)
            {
                tex = this.textures.get(this.textures.SHEEP_LEFT);

            }
            else if(this.dir[1] < 0) {
                tex = this.textures.get(this.textures.SHEEP_BACK);
            }
            else if(this.dir[1] > 0)
            {
                tex = this.textures.get(this.textures.SHEEP_UP);
            }

        }

        return new Rectangle(x, y, getWidth(), getHeight(), new int[] {0, 0, 0, 255}, tex, en.getId());
    }

    public void damage(int healthPoints)
    {
        this.healthPoints -= healthPoints;
        this.isAttacked = true;
    }

    public void setAttackedStatus(boolean attacked)
    {
        this.isAttacked = attacked;
    }

    public boolean isAttacked()
    {
        return this.isAttacked;
    }

    public int getHealthPoints()
    {
        return this.healthPoints;
    }

    public void runFromTarget(float tx, float ty)
    {
        this.enemy = new float[] {tx, ty};
    }

    public float[] getEnemyTarget()
    {
        return this.enemy;
    }

    public boolean canMove()
    {
        return this.canMove;
    }

    public Rectangle setDirection(int[] direction, float x, float y)
    {
        this.dir = direction;

        Texture tex;

        if(this.dir[0] < 0) {
            tex = this.textures.get(this.textures.SHEEP_RIGHT);
        }
        else if(this.dir[0] > 0)
        {
            tex = this.textures.get(this.textures.SHEEP_LEFT);

        }
        else if(this.dir[1] < 0) {
            tex = this.textures.get(this.textures.SHEEP_BACK);
        }
        else
        {
            tex = this.textures.get(this.textures.SHEEP_UP);
        }

        return new Rectangle(x, y, getWidth(), getHeight(), new int[] {0, 0, 0, 255}, tex, "sheep");

    }


}
