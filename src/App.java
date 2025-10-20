import processing.core.*;

//Make chicken movement more smooth
//Make wait time after train bell rings before train comes
//Train sound
//Chicken
//Make text appear - click space bar to start game (chicken can move and music starts)


public class App extends PApplet{
    float xChicken = 400;  // starting x position of chicken
    float yChicken = 500;  // starting y position of chicken
    float speedChicken = 15; // how much it moves each press
    float speedCar = 10; 

    float rectWidth = 90; //car dimensions
    float rectHeight = 50;

    float r = random(0,255); //car color
    float g = random(0,255);
    float b = random(0,255);

    float xCar = 10; //starting coordinates of car
    float yCar = 310;

    boolean GameOver = false; //whether or not chicken is dead (game over)

    // int waitTime = 2000; //2 seconds (ChatGPT)
    // int startTime;

    boolean spaceClicked = false; 

    public static void main(String[] args)  {
        PApplet.main("App");
    }

    public void setup(){
        // startTime = millis();
        // textSize(32);

    }

    public void settings(){
        size(800, 600);

    }

    public void draw(){
        background(10,150,100);

        
        fill(200,200,200); //First road
        stroke(0,0,0);
        strokeWeight(4);
        rect(-5,300,810,70);

        stroke(255,255,20); //First road lines
        line(-5,340,810,340);
        stroke(255,255,20); //First road lines
        line(-5,330,810,330);

        fill(200,200,200); //Second road
        stroke(0,0,0);
        strokeWeight(4);
        rect(-5,150,810,70);
        
        stroke(255,255,20); //Second road line
        line(-5,190,810,190);
        stroke(255,255,20); //Second road line
        line(-5,180,810,180);

        fill(255,245,215); //chicken
        stroke(0,0,0);
        strokeWeight(4);
        ellipse(xChicken,yChicken,55,55);

        while(spaceClicked==false){ //print text until space clicked
            text("Click the spacebar to start.", 210,270);
        }

       

        // check edges and reverse direction
        // if (x + rectWidth / 2 >= width || x - rectWidth / 2 <= 0) {
        //     speedChicken *= -1;  // flip direction
        // }
        if(yChicken==0){ //moves chicken back to start after reaching top
            xChicken=400;
            yChicken=500;
        }

        // while(GameOver==false){ //cars continue to come
        //     fill(r,b,g);
        //     rect(xCar, yCar, rectWidth, rectHeight);
        //     xCar += speedCar; //moves car to the right
        //     millis(); //wait two seconds (ChatGPT)            
        // }
        // if (millis() - startTime < waitTime) {
        //     fill(0);
        //     text("Waiting...", 150, 200);
        // } else {
        //     fill(0);
        //     text("Done waiting!", 150, 200);
        // }

    }
    public void keyPressed(){
        if (keyCode == UP) {
            yChicken -= speedChicken;   // move up chicken
    }

}
    public void mousePressed(){
        if(mousePressed){
            spaceClicked = true;
        }
    }

}