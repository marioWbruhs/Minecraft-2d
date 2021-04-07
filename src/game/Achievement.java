package game;

import graphics.Rectangle;

import java.util.ArrayList;

public class Achievement {
    private ArrayList<String> achievements = new ArrayList<>();
    private String newAchievement = "New Beggining";
    private Rectangle achievement = new Rectangle(-1f, -1f, 0.1f, 0.1f, new int[] {100, 150, 100, 255}, "New Beggining");

    public void update(ArrayList<Rectangle> items)
    {
        if(this.newAchievement != "New Beggining")
            this.newAchievement = "null";
        if(items.contains("flower"))
        {
            if(!this.achievements.contains("flower"))
            {
                this.achievements.add("average flower enjoyer");
                this.newAchievement = "average flower enjoyer";
            }
        }
        if(items.contains("timber"))
        {
            if(!this.achievements.contains("timber"))
            {
                this.achievements.add("timber man");
                this.newAchievement = "timber man";
            }
        }

        //if(this.newAchievement)

        this.achievement.setId(this.newAchievement);
    }

    public ArrayList<String> getAchievements()
    {
        return achievements;
    }

    public void draw()
    {
        this.achievement.draw();
        this.achievement.drawId();
    }
}
