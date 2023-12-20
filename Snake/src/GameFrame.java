import javax.swing.*;

public class GameFrame extends JFrame
{
    public GameFrame()
    {
        // add the game panel to the frame
       this.add(new GamePanel());
       // set the title of the window
       this.setTitle("Snake");
       // close the window when the user presses the close button
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // don't allow the window to be resized
       this.setResizable(false);
       // fit the window to the game
       this.pack();
       // make the window visible
       this.setVisible(true);
       // put in the middle of the screen
       this.setLocationRelativeTo(null);
    }
}
