import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;


/**
 * Created by Dylan on 4/25/2017.
 */
public class SimController extends FlowPane {

    //  controls that deal with speed and start of simulation
    private Button evolve;
    private boolean toggle;
    private Slider evolveSlider;

    //  controls that deal with initial state of simulation
    private Button random;
    private Slider randomSlider;

    //  kills all cell in simulation
    private Button clear;

    //  toggles whether or not grid is displayed
    private Button toggleGrid;

    //  spin the grid (yeah I know......, but its kewl)
    private Button flip;

    /**
     * Sets up simulation controller
     * @param simModel - simulation model this controller will be controlling
     */
    public SimController(SimModel simModel){

        //  misc stuff about look of pane
        this.setPadding(new Insets(10,0,10,0));
        this.setAlignment(Pos.CENTER);
        this.setHgap(50);

        //  set style sheet of simulation (cant go external for some reason)
        this.setStyle("-fx-background-color: \n" +
                "        #c3c4c4,\n" +
                "        linear-gradient(#d6d6d6 50%, white 100%),\n" +
                "        radial-gradient(center 50% -40%, radius 200%, #e6e6e6 45%, rgba(230,230,230,0) 50%);"
        );

        //  set up button to randomise starting position of simulation
        this.random = new Button("Random");
        this.random.addEventHandler(ActionEvent.ACTION,e->{
            simModel.randomPop(this.randomSlider.getValue()/100);
        });

        //  sets the chance a give cell has of being set during randomisation
        this.randomSlider = new Slider();
        this.randomSlider.setValue(30);

        //  add random slider and random button to pane (stacked on top of each other)
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(this.random,this.randomSlider);
        this.getChildren().add(vBox);

        //  set up slider to vary speed of simulation
        this.evolveSlider = new Slider();
        this.evolveSlider.setValue(50);
        this.evolveSlider.setOnMouseDragged(e->simModel.setDelay(this.evolveSlider.getValue()));

        //  set up button that toggles evolution of simulation
        this.toggle = false;
        this.evolve = new Button("Evolve");
        this.evolve.addEventHandler(ActionEvent.ACTION,e->{
            simModel.toggleEvolution();
            if(!this.toggle)evolve.setId("evolve");
            else evolve.setId(null);
            this.toggle = !this.toggle;
        });

        //  add evolution slider and evolution button to pane (stacked on top of each other)
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(this.evolve,this.evolveSlider);
        this.getChildren().addAll(vBox);

        //  set up button that kills every cell in simulation
        this.clear = new Button("Clear");
        this.clear.addEventHandler(ActionEvent.ACTION,e->simModel.clear());
        this.getChildren().add(this.clear);

        //  set up button that toggles the displaying of the grid
        this.toggleGrid = new Button("Toggle Grid Lines");
        this.toggleGrid.addEventHandler(ActionEvent.ACTION,e->simModel.toggleGrid());
        this.getChildren().add(this.toggleGrid);

        //  set up button that spins the simulation all kewl like
        this.flip = new Button("Flip");
        this.flip.addEventHandler(ActionEvent.ACTION,e->simModel.flip());
        this.getChildren().add(this.flip);
    }
}
