
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class TileMap {

    private BufferedImage[][] tiles;
    private int xMapHead;
    private int TILE_SIZE = 64;
    private int offsetX;
    private int offsetY;
    private int pWidth;             			
    private int pHeight;            		
    private int mapWidth;    
    private int mapHeight;         		
    private int mapMoveSize;
    public boolean tMapMoveRight = false;
    public boolean tMapMoveLeft = false;

    /**
    Creates a new TileMap with the specified width and
    height (in number of tiles) of the map.
     * @param width
     * @param height
     */
    public TileMap(int width, int height) {
        tiles = new BufferedImage[width][height];
        xMapHead = 0;
        pWidth = GamePanel.WIDTH;
        pHeight = GamePanel.HEIGHT;
        mapMoveSize = 5;
        mapWidth=tilesToPixels(width);
        mapHeight=tilesToPixels(height);
        offsetX = 0;
        offsetY = pHeight - mapHeight;
    }

    public int getMapHead() {
        return xMapHead;
    }

    /**
    Gets the width of this TileMap (number of tiles across).
     * @return
     */
    public int getWidth() {
        return tiles.length;
    }

    /**
    Gets the height of this TileMap (number of tiles down).
     */
    public int getHeight() {
        return tiles[0].length;
    }

    /**
    Gets the tile at the specified location. Returns null if
    no tile is at the location or if the location is out of
    bounds.
     */
    public BufferedImage getTile(int x, int y) {
        if (x < 0 || x >= getWidth() ||
                y < 0 || y >= getHeight()) {
            return null;
        } else {
            return tiles[x][y];
        }
    }

    /**
    Sets the tile at the specified location.
     */
    public void setTile(int x, int y, BufferedImage tile) {
        tiles[x][y] = tile;
    }

    public int tilesToPixels(int numTiles) {
        int pixelSize = numTiles * TILE_SIZE;
        return pixelSize;
    }

    public int pixelsToTiles(int pixelCoord) {
        int numTiles = pixelCoord / TILE_SIZE;
        return numTiles;
    }

    public void update() {

        if (tMapMoveLeft) {
            xMapHead = (xMapHead - mapMoveSize);
        } else if (tMapMoveRight) {
            xMapHead = (xMapHead + mapMoveSize);
        }
        xMapHead = Math.min(xMapHead, 0);
        xMapHead = Math.max(xMapHead, pWidth - mapWidth);
    }

    public void draw(Graphics g) {

        offsetX = xMapHead;
        int firstTileX = pixelsToTiles(-xMapHead);
        int lastTileX = pixelsToTiles(-xMapHead + pWidth);
        for (int y = 0; y < getHeight(); y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {
                BufferedImage image = getTile(x, y);
                if (image != null) {
                    g.drawImage(image,
                            tilesToPixels(x) + offsetX,
                            tilesToPixels(y) + offsetY,
                            null);
                }
            }
        }
    }

 

}

