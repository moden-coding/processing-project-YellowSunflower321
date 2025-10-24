import java.util.ArrayList;

import processing.core.*;

//Train sound
//Music starts when space clicked
//Make collisions between chicken and cars/train
//Add lives and levels 
//Make game over screen appear for 2 seconds
//Add water (if chicken touches stick, its speed turns into the speed and coordinates of stick)
//Add eagle
//Add play again button 
//Add game over screen


public class App extends PApplet{
    
int scene = 1; //Welcome page
PImage photo; //image of chicken

//CHICKEN
    float xChicken = 400;  // starting x,y positions of chicken
    float yChicken = 550; 
    float speedChicken = 5; // Chicken speed/movemet
    boolean left, right, up, down; 

    int lives = 3;
    int level = 1;


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
    float trainWidth = 2000; //train dimensions
    float trainHeight = 50;

    float xTrain = -3000; //starting coordinates of train
    float yTrain = 160;

    float speedTrain = 10; 

    //Train time
    int lastTrainTime = 0;
    int trainWaitTime=10000; //10 seconds 

    //RedTrainLight
    int rTrain = 0;
    int gTrain = 0;
    int bTrain = 0;
    float trainWarningTime = 3000; //3 seconds before train comes

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

    // //For top road:
    // float carWaitTimeRightTop = random(500,2000);
    // int lastCarTimeRightTop = 0;

    // float carWaitTimeLeftTop = random(500,2000); 
    // int lastCarTimeLeftTop = 0;



    int startTime = 4000;

    boolean startText = false; 
    boolean GameOver = false; //whether or not chicken is dead (game over)


    public static void main(String[] args)  {
        PApplet.main("App");
    }

    public void setup(){
        startTime = millis();
        textSize(32);
        photo = loadImage("CrossyRoadCharacter3.png"); //Loads character image
    }

    public void settings(){
        size(800, 600);
    }

    public void draw(){

    if(scene==1){ //Welcome page
        background(200,225,230);
        
        if(startText==false){ //Starting text
            fill(1,50,34);
            rect(290,150,200,80);
            fill(255,255,255);
            text("Crossy Road", 305, 200);
   

            fill(0,0,0);
            text("Please click enter to start.",220,300);
            text("Use the arrow keys to move your character.", 100,340);
        }

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

// LEVEL AND LIVES TEXT:
        fill(0,0,0);
        text("Lives = " + lives, 660, 540);
        text("Level = " + level, 660, 580);


//CHICKEN:
        imageMode(CENTER); // centers image at x,y
        image(photo, xChicken, yChicken, 45, 50); // width and height can be adjusted

        if(yChicken<=0){ //moves chicken back to start after reaching top
            xChicken=400;
            yChicken=500;
        }

    //Smooth movement of chicken:
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

        if(yChicken>=600 || xChicken<=0 || xChicken>=800){ //if chicken touches edge
            GameOver=true;
            xChicken = 400;  // starting x position of chicken
            yChicken = 550;
            scene=3;
        }

    //Calls methods of cars from left and right (x,y,speed) - for bottom road
        carsDriveByLeft(-30,380,2, bottomLeftTimer,carWaitTimeBottomLeft);
        carsDriveByRight(830, 440, 2, bottomRightTimer, carWaitTimeBottomRight);

        //top road cars
        carsDriveByLeft(-30,20,3, topLeftTimer, carWaitTimeTopLeft);
        carsDriveByRight(830,80,3, topRightTimer, carWaitTimeTopRight);

    

 //TRAIN movement: 
if((millis()-lastTrainTime)>(trainWaitTime-3000) && (millis()-lastTrainTime)<(trainWaitTime+3000)){ //If train is 3 seconds away, light turns red
        rTrain=200;
        gTrain=15;
        bTrain=15;
} else{
        rTrain=0;
        gTrain=0;
        bTrain=0;
}

if((millis()-lastTrainTime)>=trainWaitTime){
    xTrain += speedTrain;
    fill(165, 45, 45);
    rect(xTrain, yTrain, trainWidth, trainHeight);
}
    xTrain = -2000; // reset off-screen
    lastTrainTime = millis(); // reset timer
}

else if(scene==3){
    background(255,15,15);
    text("Game over.", 310, 300);

}
}

    public void keyPressed(){
        if (keyCode == ENTER){
            startText=true;
            scene++;
        }
        
        if(startText==true){
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
}

    public void keyReleased(){
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

//METHOD ON CARS DRIVING BY FROM LEFT:
    public void carsDriveByLeft(float x, float y, float speed, int lastCarTimeLeft[], float carWaitTimeLeft[]){
        //***Left Cars (list)
        if((millis()-lastCarTimeLeft[0])>carWaitTimeTopLeft[0]){
//ChatGPT-->
            int c = color(random(255), random(255), random(255));
            
            xListLeft.add(x); //Adds each new x, y, speed, and c to list (creates a new car)
            yListLeft.add(y);
            speedListLeft.add(speed);
            colorListLeft.add(c);

            lastCarTimeLeft[0]=millis();
            carWaitTimeTopLeft[0] = random(500, 1500);  // A new car drives by 0.5-2 seconds

            
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

}
        
    
