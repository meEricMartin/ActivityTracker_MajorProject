package ToDoList.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

@XmlRootElement(name = "TasksList")
public class MainModel
{

    private SimpleObjectProperty<Toggle> chosenFilter;

    private SimpleStringProperty chosenFilterAsString;

    private SimpleBooleanProperty isCompleteOnly;

    private FilteredList<TaskModel> filteredTasksList;
    private ObservableList<TaskModel> tasksList = FXCollections.observableArrayList(
            new TaskModel("Example Task1", LocalDate.now().plusDays(1), 30, "Finish This Week"),
            new TaskModel("Example Task2", LocalDate.now(), 30, " Finish Today"),
            new TaskModel("Example Task3", LocalDate.now().minusMonths(1), 50, "Overdue!"));

    public MainModel()
    {
        chosenFilter = new SimpleObjectProperty<Toggle>();
        isCompleteOnly = new SimpleBooleanProperty();
        filteredTasksList = new FilteredList<>(tasksList);
        chosenFilterAsString = new SimpleStringProperty("All");
        isCompleteOnly = new SimpleBooleanProperty();
        isCompleteOnlyProperty().set(false);
    }

    @XmlTransient
    public FilteredList<TaskModel> getFilteredTasksList()
    {
        return filteredTasksList;
    }

    public void setFilteredTasksList(FilteredList<TaskModel> source)
    {
        this.filteredTasksList = source;
    }

    @XmlElements(@XmlElement(name = "Task"))
    public ObservableList<TaskModel> getTasksList()
    {
        return tasksList;
    }

    public SimpleStringProperty chosenFilterAsStringProperty()
    {
        return chosenFilterAsString;
    }

    public String getChosenFilterAsString()
    {
        return chosenFilterAsString.get();
    }

    public void setChosenFilterAsString(String chosenFilterAsString)
    {
        this.chosenFilterAsString.set(chosenFilterAsString);
    }

    public Toggle getChosenFilter()
    {
        return this.chosenFilter.get();
    }

    public void setChosenFilter(RadioButton source)
    {
        this.chosenFilter.set(source);
        this.setChosenFilterAsString(source.getText());
    }

    public SimpleObjectProperty<Toggle> chosenFilterProperty()
    {
        return chosenFilter;
    }

    public Boolean getIsCompleteOnly()
    {
        return this.isCompleteOnly.get();
    }

    public void setIsCompleteOnly(Boolean source)
    {
        this.isCompleteOnly.set(source);
    }

    public SimpleBooleanProperty isCompleteOnlyProperty()
    {
        return isCompleteOnly;
    }

    public void ExportToXML(File file, MainModel model)
    {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(MainModel.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(model, file);

        } catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }


    public void ImportFromXML(File file, MainModel model)
    {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(MainModel.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            tasksList.clear();
            MainModel newModel = (MainModel) jaxbUnmarshaller.unmarshal(file);
            model.tasksList.addAll(newModel.getTasksList());
            newModel.finalize();
        } catch (JAXBException e)
        {
            e.printStackTrace();
        } catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void PerformFiltering()
    {
        LocalDate dateToday = LocalDate.now();
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = dateToday.get(woy);


        switch (getChosenFilterAsString())
        {
            case "Overdue":
                filteredTasksList.setPredicate(task -> {
                    return (task.getDueDate().isBefore(dateToday)
                            && (task.getIsChecked() || isCompleteOnlyProperty().getValue()));
                });
                break;
            case "Today":
                filteredTasksList.setPredicate(task -> {
                    return (task.getDueDate().isEqual(dateToday)
                            && (task.getIsChecked() || isCompleteOnlyProperty().getValue()));
                });
                break;
            case "This week":
                filteredTasksList.setPredicate(task -> {
                    return ((task.getDueDate().get(woy) == dateToday.get(woy))
                            && (task.getIsChecked() || isCompleteOnlyProperty().getValue()));
                });
                break;
            default:
                filteredTasksList.setPredicate(task -> {
                    return (task.getIsChecked() || isCompleteOnlyProperty().getValue());
                });
                break;
        }


    }

}