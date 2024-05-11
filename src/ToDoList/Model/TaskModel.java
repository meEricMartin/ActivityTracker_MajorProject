package ToDoList.Model;

import ToDoList.Resources.FXUtils.DateConverter;
import ToDoList.Resources.FXUtils.LocalDateAdapter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement(name = "Task")
public class TaskModel
{
    private SimpleBooleanProperty isChecked;
    private SimpleStringProperty title;
    private SimpleStringProperty dueDateAsString;
    private SimpleObjectProperty<LocalDate> dueDate;
    private SimpleIntegerProperty completeness;
    private SimpleStringProperty description;

    public TaskModel(String title, LocalDate dueDate, Integer completeness, String description)
    {
        this.isChecked = new SimpleBooleanProperty(false);
        this.title = new SimpleStringProperty(title);
        this.dueDateAsString = new SimpleStringProperty(DateConverter.toString(dueDate));
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.completeness = new SimpleIntegerProperty(completeness);
        this.description = new SimpleStringProperty(description);
    }

    public TaskModel()
    {
        this.isChecked = new SimpleBooleanProperty(false);
        this.title = new SimpleStringProperty("");
        this.dueDate = new SimpleObjectProperty<LocalDate>(LocalDate.now());
        this.completeness = new SimpleIntegerProperty(0);
        this.description = new SimpleStringProperty("");
    }

    @XmlAttribute(name = "title")
    public String getTitle()
    {
        return title.get();
    }

    public void setTitle(String source)
    {
        title.set(source);
    }

    @XmlAttribute(name = "description")
    public String getDescription()
    {
        return description.get();
    }

    public void setDescription(String source)
    {
        description.set(source);
    }

    public String getDueDateAsString()
    {
        return dueDateAsString.get();
    }

    public void setDueDateAsString(LocalDate source)
    {
        this.dueDateAsString.set(DateConverter.toString(source));
    }

    @XmlAttribute(name = "isChecked")
    public Boolean getIsChecked()
    {
        return isChecked.get();
    }

    public void setIsChecked(Boolean value)
    {
        this.isChecked.set(value);
    }

    @XmlAttribute(name = "completeness")
    public Integer getCompleteness()
    {
        return completeness.get();
    }

    public void setCompleteness(Integer value)
    {
        completeness.set(value);
    }

    public String getCompletenessAsString()
    {
        return completeness.getValue().toString();
    }

    public SimpleBooleanProperty isCheckedProperty()
    {
        return isChecked;
    }

    public SimpleIntegerProperty completenessProperty()
    {
        return completeness;
    }

    public SimpleStringProperty dueDateAsStringProperty()
    {
        return dueDateAsString;
    }

    public SimpleStringProperty titleProperty()
    {
        return title;
    }

    public SimpleStringProperty descriptionProperty()
    {
        return description;
    }

    public SimpleObjectProperty<LocalDate> dueDateProperty()
    {
        return dueDate;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @XmlAttribute(name = "dueDate")
    public LocalDate getDueDate()
    {
        return dueDate.getValue();
    }

    public void setDueDate(LocalDate dueDate)
    {
        this.dueDate.set(dueDate);
    }
}
