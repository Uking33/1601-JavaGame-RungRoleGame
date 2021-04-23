
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class Animation {
    private BufferedImage[] Images;
    private int numImages;
    private boolean ticksStopped;
    private boolean isRepeating;
    private int animTime;
    private int animSpeed;
    private int animTotalTime;
    private int showTime;
    private int imPosition;


    public Animation(String fileName,int num,int speed,int totalTime,boolean repeat) {
        numImages=num;
        animSpeed=speed;
        animTotalTime=totalTime;
        isRepeating=repeat;
        
        animTime=0;
        Images=setAnimImagesFromOneFile(fileName,numImages);

        imPosition = 0;
        ticksStopped = false;
    }

        public Animation(String prefixfileName,String postfixFileName ,int num,int speed,int totalTime,boolean repeat) {
        numImages=num;
        animSpeed=speed;
        animTotalTime=totalTime;
        isRepeating=repeat;

        animTime=0;
        Images=this.setAnimImagesFromFiles(prefixfileName, postfixFileName, num);

        imPosition = 0;
        ticksStopped = false;
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

    public BufferedImage[] setAnimImagesFromOneFile(String fnm, int num) {
        BufferedImage bim = loadImage(fnm);
        BufferedImage[] ims = new BufferedImage[num];
        int imWidth = (int) bim.getWidth() / num;
        int imHeight = bim.getHeight();
        Graphics g;
        for (int i = 0; i < num; i++) {
            ims[i]= new BufferedImage(imWidth,imHeight,BufferedImage.TYPE_INT_ARGB);
            g=ims[i].getGraphics();
            g.drawImage(bim, 0, 0, imWidth, imHeight, i*imWidth,0,(i*imWidth)+imWidth,imHeight, null);
            g.dispose();
        }
        return ims;
    }

   public BufferedImage[] setAnimImagesFromFiles(String prefixFileName,String postFileName, int num){
               BufferedImage[] ims = new BufferedImage[num];
       for(int i=0;i<num;i++){
           ims[i]=loadImage(prefixFileName+String.valueOf(i)+postFileName);
       }
               return ims;
   }


      public void updateImage()
  /* We assume that this method is called every animPeriod ms */
  {
    if (!ticksStopped) {
      // update total animation time, modulo the animation sequence duration
      animTime = (animTime + animSpeed) % (1000 * animTotalTime);
      showTime = (int) (1000 * animTotalTime / numImages);
      // calculate current displayable image position
      imPosition = (int) (animTime / showTime);   // in range 0 to num-1

      if ((imPosition == numImages-1) && (!isRepeating)) {  // at end of sequence
        ticksStopped = true;   // stop at this image


      }
    }
  }
      public BufferedImage getCurrentImage(){
          return Images[imPosition];
      }
        public void stop()
  /* updateTick() calls will no longer update the
     total animation time or imPosition. */
  {  ticksStopped = true;  }

  public boolean isStopped()
  {  return ticksStopped;  }

    public void resume()
  // start at previous image position
  {
    if (numImages != 0)
      ticksStopped = false;
  }

      public void draw(Graphics g,int x ,int y){
          BufferedImage im=Images[imPosition];
          g.drawImage(im, x, y, null);
      }
}
