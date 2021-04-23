
import java.awt.Graphics;
import java.awt.Point;
import java.lang.String;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class Enemy extends Sprite {

    private final static int GRAVITY = 1;
    private final static int JUMPSPEED=20;
    private Animation animRight,  animLeft;
    private int numImages;
    public boolean runningRight,  runningLeft,  standRight,  standLeft;
    private GameWorldManager world;
    private int offsetX,  offsetY;
    private int jumpSpeed;
    public boolean isJumping = false;
    public boolean canJump = false;
    public boolean onGround=false;

    public Enemy(int x,int y,GameWorldManager gameWorld) {
        super(x, y, "Images/runningRight.gif", 4);

        numImages = 9;

        animRight = new Animation("Images/runningRight.gif", 4, 50, 3, true);
        animLeft = new Animation("Images/runningLeft.gif", 4, 50, 3, true);
        runningRight = false;
        runningLeft = false;
        standRight = true;
        standLeft = false;

        world = gameWorld;
        setDX(5);
        setDY(0);
        setJumpSpeed(JUMPSPEED);
    }

    public void jump() {

        jumpSpeed += GRAVITY;
        setDY(jumpSpeed);
    }

    public void jumpStopped() {

        isJumping = false;
        setJumpSpeed(JUMPSPEED);
    }

    public void update() {
        if(dx>0) {
            runningRight=true;
            runningLeft=false;
        }
        else if(dx<0){
            runningLeft=true;
            runningRight=false;
        }
            dy += GRAVITY;
             locY += dy;
             locX += dx;

        if (runningRight) {
            animRight.updateImage();

        } else if (runningLeft) {
            animLeft.updateImage();
        }

        if (world.worldCollisionVertical(this, thisPosition(), nextPosition(), 0)) {
              setDY(0);
        } else if(dy < 0) {
            jumpStopped();
        }

        if (world.worldCollisionHorizontal(this, thisPosition(), nextPosition(), 0)) {
            dx=-dx;
        }
    }

    public Point thisPosition() {
        int thisX = locX, thisY = locY + offsetY;
        return new Point(thisX, thisY);
    }

    public Point nextPosition() {

        int nextX = locX, nextY = locY + offsetY;
        nextY += dy;
        if (runningRight) {

            nextX += dx;
        } else if (runningLeft) {

            dx = -dx;
            nextX += dx;
        }

        return new Point(nextX, nextY);
    }

    public void draw(Graphics g) {
        int locX,locY;
        locX=getX()-world.getOffsetX();
        locY=getY()-world.getOffsetY();
        
        if (runningRight) {
            animRight.draw(g, locX, locY);

        } else if (runningLeft) {
            animLeft.draw(g, locX, locY);

        } else if (standRight) {
            animRight.draw(g, locX, locY);
        } else if (standLeft) {
            animLeft.draw(g, locX, locY);
        }

    }

    private void setJumpSpeed(int jumpS) {
        if (jumpS > 0) {
            jumpS = -jumpS;
        }
        jumpSpeed = jumpS;
    }
}
