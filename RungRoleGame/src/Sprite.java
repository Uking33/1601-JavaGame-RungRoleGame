
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.String;
import javax.imageio.ImageIO;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class Sprite {

    protected int locX;
    protected int locY;
    protected int dx;
    protected int dy;
    protected Animation animPlayer;
    private int numImages;
    private String imFileName;
    private BufferedImage image;
    private int width,  height;     // image dimensions
    private static final int SIZE = 12;
    protected boolean isLooping;
    private int pWidth,  pHeight;   // panel dimensions
    private boolean isActive = true;

    public Sprite(int x, int y, String imfile, int num) {

        locX = x;
        locY = y;
        dx = 5;
        dy = 5;
        imFileName = imfile;
        numImages = num;
        if(numImages==1){
            image=setImage(imFileName);
        }
        else if(numImages>1){
         animPlayer = new Animation(imFileName, numImages,0, 0,false);
         image=animPlayer.getCurrentImage();
        }
        width=image.getWidth();
        height=image.getHeight();
    }


    public BufferedImage setImage(String fnm){
        try {
            BufferedImage im = ImageIO.read(getClass().getResource(fnm));
            return im;
        } catch (IOException e) {
            System.out.println("Load Image error for ");
            return null;
        }
    }

    public void loopImage(int animPeriod, int seqDuration) /* Switch on loop playing. The total time for the loop is
    seqDuration secs. The update interval (from the enclosing
    panel) is animPeriod ms. */ {
        if (numImages > 1) {
            animPlayer = null;   // to encourage garbage collection of previous player
            animPlayer = new Animation(imFileName, numImages, animPeriod, seqDuration,
                    true);
            isLooping = true;
        } else {
            System.out.println(" is not a sequence of images");
        }
    }  // end of loopImage()

    public void stopLooping() {
        if (isLooping) {
            animPlayer.stop();
            isLooping = false;
        }
    }  // end of stopLooping()

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean a) {
        isActive = a;
    }

    public void update() {
        if (isActive) {
            locX += dx;
            locY += dy;
            if (isLooping) {
                animPlayer.updateImage();
            }
        }
    }

    public void draw(Graphics g) {
        if (isActive) {
            if (image == null) {   // the sprite has no image
                g.setColor(Color.yellow);   // draw a yellow circle instead
                g.fillOval(locX, locY, SIZE, SIZE);
                g.setColor(Color.black);
            } else {
                if (isLooping) {
                    animPlayer.draw(g, locX, locY);
                }
                else g.drawImage(image, locX, locY, null);
            }
        }
    }

    public void setDX(int x) {
        dx = x;
    }

    public void setDY(int y) {
        dy = y;
    }

    public int getDX() {
        return dx;
    }

    public int getDY() {
        return dy;
    }

        public void setX(int x) {
        locX = x;
    }

    public void setY(int y) {
        locY = y;
    }

    public int getX() {
        return locX;
    }

    public int getY() {
        return locY;
    }

    public int getHeight() {
        return height;
    }

   public int getWidth() {
        return width;
    }


}
