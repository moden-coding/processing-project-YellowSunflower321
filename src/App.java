import java.util.ArrayList;


import processing.core.*;


//Music starts when space clicked + train sound
//Make a countdown
//Add more comments
//Mystery coin: can make you go back to the start
//Level coin: you gain a life
//Time coin: you gain time
//Two characters at the same time
//add instructions for keys to use for each character (appear until key is clicked)
//take away enter to start




public class App extends PApplet{


int scene = 1; //Welcome page/scene


//IMAGES
PImage imageChicken; //image of chicken
PImage imageHawk; //image of hawk
PImage imageDuck; //image of duck
PImage imageTitle; //image of crossy road title


//CHICKEN
   float xChicken = 400;  // starting x,y positions of chicken
   float yChicken = 550;
   float speedChicken = 5; // Chicken speed/movemet
   boolean leftChicken, rightChicken, upChicken, downChicken;


   int livesChicken = 3;
   int levelChicken = 1;


//DUCK
   float xDuck = 400;  // starting x,y positions of chicken
   float yDuck = 550;
   float speedDuck = 5; // Duck speed/movemet
   boolean leftDuck, rightDuck, upDuck, downDuck;


   int livesDuck = 3;
   int levelDuck = 1;




// ChatGPT--> LIST FOR NEW CARS
ArrayList<Float> xListLeft = new ArrayList<Float>(); //LEFT CAR
ArrayList<Float> yListLeft = new ArrayList<Float>();
ArrayList<Float> speedListLeft = new ArrayList<Float>();
ArrayList<Integer> colorListLeft = new ArrayList<Integer>();


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


   float speedTrain = 15;


   //Train time
   int lastTrainTime = 0;
   int trainWaitTime=5000; //10 seconds


   //RedTrainLight
   int rTrain = 0;
   int gTrain = 0;
   int bTrain = 0;


//CAR TIME
   //For bottom road:
   int lastCarTimeRight = 0;
   int lastCarTimeLeft = 0;


   int[] bottomLeftTimer = {0}; //ChatGPT helped me make different timers for each lane
   int[] bottomRightTimer = {0};
   int[] topLeftTimer = {0};
   int[] topRightTimer = {0};


   float[] carWaitTimeBottomLeft = { random(500,2000) }; //different wait times for each lane
   float[] carWaitTimeBottomRight = { random(500,2000) };
   float[] carWaitTimeTopLeft = { random(500,2000) };
   float[] carWaitTimeTopRight = { random(500,2000) };






   boolean GameOver = false; //whether or not chicken is dead (game over)
   boolean restart = false; //when lives=0 and the lives need to be reset


//TIMER:
   int timer = 0; //start timer at zero
   boolean startTimer = false; //timer doesn't start yet
   int timerStart = 0;
   int HighScore = 10000; //Starts high enough so that the first time played becomes the high score
   int timeLimit; //Time limit for each level
  
  
//HAWK:
   boolean OutOfTime=false; //When hawk will come
   int xHawk = -10; //hawk starts off screen
   int speedHawk=4;


//MULTIPLAYER:
   boolean multiPlayer=false;






   public static void main(String[] args)  {
       PApplet.main("App");
   }


   public void setup(){
       lastTrainTime=millis();        
        textSize(32);
       //loads images
       imageChicken = loadImage("CrossyRoadCharacter3.png");
       imageHawk = loadImage("Hawk.png");
       imageDuck = loadImage("Duck.png");
       imageTitle = loadImage("Title.png");
   }


   public void settings(){
       size(800, 600);
   }


   public void draw(){


   if(scene==1){ //Welcome page
       background(200,225,230);
       imageMode(CORNER); //sets chicken back same place each time
          
           // noStroke();
           // fill(10,150,100);
           // rect(290,150,200,80);
           // fill(290,150,200,80);
           // text("Crossy Road", 305, 200);
           image(imageChicken, 320, 360, 180, 200); //Image of chicken
           image(imageTitle, 230, 50, 350, 200); //Image of Crossy Road title






           fill(0,0,0);
           text("Welcome!",325,300);
           text("Use the arrow keys to move your character.", 100,340);


           //multiplayer button
           noStroke();
           fill(255);
           rect(80,380,200,80);
           fill(230,100,20);
           text("Multiplayer", 100, 430);


           //singleplayer button
           noStroke();
           fill(230,100,20);
           rect(530,380,200,80);
           fill(255);
           text("Singleplayer", 550, 430);




   } else if(scene==2){
       background(10,150,100);


// BOTTOM ROAD:
       fill(200,200,200); //road
       stroke(0,0,0);
       strokeWeight(4);
       rect(-5,370,810,110);


       stroke(255,255,20); //yellow lines
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
       fill(200,200,200); //road
       stroke(0,0,0);
       strokeWeight(4);
       rect(-5,10,810,110);


       stroke(255,255,20); //yellow lines
       line(-5,60,810,60);
       stroke(255,255,20);
       line(-5,70,810,70);


// LEVEL, LIVES, & TIMER TEXT:
       fill(0,0,0);
       text("Lives = " + livesChicken, 660, 540);
       text("Level = " + levelChicken, 660, 580);
       text("Time = " + timer + " seconds", 20, 580);
       if(startTimer==true){
           timer=(millis()-timerStart)/1000;
       }
  
//Time limit warning
   if(levelChicken>=3){
       fill(255);
       text("You only have " + timeLimit + " seconds to complete Level " + levelChicken, 75, 310);
   }


//CHICKEN:
       imageMode(CENTER); // centers image at x,y
       image(imageChicken, xChicken, yChicken, 45, 50); // width and height can be adjusted


       if(yChicken<=0 && levelChicken<=4){ //moves chicken back to start after reaching top
           scene=4; //game over scene
           if(multiPlayer==true){
               xChicken=300;
               yChicken=500;
           } else{
           xChicken=400;
           yChicken=500;
           }
       } else if(yChicken<=0 && levelChicken>=5){
           scene=5; //Switches to "you won" screen after last level
       }


   //Smooth movement of chicken:
       if(leftChicken==true){
           xChicken -=speedChicken;
       }
       if(rightChicken==true){
           xChicken +=speedChicken;
       }
       if(upChicken==true){
           yChicken -=speedChicken;
       }
       if(downChicken==true){
           yChicken +=speedChicken;
       }


//Reset chicken placement
       if(yChicken>=600 || xChicken<=0 || xChicken>=800){ //if chicken touches edge
           GameOver=true;
           if(multiPlayer==true){
               xChicken = 300;
               yChicken = 550;
               xDuck = 400;
               yDuck = 550;
           } else{
           xChicken = 400;  // starting x position of chicken
           yChicken = 550;
           }
           scene=3;
       }


//HAWK MOVEMENT:
if(levelChicken>=3 && OutOfTime==true){
   image(imageHawk, xHawk,yChicken, 50,50);
   xHawk+=speedHawk;
   if(xHawk>=810);
       OutOfTime=false;
   }else{
   xHawk=-10; //resets hawk's position
   OutOfTime=false;
}


//MULTIPLAYER MODE: DUCK!
if(multiPlayer==true){
   image(imageDuck, xDuck, yDuck, 35, 40); //Image of duck


//Smooth movement of chicken:
       if(leftDuck==true){
           xDuck -=speedDuck;
       }
       if(rightDuck==true){
           xDuck +=speedDuck;
       }
       if(upDuck==true){
           yDuck -=speedDuck;
       }
       if(downDuck==true){
           yDuck +=speedDuck;
       }
   }




//LEVELS!
   if(levelChicken==1){
       carsDriveByLeft(-30,380,2, bottomLeftTimer,carWaitTimeBottomLeft);
       carsDriveByRight(830, 440, 2, bottomRightTimer, carWaitTimeBottomRight);


       carsDriveByLeft(-30,20,2, topLeftTimer, carWaitTimeTopLeft); //cars speed up
       carsDriveByRight(830,80,2, topRightTimer, carWaitTimeTopRight);


   } else if(levelChicken==2){
       carsDriveByLeft(-30,380,3, bottomLeftTimer,carWaitTimeBottomLeft); // cars speed up
       carsDriveByRight(830, 440,3, bottomRightTimer, carWaitTimeBottomRight);


       carsDriveByLeft(-30,20,3, topLeftTimer, carWaitTimeTopLeft);
       carsDriveByRight(830,80,3, topRightTimer, carWaitTimeTopRight);
   } else if(levelChicken==3){
       carsDriveByLeft(-30,380,4, bottomLeftTimer,carWaitTimeBottomLeft); //cars speed up
       carsDriveByRight(830, 440,4, bottomRightTimer, carWaitTimeBottomRight);


       carsDriveByLeft(-30,20,4, topLeftTimer, carWaitTimeTopLeft);
       carsDriveByRight(830,80,4, topRightTimer, carWaitTimeTopRight);
      
       timeLimit=20;
       if(timer>=timeLimit){
           OutOfTime=true;
           if(xHawk>=820){ //Leave time for hawk to come in
           GameOver=true;
           scene=3;
           }
       }
      


   } else if(levelChicken==4){
       carsDriveByLeft(-30,380,2, bottomLeftTimer,carWaitTimeBottomLeft); //timer starts
       carsDriveByRight(830, 440, 2, bottomRightTimer, carWaitTimeBottomRight);


       carsDriveByLeft(-30,20,2, topLeftTimer, carWaitTimeTopLeft);
       carsDriveByRight(830,80,2, topRightTimer, carWaitTimeTopRight);       
      
       timeLimit=15;
       if(timer>=timeLimit){
           OutOfTime=true;
           if(xHawk>=820){ //Leave time for hawk to come in
           GameOver=true;
           scene=3;
           }
       }


   } else if(levelChicken==5){
       carsDriveByLeft(-30,380,3, bottomLeftTimer,carWaitTimeBottomLeft);
       carsDriveByRight(830, 440, 3, bottomRightTimer, carWaitTimeBottomRight);


       carsDriveByLeft(-30,20,3, topLeftTimer, carWaitTimeTopLeft);
       carsDriveByRight(830,80,3, topRightTimer, carWaitTimeTopRight);
      
       timeLimit=10;
       if(timer>=timeLimit){
           OutOfTime=true;
           if(xHawk>=820){ //Leave time for hawk to come in
           GameOver=true;
           scene=3;
           }
       }else if(levelChicken==6){
       scene=5;
   }
}
  
//Hawk goes over chicken
if(Math.abs(xHawk-xChicken)<=5){
   xChicken+=speedHawk;
}




//Chicken hits vehicle (collisions)
if(checkOverlap()==true){
   GameOver=true;
   scene=3;
}




//TRAIN movement:
if((millis()-lastTrainTime)>(trainWaitTime-3000) && (millis() - lastTrainTime) < (trainWaitTime + 3000)){ //If train is 3 seconds away, light turns red
       rTrain=200;
       gTrain=15;
       bTrain=15;
} else{
       rTrain=0;
       gTrain=0;
       bTrain=0;
}


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
   background(150,50,50);
   fill(255);
   if((livesChicken-1)==0){
       text("Game over.", 310, 300);
       text("Your Time: " + timer + " sec", 280, 440);
       levelChicken=1; //Start again at level 1
       restart=true;
       } else{
       if(livesChicken==2){
           text("You have " + (livesChicken-1) + " life left. Try Again!",180,300);
       } else{
           text("You have " + (livesChicken-1) + " lives left. Try Again!",180,300);
       }
       restart=false;


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


   image(imageChicken, 400, 150, 180, 200); //Image of chicken




//GAMEOVER BUTTON:
   fill(255); //Restart game
   noStroke();
   rect(310,330,160,50);
   fill(0);
   text("Play Again", 320,365);
   xChicken = 400;  // restart x and y positions of chicken
   yChicken = 550;
   timerStart=millis();






//"YOU PASSED" SCENE
} else if(scene==4){
   background(0,150,100);
   fill(255);
   text("You Passed!", 310, 300);
           //clear cars list to prevent overlap
       xListLeft.clear();
       yListLeft.clear();
       speedListLeft.clear();
       colorListLeft.clear();


       xListRight.clear();
       yListRight.clear();
       speedListRight.clear();
       colorListRight.clear();
   livesChicken=3; //reset the lives
   text("Your Time: " + timer + " seconds", 250, 440);
       if(timer<HighScore){
           HighScore=timer;
           text("Great job! New High Score: " + HighScore + " seconds", 200, 490); //If there is a new high score




       }else{
       text("High Score: " + HighScore + " seconds", 240, 490);
       }
  
   image(imageChicken, 400, 150, 180, 200); //Image of chicken
   xChicken = 400;  // restart x and y positions of chicken
   yChicken = 550;
   timerStart=millis(); //Counting seconds from start




   //NEXT LEVEL BUTTON:
   fill(255);
   noStroke();
   rect(310,330,160,50);
   fill(0);
   text("Next Level", 320,365);


//FINISHED GAME (SCENE)
} else if(scene==5){
   background(0,150,100);
   fill(255);
   text("You WON the Game!", 270, 300);
   image(imageChicken, 400, 480, 180, 200); //Image of chicken
   text("Click the spacebar to return home", 200, 360);
   livesChicken=3; //reset lives and level;
   levelChicken=1;
}




   }






   public void keyPressed(){

       if(OutOfTime==false){
           if(keyCode==LEFT){
               leftChicken = true;
           }
          if(keyCode==RIGHT){
               rightChicken = true;
           }
           if(keyCode==UP){
               upChicken = true;
           }
           if(keyCode==DOWN){
               downChicken = true;
           }


           if(multiPlayer==true){ //multiplayer mode uses A,W,D to move duck
               if(keyCode=='a'){
                   leftDuck=true;
               }
               if(keyCode=='d'){
                   rightDuck=true;
               }
               if(keyCode=='w'){
                   upDuck=true;
               }
               if(keyCode=='s'){
                   downDuck=true;
               }


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
   }




   public void keyReleased(){
  
   if(OutOfTime==false){ //if the chicken is not out of time
       if(keyCode==LEFT){
           leftChicken = false;
       }
       if(keyCode==RIGHT){
           rightChicken = false;
       }
       if(keyCode==UP){
           upChicken = false;
       }
       if(keyCode==DOWN){
           downChicken = false;
       }
       if(multiPlayer==true){
       if(keyCode=='a'){
           leftDuck = false;
       }
       if(keyCode=='d'){
           rightDuck = false;
       }
       if(keyCode=='w'){
           upDuck = false;
       }
       if(keyCode=='s'){
           downDuck = false;
       }
       }
   }
   }


   public void mousePressed(){
       if(scene==3){//GameOver Page
           if(mouseX>310 && mouseX<470 && mouseY>330 && mouseY<380){ //Play Again button is pressed
               scene=2; //Back to crossy road
               if(restart==true){
                   livesChicken=3; //Resets lives
               }else{
               livesChicken=livesChicken-1; //Otherwise life lost
               }
              
           }
       }


       if(scene==4){//You Passed Page
           if(mouseX>310 && mouseX<470 && mouseY>330 && mouseY<380 && levelChicken<=5){ //Next Level button is pressed
               scene=2; //Back to crossy road
               levelChicken++;
           }
       }


       if(scene==1){
           if(mouseX>80 && mouseX<280 && mouseY>380 && mouseY<460){ //multiplayer button is pressed
               multiPlayer=true; //turn on multiplayer version
               scene=2; //go to game page
                startTimer=true;
                timerStart = millis();  // start the timer
           } else if(mouseX>530 && mouseX<730 && mouseY>380 && mouseY<460){ //singleplayer button is pressed
               scene=2; //go to game page
               multiPlayer=false;
                    startTimer=true;
                timerStart = millis();  // start the timer
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


   // --- Check left lane cars ---
   for (int i = 0; i < xListLeft.size(); i++) {
       float carX = xListLeft.get(i) - 30; // adjust since you draw rect(x-30, y, 60, 30)
       float carY = yListLeft.get(i);
       float carWidth = 60;
       float carHeight = 30;


       if (chickenRight > carX && chickenLeft < carX + carWidth &&
           chickenBottom > carY && chickenTop < carY + carHeight) {
           GameOver = true;
           return true;
       }
   }


   // --- Check right lane cars ---
   for (int i = 0; i < xListRight.size(); i++) {
       float carX = xListRight.get(i) + 30; // adjust since you draw rect(x+30, y, 60, 30)
       float carY = yListRight.get(i);
       float carWidth = 60;
       float carHeight = 30;


       if (chickenRight > carX && chickenLeft < carX + carWidth &&
           chickenBottom > carY && chickenTop < carY + carHeight) {
           GameOver = true;
           return true;
       }
   }


   // --- Check train ---
   float trainWidth = 3000;
   float trainHeight = 50;
   if (chickenRight > xTrain && chickenLeft < xTrain + trainWidth &&
       chickenBottom > yTrain && chickenTop < yTrain + trainHeight) {
       GameOver = true;
       return true;
   }


   // No collision
   return false;
}


}

