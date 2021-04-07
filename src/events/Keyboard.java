package events;
import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Keyboard extends GLFWKeyCallback{

    public static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;

    }

    public static boolean isKeyDown(int keycode)
    {
        return keys[keycode];
    }

    public static int keyDown()
    {
        int keyPressed = 0;

        for(int i = 0; i<keys.length; i++)
        {
            if(keys[i])
            {
                keyPressed = i;
            }
        }

        return keyPressed;
    }

}
