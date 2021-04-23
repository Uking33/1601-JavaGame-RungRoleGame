
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class GameWorldManager {

    public int imMoveSize;
    private int TILE_SIZE = 64;

    private BufferedImage im1,  im2,  im3;
    private Ribbon bg1,  bg2,  bg3;
    private ArrayList tiles;
    private TileMap tMap;
    private BufferedImage[] tileImage;
    public boolean canMoveRight;
    public boolean canMoveLeft;
    public boolean canScroll = true;
    private ArrayList sprites;

    public GameWorldManager() {
        imMoveSize = 5;
        canMoveRight = false;
        canMoveLeft = false;

        im1 = loadImage("Images/mountains.gif");
        im2 = loadImage("Images/houses.gif");
        im3 = loadImage("Images/trees.gif");

        bg1 = new Ribbon((int) (imMoveSize * 0.2), im1);
        bg2 = new Ribbon((int) (imMoveSize * 0.5), im2);
        bg3 = new Ribbon(imMoveSize, im3);

        tileImage = new BufferedImage[10];
        tiles = new ArrayList();
        for (int i = 0; i < 9; i++) {
            tileImage[i] = loadImage("Images/tile_" + (char) (i + 'A') + ".png");
            tiles.add(tileImage[i]);
        }

        sprites = new ArrayList();
        tMap = loadTileMap("Images/map.txt");

    }

    public void setMoveFlag() {
        bg1.canMoveRight = canMoveRight;
        bg2.canMoveRight = canMoveRight;
        bg3.canMoveRight = canMoveRight;
        tMap.tMapMoveRight = canMoveRight;

        bg1.canMoveLeft = canMoveLeft;
        bg2.canMoveLeft = canMoveLeft;
        bg3.canMoveLeft = canMoveLeft;
        tMap.tMapMoveLeft = canMoveLeft;
    }

    public void draw(Graphics g) {
        bg1.draw(g);
        bg2.draw(g);
        bg3.draw(g);
        tMap.draw(g);

        for (int i = 0; i < sprites.size(); i++) {
            Enemy eny = (Enemy) sprites.get(i);
            eny.draw(g);
        }
    }

    public void update() {

        for (int i = 0; i < sprites.size(); i++) {
            Enemy eny = (Enemy) sprites.get(i);
            eny.update();
        }

        if (canScroll) {
            setMoveFlag();
            bg1.update();
            bg2.update();
            bg3.update();
            tMap.update();
        }
    }

    public TileMap loadTileMap(String filename) {

        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
            while (true) {
                String line = reader.readLine();
                // no more lines to read
                if (line == null) {
                    reader.close();
                    break;
                }


                // add every line except for comments
                if (!line.startsWith("#")) {
                    lines.add(line);
                    width = Math.max(width, line.length());
                }
            }
        } catch (IOException e) {
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y = 0; y < height; y++) {
            String line = (String) lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C, etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (BufferedImage) tiles.get(tile));
                } else if ((ch - '*') == 0) {
                    Enemy eny = new Enemy(x * TILE_SIZE, y * TILE_SIZE, this);
                    sprites.add(eny);
                }
            }
        }
        return newMap;
    }

    public BufferedImage loadImage(String fnm) {
        try {
            BufferedImage im = ImageIO.read(getClass().getResource(fnm));
            return im;
        } catch (IOException e) {
            System.out.println("Load Image error for ");
            return null;
        }
    }

    public Point checkTileCollision(Sprite sprite,
            int oldX, int oldY, int newX, int newY) {
        Point pointCache = new Point(0, 0);
        int fromX = Math.min(oldX, newX);
        int fromY = Math.min(oldY, newY);
        int toX = Math.max(oldX, newX);
        int toY = Math.max(oldY, newY);

        // get the tile locations
        int fromTileX = tMap.pixelsToTiles(fromX);
        int fromTileY = tMap.pixelsToTiles(fromY);
        int toTileX = tMap.pixelsToTiles(
                toX + sprite.getWidth() - 1);
        int toTileY = tMap.pixelsToTiles(
                toY + sprite.getHeight() - 1);

        // check each tile for a collision
        for (int x = fromTileX; x <= toTileX; x++) {
            for (int y = fromTileY; y <= toTileY; y++) {
                if (x < 0 || x >= tMap.getWidth() ||
                        tMap.getTile(x, y) != null) {
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }

        // no collision found
        return null;
    }

    public boolean worldCollisionHorizontal(Sprite sprite, Point thisPos, Point nextPos, int offSetX) {

        int dx = sprite.getDX();
        int oldX = thisPos.x;
        int newX = nextPos.x;
        int oldY = thisPos.y;
        int newY = oldY;

        Point tile =
                checkTileCollision(sprite, oldX, oldY, newX, newY);
        if (tile == null) {
            return false;
        } else {
            // line up with the tile boundary
            if (dx > 0) {
                sprite.setX(
                        tMap.tilesToPixels(tile.x) - offSetX - sprite.getWidth());
            } else if (dx < 0) {
                sprite.setX(
                        tMap.tilesToPixels(tile.x + 1) - offSetX);
            }
            return true;
        }
    }


    public boolean worldCollisionVertical(Sprite sprite, Point thisPos, Point nextPos, int offSetY) {
        // change y
        int dy = sprite.getDY();
        int oldY = thisPos.y;
        int newY = nextPos.y;
        int oldX = thisPos.x;
        int newX = oldX;
        Point tile = checkTileCollision(sprite, oldX, oldY, newX, newY);
        if (tile == null) {
            return false;
        } else {
            // line up with the tile boundary
            if (dy > 0) {
                sprite.setY(
                        tMap.tilesToPixels(tile.y) - offSetY -
                        sprite.getHeight());
            } else if (dy < 0) {
                sprite.setY(
                        tMap.tilesToPixels(tile.y + 1) - offSetY);
                sprite.setDY(0);
            }
            return true;
        }


    }

    public boolean spriteCollision(Sprite s1, Sprite s2, boolean isPlayer) {

        // get the pixel location of the Sprites
        int s1x, s1y, s2x, s2y;
        s1x = s1.getX();
        s2x = s2.getX();
        s1y = s1.getY();
        s2y = s2.getY();
        if (isPlayer) {
            s1x += getOffsetX();
            s1y += getOffsetY();
        }

        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
                s2x < s1x + s1.getWidth() &&
                s1y < s2y + s2.getHeight() &&
                s2y < s1y + s1.getHeight());
    }

    public int getOffsetY() {
        return tMap.tilesToPixels(tMap.getHeight()) - GamePanel.HEIGHT;
    }

    public int getOffsetX() {
        return -tMap.getMapHead();
    }

    public ArrayList getSprites(){
        return sprites;
    }
}
