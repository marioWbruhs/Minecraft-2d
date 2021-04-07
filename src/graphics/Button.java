package graphics;

import events.Mouse;
import graphics.Rectangle;

import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Button {
    public Texture texture;
    public Rectangle buttonController;
    public float x, y, w, h;
    public String id;
    public Mouse mouse;
    public boolean isMouse;
    public boolean isPressed;
    public float[] currentColor;
    public float[] color;
    public TTFFontRenderer fontRenderer;

    public Button(float x, float y, float w, float h, int[] color, String text)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.id = text;
        this.mouse = new Mouse();
        this.currentColor = new float[] {color[0] / 255f, color[1] / 255f, color[2] / 255f, color[3] / 255f};
        this.color = new float[] {color[0] / 255f, color[1] / 255f, color[2] / 255f, color[3] / 255f};

        this.buttonController = new Rectangle(x, y, w, h, color, text);

        try {
            this.fontRenderer = new TTFFontRenderer(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/Arial.ttf")).deriveFont(Font.TRUETYPE_FONT, 60f));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    public void setCenteredX()
    {
        this.x = 0 - this.w / 2;
    }
    public void setCenteredY()
    {
        this.y = 0 - this.h / 2;
    }

    public void setupMouse(long window, int[] screenSize)
    {
        mouse.setupMouse(window, screenSize);
        isMouse = true;
    }

    public void draw(int[] screenSize)
    {
        glPushMatrix();
        if(isPressed)
        {
            this.buttonController.setColor(new float[] {this.color[0] - 0.1f, this.color[0] - 0.1f, this.color[0] - 0.1f, this.color[3]});
        }
        else {
            this.buttonController.setColor(this.color);
        }
        this.buttonController.draw();


        glScaled(0.002, 0.003, 0.002);
        glRotated(180, 1, 0, 0);

        //glTranslated(this.x, this.y, 0);

        if(this.id == "PLAY GAME") {
            fontRenderer.drawString(this.getId(), (float) ((this.x * (screenSize[0] / 2) + this.w * (screenSize[0] / 2.1)) - (fontRenderer.getWidth(this.getId()) * 2)) + 115, -15, 0xff000000);
        }
        else if(this.id == "QUIT GAME")
        {
            fontRenderer.drawString(this.getId(), (float) ((this.x * (screenSize[0] / 2 ) + this.w * (screenSize[0] / 2.1)) - (fontRenderer.getWidth(this.getId()) * 2)) + 115, 190, 0xff000000);
        }
        else if(this.id == "TERMINAL")
        {
            fontRenderer.drawString(this.getId(), (float) ((this.x * (screenSize[0] / 2 ) + this.w * (screenSize[0] / 1.65)) - (fontRenderer.getWidth(this.getId()) * 2)), 75, 0xff000000);
        }
        else if(this.id == "PLAY AGAIN")
        {
            fontRenderer.drawString(this.getId(), (float) ((this.x * (screenSize[0] / 2) + this.w * (screenSize[0] / 2.0)) - (fontRenderer.getWidth(this.getId()) * 2)) + 115, -15, 0xff000000);
        }

        glPopMatrix();

        glEnable(GL_BLEND);
        //this.buttonController.drawId();
    }

    public float distanceTo(float[] point)
    {
        float x = point[0] - this.x;
        float y = point[1] - this.y;

        if(x < 0)
            x *= -1;
        if(y < 0)
            y *= -1;

        return (float)Math.sqrt(x * x + y * y);
    }

    public boolean isPressed(float x, float y, int[] screenSize)
    {

        boolean s1, s2, s3, s4;

        s1 = x < this.x + this.w;
        s2 = y < this.y + this.h;
        s3 = x > this.x;
        s4 = y > this.y;

        isPressed = s1 && s2 && s3 && s4;

        return s1 && s2 && s3 && s4;

    }

    public boolean isPressed()
    {
        float x, y;
        boolean s1, s2, s3, s4;

        x = this.mouse.getX();
        y = this.mouse.getY();

        s1 = x < this.x + this.w;
        s2 = y < this.y + this.h;
        s3 = x > this.x;
        s4 = y > this.y;

        isPressed = s1 && s2 && s3 && s4;

        return s1 && s2 && s3 && s4;
    }

    public String getId()
    {
        return this.id;
    }
}
