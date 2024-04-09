import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.*;
import java.io.*;


public class PlayerObject extends DrawableObject
{
   private float spedx, spedy;
   private int score;
   private int highScore;
   int gridX, gridY;
   int lastGridX;
   int lastGridY;
   Scanner fileScan = null;
   FileOutputStream outputFile = null;
   PrintWriter outputWrite = null;

   //creating the player---------------------------
   public PlayerObject(float x, float y)
   {
      super(x,y);
      
      //establishes the score
      score = 0;
      //establishes the position of the player on the grid
      gridX = (int)(x/100);
      gridY = (int)(y/100);
      lastGridX = gridX;
      lastGridY = gridY;
   }
   
   
   
   //moving the player---------------
   
   public void move()
   {
      //moves player at the speed
      setX(getX()+spedx);
      setY(getY()+spedy);
      
      //shows player on grid 
      gridX = (int)(getX()/100);
      gridY = (int)(getY()/100);
   }

   
   
   //which way the player moves------------
   
   public void act(boolean left, boolean right, boolean up, boolean down)
   {
         //moves player
         move();
         //moves player in specified direction 
         //if held down player speeds up 
         if(left)
            speedUp(-.1f,0);
         if(right)
            speedUp(.1f,0);
         if(up)
            speedUp(0,-.1f);
         if(down)
            speedUp(0,.1f);
         //if neither directions are pressed, player slows down
         if(!up && !down)
            slowDown(false, true, .025f);
         if(!left && !right)
            slowDown(true, false, .025f);
      
      //sets the score and highscore
      setScore();
      setHighScore();
     
            
   }
   
  
   
   //speeding up the player--------------------
   
   public void speedUp (float xacel, float yacel)
   {
      
         //if player at max speed of 5 then it is not allowed to speed up if not then it accelerates
         if(Math.abs(spedx)<5)
            spedx += xacel;
         else
            if(spedx<0)
               spedx = -5;
            else
               spedx=5;
          if(Math.abs(spedy)<5)
            spedy += yacel;
         else
            if(spedy<0)
               spedy = -5;
            else
               spedy=5;
      
   }
   
   
   //slowing down the player--------------
   
   public void slowDown(boolean slowX, boolean slowY, float decel)
   {
      //if key released then player slows down 
      if(slowX)
      {
         if(spedx<-.25)
            spedx+=decel;
         else if(spedx>.25)
            spedx-=decel;
         else 
            spedx = 0;
      }
      if(slowY)
      {
         if(spedy<-.25)
            spedy+=decel;
         else if(spedy>.25)
            spedy-=decel;
         else 
            spedy = 0;   
      }
   }
   
   
   //draws the player at the passed in x and y------------
   
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      //draws player
      if(!destroyed)
      {
         gc.setFill(Color.BLACK);
         gc.fillOval(x-14,y-14,27,27);
         gc.setFill(Color.CYAN);
         gc.fillOval(x-13,y-13,25,25);
      }
      
      //draw score
      gc.setFill(Color.WHITE);
      gc.fillText("Score: "+Integer.toString(score),10,20);
      gc.fillText("High Score: "+Integer.toString(highScore),10,40);
   
   
   }
   
   //setting the score of the player------------
   public int setScore()
   {
      //equation for the score
      score = (int)( Math.sqrt( (300-getX())*(300-getX()) + (300-getY())*(300-getY())));
      return score;
   }
   
   //setting the highscore---------
   public void setHighScore()
   {
      try
      {
         //access highscore file
         fileScan = new Scanner(new File("HighScore.txt"));
         highScore = fileScan.nextInt();
      }
      catch(FileNotFoundException e)
      {
      }
      
      //if score is higher than the highscore, it replaces the high score as a new highscore
      if(highScore < score)
      {
         //replacement
         highScore = score;
         try
         {
            //putting the new score in the file
            outputFile = new FileOutputStream(new File("HighScore.txt"),false);
            outputWrite = new PrintWriter(outputFile);
            outputWrite.println(score);
            outputWrite.close();
         }
         catch(IOException e)
         {
         }
      }
   }
   
   //gets the grid of x
   public int getGridX()
   {
      return gridX;
   }
   //gets the grid of y
   public int getGridY()
   {
      return gridY;
   }
   
   
   //makes new grid-----------------
   
   public boolean newGrid()
   {
      //makes the new grid 
      if(lastGridX != gridX || lastGridY != gridY)
      {
         lastGridX = gridX;
         lastGridY = gridY;
         return true;
      }
      else
      {
         lastGridX = gridX;
         lastGridY = gridY;
         return false;  
      }
   }
   
   //is the player destroyed yet---------------
   private boolean destroyed = false;
   public boolean destroyedYet()
   {
      return destroyed;
   }
   
   //player is destroyed when it touches a mine--------------------
   
   public void destroy(ArrayList<Mines> mines)
   {
      for(int i = 0; i < mines.size(); i++)
      {
         //distance from the mines and player
         double dist = distance(mines.get(i));
         //if mine and player touches
         if(dist <= 27/2 + 12/2)
         {
            //mine will blow up and player is destroyed
            destroyed = true;
            mines.get(i).setBlownUp(true);
         }
      }
   }


  

   
      
}

