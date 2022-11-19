import java.awt.*;

public class Score extends Rectangle{
    static int gameWidth;
    static int gameHeight;
    int player1;
    int player2;

    Score(int gameWidth,int gameHeight){
        Score.gameWidth = gameWidth;
        Score.gameHeight = gameHeight;

    }
    public void draw(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,60));
        g.drawLine(gameWidth/2,0,gameWidth/2,gameHeight);
        g.drawString(String.valueOf(player1),gameWidth/2-85,50);
        g.drawString(String.valueOf(player2),gameWidth/2+20,50);
    }
}
