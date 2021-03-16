import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Created by Dylan on 4/24/2017.
 */
public class Simulation extends BorderPane{

    //  represents the simulation, also handle drawing itself
    private SimModel simModel;

    //  controls various aspects of simulation (speed,starting state,ext..)
    private SimController simController;

    /**
     * Creates new Simulation
     * @param width - Width of cell matrix in simulation
     * @param height - Height of cell matrix in simulation
     * @param ruleSet - Set of rules sim runs on (ex "3/4" means born 3, survive 4, die otherwise)
     */
    public Simulation(int width,int height,String ruleSet){

        //  create model of simulation
        this.simModel = new SimModel(width,height,ruleSet);

        //  bind canvas size to size of the window (so it can resize appropriately)
        Pane pane = new Pane(this.simModel);
        this.simModel.widthProperty().bind(pane.widthProperty());
        this.simModel.heightProperty().bind(pane.heightProperty());

        //  create controler for the simulation
        this.simController = new SimController(this.simModel);

        //  add components to pane
        this.setCenter(pane);
        this.setTop(simController);
    }

    public void killSim(){
        this.simModel.killSim();
    }
}
