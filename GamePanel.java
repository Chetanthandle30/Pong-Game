import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

    static final int gameWidth= 1000;
    static final int gameHeight=(int)(gameWidth*0.5555);
    static final Dimension Screen_Size = new Dimension(gameWidth,gameHeight);
    static final int ballDiameter= 20;
    static final int paddleWidth = 25;
    static final int paddleHeight =100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    GamePanel(){
        newPaddle();
        newBall();
        score = new Score(gameWidth,gameHeight);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(Screen_Size);

        gameThread= new Thread(this);
        gameThread.start();
    }

        public void newBall(){
            random = new Random();
            ball = new Ball((gameWidth/2)-(ballDiameter/2),random.nextInt(gameHeight-ballDiameter),ballDiameter,ballDiameter);
        }
    public void newPaddle(){
        paddle1=new Paddle(0,(gameHeight/2)-(paddleHeight/2),paddleWidth, paddleHeight,1);
        paddle2=new Paddle(gameWidth-paddleWidth,(gameHeight/2)-(paddleHeight/2),paddleWidth, paddleHeight,2);
    }
    public void paint(Graphics g){
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }
    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
        Toolkit.getDefaultToolkit().sync();
    }
    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();
    }
    public void checkCollision() {
        //bounce ball on top of the window
        if (ball.y <= 0){
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= gameHeight - ballDiameter)
            ball.setYDirection(-ball.yVelocity);

        //bounces of paddles
        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;//increase in difficulty
            if (ball.yVelocity > 0) {
                ball.yVelocity++;
            }
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if (ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if (ball.yVelocity > 0) {
                ball.yVelocity++;
            }
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        //check to stop the paddle on screen.
        if (paddle1.y <= 0)
            paddle1.y = 0;
        if (paddle1.y >= (gameHeight - paddleHeight))
            paddle1.y = gameHeight - paddleHeight;
        if (paddle2.y <= 0)
            paddle2.y = 0;
        if (paddle2.y >= (gameHeight - paddleHeight))
            paddle2.y = gameHeight - paddleHeight;
        //Track the player 1 score and creates a new game
        if (ball.x <= 0){
            score.player2++;
            newPaddle();
            newBall();
            System.out.println("Player 2 - " + score.player2);
        }
        //Track the player 2  score and creates a new game
        if(ball.x >= gameWidth-ballDiameter) {
            score.player1++;
            newPaddle();
            newBall();
            System.out.println("Player 1 - " + score.player1);
        }

    }
    public void run(){
        //game loop
        long lasttime =System.nanoTime();
        double amountOfTicks = 60.0;
        double ns =1000000000/amountOfTicks;
        double delta = 0;
        while (true){
            long now =System.nanoTime();
            delta+=(now-lasttime)/ns;
            lasttime=now;
            if (delta>=1) {
                move();
                checkCollision();
                repaint();
                delta--;

            }
        }
    }
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
