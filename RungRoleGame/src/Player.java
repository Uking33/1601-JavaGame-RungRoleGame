
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
public class Player extends Sprite {

    private final static int GRAVITY = 1;
    private final static int INISPEED = 20;
    private Animation animRight, animLeft;
    private int numImages;
    public boolean runningRight, runningLeft, standRight, standLeft;
    private GameWorldManager bgManager;
    private int offsetX, offsetY;
    private int jumpSpeed;
    public boolean isJumping = false;
    public boolean canJump = false;
    public boolean onGround = false;

    public Player(GameWorldManager bg) {
        super(200, 0, "Images/player_right.gif", 6);

        numImages = 9;

        animRight = new Animation("Images/player_right.gif", 6, 50, 3, true);
        animLeft = new Animation("Images/player_left.gif", 6, 50, 3, true);
        runningRight = false;
        runningLeft = false;
        standRight = true;
        standLeft = false;

        bgManager = bg;
        offsetX = bgManager.getOffsetX();
        offsetY = bgManager.getOffsetY();
        setDX(0);
        setX(GamePanel.WIDTH / 2);
        setDY(0);
        jumpSpeed=-INISPEED;
    }

    public void update() {
        if (!isJumping) {
            dy += GRAVITY;
        } else {
            jumpSpeed += GRAVITY;
            setDY(jumpSpeed);
            onGround = false;
            if (jumpSpeed >= 0) {
                isJumping = false;
                jumpSpeed=-INISPEED;
            }
        }
        
        locY += dy;

        if (runningRight) {
            animRight.updateImage();
        } else if (runningLeft) {
            animLeft.updateImage();
        }

        if (bgManager.worldCollisionVertical(this, thisPosition(), nextPosition(), offsetY)) {
            if (dy > 0) {
                onGround = true;
                setDY(0);
            }
        } else if (dy < 0) {
            isJumping = false;
            jumpSpeed=-INISPEED;
        }

        if (bgManager.worldCollisionHorizontal(this, thisPosition(), nextPosition(), offsetX)) {
            bgManager.canScroll = false;
        } else {
            bgManager.canScroll = true;
        }
    }

    public Point thisPosition() {
        int offsetX = bgManager.getOffsetX();
        int thisX = locX + offsetX;
        int thisY = locY + offsetY;
        return new Point(thisX, thisY);
    }

    public Point nextPosition() {

        int offsetX = bgManager.getOffsetX();
        int nextX = locX + offsetX;
        int nextY = locY + offsetY;
        nextY += dy;
        if (runningRight) {
            nextX += 5;
        } else if (runningLeft) {
            nextX -= 10;
        }
        return new Point(nextX, nextY);
    }

    public void draw(Graphics g) {

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
}
