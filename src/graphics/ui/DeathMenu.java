package graphics.ui;

import events.Mouse;
import events.StateManager;
import graphics.Button;
import graphics.TTFFontRenderer;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class DeathMenu {
    private ArrayList<Button> buttons;
    private int[] screenSize;
    private TTFFontRenderer fontRenderer;
    public StateManager states;
    private Mouse mouse;

    public DeathMenu(int[] screenSize)
    {
        this.screenSize = screenSize;

        this.buttons = new ArrayList<>();

        addUI();

        this.mouse = new Mouse();


    }

    public void addUI()
    {
        this.buttons.add(new Button(-0.3f, -0.085f, 0.6f, .170f, new int[] {70, 160, 70, 255}, "PLAY AGAIN"));
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
                if(button.getId().equalsIgnoreCase("PLAY AGAIN"))
                {
                    this.states.reverseState(states.MAIN_MENU);
                    this.states.reverseState(states.IN_GAME);
                    this.states.reverseState(states.PLAYER_DEAD);
                }
                else if(button.getId().equalsIgnoreCase("QUIT GAME"))
                {
                    this.states.changeState(states.QUITTING, true);
                    this.states.quit();
                }
                else if(button.getId().equalsIgnoreCase("TERMINAL"))
                {
                    this.states.reverseState(states.MAIN_MENU);
                    this.states.reverseState(states.PLAYER_DEAD);
                    this.states.changeState(states.TERMINAL, true);
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
