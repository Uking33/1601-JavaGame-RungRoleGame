import java.awt.*;
import java.awt.image.BufferedImage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 
 * @author Administrator
 */
public class Ribbon {

	public BufferedImage bim;

	private int xImHead;

	private int pWidth;

	private int pHeight;

	private int width;

	private int moveSize;

	public boolean canMoveRight;

	public boolean canMoveLeft;

	public Ribbon(int movS, BufferedImage im) {

		bim = im;
		xImHead = 0;
		pWidth = GamePanel.WIDTH;
		pHeight = GamePanel.HEIGHT;
		width = bim.getWidth();
		moveSize = movS;
		canMoveRight = false;
		canMoveLeft = false;
	}

	public void draw(Graphics g) {
		if (xImHead == 0) {
			drawRibbon(g, bim, 0, pWidth, 0, pWidth);
		} 
		else if ((xImHead > 0) && (xImHead < pWidth)) {
			drawRibbon(g, bim, 0, xImHead, width - xImHead, width);
			drawRibbon(g, bim, xImHead, pWidth, 0, pWidth - xImHead);
		} 
		else if (xImHead >= pWidth) {
			drawRibbon(g, bim, 0, pWidth, width - xImHead, width - xImHead + pWidth);
		} 
		else if ((xImHead < 0) && (xImHead >= pWidth - width)) {
			drawRibbon(g, bim, 0, pWidth, -xImHead, pWidth - xImHead);
		} 
		else if (xImHead < pWidth - width) {
			drawRibbon(g, bim, 0, width + xImHead, -xImHead, width);
			drawRibbon(g, bim, width + xImHead, pWidth, 0, pWidth - width - xImHead);
		}
	}

	private void drawRibbon(Graphics g, BufferedImage im, int dx1, int dx2,
			int sx1, int sx2) {
		g.drawImage(im, dx1, 0, dx2, pHeight, sx1, 0, sx2, pHeight, null);
	}

	public void update() {
		if (canMoveRight) {
			xImHead = (xImHead + moveSize) % width;
		} else if (canMoveLeft) {
			xImHead = (xImHead - moveSize) % width;
		}
	}
}
