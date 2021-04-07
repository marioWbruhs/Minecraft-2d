package graphics.ui;

import graphics.Rectangle;
import graphics.TTFFontRenderer;
import main.Main;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Terminal {
    private Rectangle background = new Rectangle(-1f, -1f, 2f, 2f, new int[]{36, 31, 24, 255}, "background_vim");

    private ArrayList<String> commands = new ArrayList<>();

    public char[] characters = new char[90-65 + 10];
    TTFFontRenderer fontRenderer;
    private String command = "";
    private long delayTimeMs = 200L;
    private int offsetY = 0;
    private int defaultColor = 0xffffffff;

    public long delay = System.currentTimeMillis();

    public Terminal()
    {
        int j = 0;

        for(char k = '0'; k <= '9'; k++)
        {
            characters[j] = k;
            j++;
        }
        for(char i = 'a'; i < 'z'; i++)
        {
            characters[j] = i;
            j++;
        }
        try {
            this.fontRenderer = new TTFFontRenderer(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/Arial.ttf")).deriveFont(Font.TRUETYPE_FONT, 40f));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private void draw()
    {
        background.draw();

        glPushMatrix();

        glScaled(0.002, 0.003, 0.002);
        glRotated(180, 1, 0, 0);

        int i = 0;

        for(String s1 : this.commands) {
            ++i;

            int color = this.defaultColor;

            if(s1.contains("--error"))
            {
                color = 0xfff00000;
            }
            else if(s1.contains(":"))
            {
                color = 0xf00ff000;
            }
            else if(s1.contains("/"))
            {
                color = 0xff523c07;
            }
            else
            {
                color = this.defaultColor;
            }

            this.fontRenderer.drawString(" > " + s1, -500, -250 + ((i - 1) * 20) - offsetY, color);

        }
        this.fontRenderer.drawString(" > " + command, -500f, -250 + (this.commands.size() * 20 ) - offsetY , defaultColor);

        glPopMatrix();
    }

    private void process()
    {
        if(this.commands.size() > 0) {
            String command = this.commands.get(this.commands.size() - 1);
            if(command.length() > 0) {
                boolean commandExists = false;



                switch (command) {
                    case "clear":
                        this.commands.clear();
                        commandExists = true;
                        break;
                    case "exit":
                        System.exit(-1);
                        commandExists = true;
                        break;
                    case "--vsync":
                        glfwSwapInterval(1);
                        commandExists = true;
                        break;
                    case "--help":
                        this.commands.add("Syntax error: missing argument (game/terminal)");
                        commandExists = true;
                        break;
                    case "icon":
                        this.commands.add(":####################");
                        this.commands.add(":####################");
                        this.commands.add(":####################");
                        this.commands.add("/####################");
                        this.commands.add("/####################");
                        this.commands.add("/####################");
                        this.commands.add("/####################");

                        commandExists = true;
                        break;

                }


                if (command.contains("backgroundcolor ")) {

                    String[] colors = command.split(" ");
                    int color[] = new int[]{Integer.parseInt(colors[1]), Integer.parseInt(colors[2]), Integer.parseInt(colors[3]), Integer.parseInt(colors[4])};

                    this.background.setColor(color);

                    commandExists = true;

                }

                if (command.contains("--fps")) {
                    glfwSwapInterval(Integer.parseInt(command.split(" ")[1]));

                    commandExists = true;

                }

                if (command.contains("--help")) {
                    if (command.contains(" ")) {


                        if (command.split(" ")[1].equalsIgnoreCase("game")) {
                            this.commands.add("Game Info: In this game you have to build shelters while avoiding zombies");
                            this.commands.add("Game Tip: Craft armor by killing one sheep and then press 'E' on it when selected");
                        } else if (command.split(" ")[1].equalsIgnoreCase("terminal")) {
                            this.commands.add("clear: clear the terminal");
                            this.commands.add("exit: quit the game");
                            this.commands.add("--vsync: cap the fps at your monitor's refresh rate");
                            this.commands.add("--help: why are you even asking?");
                            this.commands.add("--fps : change the fps cap. Args = fps");
                            this.commands.add("color: change the terminal's font color. Args = index(0, 1, 2, 3, 4, 5, f)");
                            this.commands.add("backgroundcolor : change the terminal's background color. Args = r, g, b, a");
                            this.commands.add("icon: draw the icon of this game");
                            this.commands.add("Btw separate arguments by putting ' ' between them");
                        }
                    }

                    commandExists = true;

                }

                if(command.contains("color"))
                {
                    if(command.contains(" ")) {
                        String arg = command.split(" ")[1];

                        switch(arg)
                        {
                            case "0":
                                defaultColor = Color.BLACK.getRGB();
                                break;
                            case "1":
                                defaultColor = Color.DARK_GRAY.getRGB();
                                break;
                            case "2":
                                defaultColor = Color.GREEN.getRGB();
                                break;
                            case "3":
                                defaultColor = Color.BLUE.getRGB();
                                break;
                            case "4":
                                defaultColor = Color.RED.getRGB();
                                break;
                            case "5":
                                defaultColor = Color.MAGENTA.getRGB();
                                break;
                            case "f":
                                defaultColor = Color.WHITE.getRGB();
                                break;
                            default:
                                defaultColor = Color.white.getRGB();


                        }


                    }

                }

                if (!commandExists) {
                    commands.add("Error occured: Command \"" + command + "\"doesn't exist --error");
                }
            }
        }
        else
        {

        }

    }


    private String getKeyboardInput(int keyPressed)
    {
        String text = "";

        if(((keyPressed < 96 && keyPressed > 64) || (keyPressed > 47 && keyPressed < 58)) && delay + delayTimeMs <= System.currentTimeMillis())
        {
            if((keyPressed < 96 && keyPressed > 64)) {
                delay = System.currentTimeMillis();
                text += characters[keyPressed - 55];
            }
            if((keyPressed > 47 && keyPressed < 58))
            {
                delay = System.currentTimeMillis();
                text += characters[keyPressed - 48];
            }
        }

        if(delay + delayTimeMs <= System.currentTimeMillis()) {
            if (keyPressed == 32) {
                delay = System.currentTimeMillis();
                return " ";
            }
            if (keyPressed == 257) {
                delay = System.currentTimeMillis();
                return "/enter";

            }
            if (keyPressed == 47)
            {
                delay = System.currentTimeMillis();
                return "/";
            }
            if(keyPressed == 45)
            {
                delay = System.currentTimeMillis();
                return "-";
            }
        }

        return text;
    }

    public void update(int keyPressed)
    {
        String keyboardInput = getKeyboardInput(keyPressed);
        if(keyboardInput != "/enter" ) {
            command += keyboardInput;

        }
        else
        {
            this.commands.add(command);
            command = "";

            process();

            if(commands.size() > 13)
            {
                offsetY += 10;
            }
        }




        draw();

    }
}
