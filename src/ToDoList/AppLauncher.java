package ToDoList;

import ToDoList.Controller.MainViewController;
import ToDoList.Model.MainModel;
import ToDoList.View.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppLauncher extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        MainModel model = new MainModel();
        MainViewController controller = new MainViewController(model);
        MainView view = new MainView(model, controller);

        Scene scene = new Scene(view.getAsParent(), 800, 600);
        // Setting up starting scene and showing it
        scene.getStylesheets().add("ToDoList/styling/TasksStyling.css");
        System.out.println(model.getChosenFilter());
        primaryStage.setScene(scene);
        primaryStage.setTitle("ToDo List");
        primaryStage.getIcons().add(new Image("ToDoList/Resources/icons/appIcon.png"));
        primaryStage.show();
    }
}
