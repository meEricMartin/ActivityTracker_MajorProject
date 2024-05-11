package ToDoList.View;


import ToDoList.Controller.MainViewController;
import ToDoList.Controller.TaskViewController;
import ToDoList.Model.MainModel;
import ToDoList.Model.TaskModel;
import ToDoList.Resources.CustomControls.BorderedTitledBox;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class MainView
{
    Menu menu;
    private BorderPane view;
    private MenuBar menuBar;
    private MenuItem loadCommand;
    private MenuItem saveCommand;
    private MenuItem exitCommand;

    private VBox topVBox;
    private BorderedTitledBox filtersBorderedTitledBox;
    private ToggleGroup filterToggleGroup;
    private HBox filtersHBox;
    private RadioButton allEventsRadioButton;
    private RadioButton overdueEventsRadioButton;
    private RadioButton todayEventsRadioButton;
    private RadioButton thisWeekEventsRadioButton;
    private CheckBox isCompleteEventCheckBox;
    private Region filler;

    private TableView tasksTableView;
    private TableColumn<TaskModel, LocalDate> dueDateTableColumn;
    private TableColumn<TaskModel, String> titleTableColumn;
    private TableColumn<TaskModel, String> completenessTableColumn;
    private TableColumn<TaskModel, String> descriptionTableColumn;
    private TableColumn<TaskModel, Boolean> checkboxTableColumn;
    private CheckBox allTasksCheckBox;

    private HBox buttonBox;
    private Button addTask;


    // deploy corresponding model
    private MainModel model;
    private MainViewController controller;

    public MainView(MainModel model, MainViewController controller)
    {
        // tie given model and controller to internal model and controller for full deployment
        this.model = model;
        this.controller = controller;


        this.CreateAndConfigureTop();
        this.CreateAndConfigureCenter();
        this.CreateAndConfigureBottom();
        this.CreateAndConfigurePane();
        this.UpdateControllerFromListeners();
        this.ObserveModelAndUpdateControls();
    }

    private void CreateAndConfigureCenter()
    {
        tasksTableView = new TableView();
        tasksTableView.setEditable(false);
        tasksTableView.setItems(model.getFilteredTasksList());

        tasksTableView.setRowFactory(tv -> {
            TableRow<TaskModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                {
                    TaskModel rowData = row.getItem();

                    try
                    {
                        // Load content overview.
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/ToDoList/View/TaskView.fxml"));
                        TaskViewController taskController = new TaskViewController(rowData);
                        loader.setController(taskController);
                        // Set content into the center of root layout.
                        Parent root = loader.load();
                        Stage popupStage = new Stage();
                        //popupStage.initStyle(StageStyle.UNDECORATED);


                        popupStage.setTitle("Edit Appoinment");
                        popupStage.setScene(new Scene(root));
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.initOwner(buttonBox.getScene().getWindow());
                        taskController.setAddSaveButtonText("Save");
                        popupStage.showAndWait();
                        tasksTableView.refresh();

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }


                }
            });
            return row;
        });

        allTasksCheckBox = new CheckBox();

        checkboxTableColumn = new TableColumn();
        dueDateTableColumn = new TableColumn("Due Date");
        titleTableColumn = new TableColumn("Title");
        completenessTableColumn = new TableColumn("% Complete");
        descriptionTableColumn = new TableColumn("Description");
        checkboxTableColumn = new TableColumn();

        checkboxTableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxTableColumn));
        checkboxTableColumn.setGraphic(allTasksCheckBox);


        tasksTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        checkboxTableColumn.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        dueDateTableColumn.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        titleTableColumn.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        completenessTableColumn.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        descriptionTableColumn.setMaxWidth(1f * Integer.MAX_VALUE * 40);

        checkboxTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, Boolean>("isChecked"));
        dueDateTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, LocalDate>("dueDate"));
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("title"));
        completenessTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("completeness"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("description"));
        completenessTableColumn.setCellValueFactory(elem -> new SimpleStringProperty(elem.getValue().getCompletenessAsString() + " %"));
        // aligning values of column to center
        completenessTableColumn.getStyleClass().add("center-align");
        titleTableColumn.getStyleClass().add("center-align");
        dueDateTableColumn.getStyleClass().add("center-align");

        //tasksTableView.setItems(data);
        tasksTableView.getColumns().addAll(checkboxTableColumn, dueDateTableColumn, titleTableColumn, completenessTableColumn, descriptionTableColumn);

    }

    private void CreateAndConfigureBottom()
    {
        buttonBox = new HBox();
        addTask = new Button("Add new task");
        buttonBox.getStyleClass().add("center-align");
        // add styling
        addTask.setMinHeight(50);
        buttonBox.getChildren().add(addTask);

        addTask.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                try
                {
                    // Load content overview.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/ToDoList/View/TaskView.fxml"));
                    TaskModel taskModel = new TaskModel();
                    TaskViewController taskController = new TaskViewController(taskModel);
                    loader.setController(taskController);
                    // Set content into the center of root layout.
                    Parent root = loader.load();
                    Stage popupStage = new Stage();

                    popupStage.setTitle("Add Appoinment");
                    popupStage.setScene(new Scene(root));
                    popupStage.initModality(Modality.APPLICATION_MODAL);
                    popupStage.initOwner(buttonBox.getScene().getWindow());
                    taskController.setAddSaveButtonText("Add");
                    popupStage.showAndWait();

                    model.getTasksList().add(taskModel);


                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        });
    }

    private void CreateAndConfigureTop()
    {
        topVBox = new VBox();
        menuBar = new MenuBar();
        menu = new Menu("File");
        loadCommand = new MenuItem("Load", new ImageView("ToDoList/Resources/icons/load.png"));
        saveCommand = new MenuItem("Save", new ImageView("ToDoList/Resources/icons/save.png"));
        exitCommand = new MenuItem("Exit", new ImageView("ToDoList/Resources/icons/shutdown.png"));
        allEventsRadioButton = new RadioButton("All");
        overdueEventsRadioButton = new RadioButton("Overdue");
        todayEventsRadioButton = new RadioButton("Today");
        thisWeekEventsRadioButton = new RadioButton("This week");
        isCompleteEventCheckBox = new CheckBox("Not completed");
        filterToggleGroup = new ToggleGroup();
        filler = new Region();
        filtersHBox = new HBox(30);


        exitCommand.setOnAction(e ->
        {
            Platform.exit();
            System.exit(0);
        });

        menu.getItems().addAll(loadCommand, saveCommand, exitCommand);
        menuBar.getMenus().addAll(menu);

        allEventsRadioButton.setToggleGroup(filterToggleGroup);
        overdueEventsRadioButton.setToggleGroup(filterToggleGroup);
        todayEventsRadioButton.setToggleGroup(filterToggleGroup);
        thisWeekEventsRadioButton.setToggleGroup(filterToggleGroup);
        allEventsRadioButton.setSelected(true);
        allEventsRadioButton.requestFocus();

        HBox.setHgrow(filler, Priority.ALWAYS);
        filtersHBox.getChildren().addAll(allEventsRadioButton, overdueEventsRadioButton, todayEventsRadioButton, thisWeekEventsRadioButton, filler, isCompleteEventCheckBox);
        filtersBorderedTitledBox = new BorderedTitledBox("Filter", filtersHBox);
        topVBox.getChildren().addAll(menuBar, filtersBorderedTitledBox);

        saveCommand.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Set a destination for save");
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extensionFilter);
                File file = fileChooser.showSaveDialog(view.getScene().getWindow());
                if (file != null)
                {
                    controller.ExportToXML(file, model);
                }
            }
        });
        loadCommand.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open a file to load");
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extensionFilter);
                File file = fileChooser.showOpenDialog(view.getScene().getWindow());
                if (file != null)
                {
                    controller.ImportFromXML(file, model);
                }
            }
        });

    }

    private void CreateAndConfigurePane()
    {
        view = new BorderPane();
        view.setTop(topVBox);
        view.setCenter(tasksTableView);
        view.setBottom(buttonBox);
    }

    private void UpdateControllerFromListeners()
    {
        filterToggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) ->
        {
            controller.UpdateFilterToggle((RadioButton) filterToggleGroup.getSelectedToggle());
            checkboxTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, Boolean>("isChecked"));
            dueDateTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, LocalDate>("dueDate"));
            titleTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("title"));
            completenessTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("completenessAsString"));
            descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("description"));
            completenessTableColumn.setCellValueFactory(elem -> new SimpleStringProperty(elem.getValue().getCompletenessAsString() + " %"));

        });
        isCompleteEventCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) ->
        {
            isCompleteEventCheckBox.setSelected(newValue);
            controller.UpdateIsCompleteCheckbox(isCompleteEventCheckBox.selectedProperty().get());
            checkboxTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, Boolean>("isChecked"));
            dueDateTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, LocalDate>("dueDate"));
            titleTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("title"));
            completenessTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("completeness"));
            descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("description"));
            completenessTableColumn.setCellValueFactory(elem -> new SimpleStringProperty(elem.getValue().getCompletenessAsString() + " %"));

        });

    }

    private void ObserveModelAndUpdateControls()
    {
        isCompleteEventCheckBox.selectedProperty().setValue(true);
        model.chosenFilterProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) ->
                {
                    if (!filterToggleGroup.getSelectedToggle().equals(newValue))
                    {
                        filterToggleGroup.selectToggle(newValue);
                    }
                }
        );
        model.isCompleteOnlyProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) ->
        {
            isCompleteEventCheckBox.selectedProperty().set(newValue);
        });

    }


    public Parent getAsParent()
    {
        return view;
    }

}
