package entities.player;

import events.Keyboard;
import events.Mouse;
import graphics.Rectangle;
import graphics.Texture;
import graphics.Textures;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private Rectangle playerController;
    private float size;
    private Rectangle selectedBlock;
    private Mouse mouse;
    private Inventory inventory;
    private ArrayList<Texture> textures = new ArrayList<>();
    private Texture direction;
    private int[] movingDir = {0, -1};
    private int inventoryItem = 0;
    private Rectangle inventoryItemRectangle = new Rectangle(1f, 0.885f, -0.13f, -0.13f, new int[] { 0, 0, 0, 255}, new Texture("assests/gui/inventory.png"), "inventory_background");
    private Rectangle invBackground = new Rectangle(1f, -1.0f, -0.13f, 2f, new int[] {0, 0, 0, 100}, new Texture("assests/gui/inventory.png"), "inventory_background" );
    private long scrollDelay = System.currentTimeMillis();
    private boolean canScroll = true;
    private int damage = 2;
    private String weapon = "fist";
    private boolean canAttack = true;
    private long attackDelay = System.currentTimeMillis();
    private Rectangle equipment;
    private Textures texturesL;
    private boolean canUseItem = true;
    private int healthPoints = 10;
    private int armor = 0;
    private Rectangle heart;
    private Rectangle shield;
    private boolean isDead;
    private String diedMessage;

    public Player(long window, int[] size, Textures textures)
    {
        this.texturesL = textures;

        this.size = texturesL.size;
        this.mouse = new Mouse();
        this.mouse.setupMouse(window, size);
        this.playerController = new Rectangle(0f, 0f, texturesL.size, texturesL.size, new int[] {0, 0, 0, 255}, texturesL.get(texturesL.PLAYER_BACK_1), "player");
        this.selectedBlock = new Rectangle(-10f, -10f, texturesL.size, texturesL.size, new int[] {0, 0, 0, 100}, "selected_block");
        this.inventory = new Inventory(textures);
        this.textures.add(texturesL.get(texturesL.PLAYER_BACK_1));
        this.textures.add(texturesL.get(texturesL.PLAYER_UP_1));
        this.textures.add(texturesL.get(texturesL.PLAYER_LEFT_1));
        this.textures.add(texturesL.get(texturesL.PLAYER_RIGHT_1));

        this.direction = this.textures.get(0);

        this.shield = new Rectangle( -10f, 1f - 0.2f, 0.1f, 0.1f, new int[] {0, 0, 0, 255}, this.texturesL.get(this.texturesL.SHIELD), "shield");
        this.heart = new Rectangle( -10f, 1f - 0.1f, 0.1f, 0.1f, new int[] {0, 0, 0, 255}, this.texturesL.get(this.texturesL.HEART), "heart");
    }

    public void update(ArrayList<Rectangle> blocks)
    {

        if(!this.canAttack)
        {
            if(this.attackDelay + 300L < System.currentTimeMillis())
            {
                this.canAttack = true;
            }
        }

        if(this.inventory.getSize() > 0) {
            if(this.inventoryItem < this.inventory.getSize() - 1) {

                if (this.inventory.getBlocks().get(this.inventoryItem).getId().equalsIgnoreCase("timber")) {
                    this.damage = 3;
                } else {
                    this.damage = 1;
                }
            }
            else
            {
                this.inventoryItem = this.inventory.getSize() - 1;
            }
        }

        if(Keyboard.isKeyDown(GLFW_KEY_S))
        {
            this.playerController.setTexture(this.textures.get(0));
            movingDir[1] = -1;
            movingDir[0] = 0;
        }
        else if(Keyboard.isKeyDown(GLFW_KEY_W))
        {
            this.playerController.setTexture(this.textures.get(1));
            movingDir[1] = 1;
            movingDir[0] = 0;
        }
        else if(Keyboard.isKeyDown(GLFW_KEY_A))
        {
            this.playerController.setTexture(this.textures.get(2));
            movingDir[0] = -1;
            movingDir[1] = 0;
        }
        else if(Keyboard.isKeyDown(GLFW_KEY_D))
        {
            this.playerController.setTexture(this.textures.get(3));
            movingDir[0] = 1;
            movingDir[1] = 0;
        }

        for(Rectangle block : blocks)
        {
            if(block.x > -1f && block.x < 1f && block.y > -1f && block.y < 1f)
            {
                if(block.collidesMouse(this.mouse.getX(), this.mouse.getY()) &&  block.getId().equalsIgnoreCase("grass"))
                {
                    this.selectedBlock.moveToPos(block.x, block.y);
                }
            }
        }

        if(Keyboard.isKeyDown(GLFW_KEY_1))
        {
            this.inventoryItem = 0;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_2))
        {
            this.inventoryItem = 1;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_3))
        {
            this.inventoryItem = 2;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_4))
        {
            this.inventoryItem = 3;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_5))
        {
            this.inventoryItem = 4;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_6))
        {
            this.inventoryItem = 5;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_7))
        {
            this.inventoryItem = 6;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_8))
        {
            this.inventoryItem = 7;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_9))
        {
            this.inventoryItem = 8;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_0))
        {
            this.inventoryItem = 9;
        }

        if(this.inventoryItem > this.inventory.getSize() - 1)
        {
            this.inventoryItem = this.inventory.getSize() - 1;
        }

        if(Keyboard.isKeyDown(GLFW_KEY_E) && canUseItem) {
            if (this.inventory.getSize() > 0 && equipment == null) {
                if (this.inventory.getBlocks().get(this.inventoryItem).getId().equalsIgnoreCase("wool")) {
                    this.equipment = new Rectangle(0f, 0f, this.texturesL.size, this.texturesL.size, new int[]{0, 0, 0, 255}, this.texturesL.get(this.texturesL.WOOL_SUIT_BACK), "wool_chestplate");
                    this.removeFromInventoryCurrentItem();
                    this.inventoryItem = this.inventory.getSize() - 1;
                    this.armor = 4;

                }
            } else if (equipment != null) {
                addToInventory(this.texturesL.get(texturesL.WOOL), "wool");
                this.inventoryItem = this.inventory.getSize() - 1;
                equipment = null;
                this.armor = 0;
            }

            /*System.out.println(this.inventory.getBlocks().get(this.inventoryItem).getId());

            if (this.inventory.getBlocks().get(this.inventoryItem).getId().equalsIgnoreCase("timber"))
            {
                this.removeFromInventoryCurrentItem();
                this.inventoryItem = this.inventory.getSize() - 1;

                this.damage = 3;
            }

            if(this.damage == 3)
            {
                addToInventory(this.texturesL.get(texturesL.TIMBER), "timber");
                this.inventoryItem = this.inventory.getSize() - 1;
                this.damage = 1;
            }
             */

            canUseItem = false;
        }

        if(!Keyboard.isKeyDown(GLFW_KEY_E))
        {
            canUseItem = true;
        }

        if(this.equipment != null)
        {
            if(this.movingDir[0] == -1)
                this.equipment.texture = this.texturesL.get(texturesL.WOOL_SUIT_LEFT);
            else if(this.movingDir[0] == 1)
                this.equipment.texture = this.texturesL.get(texturesL.WOOL_SUIT_RIGHT);
            else if(this.movingDir[1] == -1)
                this.equipment.texture = this.texturesL.get(texturesL.WOOL_SUIT_BACK);
            else
                this.equipment.texture = this.texturesL.get(texturesL.WOOL_SUIT_UP);
        }


        if(canScroll) {
            int mw = Mouse.getDWheel();
            //System.out.println(mw);
            if (mw < 0) {

                if(inventoryItem < this.inventory.getItems().size() - 1) {
                    inventoryItem++;
                }
                else
                {
                    inventoryItem = 0;
                }
            }
            if(mw > 0)
            {
                if(inventoryItem > 0) {
                    inventoryItem--;
                }
                else
                {
                    inventoryItem = this.inventory.getItems().size() - 1;
                }
            }

            canScroll = false;
        }

        if(scrollDelay + 300L < System.currentTimeMillis())
        {
            scrollDelay = System.currentTimeMillis();
            canScroll = true;
        }

        if(this.inventory.getSize() > 0) {

            float invy = this.inventory.getBlocks().get(this.inventoryItem).getY() + 0.115f;

            if (inventoryItem < 2) {
                //invy = 1f - (inventoryItem * 0.115f);
            }
            this.inventoryItemRectangle.setY(invy);
            this.inventoryItemRectangle.setX(1f);
        }
        else
        {
            this.inventoryItem = 0;
        }

        if(this.armor < 1 && equipment != null)
        {
            equipment = null;

            this.armor = 0;

        }

        isDying();

    }

    public void draw()
    {
        this.playerController.drawTex();
        if(equipment != null)
            equipment.drawTex();

        //this.selectedBlock.draw();

        this.invBackground.drawTex();
        this.inventoryItemRectangle.drawTex();
        this.inventory.draw();

        for(float i = 1f; i >= -(this.healthPoints / 2 - 4) * this.heart.w; i -= this.heart.h)
        {
            this.heart.moveToX(-i);
            this.heart.drawTex();
        }

        for(float i = 1f; i >= -(this.armor / 2 - 11) * this.shield.w; i -= this.shield.h)
        {
            this.shield.moveToX(-i);
            this.shield.drawTex();
        }


    }

    public boolean isDead()
    {
        return this.isDead;
    }

    private void isDying()
    {
        if(this.healthPoints < -12)
        {
            this.isDead = true;

        }
    }

    public float getSize()
    {
        return this.size;
    }

    public Rectangle getPlayer()
    {
        return playerController;
    }

    public float getX()
    {
        return this.playerController.getX();
    }
    public float getY()
    {
        return this.playerController.getY();
    }
    public int[] getDir()
    {
        return this.movingDir;
    }
    public void addToInventory(Texture tex, String id)
    {
        this.inventory.add(tex, id);
    }

    public int getInventorySize()
    {
        return this.inventory.getSize();
    }

    public int maxInventorySize()
    {
        return 14;
    }

    public Rectangle selectedInventoryItem()
    {
        Rectangle r = new Rectangle(this.inventory.getBlocks().get(this.inventoryItem).x, this.inventory.getBlocks().get(this.inventoryItem).y, this.size, this.size, new int[] {0, 0, 0, 255}, this.inventory.getBlocks().get(this.inventoryItem).getTexture(), this.inventory.getBlocks().get(this.inventoryItem).getId());
        return r;

    }

    public void removeFromInventoryCurrentItem()
    {
        this.inventory.remove(this.inventoryItem);
    }

    public boolean isInventoryEmpty()
    {
        return !(this.inventory.getSize() > 0);
    }

    public ArrayList<Rectangle> getInventory()
    {
        return this.inventory.getItems();
    }

    public int getWeaponPower()
    {
        return this.damage;
    }

    public boolean canAttack()
    {

        if(this.canAttack)
        {
            this.canAttack = false;
            this.attackDelay = System.currentTimeMillis();
            return true;
        }
        else
        {
            return false;
        }
    }

    public void getAttacked()
    {
        if(this.armor > 0)
        {
            this.armor -= 2;
        }
        else {
            this.healthPoints -= 2;
        }
    }

    public void attack(int damage)
    {
        this.healthPoints -= damage;

        if(this.equipment != null)
        {
            this.healthPoints += damage / 2;
        }
    }

    public void regenerateHealthPoints(int healthPoints)
    {
        this.healthPoints += healthPoints;
    }

    public int getHealthPoints()
    {
        return this.healthPoints;
    }


}
