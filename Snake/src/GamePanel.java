import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener
{
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    // how big do we want the items
    static final int UNIT_SIZE = 25;
    static final int TOTAL_Y_SQUARES = SCREEN_WIDTH / UNIT_SIZE;
    static final int TOTAL_X_SQUARES = SCREEN_HEIGHT / UNIT_SIZE;

    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    // higher number = slower game
    static final int DELAY = 75;
    // x coordinates of snake body parts (head, body, tail)
    final int[] x = new int[GAME_UNITS];
    // y coordinates of snake body parts
    final int[] y = new int[GAME_UNITS];
    // number of body parts
    int bodyParts = 6;

    // number of apples eaten
    int applesEaten;
    // x coordinate of apple
    int appleX;
    // y coordinate of apple
    int appleY;
    // direction of snake R, L, U, D
    char direction = 'R';
    // game running?
    boolean running = false;
    // timer
    Timer timer;
    Random random;

    public GamePanel()
    {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        // once finished initializing, start the game
        startGame();
    }

    public void startGame()
    {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics)
    {
        if(running)
        {
            //drawGrid(graphics);

            // draw the apple
            graphics.setColor(Color.red);
            graphics.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // draw the snake
            for(int i = 0; i < bodyParts; i++)
            {
                // draw the head
                if(i == 0)
                {
                    graphics.setColor(Color.green);
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    changeEyeDirection(graphics);
                }
                else
                {
                    // draw the body with random color
                    graphics.setColor(new Color(45, 180, 0));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

        }
        else
        {
            gameOver(graphics);
        }

    }

    public void changeEyeDirection(Graphics graphics)
    {
        if(direction == 'R')
        {
            // draw eyes
            graphics.setColor(Color.black);
            graphics.fillRect(x[0] + UNIT_SIZE - 5, y[0] + 5, 5, 5);
            graphics.fillRect(x[0] + UNIT_SIZE - 5, y[0] + 15, 5, 5);
        }
        else if(direction == 'L')
        {
            // draw eyes
            graphics.setColor(Color.black);
            graphics.fillRect(x[0], y[0] + 5, 5, 5);
            graphics.fillRect(x[0], y[0] + 15, 5, 5);
        }
        else if(direction == 'U')
        {
            // draw eyes
            graphics.setColor(Color.black);
            graphics.fillRect(x[0] + 5, y[0], 5, 5);
            graphics.fillRect(x[0] + 15, y[0], 5, 5);
        }
        else if(direction == 'D')
        {
            // draw eyes
            graphics.setColor(Color.black);
            graphics.fillRect(x[0] + 5, y[0] + UNIT_SIZE - 5, 5, 5);
            graphics.fillRect(x[0] + 15, y[0] + UNIT_SIZE - 5, 5, 5);
        }
    }

    // if you want to see the unit sizes
    public void drawGrid(Graphics graphics)
    {
        // change drawing color to red
        graphics.setColor(Color.gray);
        for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++)
        {
            // draw horizontal lines
            graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            // draw vertical lines
            graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }
    }

    public void newApple()
    {

        appleX = random.nextInt((int)(TOTAL_Y_SQUARES)) * UNIT_SIZE;
        appleY = random.nextInt((int)(TOTAL_X_SQUARES)) * UNIT_SIZE;

    }

    public void move()
    {

        for(int i = bodyParts; i > 0; i--)
        {
            // move the body parts
            // shift the coordinates of the body parts on x coordinates
            x[i] = x[i - 1];
            // shift the coordinates of the body parts on y coordinates
            y[i] = y[i - 1];
        }

        switch (direction)
        {
            case 'U':
                // move the head up
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                // move the head down
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                // move the head left
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                // move the head right
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple()
    {
        if((x[0] == appleX) && (y[0] == appleY))
        {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions()
    {
        // check if head collides with body
        for (int i = bodyParts; i > 0; i--)
        {
            // check if head collides with body
            if((x[0] == x[i]) && (y[0] == y[i]))
            {
                running = false;
            }
        }

        // check if head touches left border
        if(x[0] < 0)
        {
            running = false;
        }

        // check if head touches right border
        if(x[0] > SCREEN_WIDTH)
        {
            running = false;
        }

        // check if head touches top border
        if(y[0] < 0)
        {
            running = false;
        }

        // check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT)
        {
            running = false;
        }

        if(!running)
        {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics)
    {
        // display score
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, graphics.getFont().getSize());

        // display game over text
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(running)
        {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }


    public class MyKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent keyEvent)
        {
            switch (keyEvent.getKeyCode())
            {
                // limit 180 degree turns
                case KeyEvent.VK_LEFT:
                    if(direction != 'R')
                    {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L')
                    {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D')
                    {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U')
                    {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
