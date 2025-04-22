import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Random;



public class CaveDragon extends JPanel implements ActionListener, KeyListener {
    int boardHeight = 480;
    int boardWidth = 854;

    // Images
    Image backgroundImg;
    Image dragonImg;
    Image toprockImg;
    Image bottomrockImg;

    // Dragon
    int dragonX = boardWidth / 8;
    int dragonY = boardHeight / 2;
    int dragonWidth = 45;
    int dragonHeight = 45;

    class Dragon{
        int x = dragonX;
        int y = dragonY;
        int width = dragonWidth;
        int height = dragonHeight;
        Image img;

        Dragon(Image img){
            this.img = img;
        }
    }

    // rocks
    int rockX = boardWidth;
    int rockY = 0;
    int rockWidth = 60;
    int rockHeight = 300;

    class Rock{
        int x = rockX;
        int y = rockY;
        int width = rockWidth;
        int height = rockHeight;
        Image img;
        boolean passed = false;

        Rock(Image img){
            this.img = img;
        }
    }

    // game logic

    Dragon dragon;
    int velocityX = -6;
    int velocityY = -12;
    int gravity = 1;

    ArrayList<Rock> rocks;
    Random random = new Random();


    Timer gameLoop;
    Timer placeRocksTimer;
    boolean gameOver = false;
    double score = 0;

    CaveDragon(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);

        // load Images
        backgroundImg = new ImageIcon(getClass().getResource("./background.gif")).getImage();
        dragonImg = new ImageIcon(getClass().getResource("./dragon.png")).getImage();
        toprockImg = new ImageIcon(getClass().getResource("./Upcrystal.png")).getImage();
        bottomrockImg = new ImageIcon(getClass().getResource("./Downcrystal.png")).getImage();

        // dragon
        dragon = new Dragon(dragonImg);
        rocks = new ArrayList<Rock>();

        // place rocks timer
        placeRocksTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        placeRocksTimer.start();

        // game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void placePipes(){
        int randomRockY = (int) (rockY - rockHeight/4 - Math.random()*(rockHeight/2)); 
        int openingSpace = boardHeight/3;


        Rock topRock = new Rock(toprockImg);
        topRock.y = randomRockY;
        rocks.add(topRock);
        Rock bottomRock = new Rock(bottomrockImg);
        bottomRock.y = topRock.y + openingSpace + rockHeight;
        rocks.add(bottomRock);
    }

    public void draw(Graphics g){
        
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        g.drawImage(dragon.img, dragon.x, dragon.y, dragon.width, dragon.height, null);

        // rocks

        for(int i = 0; i < rocks.size(); i++){
            Rock rock = rocks.get(i);
            g.drawImage(rock.img, rock.x, rock.y, rock.width, rock.height, null);
        }

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));

        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int) score), 350, 250);
        }else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move(){
        velocityY += gravity;
        dragon.y += velocityY;
        dragon.y = Math.max(dragon.y, 0);

        // rocks
        for(int i = 0; i < rocks.size(); i++){
            Rock rock = rocks.get(i);
            rock.x += velocityX;

            if (!rock.passed && dragon.x > rock.x + rock.width){
                rock.passed = true;
                score +=0.5;
            }

            if (collision(dragon, rock)){
                gameOver = true;
            }
        }

        if(dragon.y >boardHeight){
            gameOver = true;
        }
    }

    public boolean collision(Dragon a, Rock b){
        return a.x < b.x + b.width &&   // a's top left corner doesnot reach the b's top right corner
               a.x + a.width > b.x &&   // a's top right corner doesnot reach the b's top left corner
               a.y < b.y + b.height &&  // a's top left corner doesnot reach the b's bottom left corner
               a.y + a.height >b.y;     // a's bottom left corner doesnot reach the b's top left corner

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            placeRocksTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -12;
            if (gameOver){
                dragon.y = dragonY;
                velocityY = 0;
                rocks.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeRocksTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }
    
}
