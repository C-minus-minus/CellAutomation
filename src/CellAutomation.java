import javafx.animation.Animation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

/**
 * Created by Dylan on 4/24/2017.
 */
public class CellAutomation extends Application {

    public void start(Stage primaryStage){

        //  set up screen layout
        BorderPane layout = new BorderPane();
        MyTabPane myTabPane = new MyTabPane();
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(new AutomationCreator(myTabPane),myTabPane);
        layout.setCenter(splitPane);

        //layout.setLeft(new AutomationCreator(myTabPane));
        //layout.setCenter(myTabPane);


        //  set window to fill quarter of users screen when initialised
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(layout,screenSize.getWidth()/2,screenSize.getHeight()/2);

        //  set icon image
        primaryStage.getIcons().add(new javafx.scene.image.Image("file:res/icon.png"));

        //  set style sheet for gui
        File f = new File("res/filecss.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        //  set up and show window to user
        primaryStage.setTitle("Cellular Animation Generator");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(e->System.exit(0));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
