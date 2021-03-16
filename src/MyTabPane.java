import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Created by Dylan on 4/24/2017.
 */
public class MyTabPane extends TabPane{

    /**
     * Adds a simulation conways game of life on start
     */
    public MyTabPane() {

        this.addNewSim("Conway's Game Of Life",50,50,"3/2");
    }

    /**
     * Adds a new simulation the the tabpane based on user specified specification
     * @param name - Name to appear on top of tab
     * @param width - Width of cell matrix in simulation
     * @param height - Height of cell matrix in simulation
     * @param ruleSet - Set of rules sim runs on (ex "3/4" means born 3, survive 4, die otherwise)
     */
    public void addNewSim(String name,int width,int height,String ruleSet){

        Tab tab = new Tab();
        tab.setText(name);
        Simulation sim = new Simulation(width,height,ruleSet);
        tab.setContent(sim);
        tab.setOnCloseRequest(e->sim.killSim());
        this.getTabs().add(tab);
    }
}