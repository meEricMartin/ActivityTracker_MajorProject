package ToDoList.Resources.CustomControls;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Places content in a bordered pane with a title.
 */
public final class BorderedTitledBox extends StackPane
{
    private StackPane _stackPane;

    public BorderedTitledBox(String titleString, Node content)
    {
        Label title = new Label(" " + titleString + " ");
        title.getStyleClass().add("bordered-titled-box-title");
        StackPane.setAlignment(title, Pos.TOP_LEFT);

        StackPane contentPane = new StackPane();
        content.getStyleClass().add("bordered-titled-box-content");
        contentPane.getChildren().add(content);

        getStyleClass().add("bordered-titled-box-border");
        getChildren().addAll(title, contentPane);
    }
}
