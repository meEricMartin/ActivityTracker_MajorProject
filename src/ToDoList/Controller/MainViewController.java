package ToDoList.Controller;

import ToDoList.Model.MainModel;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable
{

    private final MainModel model;

    public MainViewController(MainModel model)
    {
        this.model = model;
    }

    public void UpdateFilterToggle(RadioButton source)
    {
        model.setChosenFilter(source);
        model.PerformFiltering();
    }

    public void UpdateIsCompleteCheckbox(Boolean source)
    {
        model.setIsCompleteOnly(source);
        model.PerformFiltering();
    }

    public void ExportToXML(File file, MainModel model)
    {
        model.ExportToXML(file, model);
    }

    public void ImportFromXML(File file, MainModel model)
    {
        model.ImportFromXML(file, model);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
}
