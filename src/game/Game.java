package game;

import entities.Sheep;
import entities.Zombie;
import entities.player.Player;
import events.Keyboard;
import events.Mouse;
import game.terrain.Terrain;
import graphics.Rectangle;
import graphics.Texture;
import graphics.Textures;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Terrain terrain;
    private ArrayList<Rectangle> terrainMap;
    private ArrayList<Rectangle> caveSystem;
    private Keyboard keyboard;
    private Player player;
    private float[] moved = {0f, 0f};
    private long window;
    private long moveDelay = System.currentTimeMillis();
    private boolean canMove = true;
    private Mouse mouse;
    private ArrayList<Sheep> sheep = new ArrayList<Sheep>();
    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    private boolean canPlaceBlock = true;
    private long placeBlockDelay = System.currentTimeMillis();
    public static Textures textureList;
    private boolean cave = false;
    private Random rand = new Random();
    private int[] windowResolution;

    public Game(long window, int[] size, Textures textureList)
    {
        this.textureList = textureList;
        this.windowResolution = size;
        this.terrain = new Terrain();
        this.terrain.generate(-10f, -10f, 20f, 20f, textureList);
        this.terrainMap = this.terrain.getTerrain();
        this.caveSystem = this.terrain.getCaveSystem();
        this.window = window;
        this.player = new Player(this.window, size, this.textureList);
        glfwSetKeyCallback(this.window, keyboard = new Keyboard());
        this.mouse = new Mouse();
        this.mouse.setupMouse(window, size);

        for(int i = 0; i < rand.nextInt(10) + 40; i++)
        {
            this.sheep.add(new Sheep(rand.nextInt(20) - 10 * this.textureList.size, rand.nextInt(20) - 10 * this.textureList.size, textureList));
        }
        for(int i = 0; i < rand.nextInt(10) + 30; i++)
        {
            this.zombies.add(new Zombie(rand.nextInt(20) - 10 * this.textureList.size, rand.nextInt(20) - 10 * this.textureList.size, textureList));
        }

    }


    private void drawTerrain() {
        boolean collided = false;
        Rectangle collidedBlock = this.terrainMap.get(0);

        for (int i = 0; i < this.terrainMap.size(); i++) {
            this.terrainMap.get(i).moveX(moved[0]);
            this.terrainMap.get(i).moveY(moved[1]);

            if(this.terrainMap.get(i).collide(this.player.getPlayer()) && isBlockRigid(this.terrainMap.get(i).getId()))
            {

                collided = true;

            }




            if(this.terrainMap.get(i).x > -1f && this.terrainMap.get(i).x < 1f && this.terrainMap.get(i).y > -1f && this.terrainMap.get(i).y < 1f)
                this.terrainMap.get(i).drawTex();
        }




        //for(Sheep sheep : this.sheep)
        //{

        //}

        if(collided && !Keyboard.isKeyDown(GLFW_KEY_SPACE))
        {
            for (int i = 0; i < this.terrainMap.size(); i++) {
                this.terrainMap.get(i).moveX(-moved[0]);
                this.terrainMap.get(i).moveY(-moved[1]);

                if (this.terrainMap.get(i).x > -1f && this.terrainMap.get(i).x < 1f && this.terrainMap.get(i).y > -1f && this.terrainMap.get(i).y < 1f)
                    this.terrainMap.get(i).drawTex();
            }
        }
        else
        {
            for(Sheep sheep : this.sheep)
            {

                sheep.moveX(this.moved[0]);
                sheep.moveY(this.moved[1]);


                if(this.player.getPlayer().collide(sheep.getSheep()))
                {
                    sheep.moveX(-this.moved[0]);
                    sheep.moveY(-this.moved[1]);
                }

                sheep.update();
            }
            for(Zombie zombie : this.zombies)
            {

                zombie.moveX(this.moved[0]);
                zombie.moveY(this.moved[1]);


                if(this.player.getPlayer().collide(zombie.getzombie())) {
                    zombie.moveX(-this.moved[0]);
                    zombie.moveY(-this.moved[1]);

                    if (zombie.canAttack() && zombie.canAttack()) {
                        this.player.attack(zombie.pow);
                        zombie.setCombatStatus();
                    }
                }

                zombie.update();
            }

            /*for(Pig pig : this.pigs)
            {

                pig.moveX(this.moved[0]);
                pig.moveY(this.moved[1]);


                if(this.player.getPlayer().collide(pig.getSheep()))
                {
                    pig.moveX(-this.moved[0]);
                    pig.moveY(-this.moved[1]);
                }

                pig.update();
            }

             */
        }


    }

    public Rectangle getPlayerBlockCollider()
    {
        Rectangle coll = new Rectangle(this.player.getX() , this.player.getY(), this.player.getSize(), this.player.getSize(), new int[] {0, 0, 0, 255}, "block_breaker");

        if(this.player.getDir()[0] == 1)
        {
            coll.moveX(-this.player.getSize());
        }
        else if(this.player.getDir()[0] == -1)
        {
            coll.moveX(this.player.getSize());
        }

        if(this.player.getDir()[1] == 1)
        {
            coll.moveY(this.player.getSize());
        }
        else if(this.player.getDir()[1] == -1)
        {
            coll.moveY(-this.player.getSize());
        }

        return coll;
    }

    public void addToPlayerInventory(Texture tex, String id)
    {
        this.player.addToInventory(tex, id);
    }

    public void deleteBlock()
    {
        if(this.mouse.isButtonDown(0))
        {
            Rectangle coll = getPlayerBlockCollider();// = new Rectangle(this.player.getX() , this.player.getY(), this.player.getSize(), this.player.getSize(), new int[] {0, 0, 0, 255}, "block_breaker");


            for(Rectangle rect : this.terrainMap)
            {
                if(rect.x > -1f && rect.x < 1f && rect.y > -1f && rect.y < 1f) {
                    if (coll.collide(rect) && !rect.getId().equalsIgnoreCase("grass")) {
                        if (this.player.getInventorySize() < this.player.maxInventorySize()) {
                            this.player.addToInventory(rect.getTexture(), rect.getId());
                            this.terrainMap.remove(rect);
                            break;
                        }

                    }
                }

            }

        }
    }

    public void addBlock(Rectangle block)
    {
        this.terrainMap.add(block);
    }

    public void draw()
    {
        drawTerrain();

        for(Sheep sheep : this.sheep)
        {
            sheep.draw();
        }
        for(Zombie zombie : this.zombies)
        {
            zombie.draw();
        }


        this.player.draw();

    }

    public void update()
    {
        this.player.update(this.terrainMap);

        this.moved = new float[] {0f, 0f};
        if(canMove) {
            if (keyboard.isKeyDown(GLFW_KEY_W)) {
                this.moved[1] = -this.player.getSize();
            }
            if (keyboard.isKeyDown(GLFW_KEY_A)) {
                this.moved[0] = -this.player.getSize();
            }
            if (keyboard.isKeyDown(GLFW_KEY_S)) {
                this.moved[1] = this.player.getSize();
            }
            if (keyboard.isKeyDown(GLFW_KEY_D)) {
                this.moved[0] = this.player.getSize();
            }

            canMove = false;
        }

        for(Sheep sheep : this.sheep) {
            for (int i = 0; i < this.terrainMap.size(); i++) {
                Rectangle block = this.terrainMap.get(i);

                if (block.x > -1f && block.x < 1f && block.y > -1f && block.y < 1f) {
                    if (block.collide(sheep.getSheep()) && isBlockRigid(block.getId())) {
                        sheep.moveX(moved[0]);
                        sheep.moveY(moved[1]);
                       //System.out.println(sheep.getX() + " " + sheep.getY());
                    }
                }
            }
        }

        for(Zombie zombie : this.zombies) {
            for (int i = 0; i < this.terrainMap.size(); i++) {
                Rectangle block = this.terrainMap.get(i);

                if (block.x > -1f && block.x < 1f && block.y > -1f && block.y < 1f) {
                    if (block.collide(zombie.getzombie()) && isBlockRigid(block.getId())) {
                        zombie.moveX(moved[0]);
                        zombie.moveY(moved[1]);
                        //System.out.println(zombie.getX() + " " + zombie.getY());
                    }
                }
            }
        }

        if(moveDelay + 200L < System.currentTimeMillis())
        {
            moveDelay = System.currentTimeMillis();
            canMove = true;
        }

        deleteBlock();

        if(mouse.isButtonDown(1) && canPlaceBlock && !this.player.isInventoryEmpty())
        {
            //Rectangle coll = player.selectedInventoryItem();
            String id = this.player.selectedInventoryItem().getId();
            Texture tex = this.player.selectedInventoryItem().getTexture();

            if(this.player.selectedInventoryItem().getId().equalsIgnoreCase("timber"))
            {
                id = "wood";
                tex = this.textureList.get(this.textureList.WOOD);
            }
            else if(id.equalsIgnoreCase("wood"))
            {
                id = "planks";
                tex = this.textureList.get(this.textureList.PLANKS);
            }

            Rectangle coll = new Rectangle(this.player.selectedInventoryItem().getX(), this.player.selectedInventoryItem().getY(), this.textureList.size, this.textureList.size, new int[] {0, 0, 0, 255}, tex, id);
            coll.setX(this.player.getX());
            coll.setY(this.player.getY());

            if(this.player.getDir()[0] == 1)
            {
                coll.moveX(-this.player.getSize());
            }
            else if(this.player.getDir()[0] == -1)
            {
                coll.moveX(this.player.getSize());
            }

            if(this.player.getDir()[1] == 1)
            {
                coll.moveY(this.player.getSize());
            }
            else if(this.player.getDir()[1] == -1)
            {
                coll.moveY(-this.player.getSize());
            }

            boolean collide = false;

            for(Rectangle block : this.terrainMap)
            {
                if(block.x > -1f && block.x < 1f && block.y > -1f && block.y < 1f)
                    if(block.collide(coll) && !block.getId().equalsIgnoreCase("grass"))
                    {
                        collide = true;
                    }
            }

            if(!collide) {
                addBlock(coll);
                this.player.removeFromInventoryCurrentItem();
            }
            canPlaceBlock = false;
        }

        if(this.mouse.isButtonDown(0))
        {
            for(Sheep csheep : this.sheep)
            {
                if(getPlayerBlockCollider().collide(csheep.getSheep()) && this.player.canAttack())
                {
                    csheep.damage(this.player.getWeaponPower());
                    if(csheep.getHealthPoints() < 1  && this.player.getHealthPoints() < 10)
                    {
                        this.player.regenerateHealthPoints(5);
                    }
                    if(csheep.getHealthPoints() < 1 && this.player.getInventorySize() < this.player.maxInventorySize()) {
                        this.player.addToInventory(this.textureList.get(this.textureList.WOOL), "wool");
                    }
                }
            }

            for(Zombie czombie : this.zombies)
            {
                if(getPlayerBlockCollider().collide(czombie.getzombie()) && this.player.canAttack())
                {
                    czombie.damage(this.player.getWeaponPower());

                    if(czombie.getHealthPoints() < 1 && this.player.getHealthPoints() < 10)
                    {
                        this.player.regenerateHealthPoints(1);
                    }

                }
            }
        }

        if(placeBlockDelay + 300L < System.currentTimeMillis())
        {
            placeBlockDelay = System.currentTimeMillis();
            canPlaceBlock = true;
        }
    }


    public boolean isBlockRigid(String id)
    {
        boolean rigid = false;
        if(!(id.equalsIgnoreCase("grass") || id.equalsIgnoreCase("flower")))
            rigid = true;

        return rigid;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public void resetPlayer()
    {
        this.player = null;

        this.player = new Player(this.window, this.windowResolution, this.textureList);
    }


}
