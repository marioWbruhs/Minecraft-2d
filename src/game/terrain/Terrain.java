package game.terrain;

import graphics.Rectangle;
import graphics.Textures;

import java.util.ArrayList;
import java.util.Random;

public class Terrain {
    private ArrayList<Rectangle> terrain = new ArrayList<>();
    private ArrayList<Rectangle> caveSystem = new ArrayList<>();
    private Textures textures;
    private Random rand = new Random();

    public void generate(float sx, float sy, float w, float h, Textures tex)
    {
        textures = tex;

        for(float x = w; x > sx; x -= textures.size)
        {
            for(float y = h; y > sy; y -= textures.size)
            {
                Rectangle block = new Rectangle(x, y, textures.size, textures.size, new int[] {0, 0, 0, 255}, textures.get(textures.GRASS), "grass");

                terrain.add(block);

                if(rand.nextInt(100) > 88)
                {
                    Rectangle tree = new Rectangle(x, y, textures.size, textures.size * 1.5f, new int[] {0, 0, 0, 255}, textures.get(textures.TREE), "tree");

                    terrain.add(tree);
                }

                else if(rand.nextInt(100) > 88)
                {
                    Rectangle flower = new Rectangle(x, y, textures.size, textures.size, new int[] {0, 0, 0, 255}, textures.get(textures.FLOWER), "flower");

                    terrain.add(flower);
                }

                else if(rand.nextInt(1000) > 978)
                {
                    //Rectangle caveEntrance = new Rectangle(x, y, textures.size, textures.size, new int[] { 0, 0, 0, 255}, textures.get(textures.CAVE_ENTRANCE), "cave_entrance");


                    //terrain.add(caveEntrance);
                }


            }
        }
    }

    public ArrayList<Rectangle> getTerrain()
    {
        return terrain;
    }

    public ArrayList<Rectangle> getCaveSystem()
    {
        return this.caveSystem;
    }
}
