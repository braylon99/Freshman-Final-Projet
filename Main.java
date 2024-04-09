import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;

import java.io.*;

import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;


public class Main extends Application
{
   //creating flowpane
   FlowPane fp;
   //creating canvas
   Canvas theCanvas = new Canvas(600,600);
   //creating player and home
   PlayerObject thePlayer = new PlayerObject(300,300);
   DrawableObject home;
   //creating array list for mines
   ArrayList<Mines> mines = new ArrayList<Mines>();
   
   //starts the stage---------------------------
   public void start(Stage stage)
   {
      
      //creates flowpane
      fp = new FlowPane();
      //adds flowpane to canvas
      fp.getChildren().add(theCanvas);
      gc = theCanvas.getGraphicsContext2D();
      //draws the background pic
      drawBackground(300,300,gc);
      //draws the player
      thePlayer.draw(300,300,gc,true);
     
      //starts animation
      AnimationHandler ta = new AnimationHandler();
      ta.start();
      
      //ceates the key listeners to move the player
      theCanvas.setOnKeyPressed(new KeyListenerDown());
      theCanvas.setOnKeyReleased(new KeyListenerReleased());
      
      
      //starts scene
      Scene scene = new Scene(fp, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project :)");
      stage.show();
      
      //makes the mines
      createMines(83,3);
      
      theCanvas.requestFocus();
   }
   
   GraphicsContext gc;
   
   //animation--------------------------------
  
   public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      {
         gc.clearRect(0,0,600,600);
         
         //check for a new grid
         changeGrid();
         
         //USE THIS CALL ONCE YOU HAVE A PLAYER
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc); 
      
         //creates the player
         thePlayer.draw(300,300,gc,true);
         //if the player is not destroyed, allows for movement
         if(!thePlayer.destroyedYet())
         {
            thePlayer.act(left, right, up, down);
         }
         //destroys player if it touches mine
         thePlayer.destroy(mines);
         
         //draws the mines
         for(int i = 0; i < mines.size(); i++)
         {
            if(!mines.get(i).getBlownUp()) // draw mine if it is not blown up
               mines.get(i).draw(thePlayer.getX(), thePlayer.getY(), gc, false);
         }
        
      
      }
   }
   
   //drawing background---------------------------------   
   
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
     //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
   //figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
     //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
     //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
   }
   
   
   //creating grid--------------------------------
   
   public void changeGrid()
   {
      //called every time the player reaches a new grid
      if(thePlayer.newGrid())
      {
         //starting location for the new grid from wherever the player is on the old one
         //for left and top it is 4 away so starts at 5
         int gridLeft = thePlayer.getGridX()-5;
         int gridTop = thePlayer.getGridY()-5;
         //for right and bottom it is 3 away so starts at 4
         int gridRight = thePlayer.getGridX()+4;
         int gridBottom = thePlayer.getGridY()+4;
         //goes through the created grids, left, right, top, and bottom, and creates mines within them 
         for(int i = gridLeft; i <= gridRight; i++)
         {
            for(int j = gridTop; j <= gridBottom; j++)
            {
               if(i == gridLeft || i == gridRight || j == gridTop || j == gridBottom)
                  createMines(i,j);
            }
         }
         
         //removes the mines when the player is 800 away from them
         for(int i = 0; i < mines.size(); i++)
         {
            if(thePlayer.distance(mines.get(i)) > 800)
               mines.remove(i);
         }
      }
   }
   
   //creating the mines------------------
   
   Random rand = new Random();
   public void createMines(int gridX, int gridY)
   {
      //find x with distance forumula 
      int fromOrigin = (int)Math.sqrt(Math.pow((double)gridX*100 - 300.0,2)+Math.pow((double)gridY*100 - 300.0,2));
      int x = fromOrigin/1000;
      
      //loops through x and spawns multiple mines
      for(int i = 0; i < x; i++)
      {
         //calculates for thirty percent chance
         int num = rand.nextInt(11);
         if(num <= 3)
         {
            //creates a mine on the new grid 
            int posX = gridX*100 + (int)(rand.nextFloat()*100);
            int posY = gridY*100 + (int)(rand.nextFloat()*100);
            mines.add(new Mines(posX, posY));
         }
      }
      
   }
   
   //booleans for the directions that are used within the act() of the player
   boolean left, right, up, down;
   
   //if the button is pressed-------------------------
   
   public class KeyListenerDown implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      {
         //if the buttons are pressed it changes respective direction to true
         if (event.getCode() == KeyCode.A) 
            left=true;
         if (event.getCode() == KeyCode.D) 
            right=true;
         if (event.getCode() == KeyCode.W) 
            up=true;
         if (event.getCode() == KeyCode.S) 
            down=true;
         
      }
      
   }
   

   //if the button is released-------------------------
   
   public class KeyListenerReleased implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      {
         //if the buttons are released it changes respective direction to false
         if (event.getCode() == KeyCode.A) 
            left=false;
         if (event.getCode() == KeyCode.D) 
            right=false;
         if (event.getCode() == KeyCode.W) 
            up=false;
         if (event.getCode() == KeyCode.S) 
            down=false;  
      }
      
   }
   
   //launches program----------------------------

   public static void main(String[] args)
   {
      launch(args);
   }
}

