package ca.georgiancollege.comp1011m2022ice6;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Vector2DCanvasController implements Initializable
{
    // class variables(private data members)
    private Vector2D start = new Vector2D();
    private Vector2D end = new Vector2D();
    private Color color = Color.BLACK;
    private float line_width = 1.0f;

    @FXML
    private Canvas canvas;
    @FXML
    private ComboBox<Vector2D> endVectorComboBox;

    @FXML
    private Spinner<Double> lineWidthSpinner;

    @FXML
    private ColorPicker shapeColorPicker;

    @FXML
    private ComboBox<Vector2D> startVectorComboBox;

    @FXML
    void clearCanvasButtonClicked(ActionEvent event)
    {

    }

    @FXML
    void drawCircleButtonClicked(ActionEvent event) {

    }

    @FXML
    void drawLineButtonClicked(ActionEvent event)
    {
        GraphicsContext context = canvas.getGraphicsContext2D();
        line_width = lineWidthSpinner.getValue().floatValue();
        Utility.Instance().DrawLine(context, color, line_width, start, end);

    }

    @FXML
    void drawRectangleButtonClicked(ActionEvent event)
    {
        GraphicsContext context = canvas.getGraphicsContext2D();
        Utility.Instance().DrawRect(context, color, start, end);
    }
    @FXML
    void shapeColorPickedChanged(ActionEvent event)
    {
        color = shapeColorPicker.getValue();
    }

    @FXML
    void endVector2DComboBoxChanged(ActionEvent event) {
        end = endVectorComboBox.getSelectionModel().getSelectedItem();
    }

    @FXML
    void startVector2DComboBoxChanged(ActionEvent event) {
        start = startVectorComboBox.getSelectionModel().getSelectedItem();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // get the Vector2D data from the Database
        ArrayList<Vector2D> vectors = DBManager.Instance().readVectorTable();
        //display the vector2D data in each combo-box
        for(var vector: vectors)
        {
            startVectorComboBox.getItems().add(vector);
            endVectorComboBox.getItems().add(vector);
        }

        // configured the Line Width Spinner
        Utility.Instance().ConfigureVector2DSpinner(lineWidthSpinner, 0.25, 10.0f, 1.0f, 0.25f);

    }
}
