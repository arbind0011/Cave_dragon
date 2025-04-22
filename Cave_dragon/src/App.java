import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 854;
        int boardHeight = 480;

        JFrame frame = new JFrame("CaveDragon");
        // frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWidth, boardHeight);
        CaveDragon caveDragon = new CaveDragon();
        frame.setTitle("Cave Dragon");
        frame.add(caveDragon);
        frame.pack();
        caveDragon.requestFocus();
        frame.setVisible(true);
        ImageIcon iconImage = new ImageIcon("dragon.png");
        frame.setIconImage(iconImage.getImage());
    }
}