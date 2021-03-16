import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Created by Dylan on 4/24/2017.
 */
public class AutomationCreator extends BorderPane{

    //  creates adds a new simulation to TabPane based on data entered by user
    private Button createNew;

    //  Text that appears at top of animation creator
    private TextField simTitle;

    //  sets of check boxes, these are used to determine rule set of simulation
    private CheckBox[] survive;
    private CheckBox[] born;
    private CheckBox[] die;

    //  user enters height and width of simulation here
    private TextField widthField;
    private TextField heightField;

    //  displays error message if form filled out incorrectly
    private Label error;

    /**
     * Creates area where user can customise there own simulation
     * @param tabPane - tabpane of which to add the created simulations
     */
    public AutomationCreator(MyTabPane tabPane){

        //  sets the background image
        //this.setStyle("-fx-background-image: url(\"creatorBackground.jpg\");");

        //  create button to create simulation
        this.createNew = new Button("Create New Simulation");

        //  set up title of pane (displayed at the top)
        Text title = new Text("Rule Set");
        title.setId("title");

        //  set up error label
        this.error = new Label("Form Not Filled Out Correctly.... (You Fucking Noob!)");
        this.error.setId("Error");
        this.error.setVisible(false);

        //  create arrays of check boxes for selecting automation rules
        this.survive = new CheckBox[9];
        this.born = new CheckBox[9];
        this.die = new CheckBox[9];
        for(int i=0;i<9;i++){
            this.survive[i] = new CheckBox(""+i);
            this.born[i] = new CheckBox(""+i);
            this.die[i] = new CheckBox(""+i);
        }

        //  add checkboxes for survival of cells
        VBox vBox = new VBox();
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.getChildren().add(new Label("Survive:  "));
        for(int i=0;i<9;i++){flowPane.getChildren().add(this.survive[i]);}
        vBox.getChildren().add(flowPane);

        //  add check boxes for cells to be born
        flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.getChildren().add(new Label("Born:      "));
        for(int i=0;i<9;i++){flowPane.getChildren().add(this.born[i]);}
        vBox.getChildren().add(flowPane);

        //  add checkboxes for cells to die
        flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.getChildren().add(new Label("Die:        "));
        for(int i=0;i<9;i++){flowPane.getChildren().add(this.die[i]);}
        vBox.getChildren().add(flowPane);

        //  add boxes to get the desired height and width of sim
        flowPane = new FlowPane();
        this.heightField = new TextField();
        this.widthField = new TextField();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.getChildren().addAll(new Label("Height"),this.heightField,new Label("Width"),this.widthField);
        vBox.getChildren().add(flowPane);

        //  add text field so user can enter name for simulation
        this.simTitle = new TextField();
        flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.getChildren().addAll(new Label("Name:  "),this.simTitle);
        vBox.getChildren().add(flowPane);

        //  add error label to pane
        vBox.getChildren().add(this.error);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(40);

        //  set up button action
        this.setButtonAction(tabPane);

        //  set starting conditions of components
        this.setStartingConditions();

        //  add components to pane
        this.setCenter(vBox);
        this.setTop(title);
        this.setBottom(this.createNew);
        this.linkCheckBoxes();
        this.setAlignment(title,Pos.CENTER);
        this.setAlignment(this.createNew,Pos.CENTER);
        this.setPadding(new Insets(10,10,10,10));
    }

    /**
     * /  set starting conditions of components to be "Conway's game of life"
     */
    private void setStartingConditions(){

        //  set up check boxes to be in "game of life configuration"
        for(int i=0;i<9;i++){
            if(i==2)this.survive[i].setSelected(true);
            else if(i==3)this.born[i].setSelected(true);
            else this.die[i].setSelected(true);
        }

        //  set up height and width boxes to say "50"
        this.heightField.setText("50");
        this.widthField.setText("50");

        //  start the sim title to conway
        this.simTitle.setText("Conway's Game Of Life");
    }

    /**
     * Sets the createSim button to create simulation based off data entered
     * @param tabPane - tabPane of which to add new simulation to
     */
    private void setButtonAction(MyTabPane tabPane){
        this.createNew.addEventHandler(ActionEvent.ACTION,e->{
            String ruleSet = "";
            for(int i=0;i<this.born.length;i++){
                if(this.born[i].isSelected())ruleSet+=i;
            }ruleSet+="/";
            for(int i=0;i<this.survive.length;i++){
                if(this.survive[i].isSelected())ruleSet+=i;
            }
            int width,height;
            try {
                width = Integer.parseInt(this.widthField.getText());
                height = Integer.parseInt(this.heightField.getText());
            }catch(Exception ex){this.error.setVisible(true);return;}
            this.error.setVisible(false);
            tabPane.addNewSim(this.simTitle.getText(),width,height,ruleSet);
        });
    }

    /**
     * Make it so only one check box of each category can be clicked
     */
    private void linkCheckBoxes(){
        for(int i=0;i<9;i++){
            this.survive[i].selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    String s = observable.toString();
                    int index = Integer.parseInt(s.substring(s.indexOf('\'')+1,s.indexOf('\'')+2));
                    if(newValue){
                        survive[index].setSelected(true);
                        born[index].setSelected(false);
                        die[index].setSelected(false);
                    }
                    else if(!born[index].isSelected()&&!die[index].isSelected()){
                        survive[index].setSelected(true);
                    }
                }
            });

            this.born[i].selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    String s = observable.toString();

                    int index = Integer.parseInt(s.substring(s.indexOf('\'')+1,s.indexOf('\'')+2));
                    if(newValue){
                        survive[index].setSelected(false);
                        born[index].setSelected(true);
                        die[index].setSelected(false);
                    }
                    else if(!survive[index].isSelected()&&!die[index].isSelected()){
                        born[index].setSelected(true);
                    }
                }
            });

            this.die[i].selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    String s = observable.toString();

                    int index = Integer.parseInt(s.substring(s.indexOf('\'')+1,s.indexOf('\'')+2));
                    if(newValue){
                        survive[index].setSelected(false);
                        born[index].setSelected(false);
                        die[index].setSelected(true);
                    }
                    else if(!survive[index].isSelected()&&!born[index].isSelected()){
                        die[index].setSelected(true);
                    }
                }
            });
        }
    }
}
