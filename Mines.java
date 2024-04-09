import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.*;

//this is a mine
public class Mines extends DrawableObject
{
	private float c, t;
   private Random rand = new Random();
   private boolean BlownUp = false;
   
   //takes in its position
   public Mines(float x, float y)
   {
      super(x,y);
      t = rand.nextFloat(0,(float)Math.PI*2f);
      t += 0.02f;
      c = (float)Math.pow((float)Math.sin(t),2);
   }
   
   
   //draws itself at the passed in x and y.
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      
      int outline = 10/2+1;
      int size = 10/2;
      //creates outline of mine
      gc.setFill(Color.BLACK);
      gc.fillOval(x-outline,y-outline,12,12);
      //interpolates the colors of the mine from white and red
      t += 0.04f;
      c = (float)Math.pow((float)Math.sin(t),2);
      gc.setFill(new Color(1,c,c,1));
      gc.fillOval(x-size,y-size,10,10);
            
      
   }
   
   //makes it so the mine is blown up   
   public void setBlownUp(boolean blowup)
   {
      BlownUp = blowup;
   }
   //returns whether or not the mine is blown up
   public boolean getBlownUp()
   {
      return BlownUp;
   }
}