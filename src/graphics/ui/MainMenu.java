package graphics.ui;

import events.Mouse;
import events.StateManager;
import graphics.Button;
import graphics.TTFFontRenderer;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class MainMenu {
    private ArrayList<Button> buttons;
    private int[] screenSize;
    private TTFFontRenderer fontRenderer;
    public StateManager states;
    private Mouse mouse;

    public MainMenu(int[] screenSize)
    {
        this.screenSize = screenSize;

        this.buttons = new ArrayList<>();

        addButtons();

        this.mouse = new Mouse();


    }

    public void addButtons()
    {
        this.buttons.add(new Button(-0.3f, -0.085f, 0.6f, .170f, new int[] {70, 160, 70, 255}, "PLAY GAME"));
        this.buttons.add(new Button(-0.3f, -0.7f, 0.6f, .170f, new int[] {156, 70, 70, 255}, "QUIT GAME"));
        this.buttons.add(new Button(-0.3f, -0.35f, 0.6f, .170f, new int[] {156, 161, 161, 255}, "TERMINAL"));
    }

    public void update(StateManager states)
    {
        glPushMatrix();

        this.states = states;
        for(Button button : this.buttons)
        {
            button.setCenteredX();

            if(button.isPressed(mouse.getX(), mouse.getY(), this.screenSize) && mouse.isButtonDown(0))
            {
                if(button.getId().equalsIgnoreCase("PLAY GAME"))
                {
                    this.states.reverseState(states.MAIN_MENU);
                    this.states.reverseState(states.IN_GAME);
                }
                else if(button.getId().equalsIgnoreCase("QUIT GAME"))
                {
                    this.states.changeState(states.QUITTING, true);
                    this.states.quit();
                }
                else if(button.getId().equalsIgnoreCase("TERMINAL"))
                {
                    this.states.reverseState(states.MAIN_MENU);
                    //this.states.reverseState(states.CRAFTING);
                    this.states.reverseState(states.TERMINAL);
                }
            }
            button.draw(this.screenSize);
        }

        glPopMatrix();
    }

    public StateManager getStates()
    {
        return this.states;
    }
}
