package graphics;

import java.util.ArrayList;

public class Textures {
    public ArrayList<Texture> textures = new ArrayList<>();
    public static final int
            GRASS = 0,
            TREE = 1,
            PLAYER_BACK_1 = 2,
            PLAYER_UP_1 = 3,
            PLAYER_LEFT_1 = 4,
            PLAYER_RIGHT_1 = 5,
            FLOWER = 6,
            SHEEP_UP = 7,
            SHEEP_BACK = 8,
            SHEEP_LEFT = 9,
            SHEEP_RIGHT = 10,
            TIMBER = 11,
            WOOD = 12,
            PLANKS = 13,
            WATER = 14,
            PIG_UP = 15,
            PIG_BACK = 16,
            PIG_LEFT = 17,
            PIG_RIGHT = 18,
            CAVE_ENTRANCE = 19,
            WOOL = 20,
            WOOL_SUIT_UP = 21,
            WOOL_SUIT_BACK = 22,
            WOOL_SUIT_LEFT = 23,
            WOOL_SUIT_RIGHT = 24,
            HEART = 25,
            SHIELD = 26,
            ZOMBIE_UP = 27,
            ZOMBIE_BACK = 28,
            ZOMBIE_LEFT = 29,
            ZOMBIE_RIGHT = 30;

    public float size = 0.3f;

    public Textures()
    {
        textures.add(new Texture("assests/grass.png"));
        textures.add(new Texture("assests/tree.png"));
        textures.add(new Texture("assests/player/player_back_1.png"));
        textures.add(new Texture("assests/player/player_up_1.png"));
        textures.add(new Texture("assests/player/player_left_1.png"));
        textures.add(new Texture("assests/player/player_right_1.png"));
        textures.add(new Texture("assests/flower.png"));
        textures.add(new Texture("assests/sheep_up.png"));
        textures.add(new Texture("assests/sheep_back.png"));
        textures.add(new Texture("assests/sheep_left.png"));
        textures.add(new Texture("assests/sheep_right.png"));
        textures.add(new Texture("assests/timber.png"));
        textures.add(new Texture("assests/wood.png"));
        textures.add(new Texture("assests/planks.png"));
        textures.add(new Texture("assests/water.png"));
        textures.add(new Texture("assests/pig_up.png"));
        textures.add(new Texture("assests/pig_back.png"));
        textures.add(new Texture("assests/pig_left.png"));
        textures.add(new Texture("assests/pig_right.png"));
        textures.add(new Texture("assests/cave_entrance.png"));
        textures.add(new Texture("assests/wool.png"));
        textures.add(new Texture("assests/player/suits/wool_suit_up.png"));
        textures.add(new Texture("assests/player/suits/wool_suit_back.png"));
        textures.add(new Texture("assests/player/suits/wool_suit_left.png"));
        textures.add(new Texture("assests/player/suits/wool_suit_right.png"));
        textures.add(new Texture("assests/heart.png"));
        textures.add(new Texture("assests/shield.png"));
        textures.add(new Texture("assests/zombie_up.png"));
        textures.add(new Texture("assests/zombie_back.png"));
        textures.add(new Texture("assests/zombie_left.png"));
        textures.add(new Texture("assests/zombie_right.png"));

    }

    public Texture get(int index)
    {
        return textures.get(index);
    }
}
