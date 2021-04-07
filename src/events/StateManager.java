package events;

import events.Keyboard;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class StateManager {
    public ArrayList<Boolean> states = new ArrayList<>();

    public static final int
            IN_GAME = 0,
            MAIN_MENU = 1,
            TERMINAL = 2,
            QUITTING = 3,
            PLAYER_DEAD = 4;

    public boolean canPressEscape;

    public StateManager()
    {
        states.add(false); //true if the user is in game
        states.add(true);  //true if the user is in main menu
        states.add(false); //true if the user is using the terminal
        states.add(false); //true if the player is leaving the game
        states.add(false); //true if the player is dead

    }

    public void changeState(int state, boolean val)
    {
        states.set(state, val);
    }

    public void reverseState(int state)
    {
        states.set(state, !states.get(state));
    }

    public boolean isState(int state)
    {
        return states.get(state);
    }

    public int getState()
    {
        int i = 0;

        for(Boolean bool : states)
        {
            if(bool)
            {
                return i;
            }
            i++;
        }

        return -1;
    }

    public void setStates(StateManager states)
    {
        int i = 0;
        for(boolean state : this.states)
        {
            this.states.set(i, state);

            i++;
        }
    }

    public void update()
    {

        if(Keyboard.isKeyDown(GLFW_KEY_ESCAPE) && canPressEscape)
        {
            canPressEscape = false;
            if(!this.isState(MAIN_MENU)) {
                this.changeState(IN_GAME, false);
                this.changeState(TERMINAL, false);
                this.changeState(MAIN_MENU, true);
            }
            else
            {
                this.changeState(IN_GAME, true);
                this.changeState(TERMINAL, false);
                this.changeState(MAIN_MENU, false);
            }
        }

        if(!Keyboard.isKeyDown(GLFW_KEY_ESCAPE))
        {
            canPressEscape = true;
        }
    }

    public void quit()
    {
        this.changeState(this.MAIN_MENU, false);
        this.changeState(this.IN_GAME, false);
        this.changeState(this.TERMINAL, false);

    }

    public void playerDied()
    {
        this.changeState(0, false);
        this.changeState(1, false);
        this.changeState(2, false);
        this.changeState(3, false);
        this.changeState(4, true);

    }


}
