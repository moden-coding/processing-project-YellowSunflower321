import java.util.ArrayList;


import processing.core.*;
import processing.sound.*;

//***ChatGPT was mainly used to create a list for the cars and to check for collisions

public class App extends PApplet{

int scene = 1; //Start at welcome page

//IMAGES
PImage imageChicken; //image of chicken
PImage imageHawk; //image of Hawk


//IMPORT SOUND (both start off)
boolean playTrainSound = false;
boolean playMusic = false; 
SoundFile trainSound; 
SoundFile music;


//CHICKEN
   // starting x,y positions of chicken
   float xChicken = 400; 
   float yChicken = 550;
   // Chicken speed/movemet
   float speedChicken = 5; 
   boolean left, right, up, down;

    //lives and level setup
   int lives = 3;
   int level = 1;


// ChatGPT--> //x, y, speed, and color variables for the list

//car that comes from left
ArrayList<Float> xListLeft = new ArrayList<Float>(); //LEFT CAR
ArrayList<Float> yListLeft = new ArrayList<Float>();
ArrayList<Float> speedListLeft = new ArrayList<Float>();
ArrayList<Integer> colorListLeft = new ArrayList<Integer>();

//car that comes from right
ArrayList<Float> xListRight = new ArrayList<Float>(); //RIGHT CAR
ArrayList<Float> yListRight = new ArrayList<Float>();
ArrayList<Float> speedListRight = new ArrayList<Float>();
ArrayList<Integer> colorListRight = new ArrayList<Integer>();
//<--ChatGPT


//TRAIN
   float trainWidth = 3000; //train dimensions
   float trainHeight = 50;

   float xTrain = -3000; //starting coordinates of train
   float yTrain = 160;

   float speedTrain = 15; //train speed (fast)

   //Train time:
   int lastTrainTime = 0;
   int trainWaitTime=5000; //5 seconds

   //RedTrainLight (starts black)
   int rTrain = 0;
   int gTrain = 0;
   int bTrain = 0;

//CAR TIME
   int lastCarTimeRight = 0;
   int lastCarTimeLeft = 0;

   int[] bottomLeftTimer = {0}; //ChatGPT helped me make different timers for each lane
   int[] bottomRightTimer = {0};
   int[] topLeftTimer = {0};
   int[] topRightTimer = {0};

   float[] carWaitTimeBottomLeft = { random(500,2000) }; //different wait times for each lane (random between 0.5-2 seconds)
   float[] carWaitTimeBottomRight = { random(500,2000) };
   float[] carWaitTimeTopLeft = { random(500,2000) };
   float[] carWaitTimeTopRight = { random(500,2000) };

   boolean restart = false; //when lives=0 and the lives need to be reset


//TIMER:
   int timer = 0; //start timer at zero
   boolean startTimer = false; //timer doesn't start yet
   int timerStart = 0;
   int HighScore = 10000; //Starts high enough so that the first time played becomes the high score
   int timeLimit; //Time limit for levels 3-5
  
  
//HAWK:
   boolean OutOfTime=false; //When hawk will come
   int xHawk = -10; //hawk starts off screen
   int speedHawk=4; 

//COINS:
    //coin count from start
    int coins = 0;
    //random location of coin
    float coinX = random(30,770);
    float coinY = random(30,570);
    boolean showCoin=false; //if coin appears


   public static void main(String[] args)  {
       PApplet.main("App");
   }


   public void setup(){
       lastTrainTime=millis();        
       textSize(32);
       //loads images
       imageChicken = loadImage("CrossyRoadCharacter3.png");
       imageHawk = loadImage("Hawk.png");
       trainSound = new SoundFile(this, "trainSound.mp3"); //load train sound
       music = new SoundFile(this, "music.mp3");

     if(!playMusic){ //play music repeatedly after it is finished
        float volume = 2/10;
       music.amp(volume);
       music.loop();
       playMusic=true;
    } else{
        playMusic=false;
    }


   }


   public void settings(){
       size(800, 600);
   }


   public void draw(){

   if(scene==1){ //Welcome page
       background(200,225,230);
       imageMode(CORNER); //sets chicken back same place each time
          
           noStroke();
           fill(230,100,20);
           rect(290,150,200,80);
           fill(255,255,255);
           text("Crossy Road", 305, 200);
           image(imageChicken, 320, 360, 180, 200); //Image of chicken




           fill(0,0,0);
           text("Please click enter to start.",220,300);
           text("Use the arrow keys to move your character.", 100,340);




   } else if(scene==2){
       background(10,150,100);


// BOTTOM ROAD:
       fill(200,200,200);
       stroke(0,0,0);
       strokeWeight(4);
       rect(-5,370,810,110);


//BOTTOM YELLOW LINES
       stroke(255,255,20);
       line(-5,430,810,430);
       stroke(255,255,20);
       line(-5,420,810,420);


   // TRAIN TRACK:
       fill(80,80,80);
       stroke(0,0,0);
       strokeWeight(6);
       rect(-5,150,810,70);


   //TRAIN LIGHT:
       fill(rTrain,gTrain,bTrain);
       stroke(0,0,0);
       strokeWeight(4);
       rect(10,240,25,25);


     
   // TOP ROAD:
       fill(200,200,200);
       stroke(0,0,0);
       strokeWeight(4);
       rect(-5,10,810,110);

    //TOP YELLOW LINES
       stroke(255,255,20); 
       line(-5,60,810,60);
       stroke(255,255,20);
       line(-5,70,810,70);

//COIN APPEARS
    stroke(0);
    fill(255, 215, 0);
    circle(coinX, coinY, 20);
    

// LEVEL, LIVES, COINS & TIMER TEXT:
       fill(0,0,0);
       text("Lives = " + lives, 660, 540);
       text("Level = " + level, 660, 580);
       text("Coins = " + coins, 20, 540);
       text("Time = " + timer + " seconds", 20, 580);
       if(startTimer==true){ 
           timer=(millis()-timerStart)/1000; //time from when ENTER is pressed/game starts
       }



  
//Time limit warning
   if(level>=3){
       fill(255);
       text("You only have " + timeLimit + " seconds to complete Level " + level, 75, 310);
   }


//CHICKEN:
       imageMode(CENTER); // centers image at x,y
       image(imageChicken, xChicken, yChicken, 45, 50); // width and height can be adjusted

       //moves chicken back to start after reaching top and passing the level
       if(yChicken<=0 && level<=4){ 
           scene=4;
           xChicken=400;
           yChicken=500;
       } else if(yChicken<=0 && level>=5){ //Switches to "you won" screen after last level (5)
           scene=5;
       }


   //Smooth movement of chicken (Dr. Moden code):
       if(left==true){
           xChicken -=speedChicken;
       }
       if(right==true){
           xChicken +=speedChicken;
       }
       if(up==true){
           yChicken -=speedChicken;
       }
       if(down==true){
           yChicken +=speedChicken;
       }


//Reset chicken placement
       if(yChicken>=600 || xChicken<=0 || xChicken>=800){ //if chicken touches edge
           xChicken = 400;  // starting x position of chicken
           yChicken = 550;
           scene=3;
       }


//Chicken hits vehicle (collisions)
if(checkOverlap()==true){
   scene=3; //game over
}
  
//Chicken touches coin
touchCoin(); //call method: changes position of coin if chicken touches it


//HAWK MOVEMENT:
if(level>=3 && OutOfTime==true){
   image(imageHawk, xHawk,yChicken, 50,50);
   xHawk+=speedHawk; 
   //until hawk moves off screen
   if(xHawk>=810);
       OutOfTime=false;
   }else{
   xHawk=-10; //resets hawk's position
   OutOfTime=false;
}

//Hawk goes over chicken
if(Math.abs(xHawk-xChicken)<=5){
   xChicken+=speedHawk; //chicken moves with hawk at hawk's speed
}


//LEVELS!

    //calls car method with x, y, speed, and different timers and wait time (for cars coming from left and right)
   if(level==1){
    //normal speed
       carsDriveByLeft(-30,380,2, bottomLeftTimer,carWaitTimeBottomLeft);
       carsDriveByRight(830, 440, 2, bottomRightTimer, carWaitTimeBottomRight);

       carsDriveByLeft(-30,20,2, topLeftTimer, carWaitTimeTopLeft); 
       carsDriveByRight(830,80,2, topRightTimer, carWaitTimeTopRight);

       showCoin=true;

   } else if(level==2){
    //speed gets faster
       carsDriveByLeft(-30,380,3, bottomLeftTimer,carWaitTimeBottomLeft); 
       carsDriveByRight(830, 440,3, bottomRightTimer, carWaitTimeBottomRight);

       carsDriveByLeft(-30,20,3, topLeftTimer, carWaitTimeTopLeft);
       carsDriveByRight(830,80,3, topRightTimer, carWaitTimeTopRight);

        showCoin=true;

   } else if(level==3){
    //speed gets faster
       carsDriveByLeft(-30,380,4, bottomLeftTimer,carWaitTimeBottomLeft);
       carsDriveByRight(830, 440,4, bottomRightTimer, carWaitTimeBottomRight);

       carsDriveByLeft(-30,20,4, topLeftTimer, carWaitTimeTopLeft);
       carsDriveByRight(830,80,4, topRightTimer, carWaitTimeTopRight);

        showCoin=true;

      //20 second time limit
       timeLimit=20;
       if(timer>=timeLimit){
           OutOfTime=true; //initiates hawk
           if(xHawk>=820){ //Switches to game over page when he hawk moves off the screen
            scene=3;
           }
       }
      


   } else if(level==4){
    //normal speed again
       carsDriveByLeft(-30,380,2, bottomLeftTimer,carWaitTimeBottomLeft); //timer starts
       carsDriveByRight(830, 440, 2, bottomRightTimer, carWaitTimeBottomRight);


       carsDriveByLeft(-30,20,2, topLeftTimer, carWaitTimeTopLeft);
       carsDriveByRight(830,80,2, topRightTimer, carWaitTimeTopRight);       
      
       //15 second time limit
       timeLimit=15;
       if(timer>=timeLimit){
           OutOfTime=true;
           if(xHawk>=820){ //Leave time for hawk to come in
           scene=3;
           }
       }

       showCoin=true;

   } else if(level==5){
        //speed gets faster
       carsDriveByLeft(-30,380,3, bottomLeftTimer,carWaitTimeBottomLeft);
       carsDriveByRight(830, 440, 3, bottomRightTimer, carWaitTimeBottomRight);


       carsDriveByLeft(-30,20,3, topLeftTimer, carWaitTimeTopLeft);
       carsDriveByRight(830,80,3, topRightTimer, carWaitTimeTopRight);
      
       //ten second time limit
       timeLimit=10;
       if(timer>=timeLimit){
           OutOfTime=true;
           if(xHawk>=820){ //Leave time for hawk to come in
           scene=3;
           }
       }else if(level==6){
       scene=5;
   }

    showCoin=true;

}


//TRAIN movement:
if((millis()-lastTrainTime)>(trainWaitTime-3000) && (millis() - lastTrainTime) < (trainWaitTime + 3000)){ //If train is 3 seconds away, light turns red
       
    //red rgb
       rTrain=200;
       gTrain=15;
       bTrain=15;
       //play train sound effect if red light comes on (ChatGPT and Dr. Moden code )
       if(!playTrainSound){ 
        float volume = 2/10;
       trainSound.amp(volume);
       trainSound.play();
       playTrainSound=true;
}


} else{
    //light turns off (black)
       rTrain=0; 
       gTrain=0;
       bTrain=0;
       playTrainSound=false;


}

//If train wait time passes, the train moves by
if((millis()-lastTrainTime)>trainWaitTime){ 
   xTrain += speedTrain;
   fill(165, 45, 45);
   rect(xTrain, yTrain, trainWidth, trainHeight);
}
 if (xTrain > width + trainWidth) {
   xTrain = -trainWidth;        // move it back off screen
   lastTrainTime = millis();    // start waiting again
 }
}


//---> GAMEOVER SCENE
else if(scene==3){
    showCoin=false;
    background(150,50,50);
   fill(255);
   //coins printed with reminder
    text("Coins = " + coins + ". Press c to use 3 coins to reset to 3 lives.", 20, 530);
    if(coins>=3){ //check if user has enough coins
       text("You have enough coins!", 20, 580);
       }
    coinX=random(30,770); //reset coin position
    coinY=random(30,570);

   //If you run out of lives
    if((lives-1)==0){
       text("Game over.", 310, 300);
       text("Your Time: " + timer + " sec", 280, 440);

       level=1; //Start again at level 1
       restart=true;
       } else{
       if(lives==2){
           text("You have " + (lives-1) + " life left. Try Again!",180,300);
       } else{
           text("You have " + (lives-1) + " lives left. Try Again!",180,300);
       }
       restart=false;


       //clear cars list to prevent overlap (ChatGPT)
       xListLeft.clear();
       yListLeft.clear();
       speedListLeft.clear();
       colorListLeft.clear();


       xListRight.clear();
       yListRight.clear();
       speedListRight.clear();
       colorListRight.clear();
   }


   image(imageChicken, 400, 150, 180, 200); //Image of chicken




//GAMEOVER BUTTON:
   fill(255); //Restart game
   noStroke();
   rect(310,330,160,50);
   fill(0);
   text("Play Again", 320,365);
   xChicken = 400;  // restart x and y positions of chicken
   yChicken = 550;
   timerStart=millis(); //restart timer



//"YOU PASSED" SCENE
} else if(scene==4){
   background(0,150,100);
   fill(255);
    coinX=random(30,770); //reset coin loaction
    coinY=random(30,570);
   
   text("You Passed!", 310, 300);
        //clear cars list to prevent overlap (ChatGPT)
       xListLeft.clear();
       yListLeft.clear();
       speedListLeft.clear();
       colorListLeft.clear();


       xListRight.clear();
       yListRight.clear();
       speedListRight.clear();
       colorListRight.clear();

   lives=3; //reset the lives
   text("Your Time: " + timer + " seconds", 250, 440); //print time

    //find high score
   if(timer<HighScore){ 
           HighScore=timer;
           text("Great job! New High Score: " + HighScore + " seconds", 200, 490); //If there is a new high score
       }else{
       text("High Score: " + HighScore + " seconds", 240, 490);
       }

   image(imageChicken, 400, 150, 180, 200); //Image of chicken
   xChicken = 400;  // restart x and y positions of chicken
   yChicken = 550;
   timerStart=millis(); //restart timer


   //NEXT LEVEL BUTTON:
   fill(255);
   noStroke();
   rect(310,330,160,50);
   fill(0);
   text("Next Level", 320,365);


//FINISHED GAME (SCENE)
} else if(scene==5){
    showCoin=false; //coin disappears
   background(0,150,100);
   fill(255);
   text("You WON the Game!", 270, 300);
   image(imageChicken, 400, 480, 180, 200); //Image of chicken
   text("Click the spacebar to return home", 200, 360);
   lives=3; //reset lives and level;
   level=1;
}
   }

   public void keyPressed(){
        //Enter takes user to game from first scene
       if (keyCode == ENTER && scene==1){
           scene++;
           startTimer=true;
           timerStart = millis();  // start the timer
       }
       if(OutOfTime==false){ //only if chicken isn't out of time (for levels 3-5)
           //smooth movement (Dr. Moden code)
            if(keyCode==LEFT){
               left = true;
           }
          if(keyCode==RIGHT){
               right = true;
           }
           if(keyCode==UP){
               up = true;
           }
           if(keyCode==DOWN){
               down = true;
           }
       }
      
       if(keyCode==' ' && scene==5){ //If user finishes game and presses 'space', they return to the home screen
           scene=1;
           xChicken = 400;  // reset chicken to start
           yChicken = 550;
            //clear cars list to prevent overlap
            xListLeft.clear();
            yListLeft.clear();
            speedListLeft.clear();
            colorListLeft.clear();

            xListRight.clear();
            yListRight.clear();
            speedListRight.clear();
            colorListRight.clear();
       }

       if(key =='c' && scene==3 && coins>=3){ //If c pressed during game and user has at least 3 coins
        coins=coins-3; //take away 3 coins
        scene=2; //back to game scene
        lives=3; //reset lives to 3
       }
   }


   public void keyReleased(){
    
        //smooth movement (Dr. Moden code)
       if(OutOfTime==false){
       if(keyCode==LEFT){
           left = false;
       }
       if(keyCode==RIGHT){
           right = false;
       }
       if(keyCode==UP){
           up = false;
       }
       if(keyCode==DOWN){
           down = false;
       }
   }
   }

   public void mousePressed(){
       if(scene==3){//GameOver Page
           if(mouseX>310 && mouseX<470 && mouseY>330 && mouseY<380){ //Play Again button is pressed
               scene=2; //Back to crossy road
               if(restart==true){
                   lives=3; //Resets lives
               }else{
               lives=lives-1; //Otherwise life lost
               }
           }
       }

       if(scene==4){//You Passed Page
           if(mouseX>310 && mouseX<470 && mouseY>330 && mouseY<380 && level<=5){ //Next Level button is pressed
               scene=2; //Back to crossy road
               level++;
           }
       }


   }


//METHOD ON CARS DRIVING BY FROM LEFT:
   public void carsDriveByLeft(float x, float y, float speed, int lastCarTimeLeft[], float carWaitTimeLeft[]){
       //***Left Cars (list)
       if((millis()-lastCarTimeLeft[0])>carWaitTimeLeft[0]){

//ChatGPT-->
           int c = color(random(255), random(255), random(255));
          
           xListLeft.add(x); //Adds each new x, y, speed, and c to list (creates a new car)
           yListLeft.add(y);
           speedListLeft.add(speed);
           colorListLeft.add(c);


           lastCarTimeLeft[0]=millis();
           carWaitTimeLeft[0] = random(500, 1500);  // A new car drives by 0.5-2 seconds
//<-- ChatGPT

       }

   // Move and draw all cars
   stroke(0); // black outline
   for (int i = xListLeft.size()-1; i >= 0; i--) {
       x = xListLeft.get(i);
       y = yListLeft.get(i);
       speed = speedListLeft.get(i);
       int c = colorListLeft.get(i);


   // draw car
       fill(c);
       rect(x-30, y, 60, 30);


       // move car
       x += speed;
       xListLeft.set(i, x); // update position


       // remove car if off-screen
       if (x-60 > width) {
       xListLeft.remove(i);
       yListLeft.remove(i);
       speedListLeft.remove(i);
       colorListLeft.remove(i);
//<--ChatGPT
   }
 }
   }
//METHOD ON CARS DRIVING BY FROM RIGHT:
   public void carsDriveByRight(float x, float y, float speed, int lastCarTimeRight[], float carWaitTimeRight[]){


       if((millis()-lastCarTimeRight[0])>carWaitTimeRight[0]){ //Car drives by every 2 seconds
           int c = color(random(255), random(255), random(255));
          
           xListRight.add(x);
           yListRight.add(y);
           speedListRight.add(speed);
           colorListRight.add(c);


           lastCarTimeRight[0]=millis();
           carWaitTimeRight[0] = random(500, 1500);  // choose new random interval


          
       }


   // Move and draw all cars
   for (int i = xListRight.size()-1; i >= 0; i--) {
       x = xListRight.get(i);
       y = yListRight.get(i);
       speed = speedListRight.get(i);
       int c = colorListRight.get(i);


   // draw car
       fill(c);
       rect(x+30, y, 60, 30);


       // move car
       x -= speed;
       xListRight.set(i, x); // update position
          if (x+100 < 0) {
      
       //remove car if off schreen
       xListRight.remove(i);
       yListRight.remove(i);
       speedListRight.remove(i);
       colorListRight.remove(i);
   }


   }


}

public  void touchCoin(){ //method for checking if chicken is touching coin
    if(xChicken>coinX && xChicken<(coinX+20) && yChicken>coinY && yChicken<(coinY+20)){ //if chicken touches coin
    coinX=random(30,770); //reset coin position
    coinY=random(30,570);
    coins++;
}
}

// Method for checking if chicken hits a car or train
public boolean checkOverlap() {
   // Dimensions of the chicken
   float chickenWidth = 45;
   float chickenHeight = 50;

   // Calculate chicken edges since imageMode(CENTER) 
   float chickenLeft = xChicken - chickenWidth / 2;
   float chickenRight = xChicken + chickenWidth / 2;
   float chickenTop = yChicken - chickenHeight / 2;
   float chickenBottom = yChicken + chickenHeight / 2;


   //Check left lane cars - ChatGPT
   for (int i = 0; i < xListLeft.size(); i++) { 
       float carX = xListLeft.get(i) - 30; // adjust since you draw rect(x-30, y, 60, 30)
       float carY = yListLeft.get(i);
       float carWidth = 60;
       float carHeight = 30;


       if (chickenRight > carX && chickenLeft < carX + carWidth && //if chicken is touching the car
           chickenBottom > carY && chickenTop < carY + carHeight) {
           return true;
       }
   }


   // --- Check right lane cars --- ChatGPT
   for (int i = 0; i < xListRight.size(); i++) {
       float carX = xListRight.get(i) + 30; // adjust since you draw rect(x+30, y, 60, 30)
       float carY = yListRight.get(i);
       float carWidth = 60;
       float carHeight = 30;


       if (chickenRight > carX && chickenLeft < carX + carWidth &&
           chickenBottom > carY && chickenTop < carY + carHeight) {
           return true;
       }
   }


   // --- Check train --- ChatGPT
   float trainWidth = 3000;
   float trainHeight = 50;
   if (chickenRight > xTrain && chickenLeft < xTrain + trainWidth &&
       chickenBottom > yTrain && chickenTop < yTrain + trainHeight) {
       return true;
   }


   // No collision
   return false;
}



}

