import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by Dylan on 4/24/2017.
 */
public class SimModel extends Canvas {

    //  height and width of game board
    private int bWidth;
    private int bHeight;

    //  represents game board (true = alive; false = dead)
    private boolean board[][];

    //  animates simulation
    private Timeline timer;
    private boolean isRunning;

    //  stores whether or not grid will be displayed when drawn
    private boolean showGrid;

    //  rule set of the simulation
    private String ruleSet;

    /**
     * Creates model of simulation to user specifications
     * @param width - Width of cell matrix in simulation
     * @param height - Height of cell matrix in simulation
     * @param ruleSet - Set of rules sim runs on (ex "3/4" means born 3, survive 4, die otherwise)
     */
    public SimModel(int width,int height,String ruleSet) {

        //  set the rule set
        this.ruleSet = ruleSet;

        //  start the grid being displayed
        this.showGrid = true;

        //  set the width and height of simulation
        this.bWidth = width;
        this.bHeight = height;

        //  set up matrix to store cell states
        this.board = new boolean[width][height];
        for(int i=0;i<width;i++){
            for(int a=0;a<height;a++){
                this.board[i][a] = false;
            }
        }

        // Redraw canvas when size changes.
        this.widthProperty().addListener(evt -> draw());
        this.heightProperty().addListener(evt -> draw());

        //  animation timer that draw the simulation at given interval
        this.timer = new Timeline(new KeyFrame(Duration.millis(100),e -> {evolve();draw();}));
        this.timer.setCycleCount(Animation.INDEFINITE);
        this.isRunning = false;

        //  allow user to user mouse to toggle cells
        /*this.setOnMouseClicked(e->{
            int x = (int)(e.getX()/(getWidth()/bWidth));
            int y = (int)(e.getY()/(getHeight()/bHeight));
            try{board[x][y] = !board[x][y];}
            catch(Exception ex){return;}
            draw();
        });*/

        this.setOnMouseDragged(e->{

            int x = (int)(e.getX()/(getWidth()/bWidth));
            int y = (int)(e.getY()/(getHeight()/bHeight));

            try{
                board[x][y] = e.getButton().equals(MouseButton.PRIMARY);//!board[x][y];
            }
            catch(Exception ex){return;}
            draw();
        });
    }

    /**
     * Kills all tiles in simulation then updates screen
     */
    public void clear(){
        for(int i=0;i<this.bWidth;i++){
            for(int a=0;a<this.bHeight;a++){
                this.board[i][a] = false;
            }
        }
        this.draw();
    }

    /**
     * Generates random initial state of game board
     * @param chance - percent change of cell being alive at start (example: .3 = 1/3 chance)
     */
    public void randomPop(double chance){
        this.clear();
        for(int i=0;i<this.bWidth;i++){
            for(int a=0;a<this.bHeight;a++){
                if(chance>Math.random()){
                    this.board[i][a] = true;
                }
            }
        }
        this.draw();
    }

    /**
     * Starts and ends animation
     */
    public void toggleEvolution(){
        if(this.isRunning){
            this.timer.stop();
        }else this.timer.play();
        this.isRunning = !this.isRunning;
    }

    /**
     * Puts the board into the next state based of the rules of life
     *  1.  Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
     *  2.  Any live cell with two or three live neighbours lives on to the next generation.
     *  3.  Any live cell with more than three live neighbours dies, as if by overpopulation.
     *  4.  Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
     */
    public void evolve(){

        //  generate duplicate of board
        boolean[][] temp = new boolean[this.bWidth][this.bHeight];
        for(int i=0;i<this.bWidth;i++){
            for(int a=0;a<this.bHeight;a++){
                temp[i][a] = this.board[i][a];
            }
        }

        //  loop through every cell on board
        for(int i=0;i<this.bWidth;i++){
            for(int a=0;a<this.bHeight;a++){

                //  count cell neighbors
                int neighborCount = 0;
                for(int b=0;b<3;b++) {
                    for (int c = 0; c < 3; c++) {
                        if (c == 1 && b == 1)continue;
                        try {
                            if (temp[i - 1 + b][a - 1 + c]) {
                                neighborCount++;
                            }
                        }catch (Exception e){}
                    }
                }

                String born = this.ruleSet.substring(0,this.ruleSet.indexOf('/'));
                String live = this.ruleSet.substring(born.length());

                if(born.indexOf('0'+neighborCount)!=-1){
                    this.board[i][a] = true;
                }
                else if(live.indexOf('0'+neighborCount)==-1){
                    this.board[i][a] = false;
                }

                /*
                //  if cell has less than 2 neighbors or more than 3, it dies of loneliness
                if(neighborCount<2||neighborCount>3){
                    this.board[i][a] = false;
                }

                //  if cell has exactly 3 neighbors it becomes alive
                else if(neighborCount==3)this.board[i][a] = true;*/
            }
        }

    }
    /**
     * Toggles whether or not grid will be displayed
     */
    public void toggleGrid(){
        this.showGrid = !this.showGrid;
        this.draw();
    }

    /**
     * draws the cell matrix on the canvas
     */
    public void draw() {

        //  find width and height of pane
        double width = getWidth();
        double height = getHeight();

        //  create buffer to draw on
        Canvas buffer = new Canvas(this.getWidth(),this.getHeight());
        GraphicsContext pen = buffer.getGraphicsContext2D();

        //  clear buffer
        pen.setFill(Color.WHITE);
        pen.fillRect(0,0,width,height);

        //  get height and width of individual cell
        double sqWidth = (width/this.bWidth);
        double sqHeight = (height/this.bHeight);

        //  draw grid and live cells
        for(int i=0;i<this.bWidth;i++){
            for(int a=0;a<this.bHeight;a++){

                //  draw black outline around cell
                if(this.showGrid){
                    pen.setFill(Color.BLACK);
                    pen.strokeRect(i*sqWidth,a*sqHeight,sqWidth,sqHeight);
                }

                //  if cell is alive color it
                if(this.board[i][a]){
                    pen.setFill(Color.BLUE);
                    pen.fillRect(i*sqWidth,a*sqHeight,sqWidth,sqHeight);
                }
            }
        }

        //  write screen buffer to screen (stops lag caused by fast animation)
        GraphicsContext g = this.getGraphicsContext2D();
        g.drawImage(buffer.snapshot(null,null),0,0);
    }

    /**
     * Sets the speed of the simulation
     * @param delay - number between 1-100, (higher number = faster simulation)
     */
    public void setDelay(double delay){
        this.timer.setRate(delay/50);
    }
    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    /**
     * spins the simulation 360 degrees, cause its kewl
     */
    public void flip(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),e -> {this.setRotate(this.getRotate()+20);draw();}));
        timeline.setCycleCount(9);
        timeline.play();
    }

    public void killSim(){
        this.timer.stop();
    }
}