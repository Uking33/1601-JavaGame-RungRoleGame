/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
//import java.awt.event.KeyAdapter;
import java.awt.event.*;
//import java.awt.event.*
//import java.awt.image.*;
import java.util.*;

/*
 *
 * @author Administrator
 */
public class GamePanel extends Panel implements Runnable, KeyListener {

    public final static int WIDTH = 500;
    public final static int HEIGHT = 400;
    private Image im;
    private Graphics dbg;
    private Thread gamethread;
    private static final int FPS = 50;
    private boolean running = false;
    private boolean isPaused = false;
    private Player player;
    private ArrayList enemy;
    private GameWorldManager gameWorld;
    public boolean isGameOver = false;

    public GamePanel() {

        setBackground(Color.pink);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        gameWorld = new GameWorldManager();
        player = new Player(gameWorld);

        setFocusable(true);
        requestFocus();
        addKeyListener(this);

    }

    public void gameStart() {
        if (!running) {
            gamethread = new Thread(this);
            gamethread.start();
        }
    }

    public void gameStop() {
        running = false;
    }

    public boolean checkCollision() {
        for (int i = 0; i < gameWorld.getSprites().size(); i++) {
            Enemy eny = (Enemy) gameWorld.getSprites().get(i);
            if (gameWorld.spriteCollision(player, eny, true)) {
                return true;
            }
        }
        return false;
    }

    public void gamePaint() {
        Graphics g;
        try {
            g = this.getGraphics();
            if (g != null && im != null) {
                g.drawImage(im, 0, 0, null);
            }
            g.dispose();
        } catch (Exception e) {
        }
    }

    public void gameRender() {
        if (im == null) {
            im = createImage(WIDTH, HEIGHT);
            if (im == null) {
                System.out.println("im is null");
            } else {
                dbg = im.getGraphics();
            }
        }

        gameWorld.draw(dbg);
        player.draw(dbg);


    }

    public void gameUpdate() {

        if (!isPaused && !checkCollision()) {

            player.update();           //man.move() must be first operated!
            gameWorld.update();


        }
    }

    public void run() {
        long t1,t2,dt,sleepTime;  
        long period=1000/FPS;  
        t1=System.nanoTime(); 
        
        running = true;
        while (running) {
            gameUpdate();
            gameRender();
            gamePaint();
            t2= System.nanoTime() ; 
            dt=(t2-t1)/1000000L; 
            sleepTime = period - dt;
            if(sleepTime<=0)        
                  sleepTime=2;
            try {     
            Thread.sleep(sleepTime); 
           } catch (InterruptedException ex) { }
              t1 = System.nanoTime();  
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_ESCAPE) {
            running = false;
            System.out.println("key is escape");
        }
        if (keycode == KeyEvent.VK_P) {
            isPaused = !isPaused;
            System.out.println("key is P");
        }
        if (!isPaused && running) {
            switch (keycode) {

                case KeyEvent.VK_DOWN:
                    break;
                case KeyEvent.VK_UP:
                    break;
                case KeyEvent.VK_RIGHT:
                    gameWorld.canMoveLeft = true;
                    player.runningRight = true;
                    player.standLeft = false;
                    break;
                case KeyEvent.VK_LEFT:
                    gameWorld.canMoveRight = true;
                    player.runningLeft = true;
                    player.standRight = false;
                    break;
                case KeyEvent.VK_SPACE:
                    if (player.onGround) {
                        player.isJumping = true;
                    }
            }
        }
        if (keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_UP || keycode == KeyEvent.VK_RIGHT || keycode == KeyEvent.VK_LEFT) {
        }
    }

    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_RIGHT) {
            gameWorld.canMoveLeft = false;
            player.runningRight = false;
            player.standRight = true;
        } else if (keycode == KeyEvent.VK_LEFT) {
            gameWorld.canMoveRight = false;
            player.runningLeft = false;
            player.standLeft = true;
        }
    }
}

