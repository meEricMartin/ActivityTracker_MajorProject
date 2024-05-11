/**
 * Sample Skeleton for 'TaskView.fxml' Controller Class
 */

package ToDoList.Controller;

import ToDoList.Model.TaskModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskViewController
{
    private final TaskModel model;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="DatePicker"
    private DatePicker DatePicker; // Value injected by FXMLLoader
    @FXML // fx:id="TitleLabel"
    private TextField TitleLabel; // Value injected by FXMLLoader
    @FXML // fx:id="CompletenessSlider"
    private Slider CompletenessSlider; // Value injected by FXMLLoader
    @FXML // fx:id="DescriptionTextBox"
    private TextArea DescriptionTextBox; // Value injected by FXMLLoader
    @FXML // fx:id="AddSaveButton"
    private Button AddSaveButton; // Value injected by FXMLLoader
    @FXML // fx:id="CancelButton"
    private Button CancelButton; // Value injected by FXMLLoader

    public TaskViewController(TaskModel model)
    {
        this.model = model;
    }

    public String getAddSaveButtonText()
    {
        return AddSaveButton.getText();
    }

    public void setAddSaveButtonText(String source)
    {
        AddSaveButton.setText(source);
    }

    @FXML
    void handleAddSaveButtonClick(ActionEvent event)
    {
        model.setTitle(TitleLabel.getText());
        model.setCompleteness((int) Math.round(CompletenessSlider.getValue()));
        model.setDueDate(DatePicker.getValue());
        model.setDescription(DescriptionTextBox.getText());
        // dirty way to automatically set checkbox. Instead controller shall detect slider set broadcast through listener. But nobody will notice this.... i hope
        if ((int) Math.round(CompletenessSlider.getValue()) == 100) model.setIsChecked(true);
        else model.setIsChecked(false);

        Stage stage = (Stage) AddSaveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCancelButtonClick(ActionEvent event)
    {
        Stage stage = (Stage) AddSaveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize()
    {
        DatePicker.setValue(model.getDueDate());
        DescriptionTextBox.setText(model.getDescription());
        TitleLabel.setText(model.getTitle());
        CompletenessSlider.setValue(model.getCompleteness());

    }

    public TaskModel getContent()
    {
        return model;
    }
}
