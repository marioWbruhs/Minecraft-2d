package events;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.nio.DoubleBuffer;
import java.util.LinkedList;
import java.util.Queue;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private static int dwheel;

    private static int dx, dy;
    private static float x, y;
    private static boolean wasSetup;

    // Event Stuff
    private static int eventX, eventY;
    private static int eventDWheel;

    private static int eventButton;
    private static boolean eventButtonState;
    private static String[] result;

    private static Queue<String> buttonQueue = new LinkedList<>();
    public static long window;
    public static int[] size;

    public boolean isButtonDown(int buttonId) {
        return GLFW.glfwGetMouseButton(this.window, buttonId) == GLFW.GLFW_TRUE;
    }


    public void setupMouse(long window, int[] size)
    {
        this.window = window;
        this.size = size;

        GLFW.glfwSetMouseButtonCallback(this.window, new GLFWMouseButtonCallback() {
            // The action is one of GLFW_PRESS or GLFW_RELEASE
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    buttonQueue.add(button + ":GLFW_PRESS:" + dwheel + ":" + getX() + ":" + getY());
                } else {
                    buttonQueue.add(button + ":GLFW_RELEASE:" + dwheel + ":" + getX() + ":" + getY());
                }

            }
        });

        glfwSetScrollCallback(this.window, new GLFWScrollCallback() {
            // on nearly every mouse it is the yoffset.
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                dwheel += (int) yoffset;
            }
        });
        wasSetup = true;
    }

    /**
     * @return Movement of the wheel since last time getDWheel() was called
     */
    public static int getDWheel() {
        int result = dwheel;
        dwheel = 0;
        return result;
    }

    /**
     * @return Absolute x axis position of mouse
     */
    public float getX() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(this.window, posX, null);
        x = (int) posX.get(0);

        x = ((float)x  / size[0] - 0.5f) * 2;
        return x;
    }

    /**
     * @return Absolute y axis position of mouse
     */
    public float getY() {
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(this.window, null, posY);
        y = this.size[1] - (int) posY.get(0);

        y = ((float)y  / size[1] - 0.5f) * 2;

        return y;
    }


    /**
     * @return Movement on the x axis since last time getDX() was called.
     */
    public static int getDX() {
        int result = dx;
        dx = 0;
        return result;
    }

    /**
     * @return Movement on the y axis since last time getDY() was called.
     */
    public static int getDY() {
        int result = dy;
        dy = 0;
        return result;
    }


    /**
     * Will "get" the next int from the Queue.
     *
     * @return false, if there is nothing more in the Queue, true when there is more to check in the Queue
     * It will automatically apply the event parameters, such as event button, etc.
     */
    public static boolean next() {
        if (!wasSetup) {
            throw new IllegalStateException("Mouse must be initialized before calling functions in it. Do it with Mouse.setup()");
        } else if (buttonQueue.isEmpty()) {
            return false;
        }
        if (buttonQueue.isEmpty()) {
            return false;
        }
        result = buttonQueue.poll().split(":");
        eventButtonState = result[1].equals("GLFW_PRESS");
        eventButton = Integer.parseInt(result[0]);
        eventDWheel = Integer.parseInt(result[2]);
        eventX = Integer.parseInt(result[3]);
        eventY = Integer.parseInt(result[4]);
        return true;
    }

    /**
     * @return the current Event button.
     */
    public static int getEventButton() {
        return eventButton;
    }

    /**
     * @return the current event button state (false == released, true == pressed)
     */
    public static boolean getEventButtonState() {
        return eventButtonState;
    }


    public static int getEventDWheel() {
        return eventDWheel;
    }

    public static int getEventX() {
        return eventX;
    }

    public static int getEventY() {
        return eventY;
    }
}
