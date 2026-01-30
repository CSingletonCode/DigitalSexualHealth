import javafx.application.Application;
import javafx.scene.Scene;
import javafx.geometry.Pos;
// import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

public class HelloFX extends Application {

    private static final double ASPECT_RATIO = 9.0/16.0;

    @Override
    public void start(Stage stage) {
        //Defines screen region + background colour
        Region appContent = new Region();
        appContent.setStyle("-fx-background-color: white;");
        //Defines window, background colour, and positioning
        StackPane root = new StackPane(appContent);
        root.setStyle("-fx-background-color: #333333;");
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 360, 640);

        //Used to ensure the screen region maintains a consistent aspect ratio of 9:16
        stage.widthProperty().addListener((obs,oldVal,newVal) -> resizeContent(appContent,scene));
        
        stage.heightProperty().addListener((obs,oldVal,newVal) -> resizeContent(appContent,scene));

        stage.setTitle("MyHealth");
        stage.setScene(scene);
        stage.show();

        resizeContent(appContent, scene);
        
    }

    private void resizeContent(Region content, Scene scene){
        double width = scene.getWidth();
        double height = scene.getHeight();
        //if the width is too large, change it to 9:16
        if (width / height > ASPECT_RATIO){
            content.setPrefHeight(height);
            content.setPrefWidth(height*ASPECT_RATIO);
        } else { //vice versa with a larger height
            content.setPrefWidth(width);
            content.setPrefHeight(width/ASPECT_RATIO);
        }
        //prevents out-of-bounds
        content.setMaxWidth(content.getPrefWidth());
        content.setMaxHeight(content.getPrefHeight());
    }

    public static void main(String[] args) {
        launch();
    }

}