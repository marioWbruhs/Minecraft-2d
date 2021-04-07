package main;

import events.Keyboard;
import events.StateManager;
import game.Achievement;
import game.Game;
import graphics.Rectangle;
import graphics.Textures;
import graphics.ui.DeathMenu;
import graphics.ui.MainMenu;
import graphics.ui.Terminal;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Main {
    private int[] resolution = {1080, 720};
    private String title = "Minecraft 2D";

    private long window;

    private Game game;
    private MainMenu mainMenu;
    private StateManager stateManager;
    private Achievement achievements;
    private int fps = 0;
    private long fpsDelay = System.currentTimeMillis();
    private int lastFps = 0;
    private Terminal terminal;
    private static Main instance = new Main();
    private Textures texturesL;
    private DeathMenu deathMenu;

    private ArrayList<Rectangle> background = new ArrayList<>();

    public void run()
    {
        init();

        createConstants();

        loop();
    }

    public static ByteBuffer load_image(String path){
        ByteBuffer image;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            image = stbi_load(path, w, h, comp, 4);
        }
        return image;
    }

    private void init()
    {
        if(!glfwInit())
        {
            throw new IllegalStateException("failed to initialise glfw");
        }

        this.window = glfwCreateWindow(resolution[0], resolution[1], title, 0, 0);

        if(this.window == 0)
        {
            throw new IllegalStateException("failed to create window");
        }

        ByteBuffer resource_01 = load_image("assests/icon.png");


        GLFWImage image = GLFWImage.malloc();
        GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(16, 16, resource_01);
        imagebf.put(0, image);
        glfwSetWindowIcon(window, imagebf);



        glfwMakeContextCurrent(this.window);

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
    }

    private void createConstants()
    {
        this.texturesL = new Textures();
        this.game = new Game(this.window, resolution, this.texturesL);
        this.mainMenu = new MainMenu(resolution);
        this.stateManager = new StateManager();
        this.deathMenu = new DeathMenu(resolution);
        this.terminal = new Terminal();

        this.background.add(new Rectangle(-1f, -1f, 2f, 2f, new int[] {255, 255, 255, 255}, "background"));
        this.background.add(new Rectangle(-1f, -1f, 2f, 2f, new int[] {54, 25, 70, 255}, "background"));
        this.background.add(new Rectangle(-1f, -1f, 2f, 2f, new int[] {0, 0, 0, 100}, "background"));
        this.background.add(new Rectangle(-1f, -1f, 2f, 2f, new int[] {36, 31, 24, 255}, "background"));
        this.background.add(new Rectangle(-1f, -1f, 2f, 2f, new int[] {214, 45, 45, 100}, "background"));
    }

    private void loop()
    {
        while(!glfwWindowShouldClose(this.window))
        {
            glfwPollEvents();

            ++fps;

            if(fpsDelay + 1000L < System.currentTimeMillis())
            {
                lastFps = fps;
                fps = 0;
                fpsDelay = System.currentTimeMillis();
                System.out.println(lastFps);
            }

            this.background.get(this.stateManager.getState()).draw();


            if(this.stateManager.isState(this.stateManager.IN_GAME)) {
                this.game.update();
                this.game.draw();

                if(this.game.getPlayer().isDead())
                {
                    this.stateManager.playerDied();
                }

            }
            else if(this.stateManager.isState(this.stateManager.MAIN_MENU))
            {
                this.mainMenu.update(stateManager);
                this.stateManager = this.mainMenu.getStates();
            }
            else if(this.stateManager.isState(this.stateManager.TERMINAL))
            {
                this.terminal.update(Keyboard.keyDown());
            }
            else if(this.stateManager.isState(this.stateManager.PLAYER_DEAD))
            {
                this.deathMenu.update(this.stateManager);
                this.stateManager = this.deathMenu.getStates();

                this.game.resetPlayer();


            }
            else
            {
                System.exit(0);
            }

            this.stateManager.update();


            glfwSwapBuffers(this.window);
        }
    }

    public static Main getInstance()
    {
        return instance;
    }

    public void setFPS(int fps)
    {
        glfwSwapInterval(fps);
    }

    public static void main(String[] args)
    {
        new Main().run();
    }
}
