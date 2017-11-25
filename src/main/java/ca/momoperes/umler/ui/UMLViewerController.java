package ca.momoperes.umler.ui;

import ca.momoperes.umler.uml.UML;
import ca.momoperes.umler.uml.UMLConstructor;
import ca.momoperes.umler.uml.UMLField;
import ca.momoperes.umler.uml.UMLMethod;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

public class UMLViewerController {

    @FXML
    private Text classNameText;
    @FXML
    private VBox fieldsBox;
    @FXML
    private VBox constructorsBox;
    @FXML
    private VBox methodsBox;
    @FXML
    private Button export;

    public void init(Scene scene, UML uml) {
        classNameText.setText(uml.getClassName());

        if (uml.getFields().size() > 0) {
            for (UMLField field : uml.getFields()) {
                Text text = new Text(field.toString());
                text.setFont(new Font(14.0));
                if (field.isStatic()) {
                    text.setUnderline(true);
                }
                fieldsBox.getChildren().add(text);
            }
        } else {
            Text none = new Text("none");
            none.setStyle("-fx-font-style: italic;");
            none.setFont(new Font(14.0));
            fieldsBox.getChildren().add(none);
        }

        if (uml.getConstructors().size() > 0) {
            for (UMLConstructor constructor : uml.getConstructors()) {
                Text text = new Text(constructor.toString());
                text.setFont(new Font(14.0));
                constructorsBox.getChildren().add(text);
            }
        } else {
            Text none = new Text("none");
            none.setStyle("-fx-font-style: italic;");
            none.setFont(new Font(14.0));
            constructorsBox.getChildren().add(none);
        }

        if (uml.getMethods().size() > 0) {
            for (UMLMethod method : uml.getMethods()) {
                Text text = new Text(method.toString());
                text.setFont(new Font(14.0));
                if (method.isStatic()) {
                    text.setUnderline(true);
                }
                methodsBox.getChildren().add(text);
            }
        } else {
            Text none = new Text("none");
            none.setStyle("-fx-font-style: italic;");
            none.setFont(new Font(14.0));
            methodsBox.getChildren().add(none);
        }

        export.setOnAction(event -> {
            FileChooser dialog = new FileChooser();
            dialog.setTitle("UMLer - Export UML for " + uml.getClassName());
            dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Document", "*.docx"));
            dialog.setInitialFileName(uml.getClassName() + "_UML.docx");
            File file = dialog.showSaveDialog(scene.getWindow());
            if (file == null) return;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("UMLer Export");
            alert.setHeaderText(null);
            alert.setContentText("Please wait while the Word document is being created...");
            alert.show();
            try {
                UML.exportToWord(file, uml);
            } catch (Exception e) {
                alert.close();
                Alert alert_2 = new Alert(Alert.AlertType.ERROR);
                alert_2.setHeaderText("Something wrong happened when trying to generate that Word Document.");
                alert_2.setContentText(e.getMessage());
                alert_2.show();
                return;
            }
            alert.close();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("UMLer Export");
            alert.setHeaderText(null);
            alert.setContentText("UML for '" + uml.getClassName() + "' was exported to " + file.getName());
            alert.showAndWait();
        });
    }
}
