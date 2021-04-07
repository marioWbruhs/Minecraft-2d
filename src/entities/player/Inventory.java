package entities.player;

import graphics.Rectangle;
import graphics.Texture;
import graphics.Textures;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Texture> textures = new ArrayList<Texture>();
    private ArrayList<Rectangle> items = new ArrayList<>();
    private Textures textureList;

    public Inventory(Textures tex)
    {
        this.textureList = tex;
    }

    public void add(int item, String id)
    {
        this.textures.add(this.textureList.get(item));
        Rectangle r = new Rectangle(0.885f, 0.9f - (this.textures.size() * 0.13f), 0.1f, 0.1f, new int[] {0, 0, 0, 255}, this.textureList.get(item), id);
        this.items.add(r);
    }

    public void add(Texture tex, String id)
    {

        if(id.equalsIgnoreCase("tree"))
        {
            tex = this.textureList.get(this.textureList.TIMBER);
            id = "timber";
        }

        this.textures.add(tex);
        Rectangle r = new Rectangle(0.885f, 0.9f - (this.textures.size() * 0.13f), 0.1f, 0.1f, new int[] {0, 0, 0, 255}, tex, id);
        this.items.add(r);
    }

    public void remove(int index)
    {
        this.textures.remove(index);
        this.items.remove(index);
        for(int i = index; i < this.items.size(); i++)
        {
            this.items.get(i).setY(0.9f - ((i + 1) * 0.13f));
        }
    }

    public void draw()
    {
        //background.drawTex();

        for(Rectangle item : this.items)
        {
            item.drawTex();
            //System.out.println(item.texture.name);
        }
    }

    public int getSize()
    {
        return this.items.size();
    }

    public ArrayList<Rectangle> getBlocks()
    {
        return this.items;
    }

    public ArrayList<Rectangle> getItems()
    {
        return this.items;
    }

}
